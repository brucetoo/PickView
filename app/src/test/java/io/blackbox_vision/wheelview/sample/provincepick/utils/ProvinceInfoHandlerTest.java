package io.blackbox_vision.wheelview.sample.provincepick.utils;

import io.blackbox_vision.wheelview.sample.provincepick.CityModel;
import io.blackbox_vision.wheelview.sample.provincepick.PickViewTestSupport;
import io.blackbox_vision.wheelview.sample.provincepick.ProvinceModel;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class ProvinceInfoHandlerTest extends PickViewTestSupport {

    private ProvinceInfoHandler provinceInfoHandler;

    private List<ProvinceModel> provinces;

    @Before
    public void setUp() {
        provinces = createProvinces();

        ProvinceModel province = provinces.get(1);

        CityModel[] cities = createCities();
        for (CityModel city : cities) {
            province.addCity(city);
        }

        provinceInfoHandler = new ProvinceInfoHandler(new ArrayList<ProvinceModel>());
    }

    @Test
    public void testConstructor() {
        assertTrue(provinceInfoHandler.getProvinceList().isEmpty());
    }

    @Test
    public void testConstructorWithNullArgument() {
        provinceInfoHandler = new ProvinceInfoHandler(null);
        assertNull(provinceInfoHandler.getProvinceList());
    }

    @Test
    public void testDocumentParsing()
            throws ParserConfigurationException, SAXException, IOException {

        // when
        parseDocument("valid-data.xml");

        // then
        assertProvinceList(provinces, provinceInfoHandler.getProvinceList());
    }

    @Test
    public void testDocumentParsingWithInitialNullProvincesList()
            throws ParserConfigurationException, SAXException, IOException {

        // given
        provinceInfoHandler = new ProvinceInfoHandler(null);

        // when
        parseDocument("valid-data.xml");

        // then
        assertProvinceList(provinces, provinceInfoHandler.getProvinceList());
    }

    @Test
    public void testEmptyDocumentParsing()
            throws ParserConfigurationException, SAXException, IOException {

        // when
        parseDocument("empty-data.xml");

        // then
        assertTrue(provinceInfoHandler.getProvinceList() == null ||
                   provinceInfoHandler.getProvinceList().isEmpty());
    }

    @Test
    public void testUnknownTagsDocumentParsing()
            throws ParserConfigurationException, SAXException, IOException {

        // given
        provinceInfoHandler = new ProvinceInfoHandler(null);

        // when
        parseDocument("unknown-data.xml");

        // then
        assertProvinceList(provinces, provinceInfoHandler.getProvinceList());
    }


    private void parseDocument(String docName)
            throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        parser.parse(this.getClass().getResourceAsStream(docName), provinceInfoHandler);
    }
}
