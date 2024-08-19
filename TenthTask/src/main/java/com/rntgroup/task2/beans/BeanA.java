package com.rntgroup.task2.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BeanA {
    private String username;
    private String password;
    private Integer poolSize;
    private String driver;
    private String url;
    private List<String> hosts;
}
