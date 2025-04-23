package edu.uclm.esi.circuits.services;

import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class ProxyBEUsuarios {

    private static ProxyBEUsuarios yo;
    private final String urlUsuarios = "http://localhost:8081/tokens/";
    private final CloseableHttpClient sharedHttpClient;

    private ProxyBEUsuarios() {
        this.sharedHttpClient = HttpClients.createDefault();
        ProxyBEUsuarios.yo = this;
    }

    public static ProxyBEUsuarios get() {
        if (yo == null) {
             yo = new ProxyBEUsuarios();
        }
        return yo;
    }

    public String checkCredit(String token) {
        String resultContent = null;

        if (token == null || token.isEmpty()) {
             System.err.println("Token cannot be null or empty.");
             return null;
        }
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        String url = this.urlUsuarios + "checkToken?token=" + actualToken;
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpResponse response = sharedHttpClient.execute(httpGet)) {
            int code = response.getCode();
            System.out.println(response.getVersion());
            System.out.println(code);
            System.out.println(response.getReasonPhrase());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultContent = EntityUtils.toString(entity);
                EntityUtils.consumeQuietly(entity);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            resultContent = null;
        }
        return resultContent;
    }

    @PreDestroy
    public void close() {
        try {
            if (this.sharedHttpClient != null) {
                this.sharedHttpClient.close();
            }
        } catch (IOException e) {
             System.err.println("Error closing HttpClient: " + e.getMessage());
        }
    }
}