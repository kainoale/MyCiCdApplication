package org.spring.examples;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Payment {
    private Amount amount;
    private String status = "initial";

    @Autowired
    Payment(Amount amount){
        this.amount = amount;
    }

    public void initiatePayment() {
        System.out.println("Currency: " + amount.getCurrency());
        System.out.println("Amount: " + amount.getAmount());
        status = "started";
    }

}
