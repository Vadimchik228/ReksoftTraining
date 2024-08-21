package com.rntgroup.task1.services;

import com.rntgroup.task1.entities.Horse;
import com.rntgroup.task1.entities.Race;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Getter
public class RaceService {
    private static final Random random = new Random();
    private final HorseService horseService;

    @Autowired
    public RaceService(HorseService horseService) {
        this.horseService = horseService;
    }

    public Race getRace() {
        // текущее время + рандомное количество минут (до 60)
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(random.nextInt(60));

        // количество участников гонки от 2 до max
        int participantCount = random.nextInt(horseService.getHorses().size() - 1) + 2;

        Set<Horse> uniqueParticipants = new HashSet<>();
        while (uniqueParticipants.size() < participantCount) {
            uniqueParticipants.add(horseService.getHorses().get(random.nextInt(horseService.getHorses().size())));
        }

        // дистанция от 2 * 500 до 8 * 500 (1000 м до 4000 м)
        int distance = (random.nextInt(7) + 2) * 500;

        Race race = Race.builder()
                .dateTime(dateTime)
                .participants(uniqueParticipants.stream().toList())
                .distance(distance)
                .build();

        printInfo(race);

        return race;
    }

    private void printInfo(Race race) {
        System.out.println("Информация о предстоящей гонке:");
        System.out.println("Время проведения: " + getFormattedDateTime(race.getDateTime()));
        System.out.println("Участники: " + race.getParticipants());
        System.out.println("Дистанция: " + race.getDistance() + " метров");
    }

    private String getFormattedDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
