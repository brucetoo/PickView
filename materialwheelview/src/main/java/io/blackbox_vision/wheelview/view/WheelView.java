package io.blackbox_vision.wheelview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.blackbox_vision.wheelview.LoopScrollListener;
import io.blackbox_vision.wheelview.R;

import static android.view.GestureDetector.SimpleOnGestureListener;


public final class WheelView extends View {
    private static final String TAG = WheelView.class.getSimpleName();

    public static final int MSG_INVALIDATE = 1000;
    public static final int MSG_SCROLL_LOOP = 2000;
    public static final int MSG_SELECTED_ITEM = 3000;

    @NonNull
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> scheduledFuture;

    @Nullable
    private LoopScrollListener loopScrollListener;

    @NonNull
    private final SimpleOnGestureListener onGestureListener  = new WheelViewGestureListener();
    private GestureDetector gestureDetector;

    private final Paint topBottomTextPaint = new Paint();  //paint that draw top and bottom text
    private final Paint centerTextPaint = new Paint();  // paint that draw center text
    private final Paint centerLinePaint = new Paint();  // paint that draw line besides center text

    private List<String> data;

    private String[] itemCount;

    private float lineSpacingMultiplier;
    private float itemHeight;

    private boolean canLoop;

    private int totalScrollY;
    private int selectedItem;
    private int textSize;
    private int maxTextWidth;
    private int maxTextHeight;
    private int topBottomTextColor;
    private int centerTextColor;
    private int centerLineColor;
    private int topLineY;
    private int bottomLineY;
    private int currentIndex;
    private int initialPosition;
    private int paddingLeftRight;
    private int paddingTopBottom;
    private int drawItemsCount;
    private int circularDiameter;
    private int widgetHeight;
    private int circularRadius;
    private int widgetWidth;

    public Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case MSG_INVALIDATE:
                invalidate();
                break;
            case MSG_SCROLL_LOOP:
                startSmoothScrollTo();
                break;
            case MSG_SELECTED_ITEM:
                itemSelected();
                break;
        }

        return false;
    });

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            initView(attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode()) {
            initView(attrs);
        }
    }

    private void initView(@NonNull AttributeSet attrs) {
        final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.WheelView);

        try {
            if (null != array) {
                topBottomTextColor = array.getColor(R.styleable.WheelView_topBottomTextColor, 0xffafafaf);
                centerTextColor = array.getColor(R.styleable.WheelView_centerTextColor, 0xff313131);
                centerLineColor = array.getColor(R.styleable.WheelView_lineColor, 0xffc5c5c5);
                canLoop = array.getBoolean(R.styleable.WheelView_canLoop, true);
                initialPosition = array.getInt(R.styleable.WheelView_initPosition, -1);
                textSize = array.getDimensionPixelSize(R.styleable.WheelView_textSize, sp2px(getContext(), 16));
                drawItemsCount = array.getInt(R.styleable.WheelView_drawItemCount, 7);
            }
        } finally {
            if (null != array) {
                array.recycle();
            }
        }

        lineSpacingMultiplier = 2.0F;

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        itemCount = new String[drawItemsCount];

        gestureDetector = new GestureDetector(getContext(), onGestureListener);
        gestureDetector.setIsLongpressEnabled(false);
    }


    private void initData() {
        if (null == data) {
            throw new IllegalArgumentException("data list must not be null!");
        }

        topBottomTextPaint.setColor(topBottomTextColor);
        topBottomTextPaint.setAntiAlias(true);
        topBottomTextPaint.setTypeface(Typeface.MONOSPACE);
        topBottomTextPaint.setTextSize(textSize);

        centerTextPaint.setColor(centerTextColor);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setTextScaleX(1.05F);
        centerTextPaint.setTypeface(Typeface.MONOSPACE);
        centerTextPaint.setTextSize(textSize);

        centerLinePaint.setColor(centerLineColor);
        centerLinePaint.setAntiAlias(true);
        centerLinePaint.setTypeface(Typeface.MONOSPACE);
        centerLinePaint.setTextSize(textSize);

        measureTextWidthHeight();

        //计算半圆周 -- maxTextHeight * lineSpacingMultiplier 表示每个item的高度  drawItemsCount = 7
        //实际显示5个,留两个是在圆周的上下面
        //lineSpacingMultiplier是指text上下的距离的值和maxTextHeight一样的意思 所以 = 2
        //drawItemsCount - 1 代表圆周的上下两面各被剪切了一半 相当于高度少了一个 maxTextHeight
        int mHalfCircumference = (int) (maxTextHeight * lineSpacingMultiplier * (drawItemsCount - 1));
        //the diameter of circular 2πr = cir, 2r = height
        circularDiameter = (int) ((mHalfCircumference * 2) / Math.PI);
        //the radius of circular
        circularRadius = (int) (mHalfCircumference / Math.PI);
        // FIXME: 7/8/16  通过控件的高度来计算圆弧的周长

        if (initialPosition == -1) {
            initialPosition = canLoop ? (data.size() + 1) / 2 : 0;
        }

        currentIndex = initialPosition;
        invalidate();
    }

    private void measureTextWidthHeight() {
        final Rect rect = new Rect();

        for (int i = 0; i < data.size(); i++) {
            final String s1 = data.get(i);

            centerTextPaint.getTextBounds(s1, 0, s1.length(), rect);

            int textWidth = rect.width();
            int textHeight = rect.height();

            maxTextWidth = (textWidth > maxTextWidth) ? textWidth : maxTextWidth;
            maxTextHeight = (textHeight > maxTextHeight) ? textHeight : maxTextHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        widgetWidth = getMeasuredWidth();
        widgetHeight = MeasureSpec.getSize(heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.i(TAG, "onMeasure -> heightMode:" + heightMode);

        itemHeight = lineSpacingMultiplier * maxTextHeight;
        //auto calculate the text's left/right value when draw
        paddingLeftRight = (widgetWidth - maxTextWidth) / 2;
        paddingTopBottom = (widgetHeight - circularDiameter) / 2;

        //topLineY = diameter/2 - itemHeight(itemHeight)/2 + paddingTopBottom
        topLineY = (int) ((circularDiameter - itemHeight) / 2.0F) + paddingTopBottom;
        bottomLineY = (int) ((circularDiameter + itemHeight) / 2.0F) + paddingTopBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (data == null) {
            super.onDraw(canvas);
            return;
        }

        super.onDraw(canvas);

        //the length of single item is itemHeight
        int mChangingItem = (int) (totalScrollY / (itemHeight));

        currentIndex = initialPosition + mChangingItem % data.size();

        if (!canLoop) { // can loop
            currentIndex = (currentIndex < 0) ? 0 : currentIndex;
            currentIndex = (currentIndex > data.size() - 1) ? data.size() - 1 : currentIndex;
        } else { //can not loop
            currentIndex = (currentIndex < 0) ? currentIndex + data.size() : currentIndex;
            currentIndex = (currentIndex > data.size() - 1) ? currentIndex - data.size() : currentIndex;
        }

        int count = 0;

        //reconfirm each item's value from dataList according to currentIndex,
        while (count < drawItemsCount) {
            int templateItem = currentIndex - (drawItemsCount / 2 - count);

            if (canLoop) {
                templateItem = (templateItem < 0) ? templateItem + data.size() : templateItem;
                templateItem = (templateItem > data.size() - 1) ? templateItem - data.size() : templateItem;
                itemCount[count] = data.get(templateItem);
            } else if (templateItem < 0 || templateItem > data.size() - 1) {
                itemCount[count] = "";
            } else {
                itemCount[count] = data.get(templateItem);
            }

            count++;
        }

        //draw top and bottom line
        canvas.drawLine(0.0F, topLineY, widgetWidth, topLineY, centerLinePaint);
        canvas.drawLine(0.0F, bottomLineY, widgetWidth, bottomLineY, centerLinePaint);

        int changingLeftY = (int) (totalScrollY % (itemHeight));
        count = 0;

        while (count < drawItemsCount) {
            canvas.save();
            // L= å * r -> å = rad
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            //get radian  L = (itemHeight * count - changingLeftY),r = circularRadius
            double radian = (itemHeight * count - changingLeftY) / circularRadius;
            // a = rad * 180 / π
            //get angle
            float angle = (float) (radian * 180 / Math.PI);

            //when angle >= 180 || angle <= 0 don't draw
            if (angle >= 180F || angle <= 0F) {
                canvas.restore();
            } else {
                // translateY = r - r*cos(å) -
                //(Math.sin(radian) * maxTextHeight) / 2 this is text offset
                int translateY = (int) (circularRadius - Math.cos(radian) * circularRadius - (Math.sin(radian) * maxTextHeight) / 2) + paddingTopBottom;

                canvas.translate(0.0F, translateY);
                //scale offset = Math.sin(radian) -> 0 - 1
                canvas.scale(1.0F, (float) Math.sin(radian));

                if (translateY <= topLineY || maxTextHeight + translateY >= bottomLineY) {
                    final int diff = (translateY <= topLineY) ? topLineY - translateY : bottomLineY - translateY;

                    final Paint topBottomPaint = (translateY <= topLineY) ? topBottomTextPaint : centerTextPaint;
                    final Paint centerPaint = (translateY <= topLineY) ? centerTextPaint : topBottomTextPaint;

                    //draw text y between 0 -> topLineY,include incomplete text
                    canvas.save();
                    canvas.clipRect(0, 0, widgetWidth, diff);
                    canvas.drawText(itemCount[count], paddingLeftRight, maxTextHeight, topBottomPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, diff, widgetWidth, (int) (itemHeight));
                    canvas.drawText(itemCount[count], paddingLeftRight, maxTextHeight, centerPaint);
                    canvas.restore();

                } else if (translateY >= topLineY && maxTextHeight + translateY <= bottomLineY) {
                    //draw center complete text
                    canvas.clipRect(0, 0, widgetWidth, (int) (itemHeight));
                    canvas.drawText(itemCount[count], paddingLeftRight, maxTextHeight, centerTextPaint);
                    //center one indicate selected item
                    selectedItem = data.indexOf(itemCount[count]);
                }

                canvas.restore();
            }

            count++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionevent) {
        switch (motionevent.getAction()) {
            case MotionEvent.ACTION_UP:
            default:
                if (!gestureDetector.onTouchEvent(motionevent)) {
                    startSmoothScrollTo();
                }
        }

        return true;
    }

    public final void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        invalidate();
    }

    /**
     * set text size
     *
     * @param size size indicate sp,not px
     */
    public final void setTextSize(float size) {
        if (size > 0) {
            textSize = sp2px(getContext(), size);
        }
    }

    public void setInitPosition(int initPosition) {
        this.initialPosition = initPosition;
        invalidate();
    }

    public void setLoopListener(@Nullable LoopScrollListener LoopListener) {
        loopScrollListener = LoopListener;
    }

    /**
     * All public method must be called before this method
     * @param list data list
     */
    public final void setItems(List<String> list) {
        this.data = list;
        initData();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    private void itemSelected() {
        if (loopScrollListener != null) {
            postDelayed(this::onItemSelected, 200L);
        }
    }

    private void onItemSelected() {
        data.get(getSelectedItem());

        if (null != loopScrollListener) {
            loopScrollListener.onItemSelect(getSelectedItem());
        }
    }

    private void cancelSchedule() {
        if (null != scheduledFuture && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

    private void startSmoothScrollTo() {
        int offset = (int) (totalScrollY % (itemHeight));
        cancelSchedule();
        scheduledFuture = executorService.scheduleWithFixedDelay(new HalfHeightRunnable(offset), 0, 10, TimeUnit.MILLISECONDS);
    }

    private void startSmoothScrollTo(float velocityY) {
        cancelSchedule();
        int velocityFling = 20;
        scheduledFuture = executorService.scheduleWithFixedDelay(new FlingRunnable(velocityY), 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    class WheelViewGestureListener extends SimpleOnGestureListener {

        @Override
        public final boolean onDown(MotionEvent motionevent) {
            cancelSchedule();
            Log.i(TAG, "WheelViewGestureListener -> onDown");
            return true;
        }

        @Override
        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            startSmoothScrollTo(velocityY);
            Log.i(TAG, "WheelViewGestureListener -> onFling");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "WheelViewGestureListener -> onScroll");
            totalScrollY = (int) ((float) totalScrollY + distanceY);

            if (!canLoop) {
                final int circleLength = (int) ((float) (data.size() - 1 - initialPosition) * (itemHeight));
                final int initPositionCircleLength = (int) (initialPosition * (itemHeight));
                final int initPositionStartY = -1 * initPositionCircleLength;

                totalScrollY = (totalScrollY < initPositionStartY) ? initPositionStartY : totalScrollY;
                totalScrollY = (totalScrollY >= circleLength) ? circleLength : totalScrollY;
            }

            invalidate();
            return true;
        }
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Use in ACTION_UP
     */
    class HalfHeightRunnable implements Runnable {
        int realTotalOffset;
        int realOffset;
        int offset;

        HalfHeightRunnable(int offset) {
            this.offset = offset;
            realTotalOffset = Integer.MAX_VALUE;
            realOffset = 0;
        }

        @Override
        public void run() {
            //first in
            if (realTotalOffset == Integer.MAX_VALUE) {

                if ((float) offset > itemHeight / 2.0F) {
                    //move to next item
                    realTotalOffset = (int) (itemHeight - (float) offset);
                } else {
                    //move to pre item
                    realTotalOffset = -offset;
                }
            }

            realOffset = (int) ((float) realTotalOffset * 0.1F);

            if (realOffset == 0) {
                realOffset = (realTotalOffset < 0) ? -1 : 1;
            }

            if (Math.abs(realTotalOffset) <= 0) {
                cancelSchedule();
                handler.sendEmptyMessage(MSG_SELECTED_ITEM);
            } else {
                totalScrollY = totalScrollY + realOffset;
                handler.sendEmptyMessage(MSG_INVALIDATE);
                realTotalOffset = realTotalOffset - realOffset;
            }
        }
    }

    /**
     * Use in {@link WheelViewGestureListener#onFling(MotionEvent, MotionEvent, float, float)}
     */
    class FlingRunnable implements Runnable {

        float velocity;
        final float velocityY;

        FlingRunnable(float velocityY) {
            this.velocityY = velocityY;
            velocity = Integer.MAX_VALUE;
        }

        @Override
        public void run() {
            if (velocity == Integer.MAX_VALUE) {
                if (Math.abs(velocityY) > 2000F) {
                    velocity = (velocityY > 0.0F) ? 2000F : - 2000F;
                } else {
                    velocity = velocityY;
                }
            }
            Log.i(TAG, "velocity->" + velocity);

            if (Math.abs(velocity) >= 0.0F && Math.abs(velocity) <= 20F) {
                cancelSchedule();
                handler.sendEmptyMessage(MSG_SCROLL_LOOP);
                return;
            }

            int i = (int) ((velocity * 10F) / 1000F);
            totalScrollY = totalScrollY - i;

            if (!canLoop) {
                float itemHeight = lineSpacingMultiplier * maxTextHeight;

                if (totalScrollY <= (int) ((float) (-initialPosition) * itemHeight)) {
                    velocity = 40F;
                    totalScrollY = (int) ((float) (-initialPosition) * itemHeight);
                } else if (totalScrollY >= (int) ((float) (data.size() - 1 - initialPosition) * itemHeight)) {
                    totalScrollY = (int) ((float) (data.size() - 1 - initialPosition) * itemHeight);
                    velocity = -40F;
                }
            }

            velocity = (velocity < 0.0F) ? velocity + 20F : velocity - 20F;
            handler.sendEmptyMessage(MSG_INVALIDATE);
        }
    }
}
