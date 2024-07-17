package com.rntgroup.customcollector.entities;

import java.util.Map;

public class Order {
    private String address;
    private Map<Product, Status> products;

    public Order(String address, Map<Product, Status> products) {
        this.address = address;
        this.products = products;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<Product, Status> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Status> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
               "address='" + address + '\'' +
               ", products=" + products +
               '}';
    }
}
