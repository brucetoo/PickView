package io.blackbox_vision.wheelview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import io.blackbox_vision.wheelview.view.DatePickerPopUpWindow;
import io.blackbox_vision.wheelview.view.WheelView;

import java.util.ArrayList;


public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.date).setOnClickListener(v -> {
            final DatePickerPopUpWindow datePicker = new DatePickerPopUpWindow.Builder(getApplicationContext())
                    .setMinYear(1990)
                    .setMaxYear(2550)
                    .setSelectedDate("2013-11-11")
                    .setOnDateSelectedListener(this::onDateSelected)
                    .setConfirmButtonText("CONFIRM")
                    .setCancelButtonText("CANCEL")
                    .setConfirmButtonTextColor(Color.parseColor("#999999"))
                    .setCancelButtonTextColor(Color.parseColor("#009900"))
                    .setButtonTextSize(16)
                    .setViewTextSize(15)
                    .setShowDayMonthYear(true)
                    .build();

            datePicker.show(this);
        });

        findViewById(R.id.province).setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Working on...", Toast.LENGTH_SHORT).show();
        });

        final WheelView wheelView = (WheelView) findViewById(R.id.loop_view);

        wheelView.setInitialPosition(2);
        wheelView.setCanLoop(false);
        wheelView.setOnLoopScrollListener((item, position) -> {});
        wheelView.setTextSize(12);
        wheelView.setItems(getList());
    }

    private void onDateSelected(int year, int month, int dayOfMonth) {
        Toast.makeText(MainActivity.this, dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add("DAY TEST:" + i);
        }

        return list;
    }
}
