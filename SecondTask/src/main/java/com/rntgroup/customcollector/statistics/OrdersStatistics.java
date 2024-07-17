package com.rntgroup.customcollector.statistics;

import com.rntgroup.customcollector.entities.Order;
import com.rntgroup.customcollector.entities.Product;
import com.rntgroup.customcollector.entities.Status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record OrdersStatistics(Order[] orders) {

    // Общий доход со всех заказов; учитываются только те товары, которые не отменены пользователем.
    public BigDecimal totalRevenue() {
        return Arrays.stream(orders)
                .flatMap(order -> order.getProducts().entrySet().stream())
                .filter(entry -> entry.getValue() != Status.CANCELLED)
                .map(Map.Entry::getKey)
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Подсчет общего числа товаров, пришедших на склад
    public long countDeliveredItems() {
        return Arrays.stream(orders)
                .flatMap(order -> order.getProducts().entrySet().stream())
                .filter(entry -> entry.getValue() == Status.DELIVERED)
                .count();
    }

    // Для каждого отдельного адреса подсчет числа товаров, пришедших на склад
    public Map<String, Long> countDeliveredItemsByAddress() {
        return Arrays.stream(orders)
                .collect(Collectors.groupingBy(
                        Order::getAddress,
                        Collectors.flatMapping(
                                order -> order.getProducts().entrySet().stream()
                                        .filter(entry -> entry.getValue() == Status.DELIVERED),
                                Collectors.counting()
                        )
                ));
    }
}
