package cn.dahuoji.body_temperature;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.dahuoji.body_temperature.database.ColumnEntity;
import cn.dahuoji.body_temperature.database.DBConstant;
import cn.dahuoji.body_temperature.database.DBUtil;
import cn.dahuoji.body_temperature.linechart.ChartConfig;
import cn.dahuoji.body_temperature.linechart.CommonLineChart;
import cn.dahuoji.body_temperature.skinview.SkinCommonView;
import cn.dahuoji.body_temperature.skinview.TTFTextView;
import cn.dahuoji.body_temperature.util.AnimateUtil;
import cn.dahuoji.body_temperature.util.ResourcesUtil;
import cn.dahuoji.body_temperature.util.SPUtil;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private CalendarPagerAdapter calendarPagerAdapter;
    private TTFTextView selectDateText;
    private String date;
    private TTFTextView temperatureValueText;
    private Ruler ruler;
    private CommonLineChart lineChart;
    private View lineChartLayout;
    private List<DayEntity> chartPointList;
    private TTFTextView btnSexy;
    private TTFTextView btnBloodMuch;
    private TTFTextView btnBloodCommon;
    private TTFTextView btnBloodLittle;
    private TTFTextView btnBloodBit;
    private TTFTextView btnDoctorStart;
    private TTFTextView btnDoctorEnd;
    private TTFTextView extrasTips;
    private SkinCommonView transBackTop;
    private SkinCommonView transBackBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(ResourcesUtil.getColor(this, R.color.colorPrimaryDark));
//        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.iconPrint).setOnClickListener(this);
        findViewById(R.id.iconTips).setOnClickListener(this);
        TextView titleText = findViewById(R.id.titleText);
        ViewPager viewPager = findViewById(R.id.viewPager);
        SelectValueView selectValueView = findViewById(R.id.selectValueView);
        selectDateText = findViewById(R.id.selectDateText);
        selectDateText.setOnClickListener(this);
        temperatureValueText = findViewById(R.id.temperatureValueText);
        //滑动游标选择器
        ruler = findViewById(R.id.ruler);
        ruler.setOnCursorChangedListener(new Ruler.OnCursorChangedListener() {
            @Override
            public void onCursorChanged(String value) {
                temperatureValueText.setText(value);
            }
        });
        //额外的标识
        btnSexy = findViewById(R.id.btnSexy);
        btnBloodMuch = findViewById(R.id.btnBloodMuch);
        btnBloodCommon = findViewById(R.id.btnBloodCommon);
        btnBloodLittle = findViewById(R.id.btnBloodLittle);
        btnBloodBit = findViewById(R.id.btnBloodBit);
        btnDoctorStart = findViewById(R.id.btnDoctorStart);
        btnDoctorEnd = findViewById(R.id.btnDoctorEnd);
        btnSexy.setOnClickListener(this);
        btnBloodMuch.setOnClickListener(this);
        btnBloodCommon.setOnClickListener(this);
        btnBloodLittle.setOnClickListener(this);
        btnBloodBit.setOnClickListener(this);
        btnDoctorStart.setOnClickListener(this);
        btnDoctorEnd.setOnClickListener(this);
        //一位小数的选择器
        selectValueView.setOnValueItemEventListener(new ValueAdapter.OnValueItemEventListener() {
            @Override
            public void onValueItemClicked(String value) {
                temperatureValueText.setText(value);
                DBUtil.saveTemperature(
                        date,
                        value,
                        getExtraSexy(),
                        getExtraBlood(),
                        getExtraDoctor()
                );
                calendarPagerAdapter.notifyTemperatureDataChanged();
            }
        });
        //周视图 日历选择器
        calendarPagerAdapter = new CalendarPagerAdapter(this, new WeekAdapter.OnCalendarEventListener() {
            @Override
            public void onItemClicked(DayEntity dayEntity) {
                updateDateUI(dayEntity);
            }
        }, CalendarPagerAdapter.Type_Week);
        viewPager.setAdapter(calendarPagerAdapter);
        viewPager.setCurrentItem(CalendarPagerAdapter.First_Position, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 7 * (position - CalendarPagerAdapter.First_Position) - DateUtil.getWeekOfDay(calendar));
                int currentYear = calendar.get(Calendar.YEAR);
                int currencyMonth = calendar.get(Calendar.MONTH) + 1;
                titleText.setText(currentYear + "年" + currencyMonth + "月");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.btnLineChart).setOnClickListener(this);
        findViewById(R.id.btnRecord).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
        //折线图
        lineChartLayout = findViewById(R.id.lineChartLayout);
        lineChart = findViewById(R.id.lineChart);
        final View toolTipsLayout = findViewById(R.id.toolTipsLayout);
        toolTipsLayout.setVisibility(View.INVISIBLE);
        final TTFTextView toolTipsTimeText = findViewById(R.id.toolTipsTimeText);
        final TTFTextView toolTipsValueText = findViewById(R.id.toolTipsValueText);
        chartPointList = new ArrayList<>();
        lineChart.setOnCommonLineChartListener(new CommonLineChart.OnCommonLineChartListener() {
            @Override
            public void onHighLightStatusUpdate(int index, float positionX, float positionY) {
                if (index >= 0 && index < chartPointList.size()) {
                    toolTipsLayout.setVisibility(View.VISIBLE);
                    toolTipsTimeText.setText(chartPointList.get(index).getDateString());
                    toolTipsValueText.setText(chartPointList.get(index).getTemperature() + " °C " + chartPointList.get(index).getExtrasStr());
                    float tempX = positionX + toolTipsLayout.getMeasuredWidth() + WindowUtil.dip2px(LocalApplication.getContext(), 15);
                    if (tempX >= lineChart.getMeasuredWidth()) {
                        tempX = positionX - toolTipsLayout.getMeasuredWidth() - WindowUtil.dip2px(LocalApplication.getContext(), 15);
                    } else {
                        tempX = positionX + WindowUtil.dip2px(LocalApplication.getContext(), 15);
                    }
                    toolTipsLayout.setX(tempX);
                    toolTipsLayout.setY(lineChart.getTop() + WindowUtil.dip2px(LocalApplication.getContext(), 15));
                } else {
                    toolTipsLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        updateLineChartData();
        //
        date = DateUtil.getCurrentDate();
        DayEntity currentDayEntity = DBUtil.queryData(date);
        updateDateUI(currentDayEntity);
        extrasTips = findViewById(R.id.extrasTips);
        extrasTips.setVisibility(View.GONE);
        transBackTop = findViewById(R.id.transBackTop);
        transBackTop.setVisibility(View.GONE);
        transBackTop.setOnClickListener(this);
        transBackBottom = findViewById(R.id.transBackBottom);
        transBackBottom.setVisibility(View.GONE);
        transBackBottom.setOnClickListener(this);
        boolean privacy_agreed = SPUtil.getBoolean("privacy_agreed");
        if (!privacy_agreed) {
            CommonDialog commonDialog = new CommonDialog(this);
            commonDialog.show("BBT使用的权限和用途", "1. 获取本地存储权限\n" +
                    "BBT使用过程中不会联网，只有在生成图片并保存的时候需要本地存储权限，将图片为您保存到相册中，方便您的使用。");
        }
    }

    private void updateDateUI(DayEntity dayEntity) {
        if (dayEntity == null) return;
        date = dayEntity.getDateString();
        selectDateText.setText(date);
        if (!TextUtils.isEmpty(dayEntity.getTemperature())) {
            temperatureValueText.setText(dayEntity.getTemperature());
            ruler.setValue(Float.parseFloat(dayEntity.getTemperature()));
        } else {
            temperatureValueText.setText("36.00");
            ruler.setValue(36);
        }
        if (!TextUtils.isEmpty(dayEntity.getSexy())) {
            btnSexy.setTag(true);
            btnSexy.setBackgroundResource(R.drawable.extras_item_back_h);
        } else {
            btnSexy.setTag(false);
            btnSexy.setBackgroundResource(R.drawable.extras_item_back_n);
        }
        btnBloodMuch.setTag(false);
        btnBloodMuch.setBackgroundResource(R.drawable.extras_item_back_n);
        btnBloodCommon.setTag(false);
        btnBloodCommon.setBackgroundResource(R.drawable.extras_item_back_n);
        btnBloodLittle.setTag(false);
        btnBloodLittle.setBackgroundResource(R.drawable.extras_item_back_n);
        btnBloodBit.setTag(false);
        btnBloodBit.setBackgroundResource(R.drawable.extras_item_back_n);
        if (!TextUtils.isEmpty(dayEntity.getBlood())) {
            if (ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_much).equals(dayEntity.getBlood())) {
                btnBloodMuch.setTag(true);
                btnBloodMuch.setBackgroundResource(R.drawable.extras_item_back_h);
            } else if (ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_common).equals(dayEntity.getBlood())) {
                btnBloodCommon.setTag(true);
                btnBloodCommon.setBackgroundResource(R.drawable.extras_item_back_h);
            } else if (ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_little).equals(dayEntity.getBlood())) {
                btnBloodLittle.setTag(true);
                btnBloodLittle.setBackgroundResource(R.drawable.extras_item_back_h);
            } else if (ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_bit).equals(dayEntity.getBlood())) {
                btnBloodBit.setTag(true);
                btnBloodBit.setBackgroundResource(R.drawable.extras_item_back_h);
            }
        }
        btnDoctorStart.setTag(false);
        btnDoctorStart.setBackgroundResource(R.drawable.extras_item_back_n);
        btnDoctorEnd.setTag(false);
        btnDoctorEnd.setBackgroundResource(R.drawable.extras_item_back_n);
        if (!TextUtils.isEmpty(dayEntity.getDoctor())) {
            if (ResourcesUtil.getString(MainActivity.this, R.string.symbol_doctor_start).equals(dayEntity.getDoctor())) {
                btnDoctorStart.setTag(true);
                btnDoctorStart.setBackgroundResource(R.drawable.extras_item_back_h);
            } else if (ResourcesUtil.getString(MainActivity.this, R.string.symbol_doctor_end).equals(dayEntity.getDoctor())) {
                btnDoctorEnd.setTag(true);
                btnDoctorEnd.setBackgroundResource(R.drawable.extras_item_back_h);
            }
        }
        calendarPagerAdapter.notifyTemperatureDataChanged();
    }

    private void updateLineChartData() {
        chartPointList.clear();
        int perDp = WindowUtil.dip2px(this, 1);
        ChartConfig chartConfig = new ChartConfig(
                20 * perDp, 5 * perDp, 20 * perDp, 20 * perDp,
                7 * perDp, 7 * perDp,
                10 * perDp,
                WindowUtil.dip2px(this, 7),
                R.color.pool_text_color_hard,
                R.color.chart_line_fade_path_start_color,
                R.color.chart_line_fade_path_end_color,
                false);
        List<String> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (!dbUtil.isExists(tableName)) {
            dbUtil.createTable(tableName, new ColumnEntity[]{
                    new ColumnEntity(DBConstant.DATE, ColumnEntity.TYPE_TEXT, ""),
                    new ColumnEntity(DBConstant.VALUE, ColumnEntity.TYPE_TEXT, ""),
                    new ColumnEntity(DBConstant.SEXY, ColumnEntity.TYPE_TEXT, ""),
                    new ColumnEntity(DBConstant.BLOOD, ColumnEntity.TYPE_TEXT, ""),
                    new ColumnEntity(DBConstant.DOCTOR, ColumnEntity.TYPE_TEXT, "")
            });
        }
        Cursor cursor = dbUtil.queryData(tableName, null, null, null, null, null, DBConstant.DATE, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DBConstant.DATE));
            String value = cursor.getString(cursor.getColumnIndex(DBConstant.VALUE));
            String doctor = cursor.getString(cursor.getColumnIndex(DBConstant.DOCTOR));
            String blood = cursor.getString(cursor.getColumnIndex(DBConstant.BLOOD));
            String sexy = cursor.getString(cursor.getColumnIndex(DBConstant.SEXY));
            if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(value)) {
                xValues.add(date.substring(5));
                yValues.add(Double.parseDouble(value));
                String[] split = date.split("-");
                DayEntity dayEntity = new DayEntity(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2]));
                dayEntity.setTemperature(value);
                dayEntity.setDoctor(doctor);
                dayEntity.setBlood(blood);
                dayEntity.setSexy(sexy);
                chartPointList.add(dayEntity);
            }
        }
        if (xValues.size() > 0 && yValues.size() > 0) {
            lineChartLayout.setVisibility(View.VISIBLE);
            lineChart.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lineChart.updateValues(xValues, yValues, chartConfig);
                }
            }, 200);
        } else {
            lineChartLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 188 && resultCode == 199) {
            calendarPagerAdapter.notifyTemperatureDataChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnLineChart) {
            startActivity(new Intent(this, LineChartActivity.class));
        } else if (id == R.id.btnDelete) {
            DBUtil.deleteRaw(date);
            temperatureValueText.setText("36.00");
            ruler.setValue(36);
            btnSexy.setTag(false);
            btnSexy.setBackgroundResource(R.drawable.extras_item_back_n);
            btnBloodMuch.setTag(false);
            btnBloodMuch.setBackgroundResource(R.drawable.extras_item_back_n);
            btnBloodCommon.setTag(false);
            btnBloodCommon.setBackgroundResource(R.drawable.extras_item_back_n);
            btnBloodLittle.setTag(false);
            btnBloodLittle.setBackgroundResource(R.drawable.extras_item_back_n);
            btnBloodBit.setTag(false);
            btnBloodBit.setBackgroundResource(R.drawable.extras_item_back_n);
            btnDoctorStart.setTag(false);
            btnDoctorStart.setBackgroundResource(R.drawable.extras_item_back_n);
            btnDoctorEnd.setTag(false);
            btnDoctorEnd.setBackgroundResource(R.drawable.extras_item_back_n);
            calendarPagerAdapter.notifyTemperatureDataChanged();
            updateLineChartData();
        } else if (id == R.id.btnRecord) {
            DBUtil.saveTemperature(
                    date,
                    temperatureValueText.getText().toString(),
                    getExtraSexy(),
                    getExtraBlood(),
                    getExtraDoctor());
            calendarPagerAdapter.notifyTemperatureDataChanged();
            updateLineChartData();
        } else if (id == R.id.btnSexy) {
            if (btnSexy.getTag() != null && (boolean) btnSexy.getTag()) {
                btnSexy.setTag(false);
                btnSexy.setBackgroundResource(R.drawable.extras_item_back_n);
            } else {
                btnSexy.setTag(true);
                btnSexy.setBackgroundResource(R.drawable.extras_item_back_h);
            }
        } else if (id == R.id.btnBloodMuch || id == R.id.btnBloodCommon || id == R.id.btnBloodLittle || id == R.id.btnBloodBit) {
            if (view.getTag() != null && (boolean) view.getTag()) {
                view.setTag(false);
                ((TTFTextView) view).setBackgroundResource(R.drawable.extras_item_back_n);
            } else {
                btnBloodMuch.setTag(false);
                btnBloodMuch.setBackgroundResource(R.drawable.extras_item_back_n);
                btnBloodCommon.setTag(false);
                btnBloodCommon.setBackgroundResource(R.drawable.extras_item_back_n);
                btnBloodLittle.setTag(false);
                btnBloodLittle.setBackgroundResource(R.drawable.extras_item_back_n);
                btnBloodBit.setTag(false);
                btnBloodBit.setBackgroundResource(R.drawable.extras_item_back_n);
                view.setTag(true);
                ((TTFTextView) view).setBackgroundResource(R.drawable.extras_item_back_h);
            }
        } else if (id == R.id.btnDoctorStart || id == R.id.btnDoctorEnd) {
            if (view.getTag() != null && (boolean) view.getTag()) {
                view.setTag(false);
                ((TTFTextView) view).setBackgroundResource(R.drawable.extras_item_back_n);
            } else {
                btnDoctorStart.setTag(false);
                btnDoctorStart.setBackgroundResource(R.drawable.extras_item_back_n);
                btnDoctorEnd.setTag(false);
                btnDoctorEnd.setBackgroundResource(R.drawable.extras_item_back_n);
                view.setTag(true);
                ((TTFTextView) view).setBackgroundResource(R.drawable.extras_item_back_h);
            }
        } else if (id == R.id.iconPrint) {
            //startActivity(new Intent(this, PrintActivity.class));
            startActivity(new Intent(this, CalendarActivity.class));
        } else if (id == R.id.iconTips) {
            AnimateUtil.showTransBack(transBackTop);
            AnimateUtil.showTransBack(transBackBottom);
            AnimateUtil.showTransBack(extrasTips);
        } else if (id == R.id.transBackTop || id == R.id.transBackBottom) {
            AnimateUtil.hideTransBack(transBackTop);
            AnimateUtil.hideTransBack(transBackBottom);
            AnimateUtil.hideTransBack(extrasTips);
        }
    }

    private String getExtraSexy() {
        return btnSexy.getTag() != null && (boolean) btnSexy.getTag() ? ResourcesUtil.getString(MainActivity.this, R.string.symbol_sexy) : "";
    }

    private String getExtraBlood() {
        if (btnBloodMuch.getTag() != null && (boolean) btnBloodMuch.getTag()) {
            return ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_much);
        } else if (btnBloodCommon.getTag() != null && (boolean) btnBloodCommon.getTag()) {
            return ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_common);
        } else if (btnBloodLittle.getTag() != null && (boolean) btnBloodLittle.getTag()) {
            return ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_little);
        } else if (btnBloodBit.getTag() != null && (boolean) btnBloodBit.getTag()) {
            return ResourcesUtil.getString(MainActivity.this, R.string.symbol_blood_bit);
        }
        return "";
    }

    private String getExtraDoctor() {
        if (btnDoctorStart.getTag() != null && (boolean) btnDoctorStart.getTag()) {
            return ResourcesUtil.getString(MainActivity.this, R.string.symbol_doctor_start);
        } else if (btnDoctorEnd.getTag() != null && (boolean) btnDoctorEnd.getTag()) {
            return ResourcesUtil.getString(MainActivity.this, R.string.symbol_doctor_end);
        }
        return "";
    }

}
