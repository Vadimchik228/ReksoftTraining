package com.rntgroup.customcollector.collectors;

import com.rntgroup.customcollector.entities.Order;
import com.rntgroup.customcollector.entities.Product;
import com.rntgroup.customcollector.entities.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductCountCollectorTest {

    @Test
    public void testSupplier() {
        ProductCountCollector collector = new ProductCountCollector(getProduct());
        Map<Status, Integer> map = collector.supplier().get();
        assertNotNull(map);
        assertEquals(0, map.get(Status.PENDING));
        assertEquals(0, map.get(Status.PROCESSING));
        assertEquals(0, map.get(Status.SHIPPED));
        assertEquals(0, map.get(Status.DELIVERED));
        assertEquals(0, map.get(Status.CANCELLED));
    }

    @Test
    public void testAccumulator() {
        Product product = getProduct();
        ProductCountCollector collector = new ProductCountCollector(product);

        Map<Status, Integer> map = collector.supplier().get();
        Map<Product, Status> orderItems = new HashMap<>();
        orderItems.put(product, Status.PENDING);
        Order order = new Order("Address 1", orderItems);

        collector.accumulator().accept(map, order);
        assertEquals(1, map.get(Status.PENDING));
        assertEquals(0, map.get(Status.PROCESSING));
        assertEquals(0, map.get(Status.SHIPPED));
        assertEquals(0, map.get(Status.DELIVERED));
        assertEquals(0, map.get(Status.CANCELLED));
    }

    @Test
    public void testCombiner() {
        Product product = getProduct();
        ProductCountCollector collector = new ProductCountCollector(product);

        Map<Status, Integer> map1 = collector.supplier().get();
        Map<Status, Integer> map2 = collector.supplier().get();

        map1.put(Status.PENDING, 1);
        map2.put(Status.PENDING, 2);

        Map<Status, Integer> combinedMap = collector.combiner().apply(map1, map2);
        assertEquals(3, combinedMap.get(Status.PENDING));
    }

    @Test
    public void testFinisher() {
        Product product = getProduct();
        ProductCountCollector collector = new ProductCountCollector(product);

        Map<Status, Integer> map = collector.supplier().get();
        map.put(Status.PENDING, 1);

        Map<Status, Integer> finishedMap = collector.finisher().apply(map);
        assertEquals(1, finishedMap.get(Status.PENDING));
    }

    @Test
    public void testCharacteristics() {
        ProductCountCollector collector = new ProductCountCollector(getProduct());
        assertEquals(Collections.singleton(Collector.Characteristics.IDENTITY_FINISH), collector.characteristics());
    }

    @Test
    public void testProductCountCollector() {
        Product product1 = new Product("Product 1", "Description 1", new BigDecimal("10.00"));
        Product product2 = new Product("Product 2", "Description 2", new BigDecimal("20.00"));

        Map<Product, Status> orderItems1 = new HashMap<>();
        orderItems1.put(product1, Status.PENDING);
        orderItems1.put(product2, Status.SHIPPED);

        Map<Product, Status> orderItems2 = new HashMap<>();
        orderItems2.put(product1, Status.DELIVERED);
        orderItems2.put(product2, Status.PROCESSING);

        Order order1 = new Order("Address 1", orderItems1);
        Order order2 = new Order("Address 2", orderItems2);

        List<Order> orders = List.of(order1, order2);

        Map<Status, Integer> result = orders.stream()
                .collect(new ProductCountCollector(product1));

        assertEquals(1, result.get(Status.PENDING));
        assertEquals(1, result.get(Status.DELIVERED));
        assertEquals(0, result.get(Status.PROCESSING));
        assertEquals(0, result.get(Status.SHIPPED));
        assertEquals(0, result.get(Status.CANCELLED));
    }

    private Product getProduct() {
        return new Product("Some Product",
                "Some Description",
                new BigDecimal("10.00"));
    }

}
