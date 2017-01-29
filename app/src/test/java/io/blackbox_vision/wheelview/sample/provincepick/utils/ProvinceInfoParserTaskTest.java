package io.blackbox_vision.wheelview.sample.provincepick.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;

import io.blackbox_vision.wheelview.sample.provincepick.CityModel;
import io.blackbox_vision.wheelview.sample.provincepick.PickViewTestSupport;
import io.blackbox_vision.wheelview.sample.provincepick.ProvinceModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProvinceInfoParserTaskTest extends PickViewTestSupport {

    private ProvinceInfoParserTask provinceInfoParserTask;

    @Mock
    private Context mockContext;

    @Mock
    private AssetManager mockAssetManager;

    @Mock
    private Handler mockHandler;

    @Mock
    private Message mockMessage;

    private List<ProvinceModel> provinces;

    @Before
    public void setUp() throws IOException {
        provinceInfoParserTask = new ProvinceInfoParserTask(mockContext, mockHandler);

        when(mockContext.getAssets()).thenReturn(mockAssetManager);
        when(mockAssetManager.open("io/blackbox_vision/wheelview/sample/provincepick/utils/city.xml")).thenReturn(
                this.getClass().getResourceAsStream("valid-data.xml")
        );


        provinces = createProvinces();
        ProvinceModel province = provinces.get(1);
        CityModel[] cities = createCities();

        for (CityModel city : cities) {
            province.addCity(city);
        }

        doReturn(mockMessage).when(mockHandler)
                .obtainMessage(ProvinceInfoParserTask.MSG_PARSE_RESULT_CALLBACK, provinces);
    }

    @Test
    public void testConstructor() {
        assertEquals(mockContext, Whitebox.getInternalState(provinceInfoParserTask, "mContext"));
        assertEquals(mockHandler, Whitebox.getInternalState(provinceInfoParserTask, "handler"));
    }

    @Test
    public void testDoInBackground() {
        // when
        List<ProvinceModel> actual = provinceInfoParserTask.doInBackground();

        // then
        assertNotNull(actual);
        assertProvinceList(provinces, actual);
    }

    @Test
    public void testDoInBackgroundWithNullContext() {
        // given
        Whitebox.setInternalState(provinceInfoParserTask, "mContext", null);

        // when
        List<ProvinceModel> actual = provinceInfoParserTask.doInBackground();

        // then
        assertNull(actual);
    }

    @Test
    public void testOnPostExecute() {
        // when
        provinceInfoParserTask.onPostExecute(provinces);

        // then
        verify(mockHandler).sendMessage(mockMessage);
    }

    @Test
    public void testOnPostExecuteWithNullHandler() {
        // given
        Whitebox.setInternalState(provinceInfoParserTask, "handler", null);

        // when
        provinceInfoParserTask.onPostExecute(provinces);

        // then
        verify(mockHandler, never()).sendMessage(any(Message.class));
    }

}
