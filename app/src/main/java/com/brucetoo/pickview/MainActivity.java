package com.brucetoo.pickview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.brucetoo.pickview.datapick.DatePickerPopWin;

public class MainActivity extends AppCompatActivity implements DatePickerPopWin.OnDatePickCompletedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DatePickerPopWin pickerPopWin = new DatePickerPopWin(MainActivity.this, "1907-01-01", MainActivity.this);
//                pickerPopWin.showPopWin(MainActivity.this);
            }
        });

        findViewById(R.id.date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin(MainActivity.this, "1907-01-01", MainActivity.this);
                pickerPopWin.yearPickerV.setTextColor(Color.RED);//设置颜色
                pickerPopWin.monthPickerV.setTextColor(Color.BLUE);
                pickerPopWin.showPopWin(MainActivity.this);
            }
        });

        findViewById(R.id.province).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin(MainActivity.this, "1907-01-01", MainActivity.this);
                pickerPopWin.showPopWin(MainActivity.this);
            }
        });
    }

    @Override
    public void onDatePickCompleted(int year, int month, int day, String dateStr) {
        Toast.makeText(this,dateStr,Toast.LENGTH_SHORT).show();
    }
}
