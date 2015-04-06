package com.xtracker.android;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    public static void showKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(Activity context, EditText... editTexts) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (EditText editText : editTexts)
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
