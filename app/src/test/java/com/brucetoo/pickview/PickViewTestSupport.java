package com.brucetoo.pickview;

import com.brucetoo.pickview.provincepick.CityModel;
import com.brucetoo.pickview.provincepick.ProvinceModel;

import java.util.ArrayList;

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

}
