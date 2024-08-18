package com.rntgroup.task3.entities;

import com.rntgroup.task3.bpp.InjectRandomString;
import lombok.Getter;

@Getter
public class User {
    @InjectRandomString(value = {"Vadim", "Victor", "Vladislav", "Vladimir"})
    private String name;
}
