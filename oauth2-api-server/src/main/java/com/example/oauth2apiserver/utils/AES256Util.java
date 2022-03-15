package com.example.oauth2apiserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;

@Slf4j
public class AES256Util {
    private String iv;
    private Key keySpec;
    private String encryptionCode;
    private IvParameterSpec ivSpec;

    public AES256Util(String key, String code) throws UnsupportedEncodingException {
        this.iv = key.substring(0, 16);

        byte[] keyBytes = new byte[32];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
        this.encryptionCode = code;
    }

    public AES256Util(String key, String siv, Boolean isHex) throws UnsupportedEncodingException {

        SecretKeySpec keySpec = new SecretKeySpec(hexStringToByteArray(key), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToByteArray(siv));

        this.keySpec = keySpec;
        this.ivSpec = ivSpec;

    }

    public String encryptKeyString(String oriStr) {
        String encryptString = null;
        byte[] oriData = oriStr.getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] data = cipher.doFinal(oriData);
            encryptString = URLEncoder.encode(new String(Base64.encodeBase64(data)), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptString;
    }

    public String decryptKeyString(String oriStr) {
        String decryptString = null;
        byte[] data = null;
        byte[] oriData = new byte[0];
        try {
            oriData = Base64.decodeBase64(URLDecoder.decode(oriStr, "UTF-8").getBytes());

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            data = cipher.doFinal(oriData);
            decryptString = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptString;
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * AES256 으로 암호화 한다.
     *
     * @param str 암호화할 문자열
     * @return
     */
    public String encrypt(String str) {
        String enStr = null;
        try {
            if (encryptionCode.equals("02")) {
                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
                byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
                enStr = URLEncoder.encode(new String(Base64.encodeBase64(encrypted)), "UTF-8");
            } else {
                enStr = URLEncoder.encode(str, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     *
     * @param str 복호화할 문자열
     * @return
     */
    public String decrypt(String str) {
        String descStr = null;
        try {
            String decodingStr = URLDecoder.decode(str, "UTF-8");
            if (encryptionCode.equals("02")) {
                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
                byte[] byteStr = Base64.decodeBase64(decodingStr.getBytes());
                descStr = new String(c.doFinal(byteStr), "UTF-8");
            } else {
                descStr = decodingStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descStr;

    }


    /**
     * AES256 으로 암호화 한다.
     *
     * @param str 암호화할 문자열
     * @return
     */
    public String encryptEcb(String str) {
        String enStr = null;
        try {
            if (encryptionCode.equals("02")) {
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                c.init(Cipher.ENCRYPT_MODE, keySpec);
                byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
                enStr = URLEncoder.encode(new String(Base64.encodeBase64(encrypted)), "UTF-8");
            } else {
                enStr = URLEncoder.encode(str, "UTF-8");
            }
        } catch (Exception e) {

        }
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     *
     * @param str 복호화할 문자열
     * @return
     */
    public String decryptEcb(String str) {
        String descStr = null;
        try {
            String decodingStr = URLDecoder.decode(str, "UTF-8");
            if (encryptionCode.equals("02")) {
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, keySpec);
                byte[] byteStr = Base64.decodeBase64(decodingStr.getBytes());
                descStr = new String(c.doFinal(byteStr), "UTF-8");
            } else {
                descStr = decodingStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descStr;

    }

    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     */
    public byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    /**
     * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
     */
    public String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    /**
     * AES 암호화
     */
    public String encryptHex(String message) {


        byte[] encrypted = new byte[0];
        try {
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, this.keySpec);
            encrypted = cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayToHex(encrypted);
    }

    /**
     * AES 복호화
     */
    public String decryptHex(String encrypted) {
        // Instantiate the cipher
        byte[] original = null;

        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, this.keySpec);
            original = cipher.doFinal(hexToByteArray(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(original);
    }
}