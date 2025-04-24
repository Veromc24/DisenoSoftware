package edu.uclm.esi.payments.services;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProxyStripe {

    public String prepay(JSONObject jsoConf, long amount) throws Exception {
        try {
            String key = jsoConf.getString("Stripe.apiKey");
            Stripe.apiKey = key;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setCurrency("eur")
                    .setAmount(amount)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            JSONObject json = new JSONObject(intent.toJson());
            return json.getString("client_secret");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Error al procesar tu solicitud", e);
        }
    }
}