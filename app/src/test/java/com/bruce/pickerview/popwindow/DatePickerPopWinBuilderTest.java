package com.bruce.pickerview.popwindow;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatePickerPopWin.class})
public class DatePickerPopWinBuilderTest {

    private static final int MIN_YEAR = 1950;

    private static final int MAX_YEAR = 2050;

    private static final String TEXT_CANCEL = "Cancel Text";

    private static final String TEXT_CONFIRM = "Confirm Text";

    private static final String DATE_CHOSE = "2011-11-11";

    private static final int COLOR_CANCEL = 11;

    private static final int COLOR_CONFIRM = 22;

    private static final int BTN_TEXT_SIZE = 14;

    private static final int VIEW_TEXT_SIZE = 16;


    private DatePickerPopWin.Builder builder;

    private Context mContext;

    private DatePickerPopWin.OnDatePickedListener mListener;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mListener = mock(DatePickerPopWin.OnDatePickedListener.class);

        builder = new DatePickerPopWin.Builder(mContext, mListener);
    }

    @Test
    public void testConstructor() {
        assertEquals(mContext, Whitebox.getInternalState(builder, "context"));
        assertEquals(mListener, Whitebox.getInternalState(builder, "listener"));
    }

    @Test
    public void testMinYear() {
        // when
        DatePickerPopWin.Builder result = builder.minYear(MIN_YEAR);

        // then
        assertEquals(MIN_YEAR, Whitebox.getInternalState(builder, "minYear"));
        assertEquals(builder, result);
    }

    @Test
    public void testMaxYear() {
        // when
        DatePickerPopWin.Builder result = builder.maxYear(MAX_YEAR);

        // then
        assertEquals(MAX_YEAR, Whitebox.getInternalState(builder, "maxYear"));
        assertEquals(builder, result);
    }

    @Test
    public void testTextCancel() {
        // when
        DatePickerPopWin.Builder result = builder.textCancel(TEXT_CANCEL);

        // then
        assertEquals(TEXT_CANCEL, Whitebox.getInternalState(builder, "textCancel"));
        assertEquals(builder, result);
    }

    @Test
    public void testTextConfirm() {
        // when
        DatePickerPopWin.Builder result = builder.textConfirm(TEXT_CONFIRM);

        // then
        assertEquals(TEXT_CONFIRM, Whitebox.getInternalState(builder, "textConfirm"));
        assertEquals(builder, result);
    }

    @Test
    public void testDateChose() {
        // when
        DatePickerPopWin.Builder result = builder.dateChose(DATE_CHOSE);

        // then
        assertEquals(DATE_CHOSE, Whitebox.getInternalState(builder, "dateChose"));
        assertEquals(builder, result);
    }

    @Test
    public void testColorCancel() {
        // when
        DatePickerPopWin.Builder result = builder.colorCancel(COLOR_CANCEL);

        // then
        assertEquals(COLOR_CANCEL, Whitebox.getInternalState(builder, "colorCancel"));
        assertEquals(builder, result);
    }

    @Test
    public void testColorConfirm() {
        // when
        DatePickerPopWin.Builder result = builder.colorConfirm(COLOR_CONFIRM);

        // then
        assertEquals(COLOR_CONFIRM, Whitebox.getInternalState(builder, "colorConfirm"));
        assertEquals(builder, result);
    }

    @Test
    public void testBtnTextSize() {
        // when
        DatePickerPopWin.Builder result = builder.btnTextSize(BTN_TEXT_SIZE);

        // then
        assertEquals(BTN_TEXT_SIZE, Whitebox.getInternalState(builder, "btnTextSize"));
        assertEquals(builder, result);
    }

    @Test
    public void testViewTextSize() {
        // when
        DatePickerPopWin.Builder result = builder.viewTextSize(VIEW_TEXT_SIZE);

        // then
        assertEquals(VIEW_TEXT_SIZE, Whitebox.getInternalState(builder, "viewTextSize"));
        assertEquals(builder, result);
    }

    @Test
    public void testBuild() throws Exception {
        // given
        DatePickerPopWin mDatePickerPopWin = mock(DatePickerPopWin.class);
        whenNew(DatePickerPopWin.class).withArguments(builder).thenReturn(mDatePickerPopWin);

        // when
        DatePickerPopWin result = builder.build();

        // then
        assertEquals(mDatePickerPopWin, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildIllegalParameters() throws Exception {
        // given
        builder
                .minYear(MAX_YEAR)
                .maxYear(MIN_YEAR);

        DatePickerPopWin mDatePickerPopWin = mock(DatePickerPopWin.class);
        whenNew(DatePickerPopWin.class).withArguments(builder).thenReturn(mDatePickerPopWin);

        // when
        builder.build();

        // then
        fail("DatePickerPopWin.Builder should throw IllegalArgumentException " +
                "when minYear is greater then maxYear");
    }

}
