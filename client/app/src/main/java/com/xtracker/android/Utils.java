package com.xtracker.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static Bitmap fetchImage(File file) {
        if (file == null)
            return null;
        Bitmap bm = null;
        try {
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

            bm = BitmapFactory.decodeFile(file.getAbsolutePath(),
                    btmapOptions);
/*
            String path = android.os.Environment
                    .getExternalStorageDirectory()
                    + File.separator
                    + "Phoenix" + File.separator + "default";
            //f.delete();
            OutputStream fOut = null;
            File file = new File(path, String.valueOf(System
                    .currentTimeMillis()) + ".jpg");
            try {
                fOut = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
