package com.impakter.impakter.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DateTimeUtility {

    public static String convertTimeStampToDate(String timeStamp,
                                                String outputFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(outputFormat,
                Locale.getDefault());
        try {
            Date date = new Date(Long.parseLong(timeStamp) * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    public static String convertDatetoTimeStamp(Date date) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            result = cal.getTimeInMillis() + "";
        } catch (Exception ex) {
            result = "";
        }
        return result;
    }
    public static String convertDatetoTimeStampInSecon(Date date) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            result = (cal.getTimeInMillis()/1000L) + "";
        } catch (Exception ex) {
            result = "";
        }
        return result;
    }

    public static long getDateDiff(Date curDate, Date specDate, TimeUnit timeUnit) {
        long diff = curDate.getTime() - specDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getDateDiff(long curTimeStamp, long specTimeStamp) {
        try {
            long diff = specTimeStamp - curTimeStamp;
            long day = diff / 24 / 60 / 60;

            String strDay = day + "";
            if (strDay.contains(".")) {
                strDay = strDay.substring(0, strDay.indexOf("."));
            }

            return Integer.parseInt(strDay);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static String formatDate(Date date, String outputFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(outputFormat,
                Locale.getDefault());
        return formater.format(date);
    }

    public static String convertStringToDate(String strDate,
                                             String inputFormat, String outputFormat) {
        SimpleDateFormat dateFormaterInput, dateFormaterOutput;
        dateFormaterInput = new SimpleDateFormat(inputFormat,
                Locale.getDefault());
        dateFormaterOutput = new SimpleDateFormat(outputFormat,
                Locale.getDefault());
        Date dob;
        try {
            dob = dateFormaterInput.parse(strDate);
            return dateFormaterOutput.format(dob);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return strDate;
        }
    }

    public static Date getDateValueFromString(String stringFormat, String dateString) {
        DateFormat formatter = new SimpleDateFormat(stringFormat,
                Locale.getDefault());
        try {
            return formatter.parse(dateString);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String convertTimeStampToStartOfDate(String timeStamp) {
        String result = "";

        String strDate = DateTimeUtility.convertTimeStampToDate(
                timeStamp.substring(0, timeStamp.length() - 3), "MM/dd/yyyy") + " 00:00:00";
        Date date = new Date(strDate);

        result = (date.getTime() + "").substring(0,
                (date.getTime() + "").length() - 3);

        return result;
    }

    public static ArrayList<String> countDown(long curTimeStamp, long specTimeStamp) {
        ArrayList<String> arr = new ArrayList<>();

        long diff = specTimeStamp - curTimeStamp;

        long sec = diff % 60;
        long min = (diff / 60) % 60;
        long hour = (diff / 60 / 60) % 24;
        long day = diff / 24 / 60 / 60;

        String strSec = sec + "";
        if (sec < 10) {
            strSec = "0" + sec;
        }
        if (strSec.contains(".")) {
            strSec = strSec.substring(0, strSec.indexOf("."));
        }

        String strMin = min + "";
        if (min < 10) {
            strMin = "0" + min;
        }
        if (strMin.contains(".")) {
            strMin = strMin.substring(0, strMin.indexOf("."));
        }

        String strHour = hour + "";
        if (hour < 10) {
            strHour = "0" + hour;
        }
        if (strHour.contains(".")) {
            strHour = strHour.substring(0, strHour.indexOf("."));
        }

        String strDay = day + "";
        if (day < 10) {
            strDay = "0" + day;
        }
        if (strDay.contains(".")) {
            strDay = strDay.substring(0, strDay.indexOf("."));
        }

        arr.add(strDay);
        arr.add(strHour);
        arr.add(strMin);
        arr.add(strSec);

        return arr;
    }
}
