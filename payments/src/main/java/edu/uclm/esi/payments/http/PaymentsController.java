package edu.uclm.esi.payments.http;

import edu.uclm.esi.payments.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import edu.uclm.esi.payments.services.ProxyUsers;

import java.util.Map;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {

    @Autowired
    private PaymentsService service;

    @Autowired
    private ProxyUsers proxyUsers;

    @GetMapping("/prepay")
    public String prepay() {
        try {
            return this.service.prepay();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/addCredit")
    public String addCredit(@RequestBody Map<String, Object> data) {
        int amount = (int) data.get("amount");
        return proxyUsers.addCredit(amount);
    }
}
