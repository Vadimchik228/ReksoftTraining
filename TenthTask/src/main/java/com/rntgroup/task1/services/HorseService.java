package com.rntgroup.task1.services;

import com.rntgroup.task1.entities.Horse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
public class HorseService {
    List<Horse> horses;

    @Autowired
    public HorseService(List<Horse> horses) {
        this.horses = horses;
    }
}
