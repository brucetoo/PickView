package io.blackbox_vision.wheelview.sample.popwindow;


import io.blackbox_vision.wheelview.sample.BuildConfig;
import io.blackbox_vision.wheelview.sample.MainActivity;

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
public class DatePickerPopWinTest {

    private DatePickerPopWin datePickerPopWin;

    private DatePickerPopWin.Builder builder;

    @Before
    public void setUp() {

        MainActivity mContext = Robolectric.setupActivity(MainActivity.class);
        DatePickerPopWin.OnDatePickedListener mListener =
                mock(DatePickerPopWin.OnDatePickedListener.class);

        builder = new DatePickerPopWin.Builder(mContext, mListener);
        datePickerPopWin = new DatePickerPopWin(builder);
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

        assertEquals(Whitebox.getInternalState(builder, "textCancel"),
                datePickerPopWin.cancelBtn.getText());
        assertEquals(Whitebox.getInternalState(builder, "textConfirm"),
                datePickerPopWin.confirmBtn.getText());
        assertEquals(Whitebox.getInternalState(builder, "btnTextSize"),
                (int) datePickerPopWin.cancelBtn.getTextSize());
        assertEquals(Whitebox.getInternalState(builder, "btnTextSize"),
                (int) datePickerPopWin.confirmBtn.getTextSize());
    }
}
