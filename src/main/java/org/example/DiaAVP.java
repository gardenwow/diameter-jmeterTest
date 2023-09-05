/*
package org.example;

import java.util.Arrays;
import java.util.Objects;

import static org.example.AVPConstant.*;

abstract class DiaAVP {
    private int avpVendor = AVP_VENDOR_UNKNOWN;
    private int avpCode = AVP_CODE_UNKNOWN;
    private int avpType = Integer.parseInt(AVP_TYPE_UNKNOWN);
    private int avpFlags = AVP_FLAG_CLEAR;
    private byte[] avpValue;

    public DiaAVP() {
        _initAVPValue();
    }

    public String getAVPName() {
        int vendor = 0;
        if (getAVPFlags() != 0) {
            vendor = getAVPVendor();
        }
        int code = getAVPCode();
        DiaAVPDict dict = new DiaAVPDict();
        if (dict.hasAVPDef(vendor, code)) {
            return dict.getAVPDefName(getAVPVendor(), getAVPCode());
        }
        return AVP_NAME_NONE;
    }

    public int getAVPType() {
        int vendor = 0;
        if (getAVPFlags() != 0) {
            vendor = getAVPVendor();
        }
        int code = getAVPCode();
        DiaAVPDict dict = new DiaAVPDict();
        if (dict.hasAVPDef(vendor, code)) {
            return Integer.parseInt(dict.getAVPDefType(getAVPVendor(), getAVPCode()));
        }
        return Integer.parseInt(AVP_TYPE_UNKNOWN);
    }

    public int getAVPCode() {
        return avpCode;
    }

    public void setAVPCode(int avpCode) {
        this.avpCode = avpCode;
    }

    public int getAVPFlags() {
        return avpFlags;
    }

    public void setAVPFlags(int avpFlags) {
        this.avpFlags = avpFlags;
    }

    public void setAVPVSFlag() {
        avpFlags = avpFlags | 0x80;
    }

    public void clearAVPVSFlag() {
        avpFlags = avpFlags & 0x7F;
    }

    public boolean getAVPVSFlag() {
        return ((avpFlags & 0x80) >> 7) == 1;
    }

    public void setAVPMandatoryFlag() {
        avpFlags = avpFlags | 0x40;
    }

    public void clearAVPMandatoryFlag() {
        avpFlags = avpFlags & 0xBF;
    }

    public boolean getAVPMandatoryFlag() {
        return ((avpFlags & 0x40) >> 6) == 1;
    }

    public void setAVPProtectedFlag() {
        avpFlags = avpFlags | 0x20;
    }

    public void clearAVPProtectedFlag() {
        avpFlags = avpFlags & 0xDF;
    }

    public boolean getAVPProtectedFlag() {
        return ((avpFlags & 0x20) >> 5) == 1;
    }

    public int getAVPVendor() {
        return avpVendor;
    }

    public void setAVPVendor(int avpVendor) {
        this.avpVendor = avpVendor;
    }

    public byte[] getAVPValue() {
        return avpValue;
    }

    public void setAVPValue(byte[] value) {
        if (_validAVPValue(value)) {
            avpValue = value;
        } else {
            throw new AssertionError("Invalid AVP value");
        }
    }

    public int getAVPHeaderLength() {
        int l = AVP_CODE_BUFF_LEN + AVP_FLAGS_BUFF_LEN + AVP_LENGTH_BUFF_LEN;
        if (getAVPVSFlag()) {
            l += AVP_VENDOR_BUFF_LEN;
        }
        return l;
    }

    public static int getAlignmentLength(int len) {
        return (len % 4 == 0) ? len : ((len / 4) + 1) * 4;
    }

    protected abstract byte[] _decodeAVPValue(byte[] valueBytes);

    // Метод для кодирования значения AVP.
    protected abstract byte[] _encodeAVPValue(byte[] valueBytes);

    // Проверка допустимости значения AVP.
    protected abstract boolean _validAVPValue(byte[] value);

    // Метод для инициализации значения AVP.
    protected abstract void _initAVPValue();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaAVP diaAVP = (DiaAVP) o;
        return avpVendor == diaAVP.avpVendor && avpCode == diaAVP.avpCode && avpType == diaAVP.avpType && avpFlags == diaAVP.avpFlags && Arrays.equals(avpValue, diaAVP.avpValue);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(avpVendor, avpCode, avpType, avpFlags);
        result = 31 * result + Arrays.hashCode(avpValue);
        return result;
    }

    @Override
    public String toString() {
        return "DiaAVP{" +
                "avpVendor=" + avpVendor +
                ", avpCode=" + avpCode +
                ", avpType=" + avpType +
                ", avpFlags=" + avpFlags +
                ", avpValue=" + Arrays.toString(avpValue) +
                '}';
    }
}
*/
