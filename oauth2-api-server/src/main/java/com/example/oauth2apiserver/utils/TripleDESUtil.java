package com.example.oauth2apiserver.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TripleDESUtil {
    private String key;

    public TripleDESUtil(String key) {
        this.key = key;
    }


    /**
     * 문자열 대칭 암호화
     * return String 암호화된 문자열
     */
    public String encrypt(String input) {


        if (input == null || input.length() == 0) return "";

        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DESede");
        byte[] outputBytes = null;
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] plainTextBytes = input.getBytes("utf-8");

            outputBytes = cipher.doFinal(plainTextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeHex(outputBytes);
    }

    /**
     * 문자열 대칭 복호화
     * return String 복호화된 문자열
     */
    public String decrypt(String input) {

        if (input == null || input.length() == 0) return "";

        String outputStr = null;
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DESede");
        try {
            Cipher decipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

            decipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] inputBytes = decodeHex(input);
            byte[] outputBytes = decipher.doFinal(inputBytes);
            outputStr = new String(outputBytes, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStr;
    }

    private static String encodeHex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }


    private static byte[] decodeHex(String str) throws Exception {
        return Hex.decodeHex(str.toCharArray());
    }


}
