package com.brucetoo.pickview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期选择弹层类
 */
public class DatePickerPopWin extends PopupWindow implements OnClickListener,
        PickerView.OnPickedListener {

	private static final int DEFAULT_MIN_YEAR = 1900; // 默认最小的年份

	private static final int REQUEST_CODE_YEAR = 1; // 年份请求码
	private static final int REQUEST_CODE_MONTH = 2; // 月份请求码
	private static final int REQUEST_CODE_DAY = 3; // 日期请求码

	private Button cancelBtn;
	private Button confirmBtn;
	private PickerView yearPickerV;
	private PickerView monthPickerV;
	private PickerView dayPickerV;
	private View pickerContainerV;
	private View contentView;

	private int minYear; // 最小年份
	private int maxYear; // 最大年份
	private int yearPos = 0; // 选择的年份的位置
	private int monthPos = 0;// 选择的月份的位置
	private int dayPos = 0;// 选择的日期的位置
	private Context mContext; // 上下文
	private OnDatePickCompletedListener mListener; // 完成日期选择事件监听器

	public DatePickerPopWin(Context cxt, String dateStr,
			OnDatePickCompletedListener l) {

		this(cxt, DEFAULT_MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR),
				dateStr, l);
	}

    /**
     * 构造函数
     * @param cxt
     * @param minYear 最小年
     * @param maxYear 最大年份
     * @param dateStr 1900-01-02
     * @param l
     */
	public DatePickerPopWin(Context cxt, int minYear, int maxYear,
			String dateStr, OnDatePickCompletedListener l) {

		this.mContext = cxt;
		this.minYear = minYear;
		this.maxYear = maxYear;
		this.mListener = l;

		setSelectedDate(dateStr); // 设置选择日期
		init(); // 初始化
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void init() {

		contentView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_date_picker, null);
		cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
		confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
		yearPickerV = (PickerView) contentView.findViewById(R.id.picker_year);
		monthPickerV = (PickerView) contentView.findViewById(R.id.picker_month);
		dayPickerV = (PickerView) contentView.findViewById(R.id.picker_day);
		pickerContainerV = contentView.findViewById(R.id.container_picker);

		initPickerViews(); // 初始化年份和月份滚动选择器试图
		initDayPickerView();

		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		contentView.setOnClickListener(this);
		yearPickerV.setOnPickedListener(this);
		monthPickerV.setOnPickedListener(this);
		dayPickerV.setOnPickedListener(this);

        yearPickerV.setTextColor(Color.RED);
        yearPickerV.setMaxTextSize(70);

		yearPickerV.setRequestCode(REQUEST_CODE_YEAR);
		monthPickerV.setRequestCode(REQUEST_CODE_MONTH);
		dayPickerV.setRequestCode(REQUEST_CODE_DAY);

		setTouchable(true);
		setFocusable(true);
		// setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setAnimationStyle(R.style.FadePopWin);
		setContentView(contentView);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
	}

	/**
	 * 初始化所有滚动选择器试图
     * 年份和月份的滚动选择器单独处理
	 */
	private void initPickerViews() {

		int yearCount = maxYear - minYear;
		List<PickerItem> yearList = new ArrayList<PickerItem>();
		List<PickerItem> monthList = new ArrayList<PickerItem>();

		for (int i = 0; i < yearCount; i++) {

			yearList.add(new DatePickerItem(minYear + i, i));
		}

		for (int j = 0; j < 12; j++) {

			monthList.add(new DatePickerItem(j + 1, j));
		}

		yearPickerV.setData(yearList);
		yearPickerV.setSelected(yearPos);
		monthPickerV.setData(monthList);
		monthPickerV.setSelected(monthPos);
	}

	/**
	 * 初始化日期滚动选择器试图
	 */
	private void initDayPickerView() {

		int dayCount;
		Calendar calendar = Calendar.getInstance();
		List<PickerItem> dayList = new ArrayList<PickerItem>();

		calendar.set(Calendar.YEAR, minYear + yearPos);
		calendar.set(Calendar.MONTH, monthPos);

        //获取该月份最大的天数
		dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < dayCount; i++) {

			dayList.add(new DatePickerItem(i + 1, i));
		}

		dayPickerV.setData(dayList);
		dayPickerV.setSelected(dayPos);
	}

	/**
	 * 设置选中日期
	 * 
	 * @param dateStr
	 */
	public void setSelectedDate(String dateStr) {

		if (!TextUtils.isEmpty(dateStr)) {

			long milliseconds = getLongFromyyyyMMdd(dateStr);
			Calendar calendar = Calendar.getInstance(Locale.CHINA);

			if (milliseconds != -1) {

				calendar.setTimeInMillis(milliseconds);
				yearPos = calendar.get(Calendar.YEAR) - minYear;
				monthPos = calendar.get(Calendar.MONTH);
				dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
			}
		}
	}

	/**
	 * 显示日期选择器弹层
	 * 
	 * @param activity
	 */
	public void showPopWin(Activity activity) {

		if (null != activity) {

			TranslateAnimation trans = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0, Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0);

            //显示在最底部
			showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
					0, 0);

			trans.setDuration(400);
			trans.setInterpolator(new AccelerateDecelerateInterpolator());

			pickerContainerV.startAnimation(trans);
		}
	}

	/**
	 * 关闭日期选择器弹层
	 */
	public void dismissPopWin() {

		TranslateAnimation trans = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

		trans.setDuration(400);
		trans.setInterpolator(new AccelerateInterpolator());
		trans.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				dismiss();
			}
		});

		pickerContainerV.startAnimation(trans);
	}

	@Override
	public void onPicked(int reqCode, PickerItem item) {

		DatePickerItem dateItem = (DatePickerItem) item;

		switch (reqCode) {

		case REQUEST_CODE_YEAR:
			yearPos = dateItem.position;
			initDayPickerView();
			break;
		case REQUEST_CODE_MONTH:
			dayPos = 0;
			monthPos = dateItem.position;
            //月份选择过后要根据月份重新计算被选中的day
			initDayPickerView();
			break;
		case REQUEST_CODE_DAY:
			dayPos = dateItem.position;
			break;
		}
	}

	@Override
	public void onClick(View v) {

		if (v == contentView || v == cancelBtn) {

			dismissPopWin();
		} else if (v == confirmBtn) {

			if (null != mListener) {

				int year = minYear + yearPos;
				int month = monthPos + 1;
				int day = dayPos + 1;
				StringBuffer sb = new StringBuffer();

				sb.append(String.valueOf(year));
				sb.append("-");
				sb.append(DatePickerItem.format2LenStr(month));
				sb.append("-");
				sb.append(DatePickerItem.format2LenStr(day));

				mListener.onDatePickCompleted(year, month, day, sb.toString());
			}

			dismissPopWin();
		}
	}

    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        }
        else {
            return -1;
        }
    }

	/**
	 * 日期选择完成事件监听器接口
	 * 
	 * @author g7734
	 */
	public interface OnDatePickCompletedListener {

		/**
		 * 完成日期选择事件
		 * 
		 * @param year
		 *            年份
		 * @param month
		 *            月份
		 * @param day
		 *            日期
		 * @param dateStr
		 *            日期字符串（如：1907-01-01）
		 */
		public void onDatePickCompleted(int year, int month, int day,
                                        String dateStr);
	}
}
