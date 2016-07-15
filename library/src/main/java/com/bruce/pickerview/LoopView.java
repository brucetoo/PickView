package com.bruce.pickerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LoopView extends View {

    private static final String TAG = LoopView.class.getSimpleName();

    public static final int MSG_INVALIDATE = 1000;
    public static final int MSG_SCROLL_LOOP = 2000;
    public static final int MSG_SELECTED_ITEM = 3000;

    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mScheduledFuture;
    private int mTotalScrollY;
    private LoopScrollListener mLoopListener;
    private GestureDetector mGestureDetector;
    private int mSelectedItem;
    private GestureDetector.SimpleOnGestureListener mOnGestureListener;
    private Context mContext;
    private Paint mTopBottomTextPaint;  //paint that draw top and bottom text
    private Paint mCenterTextPaint;  // paint that draw center text
    private Paint mCenterLinePaint;  // paint that draw line besides center text
    private ArrayList mDataList;
    private int mTextSize;
    private int mMaxTextWidth;
    private int mMaxTextHeight;
    private int mTopBottomTextColor;
    private int mCenterTextColor;
    private int mCenterLineColor;
    private float lineSpacingMultiplier;
    private boolean mCanLoop;
    private int mTopLineY;
    private int mBottomLineY;
    private int mCurrentIndex;
    private int mInitPosition;
    private int mPaddingLeftRight;
    private int mPaddingTopBottom;
    private float mItemHeight;
    private int mDrawItemsCount;
    private int mCircularDiameter;
    private int mWidgetHeight;
    private int mCircularRadius;
    private int mWidgetWidth;


    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_INVALIDATE)
                invalidate();
            if (msg.what == MSG_SCROLL_LOOP)
                startSmoothScrollTo();
            else if (msg.what == MSG_SELECTED_ITEM)
                itemSelected();
            return false;
        }
    });

    public LoopView(Context context) {
        this(context, null);
    }

    public LoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context,attrs);
    }


    private void initView(Context context,AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoopView);
        if (array != null) {
            mTopBottomTextColor = array.getColor(R.styleable.LoopView_topBottomTextColor, 0xffafafaf);
            mCenterTextColor = array.getColor(R.styleable.LoopView_centerTextColor, 0xff313131);
            mCenterLineColor = array.getColor(R.styleable.LoopView_lineColor, 0xffc5c5c5);
            mCanLoop = array.getBoolean(R.styleable.LoopView_canLoop, true);
            mInitPosition = array.getInt(R.styleable.LoopView_initPosition, -1);
            mTextSize = array.getDimensionPixelSize(R.styleable.LoopView_textSize, sp2px(context, 16));
            mDrawItemsCount = array.getInt(R.styleable.LoopView_drawItemCount, 7);
            array.recycle();
        }

        lineSpacingMultiplier = 2.0F;

        this.mContext = context;

        mOnGestureListener = new LoopViewGestureListener();

        mTopBottomTextPaint = new Paint();
        mCenterTextPaint = new Paint();
        mCenterLinePaint = new Paint();

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        mGestureDetector = new GestureDetector(context, mOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
    }


    private void initData() {

        if (mDataList == null) {
            throw new IllegalArgumentException("data list must not be null!");
        }
        mTopBottomTextPaint.setColor(mTopBottomTextColor);
        mTopBottomTextPaint.setAntiAlias(true);
        mTopBottomTextPaint.setTypeface(Typeface.MONOSPACE);
        mTopBottomTextPaint.setTextSize(mTextSize);

        mCenterTextPaint.setColor(mCenterTextColor);
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setTextScaleX(1.05F);
        mCenterTextPaint.setTypeface(Typeface.MONOSPACE);
        mCenterTextPaint.setTextSize(mTextSize);

        mCenterLinePaint.setColor(mCenterLineColor);
        mCenterLinePaint.setAntiAlias(true);
        mCenterLinePaint.setTypeface(Typeface.MONOSPACE);
        mCenterLinePaint.setTextSize(mTextSize);

        measureTextWidthHeight();

        //计算半圆周 -- mMaxTextHeight * lineSpacingMultiplier 表示每个item的高度  mDrawItemsCount = 7
        //实际显示5个,留两个是在圆周的上下面
        //lineSpacingMultiplier是指text上下的距离的值和maxTextHeight一样的意思 所以 = 2
        //mDrawItemsCount - 1 代表圆周的上下两面各被剪切了一半 相当于高度少了一个 mMaxTextHeight
        int mHalfCircumference = (int) (mMaxTextHeight * lineSpacingMultiplier * (mDrawItemsCount - 1));
        //the diameter of circular 2πr = cir, 2r = height
        mCircularDiameter = (int) ((mHalfCircumference * 2) / Math.PI);
        //the radius of circular
        mCircularRadius = (int) (mHalfCircumference / Math.PI);
        // FIXME: 7/8/16  通过控件的高度来计算圆弧的周长

        if (mInitPosition == -1) {
            if (mCanLoop) {
                mInitPosition = (mDataList.size() + 1) / 2;
            } else {
                mInitPosition = 0;
            }
        }
        mCurrentIndex = mInitPosition;
        invalidate();
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < mDataList.size(); i++) {
            String s1 = (String) mDataList.get(i);
            mCenterTextPaint.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > mMaxTextWidth) {
                mMaxTextWidth = textWidth;
            }
            int textHeight = rect.height();
            if (textHeight > mMaxTextHeight) {
                mMaxTextHeight = textHeight;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidgetWidth = getMeasuredWidth();
        mWidgetHeight = MeasureSpec.getSize(heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Log.i(TAG, "onMeasure -> heightMode:" + heightMode);

        mItemHeight = lineSpacingMultiplier * mMaxTextHeight;
        //auto calculate the text's left/right value when draw
        mPaddingLeftRight = (mWidgetWidth - mMaxTextWidth) / 2;
        mPaddingTopBottom = (mWidgetHeight - mCircularDiameter) / 2;

        //topLineY = diameter/2 - itemHeight(mItemHeight)/2 + mPaddingTopBottom
        mTopLineY = (int) ((mCircularDiameter - mItemHeight) / 2.0F) + mPaddingTopBottom;
        mBottomLineY = (int) ((mCircularDiameter + mItemHeight) / 2.0F) + mPaddingTopBottom;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (mDataList == null) {
            super.onDraw(canvas);
            return;
        }

        super.onDraw(canvas);

        //the length of single item is mItemHeight
        int mChangingItem = (int) (mTotalScrollY / (mItemHeight));
        mCurrentIndex = mInitPosition + mChangingItem % mDataList.size();
        if (!mCanLoop) { // can loop
            if (mCurrentIndex < 0) {
                mCurrentIndex = 0;
            }
            if (mCurrentIndex > mDataList.size() - 1) {
                mCurrentIndex = mDataList.size() - 1;
            }
        } else { //can not loop
            if (mCurrentIndex < 0) {
                mCurrentIndex = mDataList.size() + mCurrentIndex;
            }
            if (mCurrentIndex > mDataList.size() - 1) {
                mCurrentIndex = mCurrentIndex - mDataList.size();
            }
        }

        int count = 0;
        String itemCount[] = new String[mDrawItemsCount];
        //reconfirm each item's value from dataList according to currentIndex,
        while (count < mDrawItemsCount) {
            int templateItem = mCurrentIndex - (mDrawItemsCount / 2 - count);
            if (mCanLoop) {
                if (templateItem < 0) {
                    templateItem = templateItem + mDataList.size();
                }
                if (templateItem > mDataList.size() - 1) {
                    templateItem = templateItem - mDataList.size();
                }
                itemCount[count] = (String) mDataList.get(templateItem);
            } else if (templateItem < 0) {
                itemCount[count] = "";
            } else if (templateItem > mDataList.size() - 1) {
                itemCount[count] = "";
            } else {
                itemCount[count] = (String) mDataList.get(templateItem);
            }
            count++;
        }

        //draw top and bottom line
        canvas.drawLine(0.0F, mTopLineY, mWidgetWidth, mTopLineY, mCenterLinePaint);
        canvas.drawLine(0.0F, mBottomLineY, mWidgetWidth, mBottomLineY, mCenterLinePaint);

        count = 0;
        int changingLeftY = (int) (mTotalScrollY % (mItemHeight));
        while (count < mDrawItemsCount) {
            canvas.save();
            // L= å * r -> å = rad
            float itemHeight = mMaxTextHeight * lineSpacingMultiplier;
            //get radian  L = (itemHeight * count - changingLeftY),r = mCircularRadius
            double radian = (itemHeight * count - changingLeftY) / mCircularRadius;
            // a = rad * 180 / π
            //get angle
            float angle = (float) (radian * 180 / Math.PI);

            //when angle >= 180 || angle <= 0 don't draw
            if (angle >= 180F || angle <= 0F) {
                canvas.restore();
            } else {
                // translateY = r - r*cos(å) -
                //(Math.sin(radian) * mMaxTextHeight) / 2 this is text offset
                int translateY = (int) (mCircularRadius - Math.cos(radian) * mCircularRadius - (Math.sin(radian) * mMaxTextHeight) / 2) + mPaddingTopBottom;
                canvas.translate(0.0F, translateY);
                //scale offset = Math.sin(radian) -> 0 - 1
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= mTopLineY) {
                    //draw text y between 0 -> mTopLineY,include incomplete text
                    canvas.save();
                    canvas.clipRect(0, 0, mWidgetWidth, mTopLineY - translateY);
                    canvas.drawText(itemCount[count], mPaddingLeftRight, mMaxTextHeight, mTopBottomTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mTopLineY - translateY, mWidgetWidth, (int) (itemHeight));
                    canvas.drawText(itemCount[count], mPaddingLeftRight, mMaxTextHeight, mCenterTextPaint);
                    canvas.restore();
                } else if (mMaxTextHeight + translateY >= mBottomLineY) {
                    //draw text y between  mTopLineY -> mBottomLineY ,include incomplete text
                    canvas.save();
                    canvas.clipRect(0, 0, mWidgetWidth, mBottomLineY - translateY);
                    canvas.drawText(itemCount[count], mPaddingLeftRight, mMaxTextHeight, mCenterTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mBottomLineY - translateY, mWidgetWidth, (int) (itemHeight));
                    canvas.drawText(itemCount[count], mPaddingLeftRight, mMaxTextHeight, mTopBottomTextPaint);
                    canvas.restore();
                } else if (translateY >= mTopLineY && mMaxTextHeight + translateY <= mBottomLineY) {
                    //draw center complete text
                    canvas.clipRect(0, 0, mWidgetWidth, (int) (itemHeight));
                    canvas.drawText(itemCount[count], mPaddingLeftRight, mMaxTextHeight, mCenterTextPaint);
                    //center one indicate selected item
                    mSelectedItem = mDataList.indexOf(itemCount[count]);
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
                if (!mGestureDetector.onTouchEvent(motionevent)) {
                    startSmoothScrollTo();
                }
        }
        return true;
    }

    public final void setCanLoop(boolean canLoop) {
        mCanLoop = canLoop;
        invalidate();
    }

    /**
     * set text size
     *
     * @param size size indicate sp,not px
     */
    public final void setTextSize(float size) {
        if (size > 0) {
            mTextSize = sp2px(mContext, size);
        }
    }

    public void setInitPosition(int initPosition) {
        this.mInitPosition = initPosition;
        invalidate();
    }

    public void setLoopListener(LoopScrollListener LoopListener) {
        mLoopListener = LoopListener;
    }

    /**
     * All public method must be called before this method
     * @param list data list
     */
    public final void setDataList(List<String> list) {
        this.mDataList = (ArrayList) list;
        initData();
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }


    private void itemSelected() {
        if (mLoopListener != null) {
            postDelayed(new SelectedRunnable(), 200L);
        }
    }

    private void cancelSchedule() {

        if (mScheduledFuture != null && !mScheduledFuture.isCancelled()) {
            mScheduledFuture.cancel(true);
            mScheduledFuture = null;
        }
    }

    private void startSmoothScrollTo() {
        int offset = (int) (mTotalScrollY % (mItemHeight));
        cancelSchedule();
        mScheduledFuture = mExecutor.scheduleWithFixedDelay(new HalfHeightRunnable(offset), 0, 10, TimeUnit.MILLISECONDS);
    }

    private void startSmoothScrollTo(float velocityY) {
        cancelSchedule();
        int velocityFling = 20;
        mScheduledFuture = mExecutor.scheduleWithFixedDelay(new FlingRunnable(velocityY), 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

        @Override
        public final boolean onDown(MotionEvent motionevent) {
            cancelSchedule();
            Log.i(TAG, "LoopViewGestureListener->onDown");
            return true;
        }

        @Override
        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            startSmoothScrollTo(velocityY);
            Log.i(TAG, "LoopViewGestureListener->onFling");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "LoopViewGestureListener->onScroll");
            mTotalScrollY = (int) ((float) mTotalScrollY + distanceY);
            if (!mCanLoop) {
                int initPositionCircleLength = (int) (mInitPosition * (mItemHeight));
                int initPositionStartY = -1 * initPositionCircleLength;
                if (mTotalScrollY < initPositionStartY) {
                    mTotalScrollY = initPositionStartY;
                }

                int circleLength = (int) ((float) (mDataList.size() - 1 - mInitPosition) * (mItemHeight));
                if (mTotalScrollY >= circleLength) {
                    mTotalScrollY = circleLength;
                }
            }

            invalidate();
            return true;
        }
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    class SelectedRunnable implements Runnable {

        @Override
        public final void run() {
            LoopScrollListener listener = LoopView.this.mLoopListener;
            int selectedItem = getSelectedItem();
            mDataList.get(selectedItem);
            listener.onItemSelect(selectedItem);
        }
    }

    /**
     * Use in ACTION_UP
     */
    class HalfHeightRunnable implements Runnable {

        int realTotalOffset;
        int realOffset;
        int offset;

        public HalfHeightRunnable(int offset) {
            this.offset = offset;
            realTotalOffset = Integer.MAX_VALUE;
            realOffset = 0;
        }

        @Override
        public void run() {
            //first in
            if (realTotalOffset == Integer.MAX_VALUE) {

                if ((float) offset > mItemHeight / 2.0F) {
                    //move to next item
                    realTotalOffset = (int) (mItemHeight - (float) offset);
                } else {
                    //move to pre item
                    realTotalOffset = -offset;
                }
            }

            realOffset = (int) ((float) realTotalOffset * 0.1F);

            if (realOffset == 0) {

                if (realTotalOffset < 0) {
                    realOffset = -1;
                } else {
                    realOffset = 1;
                }
            }
            if (Math.abs(realTotalOffset) <= 0) {
                cancelSchedule();
                mHandler.sendEmptyMessage(MSG_SELECTED_ITEM);
                return;
            } else {
                mTotalScrollY = mTotalScrollY + realOffset;
                mHandler.sendEmptyMessage(MSG_INVALIDATE);
                realTotalOffset = realTotalOffset - realOffset;
                return;
            }
        }
    }

    /**
     * Use in {@link LoopViewGestureListener#onFling(MotionEvent, MotionEvent, float, float)}
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
                    if (velocityY > 0.0F) {
                        velocity = 2000F;
                    } else {
                        velocity = -2000F;
                    }
                } else {
                    velocity = velocityY;
                }
            }
            Log.i(TAG, "velocity->" + velocity);
            if (Math.abs(velocity) >= 0.0F && Math.abs(velocity) <= 20F) {
                cancelSchedule();
                mHandler.sendEmptyMessage(MSG_SCROLL_LOOP);
                return;
            }
            int i = (int) ((velocity * 10F) / 1000F);
            mTotalScrollY = mTotalScrollY - i;
            if (!mCanLoop) {
                float itemHeight = lineSpacingMultiplier * mMaxTextHeight;
                if (mTotalScrollY <= (int) ((float) (-mInitPosition) * itemHeight)) {
                    velocity = 40F;
                    mTotalScrollY = (int) ((float) (-mInitPosition) * itemHeight);
                } else if (mTotalScrollY >= (int) ((float) (mDataList.size() - 1 - mInitPosition) * itemHeight)) {
                    mTotalScrollY = (int) ((float) (mDataList.size() - 1 - mInitPosition) * itemHeight);
                    velocity = -40F;
                }
            }
            if (velocity < 0.0F) {
                velocity = velocity + 20F;
            } else {
                velocity = velocity - 20F;
            }
            mHandler.sendEmptyMessage(MSG_INVALIDATE);
        }
    }
}
