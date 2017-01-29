package io.blackbox_vision.wheelview.popwindow;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.blackbox_vision.wheelview.view.DatePickerPopUpWindow;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatePickerPopUpWindow.class})
public class DatePickerPopUpWindowBuilderTest {

    private static final int MIN_YEAR = 1950;

    private static final int MAX_YEAR = 2050;

    private static final String TEXT_CANCEL = "Cancel Text";

    private static final String TEXT_CONFIRM = "Confirm Text";

    private static final String DATE_CHOSE = "2011-11-11";

    private static final int COLOR_CANCEL = 11;

    private static final int COLOR_CONFIRM = 22;

    private static final int BTN_TEXT_SIZE = 14;

    private static final int VIEW_TEXT_SIZE = 16;


    private DatePickerPopUpWindow.Builder builder;

    private Context mContext;

    private DatePickerPopUpWindow.OnDateSelectedListener mListener;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mListener = mock(DatePickerPopUpWindow.OnDateSelectedListener.class);

        builder = new DatePickerPopUpWindow.Builder(mContext);
    }

    @Test
    public void testConstructor() {
        assertEquals(mContext, Whitebox.getInternalState(builder, "context"));
        assertEquals(mListener, Whitebox.getInternalState(builder, "listener"));
    }

    @Test
    public void testMinYear() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setMinYear(MIN_YEAR);

        // then
        assertEquals(MIN_YEAR, Whitebox.getInternalState(builder, "minYear"));
        assertEquals(builder, result);
    }

    @Test
    public void testMaxYear() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setMaxYear(MAX_YEAR);

        // then
        assertEquals(MAX_YEAR, Whitebox.getInternalState(builder, "maxYear"));
        assertEquals(builder, result);
    }

    @Test
    public void testTextCancel() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setCancelButtonText(TEXT_CANCEL);

        // then
        assertEquals(TEXT_CANCEL, Whitebox.getInternalState(builder, "textCancel"));
        assertEquals(builder, result);
    }

    @Test
    public void testTextConfirm() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setConfirmButtonText(TEXT_CONFIRM);

        // then
        assertEquals(TEXT_CONFIRM, Whitebox.getInternalState(builder, "textConfirm"));
        assertEquals(builder, result);
    }

    @Test
    public void testDateChose() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setSelectedDate(DATE_CHOSE);

        // then
        assertEquals(DATE_CHOSE, Whitebox.getInternalState(builder, "dateChose"));
        assertEquals(builder, result);
    }

    @Test
    public void testColorCancel() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setCancelButtonTextColor(COLOR_CANCEL);

        // then
        assertEquals(COLOR_CANCEL, Whitebox.getInternalState(builder, "colorCancel"));
        assertEquals(builder, result);
    }

    @Test
    public void testColorConfirm() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setConfirmButtonTextColor(COLOR_CONFIRM);

        // then
        assertEquals(COLOR_CONFIRM, Whitebox.getInternalState(builder, "colorConfirm"));
        assertEquals(builder, result);
    }

    @Test
    public void testBtnTextSize() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setButtonTextSize(BTN_TEXT_SIZE);

        // then
        assertEquals(BTN_TEXT_SIZE, Whitebox.getInternalState(builder, "btnTextSize"));
        assertEquals(builder, result);
    }

    @Test
    public void testViewTextSize() {
        // when
        DatePickerPopUpWindow.Builder result = builder.setViewTextSize(VIEW_TEXT_SIZE);

        // then
        assertEquals(VIEW_TEXT_SIZE, Whitebox.getInternalState(builder, "viewTextSize"));
        assertEquals(builder, result);
    }

    @Test
    public void testBuild() throws Exception {
        // given
        DatePickerPopUpWindow mDatePickerPopWin = mock(DatePickerPopUpWindow.class);
        whenNew(DatePickerPopUpWindow.class).withArguments(builder).thenReturn(mDatePickerPopWin);

        // when
        DatePickerPopUpWindow result = builder.build();

        // then
        assertEquals(mDatePickerPopWin, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildIllegalParameters() throws Exception {
        // given
        builder
                .setMaxYear(MAX_YEAR)
                .setMinYear(MIN_YEAR);

        DatePickerPopUpWindow mDatePickerPopWin = mock(DatePickerPopUpWindow.class);
        whenNew(DatePickerPopUpWindow.class).withArguments(builder).thenReturn(mDatePickerPopWin);

        // when
        builder.build();

        // then
        fail("DatePickerPopUpWindow.Builder should throw IllegalArgumentException " +
                "when minYear is greater then maxYear");
    }

}
