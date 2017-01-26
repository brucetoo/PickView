package io.blackbox_vision.wheelview.sample.provincepick;


import io.blackbox_vision.wheelview.sample.view.PickerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 省份信息实体类
 */
public class ProvinceModel implements PickerItem {

	public String id; // 省份编码
	public String name; // 名称
	private ArrayList<CityModel> mCityList; // 城市列表

	/**
	 * 添加城市信息
	 *
	 * @param city
	 */
	public void addCity(CityModel city) {

		if (null == city)
			return;

		if (null == mCityList)
			mCityList = new ArrayList<CityModel>();

		mCityList.add(city);
	}

	/**
	 * 获取某个城市
	 *
	 * @param position
	 * @return
	 */
	public CityModel getCity(int position) {

		return (position >= 0 && position < getCityCount()) ? mCityList.get(position) : null;
	}

	/**
	 * @return
	 */
	public ArrayList<CityModel> getCityList() {

		return mCityList;
	}

	/**
	 * 获取城市数量
	 *
	 * @return
	 */
	public int getCityCount() {

		return (null == mCityList) ? 0 : mCityList.size();
	}

	/**
	 * 获取城市id列表
	 *
	 * @return
	 */
	public List<String> getCityIdList() {

		int count = getCityCount();
		List<String> cityIdList = new ArrayList<String>();

		for (int i = 0; i < count; i++) {

			cityIdList.add(mCityList.get(i).id);
		}

		return cityIdList;
	}

	/**
	 * 获取城市名称列表
	 *
	 * @return
	 */
	public List<String> getCityNameList() {

		int count = getCityCount();
		List<String> cityList = new ArrayList<String>();

		for (int i = 0; i < count; i++) {

			cityList.add(mCityList.get(i).name);
		}

		return cityList;
	}

	@Override
	public String getText() {

		return (null == name) ? "" : name;
	}

	public String toString() {

		return name + "[" + id + "][" + getCityCount() + " cities]";
	}
}
