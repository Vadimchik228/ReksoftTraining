package com.rntgroup;

import com.rntgroup.parsers.domparser.PlayDomParser;
import com.rntgroup.parsers.saxparser.PlaySaxParser;
import com.rntgroup.statistics.PlayStatistics;
import com.rntgroup.statistics.TagDistribution;
import com.rntgroup.util.CsvWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ApplicationRunner {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        var playSaxParser = new PlaySaxParser();
        var playDomParser = new PlayDomParser();
        var file = new File(Objects.requireNonNull(ApplicationRunner.class.getClassLoader()
                .getResource("hamlet.xml")).getPath());

        System.out.println("Демонстрация, что оба парсера дают одинаковый результат: ");
        var saxPlay = playSaxParser.parse(file);
        var domPlay = playDomParser.parse(file);
        System.out.println(saxPlay.equals(domPlay));

        System.out.println("Количество уникальных слов в репликах Гамлета: "
                           + PlayStatistics.countUniqueHamletWords(saxPlay));

        System.out.println("Распределение xml тегов в исходном файле:");
        var distribution = TagDistribution.countXmlTags(file);
        printMap(distribution);
        CsvWriter.writeToCsv(distribution, "distribution.csv");

    }

    public static void printMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
