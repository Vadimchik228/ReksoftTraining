package com.rntgroup.customcollector.statistics;

import com.rntgroup.customcollector.entities.Order;
import com.rntgroup.customcollector.entities.Product;
import com.rntgroup.customcollector.entities.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdersStatisticsTest {
    private OrdersStatistics ordersStatistics;

    @BeforeEach
    public void setUp() {
        Product product1 = new Product("Product 1", "Description 1", new BigDecimal("100.00"));
        Product product2 = new Product("Product 2", "Description 2", new BigDecimal("200.00"));

        Order order1 = new Order("Address1", Map.of(product1, Status.DELIVERED, product2, Status.PROCESSING));
        Order order2 = new Order("Address2", Map.of(product1, Status.CANCELLED, product2, Status.DELIVERED));

        ordersStatistics = new OrdersStatistics(new Order[]{order1, order2});
    }

    @Test
    public void testTotalRevenue() {
        BigDecimal expectedRevenue = new BigDecimal("500.00");
        assertEquals(expectedRevenue, ordersStatistics.totalRevenue());
    }

    @Test
    public void testCountDeliveredItems() {
        long expectedCount = 2;
        assertEquals(expectedCount, ordersStatistics.countDeliveredItems());
    }

    @Test
    public void testCountDeliveredItemsByAddress() {
        Map<String, Long> expectedCountByAddress = Map.of(
                "Address1", 1L,
                "Address2", 1L
        );
        assertEquals(expectedCountByAddress, ordersStatistics.countDeliveredItemsByAddress());
    }
}
