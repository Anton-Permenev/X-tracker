package com.xtracker.android.rest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xtracker.android.R;
import com.xtracker.android.Utils;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedOutput;

public class Filter extends OkClient {
    private long userId;
    private String privateKey = "1";
    private Context context;

    public Filter() {
    }

    @Override
    public Response execute(Request request) throws IOException {
        String method = request.getMethod();
        TypedOutput body = request.getBody();
        String url = request.getUrl();
        List<Header> headers = new ArrayList<>();
        headers.addAll(request.getHeaders());

        String data = "";
        if (method.equals("POST")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) body.length());
            body.writeTo(baos);
            data = baos.toString();
        }
        String hmac = "";
        try {
            hmac = Utils.generateHmac(privateKey, data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            //Toast.makeText(context, R.string.internal_error, Toast.LENGTH_SHORT).show();
        }

        if (method.equals("POST")) {
            headers.add(new Header("user_id", String.valueOf(userId)));
            headers.add(new Header("hmac", hmac));
        } else if (method.equals("GET")){
            URL u = new URL(url);
            url += u.getQuery() == null ? "?" : "&";
            url += "user_id=" + userId + "&hmac=" + hmac;
        }

        Request req = new Request(method, url, headers, body);
        return super.execute(req);
    }

    public void setKeys(long userId, String privateKey) {
        this.userId = userId;
        this.privateKey = privateKey;
    }
}
