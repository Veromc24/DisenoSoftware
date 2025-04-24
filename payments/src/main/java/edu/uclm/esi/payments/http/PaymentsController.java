package edu.uclm.esi.payments.http;

import edu.uclm.esi.payments.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {

    @Autowired
    private PaymentsService service;

    @GetMapping("/prepay")
    public String prepay() {
        try {
            return this.service.prepay();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
