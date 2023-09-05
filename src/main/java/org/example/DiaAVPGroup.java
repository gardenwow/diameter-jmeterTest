/*
package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.AVPConstant.AVP_LENGTH_BUFF_LEN;
import static org.example.AVPConstant.ZERO_LEN_BYTES;

public class DiaAVPGroup extends DiaAVP {

    public static List<byte[]> cutAVPBytes(byte[] avpBytes) {
        final int LENGTH_POS = 5;
        int p = 0;
        List<byte[]> result = new ArrayList<>();
        while (p <= avpBytes.length) {
            try {
                byte[] lengthBytes = new byte[AVP_LENGTH_BUFF_LEN];
                System.arraycopy(avpBytes, p + LENGTH_POS, lengthBytes, 0, AVP_LENGTH_BUFF_LEN);
                if (Arrays.equals(ZERO_LEN_BYTES, lengthBytes)) {
                    break;
                }
                int length = decodeUIntValue(lengthBytes);
                int alignedLength = getAlignmentLength(length);
                byte[] avp = new byte[alignedLength];
                System.arraycopy(avpBytes, p, avp, 0, alignedLength);
                result.add(avp);
                p += alignedLength;
            } catch (Exception e) {
                break;
            }
        }
        return result;
    }

    public void addAVP(DiaAVP avp, int index) {
        List<DiaAVP> avps = getAVPValue();
        if (index == -1) {
            avps.add(avp);
        } else if (index < avps.size()) {
            avps.add(index, avp);
        } else {
            throw new IllegalArgumentException("Index to insert is too big to this DiaAVPGroup.");
        }
        setAVPValue(avps);
    }

    public void removeAVP(int index) {
        List<DiaAVP> avps = getAVPValue();
        if (index < avps.size()) {
            avps.remove(index);
        } else {
            throw new IllegalArgumentException("Index to remove must be less than the length of avps in this Group AVP object.");
        }
        setAVPValue(avps);
    }

    public int getAVPCount() {
        List<DiaAVP> avps = getAVPValue();
        return avps.size();
    }

    private DiaAVP __getAVPByCode(int vendor, int code, int sameAVPIndex) {
        List<DiaAVP> avps = getAVPValue();
        int i = 0;
        DiaAVP result = null;
        for (DiaAVP avp : avps) {
            if (vendor == avp.getAVPVendor() && code == avp.getAVPCode()) {
                i++;
            }
            if (i > sameAVPIndex) {
                result = avp;
                break;
            }
        }
        return result;
    }

    public DiaAVP getAVPByPath(DiaAVPPath avpPath) {
        if (avpPath.getLayersCount() <= 0) {
            return this;
        }
        DiaAVP avp = null;
        int vendor = avpPath.getAVPVendor(0);
        int code = avpPath.getAVPCode(0);
        int sameAVPIndex = avpPath.getAVPSameAVPIndex(0);
        avp = this.__getAVPByCode(vendor, code, sameAVPIndex);
        if (avp == null) {
            return null;
        }
        if (avpPath.getLayersCount() > 1) {
            DiaAVPPath tmpPath = new DiaAVPPath();
            tmpPath.setPath(avpPath.getPath());
            tmpPath.removeTop();
            avp = avp.getAVPByPath(tmpPath);
        }
        return avp;
    }

    public int addAVPByPath(DiaAVPPath rootAVPPath, DiaAVP avp, int index) {
        DiaAVP rootAVP = getAVPByPath(rootAVPPath);
        if (!(rootAVP instanceof DiaAVPGroup)) {
            return -1;
        }
        ((DiaAVPGroup) rootAVP).addAVP(avp, index);
        return 0;
    }

    public int removeAVPByPath(DiaAVPPath avpPath) {
        if (avpPath.getLayersCount() <= 0) {
            return -1;
        }
        int last = avpPath.getLayersCount() - 1;
        int vendor = avpPath.getAVPVendor(last);
        int code = avpPath.getAVPCode(last);
        int sameAVPIndex = avpPath.getAVPSameAVPIndex(last);
        DiaAVPPath rootAVPPath = new DiaAVPPath();
        rootAVPPath.setPath(avpPath.getPath());
        rootAVPPath.removeTail();
        DiaAVP rootAVP = getAVPByPath(rootAVPPath);
        if (rootAVP == null) {
            return -1;
        }
        DiaAVP avp = rootAVP.__getAVPByCode(vendor, code, sameAVPIndex);
        List<DiaAVP> avps = rootAVP.getAVPValue();
        avps.remove(avp);
        return 0;
    }

    @Override
    protected List<DiaAVP> _decodeAVPValue(byte[] valueBytes) {
        List<DiaAVP> retValue = new ArrayList<>();
        DiaAVPFactory factory = new DiaAVPFactory();
        List<byte[]> avpsBytes = cutAVPBytes(valueBytes);
        for (byte[] avpBytes : avpsBytes) {
            DiaAVP avpObj = factory.generateDiaAVPObject(avpBytes);
            retValue.add(avpObj);
        }
        return retValue;
    }

    @Override
    protected byte[] _encodeAVPValue(List<DiaAVP> avpValue) {
        List<DiaAVP> value = getAVPValue();
        byte[] retValue = new byte[0];
        for (DiaAVP subAvp : value) {
            retValue = Arrays.copyOf(retValue, retValue.length + subAvp.encode().length);
            System.arraycopy(subAvp.encode(), 0, retValue, retValue.length - subAvp.encode().length, subAvp.encode().length);
        }
        return retValue;
    }

    @Override
    protected boolean _validAVPValue(List<DiaAVP> value) {
        if (!(value instanceof List)) {
            throw new IllegalArgumentException("GROUP AVP value should be a list containing DiaAVP sub-class objects.");
        }
        for (Object obj : value) {
            if (!(obj instanceof DiaAVP)) {
                throw new IllegalArgumentException("GROUP AVP value should be a list containing DiaAVP sub-class objects.");
            }
        }
        return true;
    }

    @Override
    protected void _initAVPValue() {
        setAVPValue(new ArrayList<>());
    }

    @Override
    public int length() {
        int l = 0;
        for (DiaAVP subAvp : getAVPValue()) {
            l += getAlignmentLength(subAvp.length());
        }
        return getAVPHeaderLength() + l;
    }

    @Override
    public DiaAVP get(int index) {

*/
