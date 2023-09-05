/*
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiaMessage {
    private static final int MSG_HEADER_BUFF_LEN = 20;
    private static final int MSG_VERSION_BUFF_LEN = 1;
    private static final int MSG_LENGTH_BUFF_LEN = 3;
    private static final int MSG_FLAGS_BUFF_LEN = 1;
    private static final int MSG_COMMAND_CODE_BUFF_LEN = 3;
    private static final int MSG_APPLICATION_ID_BUFF_LEN = 4;
    private static final int MSG_HBH_BUFF_LEN = 4;
    private static final int MSG_E2E_BUFF_LEN = 4;
    private static final int MSG_FLAGS_UNSET = 0;
    private static final int MSG_COMMAND_CODE_UNSET = 0;
    private static final int MSG_APP_ID_UNSET = 0;
    private static final int MSG_HBH_ID_UNSET = 0;
    private static final int MSG_E2E_ID_UNSET = 0;

    private byte version = 0x01;
    private byte flags = MSG_FLAGS_UNSET;
    private int commandCode = MSG_COMMAND_CODE_UNSET;
    private int applicationID = MSG_APP_ID_UNSET;
    private int hbhID = MSG_HBH_ID_UNSET;
    private int e2eID = MSG_E2E_ID_UNSET;
    private List<DiaAVP> avps = new ArrayList<>();

    public void setE2EID(int e2eID) {
        this.e2eID = e2eID;
    }

    public void setHBHID(int hbhID) {
        this.hbhID = hbhID;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public byte getFlags() {
        return flags;
    }

    public void setRequestFlag() {
        flags = (byte) (flags | 0x80);
    }

    public boolean getRequestFlag() {
        return (flags & 0x80) == 0x80;
    }

    public void setCommandCode(int cmdCode) {
        commandCode = cmdCode;
    }

    public int getCommandCode() {
        return commandCode;
    }

    public void setApplicationID(int appID) {
        applicationID = appID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void generateHBHID() {
        hbhID = new Random().nextInt();
    }

    public void generateE2EID() {
        e2eID = new Random().nextInt();
    }

    public List<DiaAVP> getAVPs() {
        return avps;
    }

    public DiaAVP getAVPByPath(String avpPath) {
        DiaAVPGroup fakeRoot = new DiaAVPGroup();
        fakeRoot.setAVPValue(avps);
        DiaAVP avp = fakeRoot.getAVPByPath(avpPath);
        fakeRoot = null;
        return avp;
    }

    public void addAVPByPath(String groupAVPPath, DiaAVP avp, int index) {
        DiaAVPGroup fakeRoot = new DiaAVPGroup();
        fakeRoot.setAVPValue(avps);
        fakeRoot.addAVPByPath(groupAVPPath, avp, index);
        fakeRoot = null;
    }

    public void removeAVPByPath(String avpPath) {
        DiaAVPGroup fakeRoot = new DiaAVPGroup();
        fakeRoot.setAVPValue(avps);
        fakeRoot.removeAVPByPath(avpPath);
        fakeRoot = null;
    }

    public void decode(byte[] bytesBuff) {
        decodeMessageHeader(bytesBuff);
        byte[][] avpsBytesBuff = DiaAVPGroup.cutAVPBytes(bytesBuff[MSG_HEADER_BUFF_LEN:]);
        DiaAVPFactory avpFactory = new DiaAVPFactory();
        for (byte[] avpBuffsBuff : avpsBytesBuff) {
            DiaAVP avp = avpFactory.generateDiaAVPObject(avpBuffsBuff);
            if (avp != null) {
                avps.add(avp);
            } else {
                throw new RuntimeException("Got a Null Pointer when decoding Diameter AVP.");
            }
        }
    }

    public byte[] encode() {
        byte[] r = new byte[0];
        r = ByteUtils.concat(r, encodeMessageHeader());
        for (DiaAVP avp : avps) {
            r = ByteUtils.concat(r, avp.encode());
        }
        return r;
    }

    public int length() {
        int r = MSG_HEADER_BUFF_LEN;
        for (DiaAVP avp : avps) {
            r += avp.getAlignmentLength(avp.length());
        }
        return r;
    }

    private void decodeMessageHeader(byte[] headerBytesBuff) {
        int p = 0;
        byte version = headerBytesBuff[p];
        p += MSG_VERSION_BUFF_LEN;
        if (this.version != version) {
            throw new RuntimeException("Diameter version is not 1 but " + version);
        }
        p += MSG_LENGTH_BUFF_LEN;
        byte flags = headerBytesBuff[p];
        setFlags(flags);
        p += MSG_FLAGS_BUFF_LEN;
        int cmdCode = ByteUtils.bytesToInt(headerBytesBuff, p, MSG_COMMAND_CODE_BUFF_LEN);
        setCommandCode(cmdCode);
        p += MSG_COMMAND_CODE_BUFF_LEN;
        int appID = ByteUtils.bytesToInt(headerBytesBuff, p, MSG_APPLICATION_ID_BUFF_LEN);
        setApplicationID(appID);
        p += MSG_APPLICATION_ID_BUFF_LEN;
        int hbhID = ByteUtils.bytesToInt(headerBytesBuff, p, MSG_HBH_BUFF_LEN);
        setHBHID(hbhID);
        p += MSG_HBH_BUFF_LEN;
        int e2eID = ByteUtils.bytesToInt(headerBytesBuff, p, MSG_E2E_BUFF_LEN);
        setE2EID(e2eID);
    }

    private byte[] encodeMessageHeader() {
        byte[] r = new byte[0];
        r = ByteUtils.concat(r, encodeUIntValue(version, MSG_VERSION_BUFF_LEN));
        r = ByteUtils.concat(r, encodeUIntValue(length(), MSG_LENGTH_BUFF_LEN));
        r = ByteUtils.concat(r, encodeUIntValue(getFlags(), MSG_FLAGS_BUFF_LEN));
        r = ByteUtils.concat(r, ByteUtils.intToBytes(getCommandCode(), MSG_COMMAND_CODE_BUFF_LEN));
        r = ByteUtils.concat(r, ByteUtils.intToBytes(getApplicationID(), MSG_APPLICATION_ID_BUFF_LEN));
        r = ByteUtils.concat(r, ByteUtils.intToBytes(getHBHID(), MSG_HBH_BUFF_LEN));
        r = ByteUtils.concat(r, ByteUtils.intToBytes(getE2EID(), MSG_E2E_BUFF_LEN));
        return r;
    }

    private byte[] encodeUIntValue(long intValue, int intValueLength) {
        return ByteUtils.longToBytes(intValue, intValueLength);
    }
}
*/
