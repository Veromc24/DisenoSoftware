package edu.uclm.esi.user.services;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class ProxyBEUsuarios {

    private final String urlUsuarios = "http://localhost:8081/tokens/";
    private final CloseableHttpClient httpClient;

    public ProxyBEUsuarios() {
        this.httpClient = org.apache.hc.client5.http.impl.classic.HttpClients.createDefault();
    }

    public String checkCredit(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty.");
        }

        String url = this.urlUsuarios + "checkToken?token=" + token;
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error checking user credit: " + e.getMessage(), e);
        }
        return null;
    }
}