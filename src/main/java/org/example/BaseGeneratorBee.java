/*
package org.example;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BaseGeneratorBee {
    private String testCalledStation;
    private int ccRequestNum = -1;
    private String msisdn;
    private String imsi;
    private String sessionID;
    private boolean status = true;
    private String generatorName;

    private static Map<String, List<BaseGeneratorBee>> generatorDict = new HashMap<>();

    public BaseGeneratorBee(String requestNodeName, String msisdn, String imsi, String testCalledStation, int timeDelta) {
        this.testCalledStation = testCalledStation;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.sessionID = Arrays.toString(generateSessionID(msisdn, timeDelta));
        this.generatorName = null;

        if (!generatorDict.containsKey(requestNodeName)) {
            generatorDict.put(requestNodeName, new ArrayList<>());
        }
        generatorDict.get(requestNodeName).add(this);
    }

    public void closeSession() {
        status = false;
    }

    public void testMessage(boolean terminate, boolean needReq) {
        // idk
    }

    public int getNextCC() {
        ccRequestNum++;
        return ccRequestNum;
    }

    public void setSubscriptionData(Message msg) {
        List<AVP> subscriptionsIDs = findAllAVPsByName(msg, "Subscription-Id");
        AVP msisdnData = null;
        AVP imsiData = null;

        if (subscriptionsIDs.size() != 2) {
            throw new AssertionError("Нет обеих нужных AVP");
        }

        AVP tmpAVP = scanAVP(subscriptionsIDs.get(0), "Subscription-Id-Type");
        ByteBuffer buffer = ByteBuffer.wrap(tmpAVP.getAVPValue());
        if (buffer.getInt() == 1) {
            imsiData = subscriptionsIDs.get(0);
            msisdnData = subscriptionsIDs.get(1);
        } else {
            imsiData = subscriptionsIDs.get(1);
            msisdnData = subscriptionsIDs.get(0);
        }

        scanAVP(msisdnData, "Subscription-Id-Data").setAVPValue(msisdn.getBytes());
        scanAVP(imsiData, "Subscription-Id-Data").setAVPValue(imsi.getBytes());
    }

    public static void remover(Message msg, String removeItem) {
        try {
            List<AVP> avpsToRemove = new ArrayList<>();
            List<AVP> avps = msg.getAVPs();

            for (AVP avp : avps) {
                String avpName = Arrays.toString(avp.getName());
                if (avpName.equals(removeItem)) {
                    avpsToRemove.add(avp);
                }
            }

            for (AVP avp : avpsToRemove) {
                msg.getAVPs().remove(avp);
            }
        } catch (Exception e) {
            System.err.println("Ошибка удаления");
            e.printStackTrace(System.err);
        }
    }

    private byte[] generateSessionID(String msisdn, int timeDelta) {
        int sessionSaver = 0;
        String testOriginHostGy = "ykazat Host---------------------------------";
        String res = testOriginHostGy + ";" + msisdn + ";" + (System.currentTimeMillis() / 1000 + timeDelta) + ";" + sessionSaver;
        sessionSaver++;
        return res.getBytes(StandardCharsets.UTF_8);
    }


    public List<AVP> findAllAVPsByName(Message msg, String avpName) {
        List<AVP> res = new ArrayList<>();
        List<AVP> avps = msg.getAVPs();
        for (AVP avp : avps) {
            scanAVP(avp, avpName);
        }
        return res;
    }

    public AVP scanAVP(AVP avp, String avpSearchName) {
        String avpName = Arrays.toString(avp.getName());
        Object value = avp.getAVPValue();

        if (avpName.equals(avpSearchName)) {
            return avp;
        }

        if (value instanceof List) {
            List<AVP> valueList = (List<AVP>) value;
            for (AVP sub : valueList) {
                AVP subAVP = scanAVP(sub, avpSearchName);
                if (subAVP != null) {
                    return subAVP;
                }
            }
        }
        return null;
    }
}


*/
