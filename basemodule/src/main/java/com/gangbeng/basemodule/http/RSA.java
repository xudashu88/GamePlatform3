package com.gangbeng.basemodule.http;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {
    public RSA() {
    }

    public static String encryptData(String data, String publicKey) throws Exception {
        PublicKey publicKey1 = getPublicKey(Base64.decode(publicKey, 0));
        byte[] b = encryptData(data.getBytes(), publicKey1);
        return Base64.encodeToString(b, 0);
    }

    public static String decryptData(String data, String privateKey) throws Exception {
        PrivateKey privateKey1 = getPrivateKey(Base64.decode(privateKey, 0));
        byte[] d = Base64.decode(data, 0);
        byte[] result = decryptData(d, privateKey1);
        return new String(result, "UTF-8");
    }

    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            e.init(1, publicKey);
            return e.doFinal(data);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher e = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            e.init(2, privateKey);
            return e.doFinal(encryptedData);
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] decryptData(byte[] encryptedData, PublicKey publicKey) {
        try {
            Cipher e = Cipher.getInstance("RSA");
            e.init(2, publicKey);
            return e.doFinal(encryptedData);
        } catch (Exception var3) {
            return null;
        }
    }

    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] e = Base64.decode(publicKeyStr, 0);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(e);
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException var4) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException var5) {
            throw new Exception("公钥非法");
        } catch (NullPointerException var6) {
            throw new Exception("公钥数据为空");
        }
    }

    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] e = Base64.decode(privateKeyStr, 0);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException var4) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException var5) {
            throw new Exception("私钥非法");
        } catch (NullPointerException var6) {
            throw new Exception("私钥数据为空");
        }
    }
}