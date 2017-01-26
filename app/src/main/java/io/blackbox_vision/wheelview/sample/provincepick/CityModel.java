package io.blackbox_vision.wheelview.sample.provincepick;


import io.blackbox_vision.wheelview.sample.view.PickerItem;

/**
 * 城市信息实体类
 */
public class CityModel implements PickerItem {

	public String id; // 城市编码
	public String name; // 名称

	@Override
	public String getText() {

		return (null == name) ? "" : name;
	}

	public String toString() {

		return name + "[" + id + "]";
	}
}
