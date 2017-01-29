package io.blackbox_vision.wheelview.sample.provincepick.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import io.blackbox_vision.wheelview.sample.provincepick.ProvinceModel;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * 解析地址信息Task类
 *
 * @author g7734
 */
public class ProvinceInfoParserTask extends AsyncTask<Void, Void, List<ProvinceModel>> {

	/** 解析结果回调消息 */
	public static final int MSG_PARSE_RESULT_CALLBACK = 0x100;

	private Context mContext; // 上下文
	private Handler mHandler; // 解析结果分发

	public ProvinceInfoParserTask(Context context, Handler handler) {

		this.mContext = context;
		this.mHandler = handler;
	}

	@Override
	protected List<ProvinceModel> doInBackground(Void... params) {

		if (null == mContext)
			return null;

		List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

		try {
			SAXParserFactory sax = SAXParserFactory.newInstance();// 创建解析器
			XMLReader reader = sax.newSAXParser().getXMLReader();
			InputStream in = mContext.getAssets().open("io/blackbox_vision/wheelview/sample/provincepick/utils/city.xml");

			reader.setContentHandler(new ProvinceInfoHandler(provinceList));// 为reader设置内容处理器
			reader.parse(new InputSource(in));// 开始解析文件
		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

		return provinceList;
	}

	@Override
	protected void onPostExecute(List<ProvinceModel> result) {

		super.onPostExecute(result);

		if (null != mHandler)
			mHandler.sendMessage(mHandler.obtainMessage(MSG_PARSE_RESULT_CALLBACK, result));
	}
}