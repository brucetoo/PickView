package com.bruce.pickerview;

import android.graphics.Canvas;

import com.brucetoo.pickview.*;

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
        loopView.setTextSize(16.0f);
        loopView.setArrayList(new ArrayList(Arrays.asList("1", "2", "3")));
        loopView.setInitPosition(1);
    }

    @Test
    public void testInitialSelectedItem() {
        assertEquals(0, loopView.getSelectedItem());
    }

    @Test
    public void testChangeOfSelectedItemDown() throws Exception {
        // given
        Whitebox.setInternalState(loopView, "totalScrollY", 50);

        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(0, loopView.getSelectedItem());
    }

    @Test
    public void testChangeOfSelectedItemUp() throws Exception {
        // given
        Whitebox.setInternalState(loopView, "totalScrollY", -50);

        // when
        Whitebox.invokeMethod(loopView, "onDraw", new Canvas());

        // then
        assertEquals(0, loopView.getSelectedItem());
    }
}
