package io.blackbox_vision.wheelview.sample.provincepick.utils;


import io.blackbox_vision.wheelview.sample.provincepick.CityModel;
import io.blackbox_vision.wheelview.sample.provincepick.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市信息XML文（city.xml）件解析类
 */
public class ProvinceInfoHandler extends DefaultHandler {

	private StringBuilder mBuilder;
	private CityModel mCity;
	private ProvinceModel mProvince;
	private List<ProvinceModel> mList;

	public ProvinceInfoHandler(List<ProvinceModel> list) {

		this.mList = list;
	}

	/**
	 * 返回解析后得到的省份信息列表
	 *
	 * @return 省份信息列表
	 */
	public List<ProvinceModel> getProvinceList() {

		return mList;
	}

	@Override
	public void startDocument() throws SAXException {

		super.startDocument();

		if (null == mList)
			mList = new ArrayList<ProvinceModel>();

		mBuilder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);

		if ("province".equals(qName)) {

			mProvince = new ProvinceModel();
			mProvince.id = attributes.getValue("id");
			mProvince.name = attributes.getValue("name");
		} else if ("city".equals(qName)) {

			mCity = new CityModel();
			mCity.id = attributes.getValue("id");
			mCity.name = attributes.getValue("name");
		}

		mBuilder.setLength(0); // 将字符长度设置为0 以便重新开始读取元素内的字符节点
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		super.characters(ch, start, length);

		mBuilder.append(ch, start, length); // 将读取的字符数组追加到mBuilder中
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		super.endElement(uri, localName, qName);

		if ("province".equals(qName)) {

			mList.add(mProvince);
		} else if ("city".equals(qName)) {

			mProvince.addCity(mCity);
		}
	}
}
