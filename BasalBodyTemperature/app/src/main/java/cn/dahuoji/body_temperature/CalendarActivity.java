package cn.dahuoji.body_temperature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cn.dahuoji.body_temperature.skinview.TTFTextView;

public class CalendarActivity extends BaseActivity implements View.OnClickListener {

    private CalendarPagerAdapter calendarPagerAdapter;
    private TTFTextView btnQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        TextView titleText = findViewById(R.id.titleText);
        ViewPager viewPager = findViewById(R.id.viewPager);
        calendarPagerAdapter = new CalendarPagerAdapter(this, new WeekAdapter.OnCalendarEventListener() {
            @Override
            public void onItemClicked(DayEntity dayEntity) {
                calendarPagerAdapter.notifyTemperatureDataChanged();
                Log.d(">>>CalendarActivity", "=== month item clicked");
                if (TextUtils.isEmpty(DateUtil.selectDateEnd)) {
                    btnQuery.setAlpha(0.4f);
                } else {
                    btnQuery.setAlpha(1.0f);
                }
            }
        }, CalendarPagerAdapter.Type_Month);
        viewPager.setAdapter(calendarPagerAdapter);
        viewPager.setCurrentItem(CalendarPagerAdapter.First_Position, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currencyMonth = calendar.get(Calendar.MONTH) + 1;
                int temp = currentYear * 12 + currencyMonth + (position - CalendarPagerAdapter.First_Position);
                int month = temp % 12;
                if (month == 0) month = 12;
                int year = (temp - month) / 12;
                titleText.setText(year + "年" + month + "月");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(this);
        btnQuery.setAlpha(0.4f);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnQuery) {
            if (btnQuery.getAlpha() < 1) {
                Toast.makeText(this, "请选择一个时间段", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, PrintActivity.class);
                startActivity(intent);
            }
        }
    }
}