package io.blackbox_vision.wheelview.sample.provincepick;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CityModelTest extends PickViewTestSupport {

    private CityModel model;

    @Before
    public void setUp() throws Exception {
        model = new CityModel();
    }

    @Test
    public void testToString() {
        // when
        model.id = CITY_ID;
        model.name = CITY_NAME;

        // then
        assertEquals("CityName[CityId]", model.toString());
    }

    @Test
    public void testGetText() {
        // when
        model.name = CITY_NAME;
        // then
        assertEquals(CITY_NAME, model.getText());

        // when
        model.name = null;
        // then
        assertEquals("", model.getText());
    }
}
