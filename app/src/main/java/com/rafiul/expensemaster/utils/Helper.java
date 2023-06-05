package com.rafiul.expensemaster.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, YYYY");
        return simpleDateFormat.format(date);
    }

    public static String formatDateByMonth(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM, YYYY");
        return simpleDateFormat.format(date);
    }
}
