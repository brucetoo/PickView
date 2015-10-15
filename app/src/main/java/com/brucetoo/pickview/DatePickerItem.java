package com.brucetoo.pickview;

/**
 * 日期PickerView item信息实体类
 */
public class DatePickerItem implements PickerItem {

	public int position; // 所处的序号
	public String mDateComponent; // 日期元素

	public DatePickerItem(int num, int pos) {

		this.position = pos;
		this.mDateComponent = format2LenStr(num);
	}

	@Override
	public String getText() {

		return mDateComponent;
	}

	/**
	 * 格式成两位长度字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String format2LenStr(int num) {

		return (num < 10) ? "0" + num : String.valueOf(num);
	}
}
