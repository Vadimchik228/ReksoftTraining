package com.rntgroup.collectiontostream.statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuthorStatisticsTest extends StatisticsTestBase {

    @Test
    public void testMaxBookForAuthor() {
        var maxBookForAuthor = authorStatistics.maxBookForAuthor();
        assertEquals(6, maxBookForAuthor.size());
        assertEquals("Двенадцать стульев", maxBookForAuthor.get(ilf).get().getTitle());
        assertEquals("Двенадцать стульев", maxBookForAuthor.get(petrov).get().getTitle());
        assertEquals("Идиот", maxBookForAuthor.get(dostoevsky).get().getTitle());
        assertEquals("Портрет Дориана Грея", maxBookForAuthor.get(wilde).get().getTitle());
        assertEquals("Война миров", maxBookForAuthor.get(wells).get().getTitle());
        assertFalse(maxBookForAuthor.get(remark).isPresent());
    }

}
