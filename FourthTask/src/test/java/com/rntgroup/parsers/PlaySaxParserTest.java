package com.rntgroup.parsers;

import com.rntgroup.TestBase;
import com.rntgroup.entities.Play;
import com.rntgroup.parsers.saxparser.PlaySaxParser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlaySaxParserTest extends TestBase {
    private final PlaySaxParser parser;

    public PlaySaxParserTest() throws ParserConfigurationException, SAXException {
        parser = new PlaySaxParser();
    }

    @Test
    public void testParse() throws IOException, SAXException {
        URL resourceUrl = getClass().getClassLoader().getResource("test_hamlet.xml");
        assertNotNull(resourceUrl, "Файл test_hamlet.xml не найден в resources.");
        File file = new File(resourceUrl.getPath());
        Play actualPlay = parser.parse(file);
        assertEquals(play, actualPlay);
    }

}
