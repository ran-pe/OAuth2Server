package com.example.oauth2apiserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

@Slf4j
public class AES128Util {
    private static final int keySize = 128;

    private final Cipher cipher;

    public AES128Util() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String salt, String iv, String passPhrase, int iterationCount, String plaintext) {
        String encryptedStr = null;
        try {
            if (salt != null) {
                SecretKey key = generateKey(salt, passPhrase, iterationCount);
                byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
                encryptedStr = encodeBase64(encrypted);
            } else {
                SecretKeySpec key = new SecretKeySpec(passPhrase.getBytes(), "AES");

                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));
                encryptedStr = encodeBase64(encrypted);
            }
        } catch (Exception e) {

        }
        return encryptedStr;
    }

    public String decrypt(String salt, String iv, String passPhrase, int iterationCount, String ciphertext) {
        String decrypedStr = null;
        byte[] decrypted;
        try {
            if (salt != null) {
                SecretKey key = generateKey(salt, passPhrase, iterationCount);
                decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, decodeBase64(ciphertext));
                decrypedStr = new String(decrypted, "UTF-8");
            } else {
                SecretKeySpec key = new SecretKeySpec(passPhrase.getBytes(), "AES");

                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);
                decrypted = cipher.doFinal(decodeBase64(ciphertext));
                decrypedStr = new String(decrypted, "UTF-8");
            }
        } catch (Exception e) {

        }
        return decrypedStr;
    }

    public String encryptCbc(String salt, String iv, String passPhrase, int iterationCount, String plaintext) {
        byte[] encrypted = null;
        try {
            if (salt != null) {
                SecretKey key = generateKey(salt, passPhrase, iterationCount);
                encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
                return encodeBase64(encrypted);
            } else {
                SecretKeySpec key = new SecretKeySpec(passPhrase.getBytes(), "AES");

                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
                encrypted = c.doFinal(plaintext.getBytes("UTF-8"));

            }
        } catch (Exception e) {

        }
        return encodeBase64(encrypted);
    }

    public String decryptCbc(String salt, String iv, String passPhrase, int iterationCount, String ciphertext) {
        String decrypedStr = null;
        try {
            byte[] decrypted;
            if (salt != null) {
                SecretKey key = generateKey(salt, passPhrase, iterationCount);
                decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, decodeBase64(ciphertext));
                decrypedStr = new String(decrypted, "UTF-8");
            } else {
                SecretKeySpec key = new SecretKeySpec(passPhrase.getBytes(), "AES");

                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes("UTF-8")));
                decrypted = c.doFinal(decodeBase64(ciphertext));
                decrypedStr = new String(decrypted, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypedStr;
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) throws Exception {
        cipher.init(encryptMode, key, new IvParameterSpec(decodeHex(iv)));
        return cipher.doFinal(bytes);
    }


    private SecretKey generateKey(String salt, String passPhrase, int iterationCount) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), decodeHex(salt), iterationCount, keySize);
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return key;
    }

    private static String encodeBase64AndUrlEncoding(byte[] bytes) {
        String result = null;
        try {
            result = URLEncoder.encode(Base64.encodeBase64String(bytes), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static byte[] decodeBase64AndUrlEncoding(String str) throws UnsupportedEncodingException {
        return Base64.decodeBase64(URLDecoder.decode(str, "UTF-8"));
    }

    private static String encodeBase64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }


    private static byte[] decodeBase64(String str) {
        return Base64.decodeBase64(str);
    }


    private static String encodeHex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }


    private static byte[] decodeHex(String str) throws Exception {
        return Hex.decodeHex(str.toCharArray());
    }


    private static String getRandomHexString(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return encodeHex(salt);

    }

    public static void main(String[] args) {

        String salt = null;
        String iv = "smc1217skku1700@";
        String passPhrase = "smc1217skku1700@";
        int iterationCount = 0;

        String encUsername = "YiU0mnhMlW6hN9ABg1Du8A==";
        String encBirthday = "ikDbDqhh1JtftshkwdXA+Q==";
        String encPatno = "TGxECVr3mJWqU/Gto3inZg==";

        String userId = "youngran";
        String passwd = "eodfks$$2021";

        String encryptUserId = AES128Util.encryptCbcTest(salt, iv, passPhrase, iterationCount, userId);
        String encryptPasswd = AES128Util.encryptCbcTest(salt, iv, passPhrase, iterationCount, passwd);
        System.out.println("encryptUserId: " + encryptUserId);
        System.out.println("encryptPasswd: " + encryptPasswd);

        String decUsername = AES128Util.decryptCbcTest(salt, iv, passPhrase, iterationCount, encUsername);
        String decBirthday = AES128Util.decryptCbcTest(salt, iv, passPhrase, iterationCount, encBirthday);
        String decPatno = AES128Util.decryptCbcTest(salt, iv, passPhrase, iterationCount, encPatno);
        System.out.println("decUsername: " + decUsername);
        System.out.println("decBirthday: " + decBirthday);
        System.out.println("decPatno: " + decPatno);

    }

    public static String encryptCbcTest(String salt, String iv, String passPhrase, int iterationCount, String plaintext) {
        AES128Util aes128Util = new AES128Util();

        String encryptText = aes128Util.encryptCbc(salt, iv, passPhrase, iterationCount, plaintext);
        return encryptText;
    }

    public static String decryptCbcTest(String salt, String iv, String passPhrase, int iterationCount, String ciphertext) {
        AES128Util aes128Util = new AES128Util();

        String decryptText = aes128Util.decryptCbc(salt, iv, passPhrase, iterationCount, ciphertext);
        return decryptText;
    }
}