package com.rntgroup.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@UtilityClass
public class CsvWriter {

    public static void writeToCsv(Map<String, Integer> data, String fileName) throws IOException {
        if (!fileName.endsWith(".csv")) fileName += ".csv";
        File csvFile = new File("src/main/resources/" + fileName);

        if (!csvFile.exists()) csvFile.createNewFile();

        try (FileWriter writer = new FileWriter(csvFile, false)) {
            writer.append("word,amount\n");
            data.forEach((tag, count) -> {
                try {
                    writer.append(tag).append(',').append(String.valueOf(count)).append('\n');
                } catch (IOException e) {
                    System.err.println("Error writing to CSV file: " + e.getMessage());
                }
            });
        }
    }

}
