package com.bruce.pickerview.popwindow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brucetoo.pickview.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({MainActivity.class, LayoutInflater.class, DatePickerPopWin.class, View.class})
public class DatePickerPopWinTest {

    private DatePickerPopWin datePickerPopWin;

    private DatePickerPopWin.Builder builder;

    @Before
    public void setUp() {

        DatePickerPopWin.OnDatePickedListener mListener =
                mock(DatePickerPopWin.OnDatePickedListener.class);
        MainActivity mContext = mock(MainActivity.class);

        LayoutInflater mLayoutInflater = mock(LayoutInflater.class);
        doReturn(mockContentView()).when(mLayoutInflater)
                .inflate(eq(com.bruce.pickerview.R.layout.layout_date_picker), any(ViewGroup.class));

        mockStatic(LayoutInflater.class);
        when(LayoutInflater.from(eq(mContext))).thenReturn(mLayoutInflater);

        builder = new DatePickerPopWin.Builder(mock(MainActivity.class), mListener);

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
                Whitebox.getInternalState(datePickerPopWin, "btnTextSize"));
        assertEquals(Whitebox.getInternalState(builder, "viewTextSize"),
                Whitebox.getInternalState(datePickerPopWin, "viewTextSize"));
    }

    private View mockContentView() {
        View mContentView = mock(View.class);

        doReturn(mock(Button.class)).when(mContentView)
                .findViewById(com.bruce.pickerview.R.id.btn_cancel);
        doReturn(mock(Button.class)).when(mContentView)
                .findViewById(com.bruce.pickerview.R.id.btn_confirm);
        doReturn(mock(Button.class)).when(mContentView)
                .findViewById(com.bruce.pickerview.R.id.picker_year);
        doReturn(mock(Button.class)).when(mContentView)
                .findViewById(com.bruce.pickerview.R.id.picker_month);
        doReturn(mock(Button.class)).when(mContentView)
                .findViewById(com.bruce.pickerview.R.id.picker_day);
        doReturn(mock(Button.class)).when(mContentView)
                .findViewById(com.bruce.pickerview.R.id.container_picker);

        return mContentView;
    }
}
