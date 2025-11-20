package org.example;

import org.spring.examples.Amount;
import org.spring.examples.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final Payment payment;

    @Autowired
    public HelloController(Payment payment) {
        this.payment = payment;
    }

    @GetMapping("/api/hello")
    public String hello() {
        payment.initiatePayment();

        return payment.getAmount().getCurrency() + " " + payment.getStatus();
    }
}