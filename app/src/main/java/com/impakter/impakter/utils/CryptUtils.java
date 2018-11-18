package com.impakter.impakter.utils;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by GL62 on 10/2/2017.
 */

public class CryptUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "\"AES/CBC/ZeroPadding\", \"BC\"";
    private static byte[] IV = new byte[16];

//    public static void encrypt(byte[] key, byte[] inputBytes, File outputFile)
//            throws CryptoException {
//        doCrypto(Cipher.ENCRYPT_MODE, key, inputBytes, outputFile);
//    }
//
//    public static void decrypt(byte[] key, byte[] inputBytes, File outputFile)
//            throws CryptoException {
//        doCrypto(Cipher.DECRYPT_MODE, key, inputBytes, outputFile);
//    }

    public static String encrypt(byte[] key, byte[] inputBytes) throws CryptoException {
        String message = "";
        try {

            Key secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));

            byte[] outputBytes = cipher.doFinal(inputBytes);
            message = new String(outputBytes, "UTF-8");

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException ex) {
            Log.i("PdfFragment", "doCrypto: " + ex.toString());
            throw new CryptoException("Error encrypting/decrypting file", ex);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static String decrypt(byte[] key, String msgBase64) throws CryptoException {
        String message = "";
        try {

            Key secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));

            byte[] data = Base64.decode(msgBase64, Base64.DEFAULT);
            byte[] outputBytes = cipher.doFinal(data);
            message = new String(outputBytes, "UTF-8");

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException ex) {
            Log.i("PdfFragment", "doCrypto: " + ex.toString());
            throw new CryptoException("Error encrypting/decrypting file", ex);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return message;
    }
    public static byte[] generateKey(String password) throws Exception {
        byte[] keyStart = password.getBytes("UTF-8");

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(keyStart);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }

        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
        }
        Log.e("Pdf", "bytesToHex: " + str);
        return str;
    }


    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            Log.e("Pdf", "hexToBytes: " + buffer.length);
            return buffer;
        }
    }


    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }
}
