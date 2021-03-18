package cn.dahuoji.body_temperature;

import android.text.TextUtils;

public class DayEntity {
    private int year;
    private int month;
    private int day;
    public static final int POSITION_PREVIOUS = -1;
    public static final int POSITION_CURRENT = 0;
    public static final int POSITION_NEXT = 1;
    private int position; //-1 上个月，0 当前月，1 下个月
    private String temperature;
    private String sexy;
    private String blood;
    private String doctor;

    public DayEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDateString() {
        String tempMonth = "" + month;
        if (tempMonth.length() == 1) tempMonth = "0" + tempMonth;
        String tempDay = "" + day;
        if (tempDay.length() == 1) tempDay = "0" + day;
        return year + "-" + tempMonth + "-" + tempDay;
    }

    public String getSexy() {
        return sexy;
    }

    public void setSexy(String sexy) {
        this.sexy = sexy;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getExtrasStr() {
        return (TextUtils.isEmpty(getSexy()) ? "" : getSexy()) +
                (TextUtils.isEmpty(getBlood()) ? "" : getBlood()) +
                (TextUtils.isEmpty(getDoctor()) ? "" : getDoctor());
    }
}
