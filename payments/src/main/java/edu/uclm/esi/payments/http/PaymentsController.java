package edu.uclm.esi.payments.http;

import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import org.json.JSONObject;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {

    static {
        com.stripe.Stripe.apiKey = "sk_test_51RHR37IsgNFjbH8wEjf4Va3CkzmZ5VtrYL47zf2p5BtL6c49p7kZtLENnVJ97OxQtZUJxaEh56vv5mFiNhWJjBBe00A3LwjBMp";
    }

    @GetMapping("/prepay")
    public String prepay() {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setCurrency("eur")
                    .setAmount(1200L)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            JSONObject json = new JSONObject(intent.toJson());
            String clientSecret = json.getString("client_secret");
            return clientSecret;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "error al procesar tu prepago", e);
        }
    }

}
