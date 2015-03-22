package com.xtracker.backend.ejb;


import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Stateless(name = "auth")
public class AuthBean {

    @EJB(beanName = "orm")
    ORMBean orm;

    @EJB(beanName = "crypto")
    CryptoBean cryptoBean;

    private final static String GET_USER_INFO= "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    private String getPrivateKey(long userId) throws SQLException {
        return orm.getUser(userId).getPrivateKey();
    }

    public String generatePrivateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String randomNum = Integer.toString(cryptoBean.getRandomGenerator().nextInt());
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] result = sha.digest(randomNum.getBytes());
        return new String(new Hex().encode(result), "UTF-8");
    }

    public String fetchEmail(String accessToken) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(new HttpGet(GET_USER_INFO + accessToken));
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        JSONObject json = new JSONObject(responseString);
        String email = "";
        if (json.has("email"))
            email = json.getString("email");
        return email;
    }

    public String generateHmac(long userId, String data) throws NoSuchAlgorithmException, InvalidKeyException, SQLException, UnsupportedEncodingException {
        String privateKey = getPrivateKey(userId);
        Mac macSHA = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes("UTF-8"), "HmacSHA256");
        macSHA.init(secretKey);
        byte[] raw = macSHA.doFinal(data.getBytes("UTF-8"));
        byte[] hex = new Hex().encode(raw);

        return new String(hex, "UTF-8");
    }

}
