package com.brucetoo.pickview;

import com.brucetoo.pickview.provincepick.CityModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertEquals("CityName[CityId]", model.toString());
    }

    @Test
    public void testGetText() {
        // when
        model.name = CITY_NAME;
        // then
        Assert.assertEquals(CITY_NAME, model.getText());

        // when
        model.name = null;
        // then
        Assert.assertEquals("", model.getText());
    }
}
