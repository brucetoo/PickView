package io.blackbox_vision.wheelview.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.blackbox_vision.wheelview.R;

import static io.blackbox_vision.wheelview.utils.DateUtils.formatDate;


public final class DatePickerWheelView extends LinearLayout {
    private static final String TAG = DatePickerWheelView.class.getSimpleName();

    private static final String SHORT_MONTH_FORMAT = "MMM";
    private static final String YEAR_FORMAT = "yyyy";
    private static final String MONTH_FORMAT = "MM";
    private static final String DAY_FORMAT = "dd";

    private static final int DEFAULT_MIN_YEAR = 1900;

    private WheelView yearSpinner;

    private WheelView monthSpinner;

    private WheelView daySpinner;

    private int textSize;

    private int minYear;

    private int maxYear;

    private boolean showDayMonthYear;

    private boolean showShortMonths;

    @NonNull
    private final List<String> years = new ArrayList<>();

    @NonNull
    private final List<String> months = new ArrayList<>();

    @NonNull
    private final List<String> days = new ArrayList<>();

    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;

    @Nullable
    private OnDateSelectedListener onDateSelectedListener;

    private Calendar calendar;

    @NonNull
    private Locale locale = Locale.getDefault();


    public DatePickerWheelView(Context context) {
        this(context, null, 0);
    }

    public DatePickerWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawDatePickerWheelView();
    }

    @TargetApi(21)
    public DatePickerWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        drawDatePickerWheelView();
    }

    private void drawDatePickerWheelView() {
        final View rootView = LayoutInflater.from(getContext()).inflate(R.layout.date_picker, this, true);

        yearSpinner = (WheelView) rootView.findViewById(R.id.picker_year);
        monthSpinner = (WheelView) rootView.findViewById(R.id.picker_month);
        daySpinner = (WheelView) rootView.findViewById(R.id.picker_day);

        yearSpinner.setCanLoop(false);
        monthSpinner.setCanLoop(false);
        daySpinner.setCanLoop(false);

        yearSpinner.setTextSize(textSize);
        monthSpinner.setTextSize(textSize);
        daySpinner.setTextSize(textSize);

        yearSpinner.addOnLoopScrollListener(this::updateYearPosition);
        yearSpinner.addOnLoopScrollListener(this::onDateSelected);

        monthSpinner.addOnLoopScrollListener(this::updateMonthPosition);
        monthSpinner.addOnLoopScrollListener(this::onDateSelected);

        daySpinner.addOnLoopScrollListener(this::updateDayPosition);
        daySpinner.addOnLoopScrollListener(this::onDateSelected);

        drawYearPickerView();
        drawMonthPickerView();
        drawDayPickerView();
    }

    private void updateYearPosition(@NonNull Object item, int position) {
        this.yearPos = position;
        invalidate();
    }

    private void updateMonthPosition(@NonNull Object item, int position) {
        this.monthPos = position;
        drawDayPickerView();
        invalidate();
    }

    private void updateDayPosition(@NonNull Object item, int position) {
        this.dayPos = position;
        invalidate();
    }

    private void onDateSelected(@NonNull Object item, int position) {
        if (null != onDateSelectedListener) {
            final int year = Integer.valueOf(years.get(yearPos));
            final int month = showShortMonths ? months.indexOf(months.get(monthPos)) : Integer.valueOf(months.get(monthPos)) - 1;
            final int dayOfMonth = Integer.valueOf(days.get(dayPos));

            onDateSelectedListener.onDateSelected(year, month, dayOfMonth);
        }
    }

    private void drawYearPickerView() {
        calendar = Calendar.getInstance(locale);
        final int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            calendar.set(Calendar.YEAR, minYear + i);

            years.add(i, formatDate(calendar, YEAR_FORMAT));
        }

        yearSpinner.setItems(years);
        yearSpinner.setInitialPosition(yearPos);
    }

    private void drawMonthPickerView() {
        calendar = Calendar.getInstance(locale);

        for (int j = 0; j <= calendar.getActualMaximum(Calendar.MONTH); j++) {
            calendar.set(Calendar.YEAR, Integer.valueOf(years.get(yearPos)));
            calendar.set(Calendar.MONTH, j);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            months.add(j, formatDate(calendar, showShortMonths ? SHORT_MONTH_FORMAT : MONTH_FORMAT).toUpperCase());
        }

        monthSpinner.setItems(months);
        monthSpinner.setInitialPosition(monthPos);
    }

    private void drawDayPickerView() {
        for (Iterator<String> iterator = days.listIterator(); iterator.hasNext(); ) {
            iterator.next();
            iterator.remove();
        }

        final int year = Integer.valueOf(years.get(yearPos));
        final int month = showShortMonths ? months.indexOf(months.get(monthPos)) + 1 : Integer.valueOf(months.get(monthPos));

        calendar = Calendar.getInstance(locale);

        for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);

            days.add(i, formatDate(calendar, DAY_FORMAT));
        }

        daySpinner.setItems(days);
        daySpinner.setInitialPosition(dayPos);
    }

    public interface OnDateSelectedListener {

        void onDateSelected(int year, int month, int dayOfMonth);
    }

    public DatePickerWheelView setOnDateSelectedListener(@Nullable OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
        return this;
    }

    public DatePickerWheelView setLocale(@NonNull Locale locale) {
        this.locale = locale;
        return this;
    }

    public DatePickerWheelView setMaxYear(int maxYear) {
        if (minYear > maxYear) {
            throw new IllegalArgumentException("minYear can't be major to maxYear");
        }

        this.maxYear = maxYear;
        return this;
    }

    public DatePickerWheelView setMinYear(int minYear) {
        this.minYear = minYear;
        return this;
    }

    public DatePickerWheelView setShowDayMonthYear(boolean showDayMonthYear) {
        this.showDayMonthYear = showDayMonthYear;
        return this;
    }

    public DatePickerWheelView setShowShortMonths(boolean showShortMonths) {
        this.showShortMonths = showShortMonths;
        return this;
    }

    public DatePickerWheelView setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }
}
