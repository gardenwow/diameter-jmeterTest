 /*
  * TeleStax, Open Source Cloud Communications
  * Copyright 2011-2016, TeleStax Inc. and individual contributors
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
  *
  * This file incorporates work covered by the following copyright and
  * permission notice:
  *
  *   JBoss, Home of Professional Open Source
  *   Copyright 2007-2011, Red Hat, Inc. and individual contributors
  *   by the @authors tag. See the copyright.txt in the distribution for a
  *   full listing of individual contributors.
  *
  *   This is free software; you can redistribute it and/or modify it
  *   under the terms of the GNU Lesser General Public License as
  *   published by the Free Software Foundation; either version 2.1 of
  *   the License, or (at your option) any later version.
  *
  *   This software is distributed in the hope that it will be useful,
  *   but WITHOUT ANY WARRANTY; without even the implied warranty of
  *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  *   Lesser General Public License for more details.
  *
  *   You should have received a copy of the GNU Lesser General Public
  *   License along with this software; if not, write to the Free
  *   Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  *   02110-1301 USA, or see the FSF site: http://www.fsf.org.
  */

package org.jdiameter.api.acc;

 import org.jdiameter.api.Answer;
 import org.jdiameter.api.NetworkReqListener;
 import org.jdiameter.api.Request;
 import org.jdiameter.api.acc.events.AccountAnswer;
 import org.jdiameter.api.acc.events.AccountRequest;
 import org.jdiameter.api.app.StateChangeListener;
 import api.ISessionFactory;
 import org.jdiameter.common.concurrent.app.IAppSessionData;
 import org.jdiameter.api.AppSessionImpl;

 import java.util.List;
 import java.util.concurrent.CopyOnWriteArrayList;
 import java.util.concurrent.locks.Lock;
 import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AppAccSessionImpl extends AppSessionImpl implements  NetworkReqListener, org.jdiameter.api.app.StateMachine {

  protected Lock sendAndStateLock = new ReentrantLock();

  protected transient List<StateChangeListener> stateListeners = new CopyOnWriteArrayList<StateChangeListener>();

  public AppAccSessionImpl(ISessionFactory sf, IAppSessionData appSessionData) {
    super(sf, appSessionData);
  }

  @Override
  public void addStateChangeNotification(StateChangeListener listener) {
    if (!stateListeners.contains(listener)) {
      stateListeners.add(listener);
    }
  }

  @Override
  public void removeStateChangeNotification(StateChangeListener listener) {
    stateListeners.remove(listener);
  }

  protected AccountRequest createAccountRequest(Request request) {
    return new AccountRequestImpl(request);
  }

  protected AccountAnswer createAccountAnswer(Answer answer) {
    return new AccountAnswerImpl(answer);
  }

  @Override
  public void release() {
    //stateListeners.clear();
    super.release();
  }

}