package com.sgsoft.servicer.util.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Viktor Rotar on 04.04.14.
 */
public class Hasher {

    public static String hashString(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(stringToHash.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i=0; i<byteData.length; i++)
        {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static boolean checkHashesEquals(String value1, String value2)
    {
        return value1.toLowerCase().equals(value2.toLowerCase());

    }
}
