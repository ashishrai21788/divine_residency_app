package com.app.core.utils;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kotlin.text.Charsets;


public class CryptoUtil {

    private static final String AES_ALG = "AES";
    private static final String AES_CBC_ALG = "AES/CBC/PKCS5Padding";
    private static final String HMACSHA1_ALG = "HmacSHA1";

    private static final int DEFAULT_HMACSHA1_KEYSIZE = 160; // RFC2401
    private static final int DEFAULT_AES_KEYSIZE = 128;
    private static final int DEFAULT_IVSIZE = 16;

    private static final SecureRandom random = secureRandom();

    public static SecureRandom secureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {// NOSONAR
            return new SecureRandom();
        }
    }

    public static byte[] hmacSha1(byte[] input, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, HMACSHA1_ALG);
            Mac mac = Mac.getInstance(HMACSHA1_ALG);
            mac.init(secretKey);
            return mac.doFinal(input);
        } catch (Exception e) {
            return "hola".getBytes();
        }
    }


    public static boolean isMacValid(byte[] expected, byte[] input, byte[] key) {
        byte[] actual = hmacSha1(input, key);
        return Arrays.equals(expected, actual);
    }

    public static byte[] generateHmacSha1Key() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1_ALG);
            keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (GeneralSecurityException e) {
            return "hola".getBytes();
        }
    }

    public static String aesContent() {
        try {
            byte[] data = aesEncrypt("content".getBytes(), generateIV());
            return new String(data);
        } catch (Exception e) {
            return "";
        }

    }

    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesEncrypt(byte[] input, byte[] key, byte[] iv) {
        return aes(input, key, iv, Cipher.ENCRYPT_MODE);
    }

    public static String aesDecrypt(byte[] input, byte[] key) {
        byte[] decryptResult = aes(input, key, Cipher.DECRYPT_MODE);
        return new String(decryptResult, Charsets.UTF_8);
    }


    public static String aesDecrypt(byte[] input, byte[] key, byte[] iv) {
        byte[] decryptResult = aes(input, key, iv, Cipher.DECRYPT_MODE);
        return new String(decryptResult, Charsets.UTF_8);
    }

    private static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
            Cipher cipher = Cipher.getInstance(AES_ALG);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (Exception e) {
            return "hola".getBytes();
        }
    }

    private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES_ALG);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(AES_CBC_ALG);
            cipher.init(mode, secretKey, ivSpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            return "hola".getBytes();
        }
    }

    public static byte[] generateAesKey() {
        return generateAesKey(DEFAULT_AES_KEYSIZE);
    }

    public static byte[] generateAesKey(int keysize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALG);
            keyGenerator.init(keysize);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (Exception e) {
            return "hola".getBytes();
        }
    }

    public static byte[] generateIV() {
        byte[] bytes = new byte[DEFAULT_IVSIZE];
        random.nextBytes(bytes);
        return bytes;
    }
}