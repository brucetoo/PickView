package io.blackbox_vision.wheelview.sample.provincepick.utils;

import android.content.Context;
import io.blackbox_vision.wheelview.sample.provincepick.PickViewTestSupport;
import io.blackbox_vision.wheelview.sample.provincepick.CityModel;
import io.blackbox_vision.wheelview.sample.provincepick.ProvinceModel;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class ProvinceInfoUtilsTest extends PickViewTestSupport {

    private static final String VALID_RESPONSE = "ProvinceName 0  CityName 0";

    private static final String EMPTY_PROVINCE_RESPONSE = "ProvinceName 1  其他";

    private static final String INVALID_CITY_RESPONSE = "ProvinceName 0  其他";

    private static final String INVALID_RESPONSE = "其他地区  其他";

    @Test
    public void testMatchAddress() throws IOException {
        // when
        Context contextMock = Mockito.mock(Context.class);
        String validProvinceId = PROVINCE_ID + "-0";
        String emptyProvinceId = PROVINCE_ID + "-1";
        String validCityId = CITY_ID + "-0";

        ArrayList<ProvinceModel> provinces = createProvinces();
        provinces.add(null);

        CityModel[] cities = createCities();

        ProvinceModel provinceModel = provinces.get(0);
        provinceModel.addCity(null);
        for (CityModel city : cities) {
            provinceModel.addCity(city);
        }


        // then
        assertEquals(
                VALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, validCityId, provinces));

        assertEquals(
                EMPTY_PROVINCE_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, emptyProvinceId, validCityId, provinces));

        assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, PROVINCE_ID, validCityId, provinces));

        assertEquals(
                INVALID_CITY_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, CITY_ID, provinces));

        assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, validCityId, new ArrayList<ProvinceModel>()));

        assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, null, validCityId, provinces));

        assertEquals(
                INVALID_CITY_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, null, provinces));

        assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, validCityId, null));

        assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, null, null, provinces));

        assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, null, null, null));
    }
}
