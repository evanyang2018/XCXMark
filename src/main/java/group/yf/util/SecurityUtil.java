package com.zap.miniapp.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author lixiaohua
 */
public class SecurityUtil {

    private static final String ALGORITHM = "DES";
    private static String desKey = "zhipengapp!@#$%^&*";

    private static Key getKey(String key) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
        DESKeySpec dks = new DESKeySpec(key.getBytes("utf-8"));
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        return skf.generateSecret(dks);

    }

    public static String encrypt(String data) throws SecurityException {
        try {
            byte[] result = encryptInteral(desKey, data.getBytes("utf-8"));
            System.out.println(HexDumper.toHexString(result));
            return new String(Base64.encodeBase64(result));
        } catch (Exception e) {
            throw new SecurityException("security encrypt error", e);
        }
    }

    public static String decrypt(String data) throws SecurityException {
        try {
            byte[] result = Base64.decodeBase64(data.getBytes("utf-8"));
            return new String(decryptInteral(desKey, result), "utf-8");
        } catch (Exception e) {
            throw new SecurityException("security decrypt error", e);
        }
    }

    private static String decryptByKey(byte[] data, String key) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        return new String(decryptInteral(key, data), "utf-8");
    }

    private static byte[] decryptInteral(String key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKey(key));
        return cipher.doFinal(data);
    }

    private static String encryptByKey(String data, String key) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        byte[] result = encryptInteral(key, data.getBytes("utf-8"));
        return new String(Base64.encodeBase64(result));
    }

    private static byte[] encryptInteral(String key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
        return cipher.doFinal(data);
    }

    public static void main(String[] args) {
//		   String mobile = "18612115840";
//		   String password = "juan801020";
//		   String mobile1 = "13810468870";
//		   System.out.println(encrypt(mobile));
//		   System.out.println(encrypt(password));
//		   System.out.println(encrypt(mobile1));
//		   String s = "Whs/zI1CTr+U/h3M4ewlCQ==";
        System.out.println(encrypt("1"));
//		   String mobile = "";
//		   System.out.println(encrypt(mobile));

    }

}
