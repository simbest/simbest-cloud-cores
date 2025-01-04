package com.simbest.cloud.cores.utils.encrypt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MochaEncryptorUtil {
    private static int[] m_s1 = new int[]{18, 46, 52, 22, 39, 0, 58, 54, 23, 37, 38, 25, 42, 36, 62, 30, 41, 14, 7, 50, 8, 9, 51, 59, 21, 15, 34, 45, 56, 3, 55, 28, 49, 32, 35, 20, 24, 53, 33, 40, 11, 17, 26, 31, 48, 5, 43, 29, 44, 12, 1, 19, 4, 13, 16, 27, 57, 47, 2, 6, 63, 10, 61, 60};
    private static int[] m_s2 = new int[]{5, 50, 58, 29, 52, 45, 59, 18, 20, 21, 61, 40, 49, 53, 17, 25, 54, 41, 0, 51, 35, 24, 3, 8, 36, 11, 42, 55, 31, 47, 15, 43, 33, 38, 26, 34, 13, 9, 10, 4, 39, 16, 12, 46, 48, 27, 1, 57, 44, 32, 19, 22, 2, 37, 7, 30, 28, 56, 6, 23, 63, 62, 14, 60};
    private static byte[] m = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};

    public MochaEncryptorUtil() {
    }

    public static String getDateString() {
        DateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        Date date = cal.getTime();
        return ((DateFormat)sdf1).format(date);
    }

    public static boolean isValid(String sourceDate, int seconds) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
        Date date = ((DateFormat)df).parse(sourceDate);
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setTime(date);
        cal.add(13, seconds);
        return cal.getTime().after(new Date());
    }

    public static String encode(String s, String s1) {
        String s2 = s1 + ":" + getDateString();
        byte[] abyte0 = new byte[64];
        (new Random(System.currentTimeMillis())).nextBytes(abyte0);
        byte[] abyte1 = s2.getBytes();

        for(int i = 0; i < abyte1.length && i < 64; ++i) {
            abyte0[i] = abyte1[i];
        }

        if (abyte1.length < 64) {
            abyte0[abyte1.length] = 0;
        }

        byte[] abyte2 = s.getBytes();

        for(int j = 0; j < abyte0.length; ++j) {
            abyte0[j] += abyte2[j % abyte2.length];
        }

        return byteToString(switchArray(abyte0, m_s1));
    }

    public static String beforeDecode(String s, String s1) {
        byte[] abyte0 = switchArray(stringToByte(s1), m_s2);
        byte[] abyte1 = s.getBytes();

        int j;
        for(j = 0; j < abyte0.length; ++j) {
            abyte0[j] -= abyte1[j % abyte1.length];
        }

        j = 64;

        for(int k = 0; k < 64; ++k) {
            if (abyte0[k] == 0) {
                j = k;
                break;
            }
        }

        return new String(abyte0, 0, j);
    }

    public static String decode(String s, String s1, int seconds) throws ParseException {
        String result = beforeDecode(s, s1);
        String[] arr = result.split(":");
        return isValid(arr[1], seconds) ? arr[0] : null;
    }

    private static byte[] switchArray(byte[] abyte0, int[] ai) {
        byte[] abyte1 = new byte[abyte0.length];

        for(int i = 0; i < abyte1.length; ++i) {
            abyte1[i] = abyte0[ai[i]];
        }

        return abyte1;
    }

    private static String byteToString(byte[] abyte0) {
        if (abyte0 != null && abyte0.length != 0) {
            byte[] abyte1 = new byte[2 * abyte0.length];

            for(int i = 0; i < abyte0.length; ++i) {
                abyte1[2 * i + 0] = m[abyte0[i] & 15];
                abyte1[2 * i + 1] = m[(abyte0[i] & 240) >> 4];
            }

            return new String(abyte1);
        } else {
            return "";
        }
    }

    private static byte[] stringToByte(String s) {
        if (s != null && s.length() == 128) {
            byte[] abyte0 = new byte[64];

            for(int i = 0; i < 64; ++i) {
                byte byte0 = 0;
                char c = s.charAt(2 * i + 0);
                char c1 = s.charAt(2 * i + 1);
                if (c >= '0' && c <= '9') {
                    byte0 = (byte)(byte0 + (c - 48));
                } else {
                    if (c < 'A' || c > 'F') {
                        throw new IllegalArgumentException();
                    }

                    byte0 = (byte)(byte0 + c - 65 + 10);
                }

                if (c1 >= '0' && c1 <= '9') {
                    byte0 = (byte)(byte0 + (c1 - 48) * 16);
                } else {
                    if (c1 < 'A' || c1 > 'F') {
                        throw new IllegalArgumentException();
                    }

                    byte0 = (byte)(byte0 + (c1 - 65 + 10) * 16);
                }

                abyte0[i] = byte0;
            }

            return abyte0;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
