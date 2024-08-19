package com.rntgroup.task1.config;

import com.rntgroup.task1.entities.Breed;
import com.rntgroup.task1.entities.Horse;
import com.rntgroup.task1.entities.Rider;
import com.rntgroup.task1.services.EmulationService;
import com.rntgroup.task1.services.HorseService;
import com.rntgroup.task1.services.RaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public List<Breed> breeds() {
        List<Breed> breeds = new ArrayList<>();
        breeds.add(Breed.builder().name("Ахалтекинская").maxSpeed(65).build());
        breeds.add(Breed.builder().name("Арабская").maxSpeed(60).build());
        breeds.add(Breed.builder().name("Тракененская").maxSpeed(55).build());
        breeds.add(Breed.builder().name("Орловская").maxSpeed(50).build());
        breeds.add(Breed.builder().name("Русская верховая").maxSpeed(45).build());
        return breeds;
    }

    @Bean
    public List<Horse> horses() {
        List<Horse> horses = new ArrayList<>();
        horses.add(Horse.builder().name("Булат")
                .breed(breeds().get(0))
                .age(5)
                .rider(riders().get(0))
                .coveredDistance(0)
                .position(0)
                .build());
        horses.add(Horse.builder()
                .name("Зорька")
                .breed(breeds().get(1))
                .age(4)
                .rider(riders().get(1))
                .coveredDistance(0)
                .position(0)
                .build());
        horses.add(Horse.builder()
                .name("Гром")
                .breed(breeds().get(2))
                .age(6)
                .rider(riders().get(2))
                .coveredDistance(0)
                .position(0)
                .build());
        horses.add(Horse.builder()
                .name("Вихрь")
                .breed(breeds().get(3))
                .age(7)
                .rider(riders().get(3))
                .coveredDistance(0)
                .position(0)
                .build());
        horses.add(Horse.builder()
                .name("Вьюга")
                .breed(breeds().get(4))
                .age(8)
                .rider(riders().get(4))
                .coveredDistance(0)
                .position(0)
                .build());
        return horses;
    }

    @Bean
    public List<Rider> riders() {
        List<Rider> riders = new ArrayList<>();
        riders.add(Rider.builder().firstName("Иван").lastName("Иванов").age(25).build());
        riders.add(Rider.builder().firstName("Петр").lastName("Петров").age(28).build());
        riders.add(Rider.builder().firstName("Сергей").lastName("Сергеев").age(30).build());
        riders.add(Rider.builder().firstName("Алексей").lastName("Алексеев").age(27).build());
        riders.add(Rider.builder().firstName("Дмитрий").lastName("Дмитриев").age(29).build());
        return riders;
    }

    @Bean
    public HorseService horseService() {
        return new HorseService(horses());
    }

    @Bean
    public RaceService raceService() {
        return new RaceService(horseService());
    }

    @Bean
    public EmulationService emulationService() {
        return new EmulationService(raceService());
    }
}
