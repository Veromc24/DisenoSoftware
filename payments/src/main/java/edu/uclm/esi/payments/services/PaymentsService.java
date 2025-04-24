package edu.uclm.esi.payments.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class PaymentsService {

    @Autowired
    private ProxyStripe proxyStripe;

    public String prepay() throws Exception {
        JSONObject jsoConf = this.readConf("conf.json");
        long amount = (long) (jsoConf.getFloat("price") * 100);
        return this.proxyStripe.prepay(jsoConf, amount);
    }

    private JSONObject readConf(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
            if (fis == null) {
                throw new RuntimeException("Configuration file not found: " + fileName);
            }
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String s = new String(b);
            return new JSONObject(s);
        } catch (Exception e) {
            throw new RuntimeException("Error reading configuration file: " + fileName, e);
        }
    }
}
