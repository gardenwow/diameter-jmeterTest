package Samler;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdiameter.api.*;
import org.jdiameter.dictionary.AvpDictionary;
import org.jdiameter.dictionary.AvpRepresentation;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

public class ExampleClientNext implements EventListener<Request, Answer> {

    private static final Logger log = Logger.getLogger(ExampleClient.class);
    static{
        //configure logging.
        configLog4j();
    }

    private static void configLog4j() {
        InputStream inStreamLog4j = ExampleClient.class.getClassLoader().getResourceAsStream("log4j.properties");
        Properties propertiesLog4j = new Properties();
        try {
            propertiesLog4j.load(inStreamLog4j);
            PropertyConfigurator.configure(propertiesLog4j);
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            if(inStreamLog4j!=null)
            {
                try {
                    inStreamLog4j.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        log.debug("log4j configured");
    }

    //configuration files
    private static final String configFile = "config/client-jdiameter-config.xml";
    private static final String dictionaryFile = "config/dictionary3.xml";
    //our destination
    private static final String serverHost = "127.0.0.1";
    private static final String serverPort = "3868";
    private static final String serverURI = "aaa://" + serverHost + ":" + serverPort;
    //our realm
    private static final String realmName = "exchange.example.org";
    // definition of codes, IDs
    private static final int commandCode = 316;
    private static final long vendorID = 66666;
    private static final long applicationID = 16777251;
    private ApplicationId authAppId = ApplicationId.createByAuthAppId(applicationID);
    private static final int exchangeTypeCode = 888;
    private static final int exchangeDataCode = 999;
    // enum values for Exchange-Type AVP
    private static final int EXCHANGE_TYPE_INITIAL = 0;
    private static final int EXCHANGE_TYPE_INTERMEDIATE = 1;
    private static final int EXCHANGE_TYPE_TERMINATING = 2;
    //list of data we want to exchange.
    private static final String[] TO_SEND = new String[] { "I want to get 3 answers", "This is second message", "Bye bye" };
    //Dictionary, for informational purposes.
    private AvpDictionary dictionary = AvpDictionary.INSTANCE;
    //stack and session factory
    private Stack stack;
    private SessionFactory factory;

    // ////////////////////////////////////////
    // Objects which will be used in action //
    // ////////////////////////////////////////
    private Session session;  // session used as handle for communication
    private int toSendIndex = 0;  //index in TO_SEND table
    private boolean finished = false;  //boolean telling if we finished our interaction

    private void initStack() {
        if (log.isInfoEnabled()) {
            log.info("Initializing Stack...");
        }
        InputStream is = null;
        try {
            //Parse dictionary, it is used for user friendly info.
            dictionary.parseDictionary(this.getClass().getClassLoader().getResourceAsStream(dictionaryFile));
            log.info("AVP Dictionary successfully parsed.");

            this.stack = new StackImpl();
            //Parse stack configuration
            is = this.getClass().getClassLoader().getResourceAsStream(configFile);
            Configuration config = new XMLConfiguration(is);
            factory = stack.init(config);
            if (log.isInfoEnabled()) {
                log.info("Stack Configuration successfully loaded.");
            }
            //Print info about applicatio
            Set<org.jdiameter.api.ApplicationId> appIds = stack.getMetaData().getLocalPeer().getCommonApplications();

            log.info("Diameter Stack  :: Supporting " + appIds.size() + " applications.");
            for (org.jdiameter.api.ApplicationId x : appIds) {
                log.info("Diameter Stack  :: Common :: " + x);
            }
            is.close();
            //Register network req listener, even though we wont receive requests
            //this has to be done to inform stack that we support application
            Network network = stack.unwrap(Network.class);
            network.addNetworkReqListener(new NetworkReqListener() {

                @Override
                public Answer processRequest(Request request) {
                    //this wontbe called.
                    return null;
                }
            }, this.authAppId); //passing our example app id.

        } catch (Exception e) {
            e.printStackTrace();
            if (this.stack != null) {
                this.stack.destroy();
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            return;
        }

        MetaData metaData = stack.getMetaData();
        //ignore for now.
        if (metaData.getStackType() != StackType.TYPE_SERVER || metaData.getMinorVersion() <= 0) {
            stack.destroy();
            if (log.isEnabledFor(org.apache.log4j.Level.ERROR)) {
                log.error("Incorrect driver");
            }
            return;
        }

        try {
            if (log.isInfoEnabled()) {
                log.info("Starting stack");
            }
            stack.start();
            if (log.isInfoEnabled()) {
                log.info("Stack is running.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stack.destroy();
            return;
        }
        if (log.isInfoEnabled()) {
            log.info("Stack initialization successfully completed.");
        }
    }

    /**
     * @return
     */
    private boolean finished() {
        return this.finished;
    }

    /**
     *
     */
    private void start() {
        try {
            //wait for connection to peer
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //do send
            this.session = this.factory.getNewSession("BadCustomSessionId;YesWeCanPassId;" + System.currentTimeMillis());
            sendNextRequest(EXCHANGE_TYPE_INITIAL);
        } catch (InternalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalDiameterStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RouteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OverloadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private void sendNextRequest(int enumType) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
        Request r = this.session.createRequest(commandCode, this.authAppId, realmName, serverURI);
        // here we have all except our custom avps

        // example how to remove AVP
        AvpSet requestAvps = r.getAvps();
        requestAvps.removeAvp(293);

        // example how to add AVP IMSI
        byte[] b = hexStringToByteArray("31313131313131313131313131313131");
        requestAvps.addAvp(1, b, true, false);

        // code , value , vendor, mandatory,protected,isUnsigned32
        // (Enumerated)
        Avp exchangeType = requestAvps.addAvp(exchangeTypeCode, (long) enumType, vendorID, true, false, true); // value
        // is
        // set
        // on
        // creation
        // code , value , vendor, mandatory,protected, isOctetString
        Avp exchengeData = requestAvps.addAvp(exchangeDataCode, TO_SEND[toSendIndex++], vendorID, true, false, false); // value
        // is

        // set
        // on
        // creation
        // send
        this.session.send(r, this);
        dumpMessage(r,true); //dump info on console
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jdiameter.api.EventListener#receivedSuccessMessage(org.jdiameter
     * .api.Message, org.jdiameter.api.Message)
     */
    @Override
    public void receivedSuccessMessage(Request request, Answer answer) {
        dumpMessage(answer,false);
        if (answer.getCommandCode() != commandCode) {
            log.error("Received bad answer: " + answer.getCommandCode());
            return;
        }
        AvpSet answerAvpSet = answer.getAvps();

        Avp exchangeTypeAvp = answerAvpSet.getAvp(exchangeTypeCode, vendorID);
        Avp exchangeDataAvp = answerAvpSet.getAvp(exchangeDataCode, vendorID);
        Avp resultAvp = answer.getResultCode();


        try {
            //for bad formatted request.
            if (resultAvp.getUnsigned32() == 5005 || resultAvp.getUnsigned32() == 5004) {
                // missing || bad value of avp
                this.session.release();
                this.session = null;
                log.error("Something wrong happened at server side!");
                finished = true;
            }
            switch ((int) exchangeTypeAvp.getUnsigned32()) {
                case EXCHANGE_TYPE_INITIAL:
                    // JIC check;
                    String data = exchangeDataAvp.getUTF8String();
                    if (data.equals(TO_SEND[toSendIndex - 1])) {
                        // ok :) send next;
                        sendNextRequest(EXCHANGE_TYPE_INTERMEDIATE);
                    } else {
                        log.error("Received wrong Exchange-Data: " + data);
                    }
                    break;
                case EXCHANGE_TYPE_INTERMEDIATE:
                    // JIC check;
                    data = exchangeDataAvp.getUTF8String();
                    if (data.equals(TO_SEND[toSendIndex - 1])) {
                        // ok :) send next;
                        sendNextRequest(EXCHANGE_TYPE_TERMINATING);
                    } else {
                        log.error("Received wrong Exchange-Data: " + data);
                    }
                    break;
                case EXCHANGE_TYPE_TERMINATING:
                    data = exchangeDataAvp.getUTF8String();
                    if (data.equals(TO_SEND[toSendIndex - 1])) {
                        // good, we reached end of FSM.
                        finished = true;
                        // release session and its resources.
                        this.session.release();
                        this.session = null;
                    } else {
                        log.error("Received wrong Exchange-Data: " + data);
                    }
                    break;
                default:
                    log.error("Bad value of Exchange-Type avp: " + exchangeTypeAvp.getUnsigned32());
                    break;
            }
        } catch (AvpDataException e) {
            // thrown when interpretation of byte[] fails
            e.printStackTrace();
        } catch (InternalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalDiameterStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RouteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OverloadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.jdiameter.api.EventListener#timeoutExpired(org.jdiameter.api.
     * Message)
     */
    @Override
    public void timeoutExpired(Request request) {


    }

    private void dumpMessage(Message message, boolean sending) {
        if (log.isInfoEnabled()) {
            log.info((sending?"Sending ":"Received ") + (message.isRequest() ? "Request: " : "Answer: ") + message.getCommandCode() + "\nE2E:"
                    + message.getEndToEndIdentifier() + "\nHBH:" + message.getHopByHopIdentifier() + "\nAppID:" + message.getApplicationId());
            log.info("AVPS["+message.getAvps().size()+"]: \n");
            try {
                printAvps(message.getAvps());
            } catch (AvpDataException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void printAvps(AvpSet avpSet) throws AvpDataException {
        printAvpsAux(avpSet, 0);
    }

    private void printAvpsAux(AvpSet avpSet, int level) throws AvpDataException {
        String prefix = "                      ".substring(0, level * 2);

        for (Avp avp : avpSet) {
            AvpRepresentation avpRep = AvpDictionary.INSTANCE.getAvp(avp.getCode(), avp.getVendorId());

            if (avpRep != null && avpRep.getType().equals("Grouped")) {
                log.info(prefix + "<avp name=\"" + avpRep.getName() + "\" code=\"" + avp.getCode() + "\" vendor=\"" + avp.getVendorId() + "\">");
                printAvpsAux(avp.getGrouped(), level + 1);
                log.info(prefix + "</avp>");
            } else if (avpRep != null) {
                String value = "";

                if (avpRep.getType().equals("Integer32"))
                    value = String.valueOf(avp.getInteger32());
                else if (avpRep.getType().equals("Integer64") || avpRep.getType().equals("Unsigned64"))
                    value = String.valueOf(avp.getInteger64());
                else if (avpRep.getType().equals("Unsigned32"))
                    value = String.valueOf(avp.getUnsigned32());
                else if (avpRep.getType().equals("Float32"))
                    value = String.valueOf(avp.getFloat32());
                else
                    //value = avp.getOctetString();
                    value = new String(avp.getOctetString(), StandardCharsets.UTF_8);

                log.info(prefix + "<avp name=\"" + avpRep.getName() + "\" code=\"" + avp.getCode() + "\" vendor=\"" + avp.getVendorId()
                        + "\" value=\"" + value + "\" />");
            }
        }
    }

    public static void main(String[] args) {

        ExampleClientNext ec = new ExampleClientNext();
        ec.initStack();
        ec.start();

        while (!ec.finished()) {
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}