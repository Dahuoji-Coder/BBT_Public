package cn.dahuoji.body_temperature;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.dahuoji.body_temperature.database.ColumnEntity;
import cn.dahuoji.body_temperature.database.DBConstant;
import cn.dahuoji.body_temperature.database.DBUtil;
import cn.dahuoji.body_temperature.linechart.ChartConfig;
import cn.dahuoji.body_temperature.linechart.CommonLineChart;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class LineChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        CommonLineChart lineChart = findViewById(R.id.lineChart);
        int perDp = WindowUtil.dip2px(this, 1);
        ChartConfig chartConfig = new ChartConfig(
                20 * perDp, 5 * perDp, 20 * perDp, 20 * perDp,
                7 * perDp, 7 * perDp,
                6 * perDp,
                WindowUtil.dip2px(this, 7),
                R.color.text_color_white,
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
        Cursor cursor = dbUtil.queryData(tableName, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(DBConstant.DATE));
            String value = cursor.getString(cursor.getColumnIndex(DBConstant.VALUE));
            xValues.add(date);
            yValues.add(Double.parseDouble(value));
        }
        lineChart.postDelayed(new Runnable() {
            @Override
            public void run() {
                lineChart.updateValues(xValues, yValues, chartConfig);
            }
        }, 200);
    }
}