package com.rntgroup.task1.services;

import com.rntgroup.task1.entities.Bet;
import com.rntgroup.task1.entities.Horse;
import com.rntgroup.task1.entities.Race;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class EmulationService {
    private static final Random random = new Random();
    private final RaceService raceService;

    @Autowired
    public EmulationService(RaceService raceService) {
        this.raceService = raceService;
    }

    public void emulateCompetition() {
        Race race = raceService.getRace();
        List<Horse> horses = race.getParticipants();

        Bet bet = makeBet(horses);

        List<Horse> finishedHorses = emulateRace(horses, race.getDistance());

        // Сортировка лошадей по финишному времени
        finishedHorses.sort(Comparator.comparingInt(Horse::getPosition));

        printRaceResults(finishedHorses, bet);
    }

    private List<Horse> emulateRace(final List<Horse> horses, final int distance) {
        List<Horse> finishedHorses = new ArrayList<>();

        try (ExecutorService executor = Executors.newFixedThreadPool(horses.size())) {
            CountDownLatch latch = new CountDownLatch(horses.size());

            for (Horse horse : horses) {
                executor.execute(() -> {
                    try {
                        while (horse.getCoveredDistance() < distance) {
                            int progress = random.nextInt(100) + 50; // от 50 до 100 метров
                            horse.setCoveredDistance(horse.getCoveredDistance() + progress);

                            printCurrentHorsePosition(horse, distance);

                            TimeUnit.SECONDS.sleep(1);
                        }

                        synchronized (finishedHorses) {
                            finishedHorses.add(horse);
                        }

                        latch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            try {
                latch.await(); // Ожидание, пока не прибегут все лошади
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return finishedHorses;
    }


    private Bet makeBet(final List<Horse> participants) {
        System.out.println("Список лошадей:");
        for (int i = 0; i < participants.size(); i++) {
            System.out.println((i + 1) + ". " + participants.get(i).getName());
        }

        int horseChoice = getHorseChoice(participants);

        BigDecimal betAmount = getBetAmount();

        return Bet.builder()
                .horse(participants.get(horseChoice - 1))
                .amount(betAmount)
                .build();
    }

    private void printCurrentHorsePosition(final Horse horse, final int distance) {
        if (horse.getCoveredDistance() < distance) {
            System.out.println("Лошадь " + horse.getName() +
                               " преодолела " + horse.getCoveredDistance() + "м");
        } else {
            System.out.println("Лошадь " + horse.getName() + " дошла до финиша!");
        }
    }

    private int getHorseChoice(final List<Horse> participants) {
        Scanner scanner = new Scanner(System.in);

        int horseChoice;
        do {
            System.out.print("Введите номер лошади, на которую хотите сделать ставку: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Некорректный ввод. Введите число.");
                scanner.next();
            }
            horseChoice = scanner.nextInt();
            scanner.nextLine(); // Очищаем буфер ввода

            if (horseChoice < 1 || horseChoice > participants.size()) {
                System.out.println("Неверный номер лошади. " +
                                   "Пожалуйста, введите число от 1 до " + participants.size());
            }
        } while (horseChoice < 1 || horseChoice > participants.size());
        return horseChoice;
    }

    private BigDecimal getBetAmount() {
        Scanner scanner = new Scanner(System.in);

        BigDecimal betAmount;
        do {
            System.out.print("Введите сумму ставки: ");
            while (!scanner.hasNextBigDecimal()) {
                System.out.println("Некорректный ввод. Введите число.");
                scanner.next();
            }
            betAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Очищаем буфер ввода

            if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Сумма ставки должна быть положительной. " +
                                   "Пожалуйста, введите число больше 0.");
            }
        } while (betAmount.compareTo(BigDecimal.ZERO) <= 0);
        return betAmount;
    }

    private void printRaceResults(final List<Horse> finishedHorses, final Bet bet) {
        System.out.println("Результаты гонки:");
        for (int i = 0; i < finishedHorses.size(); i++) {
            System.out.println((i + 1) + ". " + finishedHorses.get(i).getName());
        }

        Horse winner = finishedHorses.get(0);
        System.out.println("Победитель: " + winner.getName());

        if (winner.equals(bet.getHorse())) {
            System.out.println("Поздравляем! Вы выиграли ставку на " + winner.getName() + "!");
        } else {
            System.out.println("К сожалению, вы проиграли " + bet.getAmount() + " рублей.");
        }
    }
}
