package cn.dahuoji.body_temperature;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.LinkedList;

public class CalendarPagerAdapter extends PagerAdapter {

    private final Context context;
    private final LinkedList<View> views;
    private final WeekAdapter.OnCalendarEventListener onCalendarEventListener;
    public static final int First_Position = Integer.MAX_VALUE / 2;
    public static int Type_Week = 0;
    public static int Type_Month = 1;
    private int calendarType;

    public CalendarPagerAdapter(Context context, WeekAdapter.OnCalendarEventListener onCalendarEventListener, int calendarType) {
        this.context = context;
        this.onCalendarEventListener = onCalendarEventListener;
        this.calendarType = calendarType;
        views = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (calendarType == Type_Month) {
            View monthView = new MonthView(context, position - First_Position, onCalendarEventListener);
            container.addView(monthView);
            views.add(monthView);
            return monthView;
        } else {
            View weekView = new WeekView(context, position - First_Position, onCalendarEventListener);
            container.addView(weekView);
            views.add(weekView);
            return weekView;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        views.remove(object);
    }

    public void notifyTemperatureDataChanged() {
        super.notifyDataSetChanged();
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i) instanceof MonthView) {
                MonthView monthView = (MonthView) views.get(i);
                monthView.notifyTemperatureDataChanged();
            }
            if (views.get(i) instanceof WeekView) {
                WeekView weekView = (WeekView) views.get(i);
                weekView.notifyTemperatureDataChanged();
            }
        }
    }
}
