package io.blackbox_vision.wheelview.sample.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 滚动选择器试图类
 */
@SuppressLint("ClickableViewAccessibility")
public class PickerView extends View {

	public static final String TAG = "PickerView";

	/** text之间间距和minTextSize之比 */
	public static final float MARGIN_ALPHA = 1.5f;

	/** 自动回滚到中间的速度 */
	public static final float SPEED = 2;

	private boolean isInit = false; // 是否已初始化
	private int requestCode = 0; // 请求码-标志位
	private int txtColor = 0x333333;// 字体颜色
	private int height; // 选择器高度
	private int width;// 选择器宽度
	private int curSelectedPos;// 选中的位置，这个位置是mDataList的中心位置，一直不变
	private float lastDownY; // 最后按下的y轴坐标
	private float moveDistance = 0; // 滑动的距离
	private float maxTxtSize = 80;// 最大字体大小
	private float minTxtSize = 40;// 最小字体大小
	private float maxTxtAlpha = 255; // 最大透明度
	private float minTxtAlpha = 120;// 最小透明度
	private List<PickerItem> mDataList;// 数据列表
	private Paint mPaint;// 画笔
	private Timer mTimer;// 定时器
	private MyTimerTask mTask; // 自定义TimerTask
	private OnPickedListener mListener; // 选中事件监听器

	@SuppressLint("HandlerLeak")
	private Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (Math.abs(moveDistance) < SPEED) {

				moveDistance = 0;

				if (null != mTask) {

					mTask.cancel();
					mTask = null;
					performSelect();
				}
			} else {

				// 这里moveDistance /
				// Math.abs(moveDistance)是为了保有moveDistance的正负号，以实现上滚或下滚
				moveDistance = moveDistance - moveDistance
						/ Math.abs(moveDistance) * SPEED;
			}

			invalidate();
		}
	};

	public PickerView(Context context) {

		super(context);

		init();
	}

	public PickerView(Context context, AttributeSet attrs) {

		super(context, attrs);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		mTimer = new Timer();
		mDataList = new ArrayList<PickerItem>();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(txtColor);
	}

	/**
	 * 设置请求码
	 * 
	 * @param reqCode
	 */
	public void setRequestCode(int reqCode) {

		this.requestCode = reqCode;
	}

	/**
	 * 设置选择事件监听器
	 * 
	 * @param listener
	 */
	public void setOnPickedListener(OnPickedListener listener) {

		mListener = listener;
	}

	/**
	 * 通知选择某个item
	 */
	private void performSelect() {

		if (null != mListener && curSelectedPos >= 0
				&& curSelectedPos < mDataList.size())
			mListener.onPicked(requestCode, mDataList.get(curSelectedPos));
	}

    /**
     * 设置最大透明度 默认255
     * @param alpha
     */
    public void setMaxTextAlpha(int alpha){
        if(alpha > 0 && alpha <= 255) {
            maxTxtAlpha = alpha;
            invalidate();
        }
    }

    /**
     * 设置最小透明度 默认120
     * @param alpha
     */
    public void setMinTextAlpha(int alpha){
        if(alpha > 0) {
            minTxtSize = alpha;
            invalidate();
        }
    }

    /**
     * 设置选择选择器字体颜色
     */
    public void setTextColor(int textColor){
        mPaint.setColor(textColor);
        invalidate();
    }

	/**
	 * 设置数据源
	 * 
	 * @param datas
	 *            PickerItem列表
	 */
	public void setData(List<PickerItem> datas) {

		int size = (null == datas) ? 0 : datas.size();

		if (size == 0)
			return;

		mDataList = datas;
		curSelectedPos = size / 2;

		invalidate();
	}

	/**
	 * 设置选中的位置
	 * 
	 * @param selected
	 */
	public void setSelected(int selected) {

		curSelectedPos = selected;

		int distance = mDataList.size() / 2 - curSelectedPos;

		if (distance < 0) {

			for (int i = 0; i < -distance; i++) {

				moveHeadToTail();
				curSelectedPos--;
			}
		} else if (distance > 0) {

			for (int i = 0; i < distance; i++) {

				moveTailToHead();
				curSelectedPos++;
			}
		}

		invalidate();
	}

	/**
	 * 把第一个item移到最后一个位置
	 */
	private void moveHeadToTail() {

		PickerItem head = mDataList.get(0);

		mDataList.remove(0);
		mDataList.add(head);
	}

	/**
	 * 把最后的item移到第一个位置
	 */
	private void moveTailToHead() {

		PickerItem tail = mDataList.get(mDataList.size() - 1);

		mDataList.remove(mDataList.size() - 1);
		mDataList.add(0, tail);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		height = getMeasuredHeight();
		width = getMeasuredWidth();
		// 按照View的高度计算字体大小
		maxTxtSize = height / 4.0f;
		minTxtSize = maxTxtSize / 2f;
		isInit = true;

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// 根据index绘制view
		if (isInit)
			drawData(canvas);
	}

	/**
	 * 绘制所有item
	 * 
	 * @param canvas
	 */
	private void drawData(Canvas canvas) {

		if (curSelectedPos < 0 || (null == mDataList)
				|| (curSelectedPos >= mDataList.size()))
			return;

		// 先绘制选中的text再往上往下绘制其余的text
		float scale = parabola(height / 4.0f, moveDistance);
		float size = (maxTxtSize - minTxtSize) * scale + minTxtSize;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((maxTxtAlpha - minTxtAlpha) * scale + minTxtAlpha));
		// text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
		float x = (float) (width / 2.0);
		float y = (float) (height / 2.0 + moveDistance);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		String text = mDataList.get(curSelectedPos).getText();

		canvas.drawText(formatText(text, size), x, baseline, mPaint);

		// 绘制上方data
		for (int i = 1; (curSelectedPos - i) >= 0; i++) {

			drawOtherText(canvas, i, -1);
		}

		// 绘制下方data
		for (int i = 1; (curSelectedPos + i) < mDataList.size(); i++) {

			drawOtherText(canvas, i, 1);
		}
	}

	/**
	 * 绘制非选中的item
	 * 
	 * @param canvas
	 * @param position
	 *            距离curSelectedPos的差值
	 * @param type
	 *            1表示向下绘制，-1表示向上绘制
	 */
	private void drawOtherText(Canvas canvas, int position, int type) {

		float d = MARGIN_ALPHA * minTxtSize * position + type
				* moveDistance;
		float scale = parabola(height / 4.0f, d);
		float size = (maxTxtSize - minTxtSize) * scale + minTxtSize;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((maxTxtAlpha - minTxtAlpha) * scale + minTxtAlpha));
		float y = (float) (height / 2.0 + type * d);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		String text = mDataList.get(curSelectedPos + type * position).getText();

		canvas.drawText(formatText(text, size), (float) (width / 2.0),
				baseline, mPaint);
	}

	/**
	 * 抛物线
	 * 
	 * @param zero
	 *            零点坐标
	 * @param x
	 *            偏移量
	 * @return scale
	 */
	private float parabola(float zero, float x) {

		float f = (float) (1 - Math.pow(x / zero, 2));

		return ((f < 0) ? 0 : f);
	}

	/**
	 * 格式化文本，超出宽度则用省略号
	 * 
	 * @param text
	 * @param size
	 * @return
	 */
	private String formatText(String text, float size) {

		if (null == text)
			return "";

		if (TextUtils.isDigitsOnly(text)) {

			return text;
		} else {

			if (size * text.length() > width) {

				int maxLen = (int) (width / size);

				return text.substring(0, maxLen - 1) + "...";
			}
		}

		return text;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (null == mDataList || mDataList.isEmpty())
			return true;

		switch (event.getActionMasked()) {

		case MotionEvent.ACTION_DOWN:
			doDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			doMove(event);
			break;
		case MotionEvent.ACTION_UP:
			doUp(event);
			break;
		}

		return true;
	}

	/**
	 * 执行按下操作
	 * 
	 * @param event
	 */
	private void doDown(MotionEvent event) {

		if (mTask != null) {

			mTask.cancel();
			mTask = null;
		}

		lastDownY = event.getY();
	}

	/**
	 * 执行滑动操作
	 * 
	 * @param event
	 */
	private void doMove(MotionEvent event) {

		moveDistance += (event.getY() - lastDownY);

		if (moveDistance > MARGIN_ALPHA * minTxtSize / 2) {// 往下滑超过离开距离

			moveTailToHead();
			moveDistance = moveDistance - MARGIN_ALPHA * minTxtSize;
		} else if (moveDistance < -MARGIN_ALPHA * minTxtSize / 2) {// 往上滑超过离开距离

			moveHeadToTail();
			moveDistance = moveDistance + MARGIN_ALPHA * minTxtSize;
		}

		lastDownY = event.getY();

		invalidate();
	}

	/**
	 * 执行滑动完成操作
	 * 
	 * @param event
	 */
	private void doUp(MotionEvent event) {

		// 抬起手后curSelectedPos的位置由当前位置move到中间选中位置
		if (Math.abs(moveDistance) < 0.0001) {

			moveDistance = 0;
			return;
		}

		if (null != mTask) {

			mTask.cancel();
			mTask = null;
		}

		mTask = new MyTimerTask(updateHandler);
		mTimer.schedule(mTask, 0, 10);
	}

	/**
	 * 自定义TimerTask类
	 */
	class MyTimerTask extends TimerTask {

		Handler handler;

		public MyTimerTask(Handler handler) {

			this.handler = handler;
		}

		@Override
		public void run() {

			handler.sendMessage(handler.obtainMessage());
		}
	}

	/**
	 * 滚动选择控件完成选择事件监听器接口类
	 */
	public interface OnPickedListener {

		/**
		 * 选中事件
		 * 
		 * @param pickerId
		 *            选择器id
		 * @param item
		 *            选择的item
		 */
        void onPicked(int pickerId, PickerItem item);
	}
}
