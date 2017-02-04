package io.blackbox_vision.wheelview.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

        Log.i("DateUtils", "This is the date to parse as String -> " + dateString);

        try {
            date = formatter.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException p) {
            date = new Date(System.currentTimeMillis());
            calendar.setTime(date);
        }

        Log.i("DateUtils", "This is the date -> " + date.toString());

        return calendar;
    }
}
