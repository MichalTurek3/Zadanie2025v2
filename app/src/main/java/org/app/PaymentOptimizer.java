package org.app;

import org.app.exception.LoyaltyPointsNotAcceptableException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PaymentOptimizer {

    private final List<Order> orders;

    private final HashMap<String, PaymentMethod> methodMap;

    private final HashMap<String, BigDecimal> usage = new HashMap<>();

    /// Using constant values as fields

    private final String PAYMENT_POINTS_TEN_PERCENT = "0.1";

    private final String PAYMENT_ALL_POINTS = "100";

    public PaymentOptimizer(List<Order> orders, List<PaymentMethod> methods) {
        if (orders == null || methods == null) {
            throw new IllegalArgumentException("Orders and payment methods must not be null.");
        }
        this.orders = orders;
        this.methodMap = new HashMap<>();
        for (PaymentMethod paymentMethod : methods) {
            this.methodMap.put(paymentMethod.getId(), paymentMethod);
        }
    }

    /// Main method to solve the problem

    public HashMap<String, BigDecimal> optimizePayments() {
        try {
            for (Order order : orders) {
                if (!tryBestFullDiscount(order)) {
                    tryPartialPoints(order);
                }
            }
        } catch (IllegalArgumentException e){
            System.out.println("Potential problem with loading content.");
        }
        return usage;
    }

    /// The key to good quality Java code is optimization, using helper methods :)

    ////////////////////////////////// Helper methods //////////////////////////////////////////////////

    private boolean tryBestFullDiscount(Order order) {

        if (isPointAvailable()){
            throw new LoyaltyPointsNotAcceptableException("Loyalty points arent available.");
        }

        BigDecimal orderValue = order.getValue();
        BigDecimal bestDiscount = BigDecimal.ZERO;
        String bestMethod = "";

        List<String> promoList = order.getPromotions() != null ? order.getPromotions() : List.of();

        for (String methodId : promoList) {
            if (methodMap.containsKey(methodId)) {
                PaymentMethod pm = methodMap.get(methodId);
                if (pm.getLimit().compareTo(orderValue) >= 0) {
                    BigDecimal discount = calculateDiscount(orderValue, pm.getDiscount());
                    if (discount.compareTo(bestDiscount) > 0) {
                        bestDiscount = discount;
                        bestMethod = methodId;
                    }
                }
            }
        }

        if (methodMap.get("PUNKTY").getLimit().compareTo(orderValue) >= 0) {
            BigDecimal discount = calculateDiscount(orderValue, methodMap.get("PUNKTY").getDiscount());
            if (discount.compareTo(bestDiscount) > 0) {
                bestDiscount = discount;
                bestMethod = "PUNKTY";
            }
        }

        if (!bestMethod.isEmpty()) {
            BigDecimal toPay = orderValue.subtract(bestDiscount);
            methodMap.get(bestMethod).setLimit(methodMap.get(bestMethod).getLimit().subtract(toPay));
            usage.merge(bestMethod, toPay, BigDecimal::add);
            return true;
        }
        return false;
    }

    private void tryPartialPoints(Order order) {
        if (isPointAvailable()){
            throw new LoyaltyPointsNotAcceptableException("Loyalty points arent available.");
        }

        BigDecimal orderValue = order.getValue();
        BigDecimal pointLimit = getPointLimit();
        BigDecimal minRequired = orderValue.multiply(new BigDecimal(PAYMENT_POINTS_TEN_PERCENT));

        if (pointLimit.compareTo(minRequired) < 0) return;

        BigDecimal discount = orderValue.multiply(new BigDecimal(PAYMENT_POINTS_TEN_PERCENT));
        BigDecimal toPay = orderValue.subtract(discount);
        BigDecimal pointsUsed = toPay.min(pointLimit);
        BigDecimal leftToPay = toPay.subtract(pointsUsed);

        for (PaymentMethod pm : methodMap.values()) {
            if (!pm.getId().equals("PUNKTY") && pm.getLimit().compareTo(leftToPay) >= 0) {
                methodMap.get("PUNKTY").setLimit(pointLimit.subtract(pointsUsed));
                pm.setLimit(pm.getLimit().subtract(leftToPay));

                usage.merge("PUNKTY", pointsUsed, BigDecimal::add);
                usage.merge(pm.getId(), leftToPay, BigDecimal::add);
                break;
            }
        }
    }

    private boolean isPointAvailable() {
        return !methodMap.containsKey("PUNKTY") || methodMap.get("PUNKTY").getLimit().compareTo(BigDecimal.ZERO) <= 0;
    }

    private BigDecimal getPointLimit() {
        return methodMap.getOrDefault("PUNKTY", new PaymentMethod()).getLimit() != null
                ? methodMap.get("PUNKTY").getLimit()
                : BigDecimal.ZERO;
    }

    private BigDecimal calculateDiscount(BigDecimal value, java.math.BigInteger discountPercent) {
        return value.multiply(BigDecimal.valueOf(discountPercent.longValue()))
                .divide(new BigDecimal(PAYMENT_ALL_POINTS), 2, RoundingMode.HALF_UP);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Map<String, PaymentMethod> getMethodMap() {
        return methodMap;
    }

    public Map<String, BigDecimal> getUsage() {
        return usage;
    }

    public String getPAYMENT_POINTS_TEN_PERCENT() {
        return PAYMENT_POINTS_TEN_PERCENT;
    }

    public String getPAYMENT_ALL_POINTS() {
        return PAYMENT_ALL_POINTS;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentOptimizer that = (PaymentOptimizer) o;
        return Objects.equals(orders, that.orders) && Objects.equals(methodMap, that.methodMap) && Objects.equals(usage, that.usage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders, methodMap, usage, PAYMENT_POINTS_TEN_PERCENT, PAYMENT_ALL_POINTS);
    }

    @Override
    public String toString() {
        return "PaymentOptimizer{" +
                "orders=" + orders +
                ", methodMap=" + methodMap +
                ", usage=" + usage +
                '}';
    }
}
