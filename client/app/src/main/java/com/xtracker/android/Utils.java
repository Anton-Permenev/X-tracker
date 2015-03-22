package com.xtracker.android;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    public static String generateHmac(String privateKey, String data) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac macSHA = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes("UTF-8"), "HmacSHA256");
        macSHA.init(secretKey);
        byte[] raw = macSHA.doFinal(data.getBytes("UTF-8"));
        byte[] hex = new Hex().encode(raw);

        return new String(hex, "UTF-8");
    }
}
