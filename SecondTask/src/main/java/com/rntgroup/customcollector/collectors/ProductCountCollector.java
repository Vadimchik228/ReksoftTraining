package com.rntgroup.customcollector.collectors;

import com.rntgroup.customcollector.entities.Order;
import com.rntgroup.customcollector.entities.Product;
import com.rntgroup.customcollector.entities.Status;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.rntgroup.customcollector.entities.Status.*;

public class ProductCountCollector implements Collector<Order, Map<Status, Integer>, Map<Status, Integer>> {
    private final Product product;

    public ProductCountCollector(Product product) {
        this.product = product;
    }

    @Override
    public Supplier<Map<Status, Integer>> supplier() {
        return () -> {
            Map<Status, Integer> map = new HashMap<>();
            map.put(PENDING, 0);
            map.put(PROCESSING, 0);
            map.put(SHIPPED, 0);
            map.put(DELIVERED, 0);
            map.put(CANCELLED, 0);
            return map;
        };
    }

    @Override
    public BiConsumer<Map<Status, Integer>, Order> accumulator() {
        return (map, entity) -> {
            Status status = entity.getProducts().get(product);
            map.put(status, map.get(status) + 1);
        };
    }

    @Override
    public BinaryOperator<Map<Status, Integer>> combiner() {
        return (map1, map2) -> {
            map1.put(PENDING, map1.get(PENDING) + map2.get(PENDING));
            map1.put(PROCESSING, map1.get(PROCESSING) + map2.get(PROCESSING));
            map1.put(SHIPPED, map1.get(SHIPPED) + map2.get(SHIPPED));
            map1.put(DELIVERED, map1.get(DELIVERED) + map2.get(DELIVERED));
            map1.put(CANCELLED, map1.get(CANCELLED) + map2.get(CANCELLED));
            return map1;
        };
    }

    // Метод finisher() возвращает функцию, которая выполняет финальную трансформацию результата.
    // Функция Function.identity() возвращает накопленный результат без изменений,
    // что означает, что финальная трансформация не требует дополнительной обработки.
    @Override
    public Function<Map<Status, Integer>, Map<Status, Integer>> finisher() {
        return Function.identity();
    }

    // Характеристика IDENTITY_FINISH указывает на то,
    // что результирующая функция завершения коллектора является идентичной и не требует
    // дополнительной обработки или преобразования накопленного результата.
    // Это позволяет оптимизировать процесс сбора данных.
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
