package io.blackbox_vision.wheelview.sample.provincepick;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class PickViewTestSupport {

    protected static final String PROVINCE_ID = "ProvinceId";

    protected static final String PROVINCE_NAME = "ProvinceName";

    protected static final int PROVINCES_NUM = 10;

    protected static final String CITY_ID = "CityId";

    protected static final String CITY_NAME = "CityName";
    
    protected static final int CITIES_NUM = 10;

    protected ArrayList<ProvinceModel> createProvinces() {

        ArrayList<ProvinceModel> provinceList = new ArrayList<>();

        for (int i=0; i<PROVINCES_NUM; i++) {
            ProvinceModel provinceModel = new ProvinceModel();
            provinceModel.id = PROVINCE_ID + "-" + i;
            provinceModel.name = PROVINCE_NAME + " " + i;

            provinceList.add(provinceModel);
        }

        return provinceList;
    }

    protected CityModel[] createCities() {

        CityModel[] cities = new CityModel[CITIES_NUM];

        for (int i=0; i<CITIES_NUM; i++) {
            cities[i] = new CityModel();
            cities[i].id = CITY_ID + "-" + i;
            cities[i].name = CITY_NAME + " " + i;
        }

        return cities;
    }

    protected void assertProvinceList(List<ProvinceModel> expected, List<ProvinceModel> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i=0; i<expected.size(); i++) {
            ProvinceModel expectedProvince = expected.get(i);
            ProvinceModel actualProvince = actual.get(i);
            assertEquals(expectedProvince.id, actualProvince.id);
            assertEquals(expectedProvince.name, actualProvince.name);

            List<CityModel> expectedCities = expectedProvince.getCityList();
            List<CityModel> actualCities = actualProvince.getCityList();

            if ((expectedCities != null) && (actualCities != null)) {
                assertCityList(expectedCities, actualCities);
            } else {
                assertEquals(
                        (expectedCities == null) || expectedCities.isEmpty(),
                        (actualCities == null) || actualCities.isEmpty()
                );
            }
        }
    }

    protected void assertCityList(List<CityModel> expected, List<CityModel> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i=0; i<expected.size(); i++) {
            assertEquals(expected.get(i).id, actual.get(i).id);
            assertEquals(expected.get(i).name, actual.get(i).name);
        }
    }

}
