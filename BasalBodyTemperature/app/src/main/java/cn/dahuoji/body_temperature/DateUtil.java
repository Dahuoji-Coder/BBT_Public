package cn.dahuoji.body_temperature;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtil {

    public static String selectDate = getCurrentDate();
    public static String selectDateStart = getCurrentDate();
    public static String selectDateEnd = "";
//    public static String selectDateStart = "2021-03-01";
//    public static String selectDateEnd = "2021-03-31";

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 指定月的第一天是星期几，
     * 返回0代表周日，1~6代表周一到周六
     */
    public static int getFirstWeekOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getWeekOfDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getWeekOfDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 当前年月日
     */
    public static String getCurrentDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return sDateFormat.format(new java.util.Date());
    }

    public static String getFormatDate(int year, int month, int day) {
        String tempMonth, tempDay;
        if (month < 10) {
            tempMonth = "0" + month;
        } else {
            tempMonth = "" + month;
        }
        if (day < 10) {
            tempDay = "0" + day;
        } else {
            tempDay = "" + day;
        }
        return year + "-" + tempMonth + "-" + tempDay;
    }

}
