package com.brucetoo.pickview;

import android.widget.Button;
import android.widget.PopupWindow;

import com.bruce.pickerview.popwindow.DatePickerPopWin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "app/src/main/AndroidManifest.xml")
public class MainActivityTest {

    private static final String TITLE = "PickView";

    private static final String IN_PROGRESS = "Working on...";

    private static final String DEFAULT_DATE = "2013-11-11";

    private static final float BTN_TEXT_SIZE = 16.0f;

    private static final String CONFIRM_BTN_TEXT = "CONFIRM";

    private static final int CONFIRM_BTN_COLOR = (int) 0xFFFFFF009900l;

    private static final String CANCEL_BTN_TEXT = "CANCEL";

    private static final int CANCEL_BTN_COLOR = (int)0xFFFFFF999999l;


    private MainActivity mainActivity;


    @Before
    public void setUp() {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(TITLE, mainActivity.getTitle());
        assertNotNull(mainActivity.findViewById(R.id.date));
        assertNotNull(mainActivity.findViewById(R.id.province));
    }

    @Test
    public void testDatePickerPopWindow() {
        // given
        Button dateButton = (Button)mainActivity.findViewById(R.id.date);

        // when
        boolean result = dateButton.performClick();

        // then
        assertTrue(result);

        PopupWindow popupWindow = shadowOf(RuntimeEnvironment.application).getLatestPopupWindow();

        assertNotNull(popupWindow);
        assertTrue(popupWindow instanceof DatePickerPopWin);

        DatePickerPopWin datePickerpopupWindow = (DatePickerPopWin)popupWindow;

        assertEquals(CONFIRM_BTN_TEXT, datePickerpopupWindow.confirmBtn.getText());
        assertEquals(BTN_TEXT_SIZE, datePickerpopupWindow.confirmBtn.getTextSize());
        assertEquals(CONFIRM_BTN_COLOR, datePickerpopupWindow.confirmBtn.getCurrentTextColor());

        assertEquals(CANCEL_BTN_TEXT, datePickerpopupWindow.cancelBtn.getText());
        assertEquals(BTN_TEXT_SIZE, datePickerpopupWindow.cancelBtn.getTextSize());
        assertEquals(CANCEL_BTN_COLOR,
                datePickerpopupWindow.cancelBtn.getCurrentTextColor());
    }

    @Test
    public void testDatePickerPopWindowPostProcessor() {
        // given
        Button dateButton = (Button)mainActivity.findViewById(R.id.date);
        dateButton.performClick();
        DatePickerPopWin datePickerpopupWindow =
                (DatePickerPopWin) shadowOf(RuntimeEnvironment.application).getLatestPopupWindow();

        // when
        boolean result = datePickerpopupWindow.confirmBtn.performClick();

        // then
        assertTrue(result);
        assertEquals(DEFAULT_DATE, ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testProvincePickerPopWindow() {
        // given
        Button provinceButton = (Button)mainActivity.findViewById(R.id.province);

        // when
        boolean result = provinceButton.performClick();

        // then
        assertTrue(result);
        assertEquals(IN_PROGRESS, ShadowToast.getTextOfLatestToast());
    }
}
