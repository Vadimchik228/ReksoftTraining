package com.rntgroup.parsers.saxparser;

import com.rntgroup.entities.Play;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class PlaySaxParser {
    private final SAXParser saxParser;
    private final PlayHandler playHandler;

    public PlaySaxParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        saxParser = spf.newSAXParser();
        playHandler = new PlayHandler();
    }

    public Play parse(File file) throws IOException, SAXException {
        saxParser.parse(file, playHandler);
        return playHandler.getPlay();
    }
}
