package io.blackbox_vision.wheelview.popwindow;


import io.blackbox_vision.wheelview.sample.BuildConfig;
import io.blackbox_vision.wheelview.sample.MainActivity;
import io.blackbox_vision.wheelview.view.DatePickerPopUpWindow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "app/src/main/AndroidManifest.xml")
public class DatePickerPopUpWindowTest {

    private DatePickerPopUpWindow datePickerPopWin;

    private DatePickerPopUpWindow.Builder builder;

    @Before
    public void setUp() {

        MainActivity mContext = Robolectric.setupActivity(MainActivity.class);
        DatePickerPopUpWindow.OnDateSelectedListener mListener =
                mock(DatePickerPopUpWindow.OnDateSelectedListener.class);

        builder = new DatePickerPopUpWindow.Builder(mContext);
        datePickerPopWin = new DatePickerPopUpWindow(builder);
    }

    @Test
    public void testConstructor() {
        assertEquals(Whitebox.getInternalState(builder, "minYear"),
                     Whitebox.getInternalState(datePickerPopWin, "minYear"));
        assertEquals(Whitebox.getInternalState(builder, "maxYear"),
                Whitebox.getInternalState(datePickerPopWin, "maxYear"));
        assertEquals(Whitebox.getInternalState(builder, "textCancel"),
                Whitebox.getInternalState(datePickerPopWin, "textCancel"));
        assertEquals(Whitebox.getInternalState(builder, "textConfirm"),
                Whitebox.getInternalState(datePickerPopWin, "textConfirm"));
        assertEquals(Whitebox.getInternalState(builder, "context"),
                Whitebox.getInternalState(datePickerPopWin, "mContext"));
        assertEquals(Whitebox.getInternalState(builder, "listener"),
                Whitebox.getInternalState(datePickerPopWin, "mListener"));
        assertEquals(Whitebox.getInternalState(builder, "colorCancel"),
                Whitebox.getInternalState(datePickerPopWin, "colorCancel"));
        assertEquals(Whitebox.getInternalState(builder, "colorConfirm"),
                Whitebox.getInternalState(datePickerPopWin, "colorConfirm"));
        assertEquals(Whitebox.getInternalState(builder, "btnTextSize"),
                Whitebox.getInternalState(datePickerPopWin, "btnTextsize"));
        assertEquals(Whitebox.getInternalState(builder, "viewTextSize"),
                Whitebox.getInternalState(datePickerPopWin, "viewTextSize"));
    }
}
