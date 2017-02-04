package io.blackbox_vision.wheelview.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private static final String DEFAULT_DATE_TEMPLATE = "yyyy-MM-dd";

    private DateUtils() { }

    public static String formatDate(@NonNull Calendar calendar, @NonNull String fieldType) {
        return new SimpleDateFormat(fieldType, Locale.getDefault()).format(calendar.getTime());
    }

    public static Calendar parseDateString(@Nullable String dateString) {
        final SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_TEMPLATE, Locale.getDefault());
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        dateString = dateString != null ? dateString : "";
        Date date;

        try {
            date = formatter.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException p) {
            date = new Date(System.currentTimeMillis());
            calendar.setTime(date);
        }

        return calendar;
    }

    public static long toMilliseconds(@NonNull String dateStr) {
        final SimpleDateFormat defaultFormatter = new SimpleDateFormat(DEFAULT_DATE_TEMPLATE, Locale.getDefault());
        Date date = null;

        try {
            date = defaultFormatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (null != date) ? date.getTime() : -1L;
    }
}
