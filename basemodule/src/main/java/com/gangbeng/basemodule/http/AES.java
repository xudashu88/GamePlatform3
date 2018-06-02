package com.gangbeng.basemodule.http;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.gangbeng.basemodule.utils.LogUtil;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
* 接口加密
* */
public class AES {
    public AES() {
    }

    public static String decrypt(String key, String encrypted) {
        if(TextUtils.isEmpty(encrypted)) {
            return encrypted;
        } else {
            try {
                byte[] e = Base64.decode(encrypted.getBytes("UTF-8"), 0);
                byte[] result = decrypt(key, e);
                return new String(result, "UTF-8");
            } catch (Exception var4) {
                LogUtil.t(var4);
                return null;
            }
        }
    }

    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getSecret(key.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, skeySpec, new IvParameterSpec(Setting.AESIV));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static Map<String, String> encrypt(String cleartext) throws Exception {
        HashMap map = new HashMap();
        if(TextUtils.isEmpty(cleartext)) {
            return map;
        } else {
            byte[] send = getRawKey(generateKey().getBytes("UTF-8"));
            byte[] key = getSecret(send);
            map.put("key", new String(send, "UTF-8"));

            try {
                byte[] e = encrypt(key, cleartext.getBytes("UTF-8"));
                map.put("date", Base64.encodeToString(e, 0));
                return map;
            } catch (Exception var5) {
                LogUtil.t(var5);
                return null;
            }
        }
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, skeySpec, new IvParameterSpec(Setting.AESIV));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        byte[] raw = KStringUtil.getRandomString(16).getBytes();
        return raw;
    }

    public static byte[] getSecret(byte[] raw) {
        byte[] bs = Arrays.copyOf(raw, 16);
        bs[14] = raw[0];
        bs[15] = raw[1];
        return bs;
    }

    private static String generateKey() {
        try {
            SecureRandom e;
            if(Build.VERSION.SDK_INT >= 17) {
                e = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else {
                e = SecureRandom.getInstance("SHA1PRNG");
            }

            byte[] bytes_key = new byte[20];
            e.nextBytes(bytes_key);
            String str_key = KStringUtil.bytesToHexString(bytes_key);
            return str_key;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}