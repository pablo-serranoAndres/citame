package com.milibrodereservas.citame.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilDate {
    public static final int MINS_OF_DAY = 24 * 60;
    public static final String DAYS_OF_WEEK = "MTWRFSU";

    public static boolean validDayOfWeek(final String validity, LocalDate date) {
        if (validity == null) {
            return true;
        }

        List<String> daysValid = Arrays.stream(validity.split(",")).toList();
        List<String> processed = new ArrayList<>();
        for (String day : daysValid) {
            if (day.contains("-") && (day.length() == 3)) {
                final int from = DAYS_OF_WEEK.indexOf(day.charAt(0));
                final int to = DAYS_OF_WEEK.indexOf(day.charAt(2));
                if ((from >= 0) && (to >= 0)) {
                    if (to >= from) {
                        for (int i = from; i <= to; i++) {
                            processed.add(DAYS_OF_WEEK.substring(i, i+1));
                        }
                    } else {
                        for (int i = from; i < DAYS_OF_WEEK.length(); i++) {
                            processed.add(DAYS_OF_WEEK.substring(i, i+1));
                        }
                        for (int i = 0; i <= to; i++) {
                            processed.add(DAYS_OF_WEEK.substring(i, i+1));
                        }
                    }
                }
            } else {
                processed.add(day);
            }
        }
        switch(date.getDayOfWeek()) {
            case MONDAY: return processed.contains(DAYS_OF_WEEK.substring(0, 1));
            case TUESDAY: return processed.contains(DAYS_OF_WEEK.substring(1, 2));
            case WEDNESDAY: return processed.contains(DAYS_OF_WEEK.substring(2, 3));
            case THURSDAY: return processed.contains(DAYS_OF_WEEK.substring(3, 4));
            case FRIDAY: return processed.contains(DAYS_OF_WEEK.substring(4, 5));
            case SATURDAY: return processed.contains(DAYS_OF_WEEK.substring(5, 6));
            case SUNDAY: return processed.contains(DAYS_OF_WEEK.substring(6, 7));
        }
        return false;
    }
}
