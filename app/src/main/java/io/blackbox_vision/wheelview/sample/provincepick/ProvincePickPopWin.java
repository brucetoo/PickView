package io.blackbox_vision.wheelview.sample.provincepick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import io.blackbox_vision.wheelview.sample.R;
import io.blackbox_vision.wheelview.sample.provincepick.utils.ProvinceInfoParserTask;
import io.blackbox_vision.wheelview.sample.view.PickerItem;
import io.blackbox_vision.wheelview.sample.view.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择popwindow
 */
public class ProvincePickPopWin extends PopupWindow implements OnClickListener, PickerView.OnPickedListener {

	private static final int REQUEST_CODE_PROVINCE = 1; // 省份选择标识
	private static final int REQUEST_CODE_CITY = 2; // 城市选择标识

	private Button cancelBtn;
	private Button confirmBtn;
	private PickerView provincePickerV;
	private PickerView cityPickerV;
	private View pickerContainerV;
	private View contentView;

	private boolean isProvinceInit = false; // 是否已经初始化省份信息
	private String mProvince; // 省份名称
	private String mProvinceId; // 省份id
	private String mCity; // 城市名称
	private String mCityId; // 城市id
	private Context mContext; // 上下文
	private OnAddressPickCompletedListener mListener;// 地址选择完成事件监听器
	private List<ProvinceModel> mProvinceList = null; // 省份信息列表

    /**
     * g构造函数
     * @param cxt
     * @param provinceId 需要显示的省份ID
     * @param cityId     需要显示的城市Id
     * @param provinceList 省份列表
     * @param l 选中监听
     */
	public ProvincePickPopWin(Context cxt, String provinceId, String cityId, ArrayList<ProvinceModel> provinceList, OnAddressPickCompletedListener l) {

		this.mContext = cxt;
		this.mListener = l;
		this.mProvinceId = String.valueOf(provinceId);
		this.mCityId = String.valueOf(cityId);
		this.mProvinceList = provinceList;

		init();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void init() {

		contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_date_picker, null);
		cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
		confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
		provincePickerV = (PickerView) contentView.findViewById(R.id.picker_year);
		cityPickerV = (PickerView) contentView.findViewById(R.id.picker_month);
		pickerContainerV = contentView.findViewById(R.id.container_picker);
		contentView.findViewById(R.id.picker_day).setVisibility(View.GONE);

		if (null == mProvinceList) {

			(new ProvinceInfoParserTask(mContext, mHandler)).execute();
		} else {

			initPickerViews(mProvinceId, mCityId);
		}

		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		contentView.setOnClickListener(this);
		provincePickerV.setOnPickedListener(this);
		cityPickerV.setOnPickedListener(this);

		provincePickerV.setRequestCode(REQUEST_CODE_PROVINCE);
		cityPickerV.setRequestCode(REQUEST_CODE_CITY);

		setTouchable(true);
		setFocusable(true);
		// setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setAnimationStyle(R.style.FadeInPopWin);
		setContentView(contentView);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
	}

	/**
	 * 初始化选择器试图
	 */
	private void initPickerViews(String provinceId, String cityId) {

		if (!isProvinceInit || !mProvinceId.equals(provinceId)) {

			int selectedPos = -1;
			int count = getProvinceCount();
			ProvinceModel provinceModel;
			List<PickerItem> provinceItemList = new ArrayList<PickerItem>();

			for (int i = 0; i < count; i++) {

				provinceModel = mProvinceList.get(i);

				if (null == provinceModel)
					continue;

				provinceItemList.add(provinceModel);

				if (provinceModel.id.equals(provinceId)) {

					selectedPos = i;
					mProvinceId = provinceModel.id;
					mProvince = provinceModel.name;
					initCityPickerView(provinceModel, cityId);
				}
			}

			if (selectedPos == -1) {

				selectedPos = 0;
				provinceModel = mProvinceList.get(0);

				if (null != provinceModel) {

					mProvinceId = provinceId;
					mProvince = provinceModel.name;
				}

				initCityPickerView(provinceModel, "");
			}

			provincePickerV.setData(provinceItemList);
			provincePickerV.setSelected(selectedPos);
		}

		isProvinceInit = true;
	}

	/**
	 * 初始化城市选择器
	 * 
	 * @param provinceModel
	 * @param cityId
	 */
	private void initCityPickerView(ProvinceModel provinceModel, String cityId) {

		if (null == provinceModel)
			return;

		int selectedPos = -1;
		int count = provinceModel.getCityCount();
		CityModel cityModel;
		List<PickerItem> cityItemList = new ArrayList<PickerItem>();

		for (int i = 0; i < count; i++) {

			cityModel = provinceModel.getCity(i);

			if (null == cityModel)
				continue;

			cityItemList.add(cityModel);

			if (cityModel.id.equals(cityId)) {

				selectedPos = i;
				mCityId = cityModel.id;
				mCity = cityModel.name;
			}
		}

		if (selectedPos == -1) {

			selectedPos = 0;
			cityModel = provinceModel.getCity(0);
			mCityId = cityModel.id;
			mCity = cityModel.name;
		}

		cityPickerV.setData(cityItemList);
		cityPickerV.setSelected(selectedPos);
	}

	/**
	 * 获取省份数量
	 * 
	 * @return
	 */
	private int getProvinceCount() {

		return (null == mProvinceList) ? 0 : mProvinceList.size();
	}

	/**
	 * 显示地址选择器弹层
	 * 
	 * @param activity
	 */
	public void showPopWin(Activity activity) {

		if (null != activity) {

			TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);

			showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

			trans.setDuration(400);
			trans.setInterpolator(new AccelerateDecelerateInterpolator());

			pickerContainerV.startAnimation(trans);
		}
	}

	/**
	 * 关闭地址选择器弹层
	 */
	public void dismissPopWin() {

		TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

		trans.setDuration(400);
		trans.setInterpolator(new AccelerateInterpolator());
		trans.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
		pickerContainerV.startAnimation(trans);
	}

	/**
	 * 消息分发Handler
	 */
	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			switch (msg.what) {

			case ProvinceInfoParserTask.MSG_PARSE_RESULT_CALLBACK:
				mProvinceList = (ArrayList<ProvinceModel>) msg.obj;
				initPickerViews(mProvinceId, mCityId);
				break;
			}
		}
	};

	@Override
	public void onPicked(int reqCode, PickerItem item) {

		switch (reqCode) {

		case REQUEST_CODE_PROVINCE:

			ProvinceModel provinceModel = (ProvinceModel) item;

			mProvinceId = provinceModel.id;
			mProvince = provinceModel.name;
			initCityPickerView(provinceModel, mCityId);
			break;
		case REQUEST_CODE_CITY:

			CityModel cityModel = (CityModel) item;

			mCityId = cityModel.id;
			mCity = cityModel.name;
			break;
		}
	}

	@Override
	public void onClick(View v) {

		if (v == contentView || v == cancelBtn) {

			dismissPopWin();
		} else if (v == confirmBtn) {

			if (null != mListener)
				mListener.onAddressPickCompleted(mProvince, mProvinceId, mCity, mCityId);

			dismissPopWin();
		}
	}

	/**
	 * 地址选择完成事件监听器接口
	 */
	public interface OnAddressPickCompletedListener {

		/**
		 * 完成地址选择事件
		 * 
		 * @param province
		 *            省份名称
		 * @param provinceId
		 *            省份id
		 * @param city
		 *            城市名称
		 * @param cityId
		 *            城市id
		 */
        void onAddressPickCompleted(String province, String provinceId, String city, String cityId);
	}
}
