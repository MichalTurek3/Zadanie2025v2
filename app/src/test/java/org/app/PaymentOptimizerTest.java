package org.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentOptimizerTest {

    private PaymentOptimizer optimizer;

    @BeforeEach
    void setUp() {
        Order order1 = new Order();
        order1.setId("ORDER1");
        order1.setValue(new BigDecimal("100.00"));
        order1.setPromotions(List.of("mZysk"));

        Order order2 = new Order();
        order2.setId("ORDER2");
        order2.setValue(new BigDecimal("200.00"));
        order2.setPromotions(List.of("BosBankrut"));

        Order order3 = new Order();
        order3.setId("ORDER3");
        order3.setValue(new BigDecimal("150.00"));
        order3.setPromotions(List.of("mZysk", "BosBankrut"));

        Order order4 = new Order();
        order4.setId("ORDER4");
        order4.setValue(new BigDecimal("50.00"));

        PaymentMethod punkty = new PaymentMethod();
        punkty.setId("PUNKTY");
        punkty.setDiscount(BigInteger.valueOf(15));
        punkty.setLimit(new BigDecimal("100.00"));

        PaymentMethod mZysk = new PaymentMethod();
        mZysk.setId("mZysk");
        mZysk.setDiscount(BigInteger.valueOf(10));
        mZysk.setLimit(new BigDecimal("180.00"));

        PaymentMethod bos = new PaymentMethod();
        bos.setId("BosBankrut");
        bos.setDiscount(BigInteger.valueOf(5));
        bos.setLimit(new BigDecimal("200.00"));

        optimizer = new PaymentOptimizer(
                List.of(order1, order2, order3, order4),
                List.of(punkty, mZysk, bos)
        );
    }

    @Test
    void testOptimizePayments_returnsExpectedSpendingPerMethod() {
        Map<String, BigDecimal> result = optimizer.optimizePayments();

        assertEquals(new BigDecimal("165.00"), result.get("mZysk"));
        assertEquals(new BigDecimal("190.00"), result.get("BosBankrut"));
        assertEquals(new BigDecimal("100.00"), result.get("PUNKTY"));
    }
}
