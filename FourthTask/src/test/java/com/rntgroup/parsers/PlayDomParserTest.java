package com.rntgroup.parsers;

import com.rntgroup.TestBase;
import com.rntgroup.entities.Play;
import com.rntgroup.parsers.domparser.PlayDomParser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayDomParserTest extends TestBase {
    private final PlayDomParser parser;

    public PlayDomParserTest() throws ParserConfigurationException {
        parser = new PlayDomParser();
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
