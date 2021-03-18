package cn.dahuoji.body_temperature;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class MonthView extends LinearLayout {
    private int position;
    private MonthAdapter.OnCalendarEventListener onCalendarEventListener;
    private MonthAdapter adapter;

    public MonthView(Context context) {
        super(context);
        init(context);
    }

    public MonthView(Context context, int position, MonthAdapter.OnCalendarEventListener onCalendarEventListener) {
        super(context);
        this.position = position;
        this.onCalendarEventListener = onCalendarEventListener;
        init(context);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.month_view, this, true);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currencyMonth = calendar.get(Calendar.MONTH) + 1;
        int temp = currentYear * 12 + currencyMonth + position;
        int month = temp % 12;
        if (month == 0) month = 12;
        int year = (temp - month) / 12;
        adapter = new MonthAdapter(context, year, month);
        adapter.setOnCalendarEventListener(onCalendarEventListener);
        recyclerView.setAdapter(adapter);
    }

    public void notifyTemperatureDataChanged() {
        adapter.notifyTemperatureDataChanged();
    }
}
