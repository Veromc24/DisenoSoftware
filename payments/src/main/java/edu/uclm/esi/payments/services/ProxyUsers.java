package edu.uclm.esi.payments.services;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ProxyUsers {

    private final String urlUsuarios = "http://localhost:8081/users/";
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public String addCredit( int amount) {
        String url = urlUsuarios + "addCredit";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("amount", amount);

        try {
            httpPost.setEntity(new org.apache.hc.core5.http.io.entity.StringEntity(json.toString()));
            try (var response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}