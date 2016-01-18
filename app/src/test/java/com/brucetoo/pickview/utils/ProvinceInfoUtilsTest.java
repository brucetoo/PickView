package com.brucetoo.pickview.utils;

import android.content.Context;
import com.brucetoo.pickview.PickViewTestSupport;
import com.brucetoo.pickview.provincepick.CityModel;
import com.brucetoo.pickview.provincepick.ProvinceModel;
import com.brucetoo.pickview.provincepick.utils.ProvinceInfoUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

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
        Assert.assertEquals(
                VALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, validCityId, provinces));

        Assert.assertEquals(
                EMPTY_PROVINCE_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, emptyProvinceId, validCityId, provinces));

        Assert.assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, PROVINCE_ID, validCityId, provinces));

        Assert.assertEquals(
                INVALID_CITY_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, CITY_ID, provinces));

        Assert.assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, validCityId, new ArrayList<ProvinceModel>()));

        Assert.assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, null, validCityId, provinces));

        Assert.assertEquals(
                INVALID_CITY_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, null, provinces));

        Assert.assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, validProvinceId, validCityId, null));

        Assert.assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, null, null, provinces));

        Assert.assertEquals(
                INVALID_RESPONSE,
                ProvinceInfoUtils.matchAddress(contextMock, null, null, null));
    }
}
