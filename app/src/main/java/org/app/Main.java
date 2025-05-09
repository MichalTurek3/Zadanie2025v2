package org.app;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

            // new objectMapper to read json content
            ObjectMapper mapper = new ObjectMapper();

            // Read orders
            List<Order> orders = mapper.readValue(new File(args[0]), new TypeReference<>() {});

            // Read payment methods
            List<PaymentMethod> methods = mapper.readValue(new File(args[1]), new TypeReference<>() {});

            // Run optimizer
            PaymentOptimizer optimizer = new PaymentOptimizer(orders, methods);
            HashMap<String, BigDecimal> result = optimizer.optimizePayments();

            // Display result
            result.forEach((methodId, amount) -> System.out.println(methodId + " " + amount.setScale(2, RoundingMode.HALF_UP)));
        }

}