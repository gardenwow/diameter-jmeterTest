/*
package org.example;

import java.nio.charset.StandardCharsets;

public class CCRgyGeneratorHermes extends BaseGeneratorBee {

    private static final String EXAMPLE_MSG = "01000450c0000110000000045009100049b2008e0000010740000051687561322d67792e7067772e746573747a6f652e6570632e6d6e633039392e6d63633235302e336770706e6574776f726b2e6f72673b333834383437343433383b3234323b313239000000000001024000000c00000004000001084000003e687561322d67792e7067772e746573747a6f6e652e6570632e6d6e633039392e6d63633235302e336770706e6574776f726b2e6f7267000000000128400000367067772e746573747a6f6e652e6570632e6d6e633039392e6d63633235302e336770706e6574776f726b2e6f726700000000011b400000356865726d65732e746573747a6f6e652e6d6e633039392e6d63633235302e336770706e6574776f726b2e6f7267000000000001cd400000194d6f73636f77406875617765692e636f6d000000000001a04000000c000000010000019f4000000c00000000000001164000000ce55d7e3a000000374000000ce5630f46000001bb40000028000001c24000000c00000000000001bc40000013373936303132303231373900000001bb4000002c000001c24000000c00000001000001bc4000001732353039393636383434363436303300000001c74000000c00000001000001c84000001c000001b540000008000001b04000000c00002710000001ca0000002c000001cb0000000c00000000000001cc000000183836353737373034393038373538383800000369c0000180000028af0000036ac0000174000028af0000000280000010000028af968000810000000380000010000028af00000000000004cbc0000012000028af000000000003ecc0000013000028af00000000000034fc0000012000028af000000000004dfc0000012000028af0000000000ccc0000012000028af0000000000ccc0000013000028af00000000000369c0000018000028af0000000000";

    public CCRgyGeneratorHermes(String requestNodeName, String msisdn, String imsi, String testCalledStation, int timeDelta) {
        super(requestNodeName, msisdn, imsi, testCalledStation, timeDelta);
    }

    public DiaMessage testMessage(boolean terminate, boolean needReq, int sgsnAddress) {
        DiaMessage msg = new DiaMessage();
        msg.decode(DatatypeConverter.parseHexBinary(EXAMPLE_MSG));

        msg.setFlags((byte) 0);
        msg.setRequestFlag();
        msg.generateHBHID();
        msg.generateE2EID();

 ### Настройка полей msg ###

        setAVPValue(msg, "Session-Id", this.sessionId);
        setAVPValue(msg, "Origin-Host", gtc.testOriginHostGy.getBytes(StandardCharsets.UTF_8));
        setAVPValue(msg, "Origin-Realm", gtc.testOriginRealmGy.getBytes(StandardCharsets.UTF_8));
        setAVPValue(msg, "Destination-Host", gtc.testDestinationHostGy.getBytes(StandardCharsets.UTF_8));
        setAVPValue(msg, "Destination-Realm", gtc.testDestinationRealmGy.getBytes(StandardCharsets.UTF_8));
        setAVPValue(msg, "User-Equipment-Info-Value", "355459101111111".getBytes(StandardCharsets.UTF_8));
        this.nextCc = this.getNextCc();
        setAVPValue(msg, "CC-Request-Type", 1);
        if (this.nextCc == 0) {
            setAVPValue(msg, "CC-Request-Type", 1);
            msg.getAVPs().add(msgGenerator.getAVP("int", 455, 1));  // Multiple-Services-Indicator
        } else if (!terminate) {
            setAVPValue(msg, "CC-Request-Type", 2);
            if (!needReq) {
                AVP MSCC = findAVPByName(msg, "Multiple-Services-Credit-Control");
                MSCC.getAVPValue().remove(findAVPByName(msg, "Requested-Service-Unit"));
            }
        } else {
            setAVPValue(msg, "CC-Request-Type", 3);
            // AVP MSCC = findAVPByName(msg, "Multiple-Services-Credit-Control");
            msg.getAVPs().remove(findAVPByName(msg, "Multiple-Services-Credit-Control"));
            msg.getAVPs().remove(findAVPByName(msg, "Multiple-Services-Indicator"));
            // CCR_gy_generator_hermes.setMsccValue(this, msg, []);
            // MSCC.getAVPValue().remove(findAVPByName(msg, "Requested-Service-Unit"));
            msg.getAVPs().add(msgGenerator.getAVP("int", 295, 1));
            // msg.getAVPs().add(msg, "Termination-Cause", 1);
        }

        setAVPValue(msg, "CC-Request-Number", this.nextCc);
        setAVPValue(msg, "Event-Timestamp", timeToBytes(getCurrentDateParsed()));
        // setAVPValue(msg, "User-Equipment-Info-Value", "".getBytes(StandardCharsets.UTF_8));
        setAVPValue(msg, "Called-Station-Id", this.testCalledStation.getBytes(StandardCharsets.UTF_8));
        // TODO: BEE add Origin-State-ID generation
        this.setSubscriptionData(msg);

        if (sgsnAddress != 0) {
            this.setSgsnAddress(msg, sgsnAddress);
        }

        return msg;
    }
}
*/
