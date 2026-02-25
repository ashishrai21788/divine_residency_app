package com.app.core.extensions;


import android.os.Build;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class SecurityUtils {

    private static final String ALGORITHM = "AES_256";
    private static final String MODE = "AES";
    private static final String IV = "AEE0715D0778A4E4";
    private static final String KEY= "gajhsdjVJAXCAGX@#@#!@@#2342FE@#3";


    public static  String encrypt(String value ) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        byte[] values = cipher.doFinal(value.getBytes());

            return new String(Base64.getEncoder().encode(values));
        }
        return value;
    }


    public static  String decrypt(String value) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            byte[] values = Base64.getDecoder().decode(value);
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
            return new String(cipher.doFinal(values));
        }
        return value;
    }
}
