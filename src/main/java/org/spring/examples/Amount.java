package org.spring.examples;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Amount {
    Float amount= 1000.50F;
    String currency = "EUR";
}
