package cn.dahuoji.body_temperature;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class WeekView extends LinearLayout {
    private int position;
    private WeekAdapter.OnCalendarEventListener onCalendarEventListener;
    private WeekAdapter adapter;

    public WeekView(Context context) {
        super(context);
        init(context);
    }

    public WeekView(Context context, int position, WeekAdapter.OnCalendarEventListener onCalendarEventListener) {
        super(context);
        this.position = position;
        this.onCalendarEventListener = onCalendarEventListener;
        init(context);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.month_view, this, true);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7 * position - DateUtil.getWeekOfDay(calendar));
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        adapter = new WeekAdapter(context, currentYear, currentMonth, currentDay);
        adapter.setOnCalendarEventListener(onCalendarEventListener);
        recyclerView.setAdapter(adapter);
    }

    public void notifyTemperatureDataChanged() {
        adapter.notifyTemperatureDataChanged();
    }
}
