package io.blackbox_vision.wheelview.sample.provincepick;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class ProvinceModelTest extends PickViewTestSupport {

    private ProvinceModel model;

    private CityModel[] cities;

    @Before
    public void setUp() throws Exception {
        model = new ProvinceModel();
        cities = createCities();
    }

    @Test
    public void testGetCityIdList() {
        // when empty then
        assertTrue(model.getCityIdList().isEmpty());

        // when
        model.addCity(new CityModel());
        // then
        assertEquals(1, model.getCityIdList().size());
        assertNull(model.getCityIdList().get(0));

        // when
        for (CityModel city : cities) {
            model.addCity(city);
        }

        // then
        assertEquals(CITIES_NUM + 1, model.getCityIdList().size());
        assertNull(model.getCityIdList().get(0));

        for (int i=0; i<cities.length; i++) {
            assertEquals(cities[i].id, model.getCityIdList().get(i+1));
        }
    }

    @Test
    public void testGetCityNameList() {
        // when empty then
        assertTrue(model.getCityNameList().isEmpty());

        // when
        model.addCity(new CityModel());
        // then
        assertEquals(1, model.getCityNameList().size());
        assertNull(model.getCityNameList().get(0));

        // when
        for (CityModel city : cities) {
            model.addCity(city);
        }

        // then
        assertEquals(CITIES_NUM + 1, model.getCityNameList().size());
        assertNull(model.getCityNameList().get(0));

        for (int i=0; i<cities.length; i++) {
            assertEquals(cities[i].name, model.getCityNameList().get(i+1));
        }
    }

    @Test
    public void testGetCity() {
        // given
        CityModel cityModel = new CityModel();

        // when empty then
        assertNull(model.getCity(0));

        // when
        model.addCity(cityModel);
        // then
        assertEquals(cityModel, model.getCity(0));
        assertNull(model.getCity(-1));
        assertNull(model.getCity(1));

        // when
        for (CityModel city : cities) {
            model.addCity(city);
        }

        // then
        assertEquals(cityModel, model.getCity(0));

        for (int i=0; i<cities.length; i++) {
            assertEquals(cities[i], model.getCity(i+1));
        }

        assertNull(model.getCity(-1));
        assertNull(model.getCity(CITIES_NUM + 1));
    }

    @Test
    public void testAddCity() {
        // when empty then
        assertEquals(0, model.getCityCount());

        // when
        model.addCity(null);
        assertEquals(0, model.getCityCount());

        // when
        model.addCity(new CityModel());
        // then
        assertEquals(1, model.getCityCount());

        // when
        for (CityModel city : cities) {
            model.addCity(city);
        }

        // then
        assertEquals(CITIES_NUM + 1, model.getCityCount());
    }

    @Test
    public void testGetCityList() {
        // when / then
        assertNull(model.getCityList());

        // given
        CityModel cityModel = new CityModel();
        // when
        model.addCity(cityModel);
        // then
        assertEquals(1, model.getCityList().size());
        assertTrue(model.getCityList().contains(cityModel));

        // when
        for (CityModel city : cities) {
            model.addCity(city);
        }

        // then
        assertEquals(CITIES_NUM + 1, model.getCityList().size());
        assertEquals(cityModel, model.getCityList().get(0));
    }

    @Test
    public void testToString() {
        // when
        model.id = PROVINCE_ID;
        model.name = PROVINCE_NAME;
        // then
        assertEquals("ProvinceName[ProvinceId][0 cities]", model.toString());

        // when
        model.addCity(new CityModel());
        // then
        assertEquals("ProvinceName[ProvinceId][1 cities]", model.toString());

        // when
        for (CityModel city : cities) {
            model.addCity(city);
        }
        // then
        assertEquals("ProvinceName[ProvinceId][" + (CITIES_NUM + 1) + " cities]", model.toString());
    }

    @Test
    public void testGetText() {
        // when
        model.name = PROVINCE_NAME;
        // then
        assertEquals(PROVINCE_NAME, model.getText());

        // when
        model.name = null;
        // then
        assertEquals("", model.getText());
    }
}
