package com.example.testprep_carte.data;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    @TypeConverter
    public static Date toDate(String value) {
        try {
            return FORMATTER.parse(value);
        } catch (ParseException ex) {
            return null;
        }
    }

    @TypeConverter
    public static String fromDate(Date value) {
        if (value == null) {
            return null;
        }
        return FORMATTER.format(value);
    }
}
