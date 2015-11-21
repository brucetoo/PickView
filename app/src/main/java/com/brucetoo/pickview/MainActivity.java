package com.brucetoo.pickview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.brucetoo.pickview.provincepick.ProvinceModel;
import com.brucetoo.pickview.provincepick.ProvincePickPopWin;
import com.brucetoo.pickview.provincepick.utils.ProvinceInfoParserTask;
import com.brucetoo.pickview.provincepick.utils.ProvinceInfoUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

//    private ArrayList<ProvinceModel> mProvinceList = null; // 省份列表
//    private String mProvince = null; // 省份
//    private String mCity = null; // 城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  DatePickerPopWin pickerPopWin = new DatePickerPopWin(MainActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                      @Override
                      public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                          Toast.makeText(MainActivity.this,dateDesc,Toast.LENGTH_SHORT).show();
                      }
                  });
                pickerPopWin.showPopWin(MainActivity.this);
            }
        });

        findViewById(R.id.province).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"Working on...",Toast.LENGTH_SHORT).show();
//                if(null != mProvinceList) {
//                    ProvincePickPopWin pickPopWin = new ProvincePickPopWin(MainActivity.this,
//                            mProvince, mCity, mProvinceList, MainActivity.this);
//                    pickPopWin.showPopWin(MainActivity.this);
//                }
            }
        });
        ((new ProvinceInfoParserTask(this, mHandler))).execute();// 解析本地地址信息文件
    }


//    private Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case ProvinceInfoParserTask.MSG_PARSE_RESULT_CALLBACK: // 解析地址完成
//                    mProvinceList = (ArrayList<ProvinceModel>) msg.obj;
//                    break;
//            }
//            return false;
//        }
//    });

//    @Override
//    public void onAddressPickCompleted(String province, String provinceId, String city, String cityId) {
////        Toast.makeText(this,province+"-"+provinceId+"-"+city+"-"+cityId,Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,ProvinceInfoUtils.matchAddress(this,provinceId,cityId,mProvinceList),Toast.LENGTH_SHORT).show();
//        ProvinceInfoUtils.matchAddress(this,provinceId,cityId,mProvinceList);
//    }
}
