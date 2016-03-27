package com.bruce.pickerview;

import android.graphics.Canvas;

import com.brucetoo.pickview.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.brucetoo.pickview.BuildConfig.class, sdk = 21, manifest = "app/src/main/AndroidManifest.xml")
public class LoopViewTest {

    private LoopView loopView;

    @Before
    public void setUp() {
        MainActivity mContext = Robolectric.setupActivity(MainActivity.class);

        loopView = new LoopView(mContext);
        loopView.setTextSize(12.0f);
        loopView.setNotLoop();
        Whitebox.setInternalState(loopView, "maxTextHeight", 12);

        loopView.setArrayList(new ArrayList(Arrays.asList("1", "2", "3", "4", "5", "6", "7")));
        loopView.setInitPosition(3);
    }

    @Test
    public void testInitialSelectedItem() throws Exception {
        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(3, loopView.getSelectedItem());
    }

    @Test
    public void testChangeOfSelectedItemDown() throws Exception {
        // given
        Whitebox.setInternalState(loopView, "totalScrollY", 20);

        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(4, loopView.getSelectedItem());
    }

    @Test
    public void testChangeOfSelectedItemUp() throws Exception {
        // given
        Whitebox.setInternalState(loopView, "totalScrollY", -20);

        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(2, loopView.getSelectedItem());
    }

    @Test
    public void testChangeOfSelectedItemMaxUp() throws Exception {
        // given
        Whitebox.setInternalState(loopView, "totalScrollY", 150);

        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(0, loopView.getSelectedItem());
    }

    @Test
    public void testChangeOfSelectedItemMaxDown() throws Exception {
        // given
        Whitebox.setInternalState(loopView, "totalScrollY", -150);

        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(0, loopView.getSelectedItem());
    }
}
