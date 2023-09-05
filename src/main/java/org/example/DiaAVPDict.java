/*
package org.example;

import java.util.HashMap;
import java.util.Map;

import static org.example.AVPConstant.*;

public class DiaAVPDict {
    private static DiaAVPDict instance = null;
    private Map<Integer, Map<Integer, Map<String, String>>> avpDict;

    public DiaAVPDict() {
        avpDict = new HashMap<>();
    }

    public static DiaAVPDict getInstance() {
        if (instance == null) {
            instance = new DiaAVPDict();
        }
        return instance;
    }

    private void createAVPVendor(int avpVendor) {
        avpDict.put(avpVendor, new HashMap<>());
    }

    private void createAVPCode(int avpVendor, int avpCode) {
        avpDict.get(avpVendor).put(avpCode, new HashMap<>());
    }

    public boolean hasVendorDef(int avpVendor) {
        return avpDict.containsKey(avpVendor);
    }

    public boolean hasAVPDef(int avpVendor, int avpCode) {
        return avpDict.containsKey(avpVendor) && avpDict.get(avpVendor).containsKey(avpCode);
    }

    public boolean addAVPDef(int avpVendor, int avpCode, String avpName, String avpType) {
        if (hasAVPDef(avpVendor, avpCode)) {
            throw new IllegalArgumentException("AVP vendor and code already exist.");
        }
        if (!hasVendorDef(avpVendor)) {
            createAVPVendor(avpVendor);
        }
        createAVPCode(avpVendor, avpCode);
        avpDict.get(avpVendor).get(avpCode).put(NAME_KEY, avpName);
        avpDict.get(avpVendor).get(avpCode).put(TYPE_KEY, avpType);
        return true;
    }

    public boolean updateAVPDef(int avpVendor, int avpCode, String avpName, String avpType) {
        if (!hasVendorDef(avpVendor)) {
            throw new IllegalArgumentException("AVP vendor does not exist.");
        }
        if (!hasAVPDef(avpVendor, avpCode)) {
            throw new IllegalArgumentException("AVP code does not exist.");
        }
        avpDict.get(avpVendor).get(avpCode).put(NAME_KEY, avpName);
        avpDict.get(avpVendor).get(avpCode).put(TYPE_KEY, avpType);
        return true;
    }

    public String getAVPDefName(int avpVendor, int avpCode) {
        if (hasAVPDef(avpVendor, avpCode)) {
            return avpDict.get(avpVendor).get(avpCode).get(NAME_KEY);
        }
        return AVP_NAME_NONE;
    }

    public String getAVPDefType(int avpVendor, int avpCode) {
        if (hasAVPDef(avpVendor, avpCode)) {
            return avpDict.get(avpVendor).get(avpCode).get(TYPE_KEY);
        }
        return AVP_TYPE_UNKNOWN;
    }
}

*/
