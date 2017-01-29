package io.blackbox_vision.wheelview.utils;


import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private static final String DEFAULT_DATE_TEMPLATE = "yyyy-MM-dd";

    private DateUtils() { }

    public static String formatDate(@NonNull final Calendar calendar, @NonNull final String fieldType) {
        return new SimpleDateFormat(fieldType, Locale.getDefault()).format(calendar.getTime());
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
