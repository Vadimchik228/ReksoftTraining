package com.rntgroup.statistics;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.rntgroup.util.TagNames.*;
import static org.junit.jupiter.api.Assertions.*;

public class TagDistributionTest {
    private final Map<String, Integer> distribution = new LinkedHashMap<>() {{
        put(LINE_TAG, 5);
        put(PERSONA_TAG, 5);
        put(STAGE_DIRECTION_TAG, 5);
        put(TITLE_TAG, 4);
        put(SPEAKER_TAG, 4);
        put(SPEECH_TAG, 3);
        put(P_TAG, 2);
        put(PERSONAE_GROUP_TAG, 2);
        put(GROUP_DESCRIPTION_TAG, 2);
        put(PLAY_TAG, 1);
        put(FM_TAG, 1);
        put(PERSONAE_TAG, 1);
        put(SCENE_DESCRIPTION_TAG, 1);
        put(PLAY_SUBTITLE_TAG, 1);
        put(ACT_TAG, 1);
        put(SCENE_TAG, 1);
    }};

    @Test
    public void testCountXmlTags() throws ParserConfigurationException, IOException, SAXException {
        URL resourceUrl = getClass().getClassLoader().getResource("test_hamlet.xml");
        assertNotNull(resourceUrl, "Файл test_hamlet.xml не найден в resources.");
        File file = new File(resourceUrl.getPath());
        var actualDistribution = TagDistribution.countXmlTags(file);

        assertEquals(actualDistribution.size(), distribution.size());
        for (Map.Entry<String, Integer> expectedEntry : distribution.entrySet()) {
            String tagName = expectedEntry.getKey();
            int expectedCount = expectedEntry.getValue();
            int actualCount = actualDistribution.getOrDefault(tagName, 0);
            assertEquals(expectedCount, actualCount, "Неверное количество вхождений для тега " + tagName);
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(actualDistribution.entrySet());
        for (int i = 0; i < sortedEntries.size() - 1; i++) {
            int currentCount = sortedEntries.get(i).getValue();
            int nextCount = sortedEntries.get(i + 1).getValue();
            assertTrue(currentCount >= nextCount, "Теги в результирующей карте не отсортированы по убыванию.");
        }
    }


}
