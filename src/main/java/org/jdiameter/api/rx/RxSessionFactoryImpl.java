/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.jdiameter.api.rx;

import org.jdiameter.api.*;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.rx.events.*;
import api.ISessionFactory;
import org.jdiameter.impl.app.rx.ClientRxSessionImpl;
import org.jdiameter.impl.app.rx.IClientRxSessionData;
import org.jdiameter.common.concurrent.app.IAppSessionDataFactory;
import org.jdiameter.common.concurrent.app.rx.*;
import org.jdiameter.common.concurrent.data.ISessionDatasource;
import org.jdiameter.server.impl.app.rx.IServerRxSessionData;
import org.jdiameter.server.impl.app.rx.ServerRxSessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;

/**
 * Default Diameter Rx Session Factory implementation.
 *
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RxSessionFactoryImpl implements IRxSessionFactory, ClientRxSessionListener, ServerRxSessionListener, StateChangeListener<AppSession>,
    IRxMessageFactory, IServerRxSessionContext, IClientRxSessionContext {

  // Message timeout value (in milliseconds)
  protected int defaultDirectDebitingFailureHandling = 0;
  protected int defaultAAFailureHandling = 0;
  // its seconds
  protected long defaultValidityTime = 60;
  protected long defaultTxTimerValue = 30;
  // local not replicated listeners:
  protected ClientRxSessionListener clientSessionListener;
  protected ServerRxSessionListener serverSessionListener;
  protected StateChangeListener<AppSession> stateListener;
  protected IServerRxSessionContext serverContextListener;
  protected IClientRxSessionContext clientContextListener;
  protected IRxMessageFactory messageFactory;
  protected static final Logger logger = LoggerFactory.getLogger(RxSessionFactoryImpl.class);
  protected ISessionDatasource iss;
  protected ISessionFactory sessionFactory = null;
  protected IAppSessionDataFactory<IRxSessionData> sessionDataFactory;

  public RxSessionFactoryImpl(SessionFactory sessionFactory) {
    super();

    this.sessionFactory = (ISessionFactory) sessionFactory;
    this.iss = this.sessionFactory.getContainer().getAssemblerFacility().getComponentInstance(ISessionDatasource.class);
    this.sessionDataFactory = (IAppSessionDataFactory<IRxSessionData>) this.iss.getDataFactory(IRxSessionData.class);

    if (this.sessionDataFactory == null) {
      logger.debug("Initialized Rx SessionDataFactory is null");
    }
  }

  public RxSessionFactoryImpl(SessionFactory sessionFactory, int defaultDirectDebitingFailureHandling, int defaultAAFailureHandling,
      long defaultValidityTime, long defaultTxTimerValue) {
    this(sessionFactory);

    this.defaultDirectDebitingFailureHandling = defaultDirectDebitingFailureHandling;
    this.defaultAAFailureHandling = defaultAAFailureHandling;
    this.defaultValidityTime = defaultValidityTime;
    this.defaultTxTimerValue = defaultTxTimerValue;
  }

  /**
   * @return the clientSessionListener
   */
  @Override
  public ClientRxSessionListener getClientSessionListener() {
    return clientSessionListener != null ? clientSessionListener : this;
  }

  /**
   * @param clientSessionListener
   *          the clientSessionListener to set
   */
  @Override
  public void setClientSessionListener(final ClientRxSessionListener clientSessionListener) {
    this.clientSessionListener = clientSessionListener;
  }

  /**
   * @return the serverSessionListener
   */
  @Override
  public ServerRxSessionListener getServerSessionListener() {
    return serverSessionListener != null ? serverSessionListener : this;
  }

  /**
   * @param serverSessionListener
   *          the serverSessionListener to set
   */
  @Override
  public void setServerSessionListener(ServerRxSessionListener serverSessionListener) {
    this.serverSessionListener = serverSessionListener;
  }

  /**
   * @return the serverContextListener
   */
  @Override
  public IServerRxSessionContext getServerContextListener() {
    return serverContextListener != null ? serverContextListener : this;
  }

  /**
   * @param serverContextListener
   *          the serverContextListener to set
   */
  @Override
  public void setServerContextListener(IServerRxSessionContext serverContextListener) {
    this.serverContextListener = serverContextListener;
  }

  /**
   * @return the clientContextListener
   */
  @Override
  public IClientRxSessionContext getClientContextListener() {
    return clientContextListener != null ? clientContextListener : this;
  }

  /**
   * @return the messageFactory
   */
  @Override
  public IRxMessageFactory getMessageFactory() {
    return messageFactory != null ? messageFactory : this;
  }

  /**
   * @param messageFactory
   *          the messageFactory to set
   */
  @Override
  public void setMessageFactory(final IRxMessageFactory messageFactory) {
    this.messageFactory = messageFactory;
  }

  /**
   * @param clientContextListener
   *          the clientContextListener to set
   */
  @Override
  public void setClientContextListener(IClientRxSessionContext clientContextListener) {
    this.clientContextListener = clientContextListener;
  }

  /**
   * @return the sessionFactory
   */
  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  /**
   * @param sessionFactory
   *          the sessionFactory to set
   */
  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = (ISessionFactory) sessionFactory;
  }

  /**
   * @return the stateListener
   */
  @Override
  public StateChangeListener<AppSession> getStateListener() {
    return stateListener != null ? stateListener : this;
  }

  /**
   * @param stateListener
   *          the stateListener to set
   */
  @Override
  public void setStateListener(StateChangeListener<AppSession> stateListener) {
    this.stateListener = stateListener;
  }

  @Override
  public AppSession getSession(String sessionId, Class<? extends AppSession> aClass) {
    if (sessionId == null) {
      throw new IllegalArgumentException("SessionId must not be null");
    }
    if (!this.iss.exists(sessionId)) {
      return null;
    }
    AppSession appSession = null;
    try {
      if (aClass == ClientRxSession.class) {
        IClientRxSessionData sessionData =  (IClientRxSessionData) this.sessionDataFactory.getAppSessionData(ClientRxSession.class, sessionId);
        ClientRxSessionImpl clientSession = new ClientRxSessionImpl(sessionData, this.getMessageFactory(), sessionFactory, this.getClientSessionListener(),
            this.getClientContextListener(), this.getStateListener());

        clientSession.getSessions().get(0).setRequestListener(clientSession);
        appSession = clientSession;
      }
      else if (aClass == ServerRxSession.class) {
        IServerRxSessionData sessionData =  (IServerRxSessionData) this.sessionDataFactory.getAppSessionData(ServerRxSession.class, sessionId);
        ServerRxSessionImpl serverSession = new ServerRxSessionImpl(sessionData, this.getMessageFactory(), sessionFactory, this.getServerSessionListener(),
            this.getServerContextListener(), this.getStateListener());

        serverSession.getSessions().get(0).setRequestListener(serverSession);
        appSession = serverSession;
      }
      else {
        throw new IllegalArgumentException("Wrong session class: " + aClass + ". Supported[" + ClientRxSession.class + "," + ServerRxSession.class + "]");
      }
    }
    catch (Exception e) {
      logger.error("Failure to obtain new Rx Session.", e);
    }

    return appSession;
  }


  @Override
  public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args) {
    AppSession appSession = null;
    try {
      // FIXME:
      if (aClass == ClientRxSession.class) {
        if (sessionId == null) {
          if (args != null && args.length > 0 && args[0] instanceof Request) {
            Request request = (Request) args[0];
            sessionId = request.getSessionId();
          }
          else {
            sessionId = this.sessionFactory.getSessionId();
          }
        }
        IClientRxSessionData sessionData =  (IClientRxSessionData) this.sessionDataFactory.getAppSessionData(ClientRxSession.class, sessionId);
        sessionData.setApplicationId(applicationId);
        ClientRxSessionImpl clientSession = new ClientRxSessionImpl(sessionData, this.getMessageFactory(), sessionFactory, this.getClientSessionListener(),
            this.getClientContextListener(), this.getStateListener());
        // this goes first!
        iss.addSession(clientSession);
        clientSession.getSessions().get(0).setRequestListener(clientSession);
        appSession = clientSession;
      }
      else if (aClass == ServerRxSession.class) {
        if (sessionId == null) {
          if (args != null && args.length > 0 && args[0] instanceof Request) {
            Request request = (Request) args[0];
            sessionId = request.getSessionId();
          }
          else {
            sessionId = this.sessionFactory.getSessionId();
          }
        }
        IServerRxSessionData sessionData =  (IServerRxSessionData) this.sessionDataFactory.getAppSessionData(ServerRxSession.class, sessionId);
        sessionData.setApplicationId(applicationId);
        ServerRxSessionImpl serverSession = new ServerRxSessionImpl(sessionData, this.getMessageFactory(), sessionFactory, this.getServerSessionListener(),
            this.getServerContextListener(), this.getStateListener());
        iss.addSession(serverSession);
        serverSession.getSessions().get(0).setRequestListener(serverSession);
        appSession = serverSession;
      }
      else {
        throw new IllegalArgumentException("Wrong session class: " + aClass + ". Supported[" + ClientRxSession.class + "," + ServerRxSession.class + "]");
      }
    }
    catch (Exception e) {
      logger.error("Failure to obtain new Rx Session.", e);
    }

    return appSession;
  }


  // Default implementation of methods so there are no exception!

  // Message Handlers --------------------------------------------------------
  @Override
  public void doAARequest(ServerRxSession session, RxAARequest request) throws InternalException {
  }

  @Override
  public void doAAAnswer(ClientRxSession session, RxAARequest request, RxAAAnswer answer) throws InternalException {
  }

  @Override
  public void doReAuthRequest(ClientRxSession session, RxReAuthRequest request) throws InternalException {
  }

  @Override
  public void doReAuthAnswer(ServerRxSession session, RxReAuthRequest request, RxReAuthAnswer answer) throws InternalException {
  }

  @Override
  public void doAbortSessionRequest(ClientRxSession session, RxAbortSessionRequest request) throws InternalException {
  }

  @Override
  public void doAbortSessionAnswer(ServerRxSession session, RxAbortSessionRequest request, RxAbortSessionAnswer answer) throws InternalException {
  }

  @Override
  public void doSessionTermRequest(ServerRxSession session, RxSessionTermRequest request) throws InternalException {
  }

  @Override
  public void doSessionTermAnswer(ClientRxSession session, RxSessionTermRequest request, RxSessionTermAnswer answer) throws InternalException {
  }

  @Override
  public void doOtherEvent(AppSession session, AppRequestEvent request, AppAnswerEvent answer) throws InternalException {
  }

  // Message Factory Methods -------------------------------------------------
  @Override
  public RxAAAnswer createAAAnswer(Answer answer) {
    return new RxAAAnswerImpl(answer);
  }

  @Override
  public RxAARequest createAARequest(Request req) {
    return new RxAARequestImpl(req);
  }

  @Override
  public RxReAuthAnswer createReAuthAnswer(Answer answer) {
    return new RxReAuthAnswerImpl(answer);
  }

  @Override
  public RxReAuthRequest createReAuthRequest(Request req) {
    return new RxReAuthRequestImpl(req);
  }

  @Override
  public RxSessionTermAnswer createSessionTermAnswer(Answer answer) {
    return new RxSessionTermAnswerImpl(answer);
  }

  @Override
  public RxSessionTermRequest createSessionTermRequest(Request req) {
    return new RxSessionTermRequestImpl(req);
  }

  @Override
  public RxAbortSessionAnswer createAbortSessionAnswer(Answer answer) {
    return new RxAbortSessionAnswerImpl(answer);
  }

  @Override
  public RxAbortSessionRequest createAbortSessionRequest(Request req) {
    return new RxAbortSessionRequestImpl(req);
  }

  // Context Methods ----------------------------------------------------------
  @Override
  public void stateChanged(Enum oldState, Enum newState) {
    logger.info("Diameter Rx SessionFactory :: stateChanged :: oldState[{}], newState[{}]", oldState, newState);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object, java.lang.Enum, java.lang.Enum)
   */
  @Override
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    logger.info("Diameter Rx SessionFactory :: stateChanged :: source[{}], oldState[{}], newState[{}]", new Object[]{source, oldState, newState});
  }

  // FIXME: add ctx methods proxy calls!
  @Override
  public void sessionSupervisionTimerExpired(ServerRxSession session) {
    // this.resourceAdaptor.sessionDestroyed(session.getSessions().get(0).getSessionId(), session);
    session.release();
  }

  @Override
  public void sessionSupervisionTimerReStarted(ServerRxSession session, ScheduledFuture future) {
    // TODO Complete this method.
  }

  @Override
  public void sessionSupervisionTimerStarted(ServerRxSession session, ScheduledFuture future) {
    // TODO Complete this method.
  }

  @Override
  public void sessionSupervisionTimerStopped(ServerRxSession session, ScheduledFuture future) {
    // TODO Complete this method.
  }

  public void timeoutExpired(Request request) {
    // FIXME What should we do when there's a timeout?
  }

  @Override
  public void denyAccessOnDeliverFailure(ClientRxSession clientRxSessionImpl, Message request) {
    // TODO Complete this method.
  }

  @Override
  public void denyAccessOnFailureMessage(ClientRxSession clientRxSessionImpl) {
    // TODO Complete this method.
  }

  public void denyAccessOnTxExpire(ClientRxSession clientRxSessionImpl) {
    // this.resourceAdaptor.sessionDestroyed(clientRxSessionImpl.getSessions().get(0).getSessionId(),
    // clientRxSessionImpl);
    clientRxSessionImpl.release();
  }

  @Override
  public void grantAccessOnDeliverFailure(ClientRxSession clientRxSessionImpl, Message request) {
    // TODO Auto-generated method stub
  }

  @Override
  public void grantAccessOnFailureMessage(ClientRxSession clientRxSessionImpl) {
    // TODO Auto-generated method stub
  }

  public void grantAccessOnTxExpire(ClientRxSession clientRxSessionImpl) {
    // TODO Auto-generated method stub
  }

  @Override
  public void indicateServiceError(ClientRxSession clientRxSessionImpl) {
    // TODO Auto-generated method stub
  }

  @Override
  public long[] getApplicationIds() {
    // FIXME: What should we do here?
    return new long[]{16777236};
  }
}
