package io.blackbox_vision.wheelview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.blackbox_vision.wheelview.R;

/**
 * PopWindow for Date Pick
 */
public final class DatePickerPopUpWindow extends PopupWindow {
    private static final int DEFAULT_MIN_YEAR = 1900;
    private static final String DEFAULT_DATE_TEMPLATE = "yyyy-MM-dd";

    private Context context;

    private View rootView;
    private View container;

    private WheelView yearSpinner;
    private WheelView monthSpinner;
    private WheelView daySpinner;

    private Button cancelButton;
    private Button confirmButton;

    private String cancelButtonText;
    private String confirmButtonText;

    private int cancelButtonTextColor;
    private int confirmButtonTextColor;
    private int buttonTextSize;

    private int viewTextSize;

    private int minYear;
    private int maxYear;

    private boolean showDayMonthYear;

    @NonNull
    private final SimpleDateFormat defaultFormatter = new SimpleDateFormat(DEFAULT_DATE_TEMPLATE, Locale.getDefault());

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

    public DatePickerPopUpWindow(@NonNull final Builder builder) {
        this.minYear = builder.minYear;
        this.maxYear = builder.maxYear;
        this.cancelButtonText = builder.cancelButtonText;
        this.confirmButtonText = builder.confirmButtonText;
        this.context = builder.context;
        this.onDateSelectedListener = builder.listener;
        this.cancelButtonTextColor = builder.cancelButtonTextColor;
        this.confirmButtonTextColor = builder.confirmButtonTextColor;
        this.buttonTextSize = builder.buttonTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.showDayMonthYear = builder.showDayMonthYear;
        setSelectedDate(builder.selectedDate);
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(context).inflate(showDayMonthYear ? R.layout.layout_date_picker_inverted : R.layout.layout_date_picker, null);

        container = rootView.findViewById(R.id.container_picker);

        cancelButton = (Button) rootView.findViewById(R.id.btn_cancel);
        confirmButton = (Button) rootView.findViewById(R.id.btn_confirm);

        yearSpinner = (WheelView) rootView.findViewById(R.id.picker_year);
        monthSpinner = (WheelView) rootView.findViewById(R.id.picker_month);
        daySpinner = (WheelView) rootView.findViewById(R.id.picker_day);

        yearSpinner.setCanLoop(false);
        monthSpinner.setCanLoop(false);
        daySpinner.setCanLoop(false);

        yearSpinner.setTextSize(viewTextSize);
        monthSpinner.setTextSize(viewTextSize);
        daySpinner.setTextSize(viewTextSize);

        cancelButton.setTextColor(cancelButtonTextColor);
        cancelButton.setTextSize(buttonTextSize);

        confirmButton.setTextColor(confirmButtonTextColor);
        confirmButton.setTextSize(buttonTextSize);

        //set checked listen
        yearSpinner.setLoopListener(item -> yearPos = item);

        monthSpinner.setLoopListener(item -> {
            monthPos = item;
            drawDayPickerView();
        });

        daySpinner.setLoopListener(item -> dayPos = item);

        initPickerViews(); // init year and month loop view
        drawDayPickerView(); //init day loop view

        cancelButton.setOnClickListener(this::onClick);
        confirmButton.setOnClickListener(this::onClick);

        rootView.setOnClickListener(this::onClick);

        if (!TextUtils.isEmpty(confirmButtonText)) {
            confirmButton.setText(confirmButtonText);
        }

        if (!TextUtils.isEmpty(cancelButtonText)) {
            cancelButton.setText(cancelButtonText);
        }

        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {
        int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            final Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.YEAR, minYear + i);

            years.add(formatField(calendar, "yyyy"));
        }

        for (int j = 0; j < 12; j++) {
            final Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.MONTH, j);

            months.add(formatField(calendar, "MM"));
        }

        yearSpinner.setDataList(years);
        yearSpinner.setInitPosition(yearPos);

        monthSpinner.setDataList(months);
        monthSpinner.setInitPosition(monthPos);
    }

    /**
     * Init day item
     */
    private void drawDayPickerView() {
        final int year = Integer.valueOf(years.get(yearPos));
        final int month = Integer.valueOf(months.get(monthPos));

        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);

        if (days.size() != calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            for (Iterator<String> iterator = days.listIterator(); iterator.hasNext(); ) {
                iterator.next();
                iterator.remove();
            }

            for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                calendar.set(Calendar.DAY_OF_MONTH, i + 1);

                days.add(formatField(calendar, "dd"));
            }
        }

        daySpinner.setDataList(days);
        daySpinner.setInitPosition(dayPos);
    }

    /**
     * set selected date position value when initView.
     *
     * @param dateStr
     */
    private void setSelectedDate(@Nullable String dateStr) {
        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = toMilliseconds(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            if (milliseconds != -1) {
                calendar.setTimeInMillis(milliseconds);

                yearPos = calendar.get(Calendar.YEAR) - minYear;
                monthPos = calendar.get(Calendar.MONTH);
                dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            }
        }
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void show(@Nullable Activity activity) {
        if (null != activity) {
            final int relative = Animation.RELATIVE_TO_SELF;
            final TranslateAnimation animation = new TranslateAnimation(relative, 0, relative, 0, relative, 1, relative, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            animation.setDuration(400);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());

            container.startAnimation(animation);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismiss() {
        final int relative = Animation.RELATIVE_TO_SELF;
        final TranslateAnimation animation = new TranslateAnimation(relative, 0, relative, 0, relative, 0, relative, 1);

        animation.setDuration(400);
        animation.setInterpolator(new AccelerateInterpolator());

        container.startAnimation(animation);
        super.dismiss();
    }

    private void onClick(View v) {
        if (v == rootView || v == cancelButton) {
            dismiss();
        } else if (v == confirmButton) {
            if (null != onDateSelectedListener) {
                final int year = Integer.valueOf(years.get(yearPos));
                final int month = Integer.valueOf(months.get(monthPos));
                final int dayOfMonth = Integer.valueOf(days.get(dayPos));

                onDateSelectedListener.onDateSelected(year, month, dayOfMonth);
            }

            dismiss();
        }
    }

    private long toMilliseconds(@NonNull String dateStr) {
        Date date = null;

        try {
            date = defaultFormatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (null != date) ? date.getTime() : -1L;
    }

    private static String getDateAsString() {
        return new SimpleDateFormat(DEFAULT_DATE_TEMPLATE, Locale.getDefault()).format(new Date());
    }

    private static String formatField(@NonNull final Calendar cal, @NonNull final String fieldType) {
        return new SimpleDateFormat(fieldType, Locale.getDefault()).format(cal.getTime());
    }

    public interface OnDateSelectedListener {

        void onDateSelected(int year, int month, int dayOfMonth);
    }

    public static class Builder {

        @Nullable
        private OnDateSelectedListener listener;

        private Context context;

        private boolean showDayMonthYear = false;

        private int minYear = DEFAULT_MIN_YEAR;
        private int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

        private String cancelButtonText = "Cancel";
        private String confirmButtonText = "Confirm";

        private int cancelButtonTextColor = Color.parseColor("#999999");
        private int confirmButtonTextColor = Color.parseColor("#303F9F");

        private String selectedDate = getDateAsString();

        private int buttonTextSize = 16;
        private int viewTextSize = 25;

        public Builder(@NonNull final Context context) {
            this.context = context;
        }

        public Builder setMinYear(int minYear) {
            this.minYear = minYear;
            return this;
        }

        public Builder setMaxYear(int maxYear) {
            this.maxYear = maxYear;
            return this;
        }

        public Builder setCancelButtonText(String textCancel) {
            this.cancelButtonText = textCancel;
            return this;
        }

        public Builder setConfirmButtonText(String textConfirm) {
            this.confirmButtonText = textConfirm;
            return this;
        }

        public Builder setSelectedDate(String dateChose) {
            this.selectedDate = dateChose;
            return this;
        }

        public Builder setCancelButtonTextColor(int colorCancel) {
            this.cancelButtonTextColor = colorCancel;
            return this;
        }

        public Builder setConfirmButtonTextColor(int colorConfirm) {
            this.confirmButtonTextColor = colorConfirm;
            return this;
        }

        public Builder setButtonTextSize(int textSize) {
            this.buttonTextSize = textSize;
            return this;
        }

        public Builder setViewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public Builder setShowDayMonthYear(boolean useDayMonthYear) {
            this.showDayMonthYear = useDayMonthYear;
            return this;
        }

        public Builder setOnDateSelectedListener(@Nullable OnDateSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        public DatePickerPopUpWindow build() {
            if (minYear > maxYear) {
                throw new IllegalArgumentException();
            }

            return new DatePickerPopUpWindow(this);
        }
    }
}
