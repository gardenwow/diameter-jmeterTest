package Samler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class NapVThree extends AbstractJavaSamplerClient {

    private String typeId;
    private static final String TYPEID_NAME = "typeId";
    private String host;
    private static final String HOST_NAME = "host";
    private String port;
    private static final String PORT_NAME = "port";
    private String balance;
    private static final String BALANCE_NAME = "balance";
    private String subs_id;
    private static final String LABEL_NAME = "subs_id";
    private static int napHeaderLengthBytes = 16;
    private static long napMaxMessageId = 4294967295L;
    private static String napPayloadTemplate = ";TypeID=$typeId;SubId=$subsId;Balance=$balance;State=$state;IMSI=$imsi;COSNAME=$cosname;EventTime=$eventTime";

    private void setupValues(JavaSamplerContext context) {

        this.subs_id = context.getParameter("subs_id", "");

        this.balance = context.getParameter("balance", "");

        this.host = context.getParameter("host", "");

        this.port = context.getParameter("port", "");

        this.typeId = context.getParameter("typeId", "");


    }

    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("subs_id", "");
        params.addArgument("balance","");
        params.addArgument("host","");
        params.addArgument("port","");
        params.addArgument("typeId","");
        return params;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        this.setupValues(context);
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            /*String HOST = "10.31.59.28";
            int PORT = 31000;*/
            String HOST = this.host;
            int PORT = Integer.parseInt(this.port);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String event_time = dateFormat.format(new Date());
            String namePhone = this.subs_id;
            String type = this.typeId;

            double balances = Double.parseDouble(this.balance);

            NapRequest nap = new NapRequest('M', 12, 1, type, namePhone,
                    balances, "Suspended(S1)", "250996686756345", "PO_BL02_W", event_time);

            Socket socket = new Socket(HOST, PORT);
            OutputStream out = socket.getOutputStream();
            out.write(nap.getMessage());
            socket.close();

            result.setSuccessful(true);
        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Error: " + e.getMessage());
        }

        result.sampleEnd();
        return result;
    }

    public static class NapRequest {
        private byte[] message;
        private String payload;

        public NapRequest(char site_id, int sdp, long message_id, String type_id, String subs_id, double balance,
                          String state, String imsi, String cosname, String event_time) throws Exception {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = dateFormat.parse(event_time);

            payload = napPayloadTemplate.replace("$typeId", type_id)
                    .replace("$subsId", subs_id)
                    .replace("$balance", String.valueOf(balance))
                    .replace("$state", state)
                    .replace("$imsi", imsi)
                    .replace("$cosname", cosname)
                    .replace("$eventTime", dateFormat.format(date));

            int len_request = napHeaderLengthBytes + payload.length();
            if (len_request > 255) {
                throw new Exception("Error nap length " + len_request);
            }

            message = new byte[len_request];
            message[1] = (byte) 'T';

            message[2] = (byte) len_request;
            message[3] = (byte) '#';

            if (site_id < 'A' || site_id > 'Z') {
                throw new Exception("site id " + site_id + " - range ('A' to 'Z')");
            }
            message[4] = (byte) site_id;

            if (sdp < 1 || sdp > 99) {
                throw new Exception("sdp " + sdp + " - range (01 - 99)");
            }
            String sdpString = String.format("%02d", sdp);
            byte[] sdpBytes = sdpString.getBytes();
            System.arraycopy(sdpBytes, 0, message, 5, 2);

            if (message_id > napMaxMessageId) {
                throw new Exception("message_id " + message_id + " - range (0 - " + napMaxMessageId + ")");
            }
            String messageIdString = String.format("%08X", message_id);
            byte[] messageIdBytes = messageIdString.getBytes();
            System.arraycopy(messageIdBytes, 0, message, 7, 8);

            message[15] = (byte) '#';

            byte[] payloadBytes = payload.getBytes();
            System.arraycopy(payloadBytes, 0, message, 16, payloadBytes.length);
        }

        public byte[] getMessage() {
            return message;
        }

    }
    private String generateSubsId() {
        Random random = new Random();
        int subscriberNumber = random.nextInt(9000000) + 1000000;
        return "8906" + String.format("%07d", subscriberNumber);
    }

    private double generateBalance() {
        Random random = new Random();
        double max  = -0.99;
        double min = -0.01;
        double range = max - min;
        double scaled = random.nextDouble() * range + min;
        double rounded = Math.round(scaled * 100.0) / 100.0;
        return rounded;
    }
}
