package Samler;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;


public class DiameterSampler extends AbstractJavaSamplerClient {

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
        params.addArgument("balance", "");
        params.addArgument("host", "");
        params.addArgument("port", "");
        params.addArgument("typeId", "");
        return params;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        this.setupValues(context);
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {


        } catch (Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Error: " + e.getMessage());
        }

        result.sampleEnd();
        return result;
    }
}