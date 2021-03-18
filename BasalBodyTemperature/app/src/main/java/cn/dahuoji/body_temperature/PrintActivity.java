package cn.dahuoji.body_temperature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dahuoji.body_temperature.database.DBUtil;
import cn.dahuoji.body_temperature.skinview.TTFTextView;
import cn.dahuoji.body_temperature.util.FileUtil;
import cn.dahuoji.body_temperature.util.MathUtil;
import cn.dahuoji.body_temperature.util.ResourcesUtil;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class PrintActivity extends BaseActivity implements View.OnClickListener {

    public static final int EXTERNAL_STORAGE_REQUEST_CODE = 111;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ImageView imageView = findViewById(R.id.imageView);
        DayEntity startDayEntity = DBUtil.queryData(DateUtil.selectDateStart);
        DayEntity endDayEntity = DBUtil.queryData(DateUtil.selectDateEnd);
        List<DayEntity> recordList = DBUtil.getInstance().queryPeriodData(startDayEntity.getDateString(), endDayEntity.getDateString());
        List<DayEntity> dayList = new ArrayList<>();
        List<Float> temperatureList = new ArrayList<>();
        if (startDayEntity.getYear() == endDayEntity.getYear() && startDayEntity.getMonth() == endDayEntity.getMonth()) {
            //选择的是同一个月里面的时间段
            for (int i = startDayEntity.getDay(); i <= endDayEntity.getDay(); i++) {
                boolean hasValue = false;
                for (int j = 0; j < recordList.size(); j++) {
                    if (recordList.get(j).getDay() == i) {
                        dayList.add(recordList.get(j));
                        hasValue = true;
                        temperatureList.add(Float.parseFloat(recordList.get(j).getTemperature()));
                        break;
                    }
                }
                if (!hasValue) {
                    dayList.add(new DayEntity(startDayEntity.getYear(), startDayEntity.getMonth(), i));
                    temperatureList.add(0f);
                }
            }
        } else {
            //选择的日期不在同一个月
            int monthLastDay = DateUtil.getMonthLastDay(startDayEntity.getYear(), endDayEntity.getMonth());
            for (int i = startDayEntity.getDay(); i <= monthLastDay; i++) {
                boolean hasValue = false;
                for (int j = 0; j < recordList.size(); j++) {
                    if (recordList.get(j).getDateString().equals(DateUtil.getFormatDate(startDayEntity.getYear(), startDayEntity.getMonth(), i))) {
                        dayList.add(recordList.get(j));
                        hasValue = true;
                        temperatureList.add(Float.parseFloat(recordList.get(j).getTemperature()));
                        break;
                    }
                }
                if (!hasValue) {
                    dayList.add(new DayEntity(startDayEntity.getYear(), startDayEntity.getMonth(), i));
                    temperatureList.add(0f);
                }
            }
            for (int i = 1; i <= endDayEntity.getDay(); i++) {
                boolean hasValue = false;
                for (int j = 0; j < recordList.size(); j++) {
                    if (recordList.get(j).getDateString().equals(DateUtil.getFormatDate(endDayEntity.getYear(), endDayEntity.getMonth(), i))) {
                        dayList.add(recordList.get(j));
                        hasValue = true;
                        temperatureList.add(Float.parseFloat(recordList.get(j).getTemperature()));
                        break;
                    }
                }
                if (!hasValue) {
                    dayList.add(new DayEntity(endDayEntity.getYear(), endDayEntity.getMonth(), i));
                    temperatureList.add(0f);
                }
            }
        }
        float maxTemp = Collections.max(temperatureList);
        float minTemp = Collections.min(temperatureList);
        double max = Math.ceil(maxTemp);
        if (maxTemp == 0) {
            max = 36.0;
        } else {
            if (maxTemp == max) max = maxTemp + 0.2;
        }
        double min = Math.floor(minTemp);
        if (min < 35) min = 35;
        float scale = 2;
        int itemWidth = (int) (30 * scale);
        int itemHeight = (int) (30 * scale);
        TextPaint textPaint = new TextPaint();
        int textSize = (int) (12 * scale);
        int labelsOff = (int) (10 * scale);
        int padding = (int) (40 * scale);
        int pointSize = (int) (4 * scale);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        float yLabelWidth = Math.max(textPaint.measureText("00.0"), textPaint.measureText("doctor"));
        int bitmapWidth = (int) (dayList.size() * itemWidth + yLabelWidth + labelsOff + padding * 2);
        int extrasHeight = itemHeight * 3;
        int bitmapHeight = (int) ((max - min) / 0.1) * itemHeight + textSize + labelsOff + padding * 2 + extrasHeight;
        Bitmap.Config config = Bitmap.Config.ARGB_4444;
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, config);
        Canvas canvas = new Canvas(bitmap);
        Paint gridLinePaint = new Paint();
        gridLinePaint.setColor(Color.parseColor("#CCCCCC"));
        gridLinePaint.setAntiAlias(true);
        gridLinePaint.setStyle(Paint.Style.STROKE);
        gridLinePaint.setStrokeWidth(1);
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#FFFFFF"));
        bgPaint.setStyle(Paint.Style.FILL);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.parseColor("#CC3333"));
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#CC3333"));
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2 * scale);
        TextPaint extrasPaint = new TextPaint();
        extrasPaint.setColor(Color.parseColor("#CC3333"));
        extrasPaint.setAntiAlias(true);
        extrasPaint.setTextSize(textSize);
        extrasPaint.setTextAlign(Paint.Align.CENTER);
        Paint bloodPaint = new Paint();
        bloodPaint.setColor(Color.parseColor("#22BB6666"));
        bloodPaint.setAntiAlias(true);
        bloodPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, bgPaint);
        int baseLineHeight = bitmapHeight - padding - textSize - labelsOff;
        //Y轴
        canvas.drawLine(padding + yLabelWidth + labelsOff, baseLineHeight, padding + yLabelWidth + labelsOff, padding, gridLinePaint);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        int yItemCount = Integer.parseInt(MathUtil.getFormatNumberForElectricity((max - min) * 10, 0));
        //Log.d(">>>PrintActivity", "===(max - min) * 10 = " + yItemCount);
        for (int i = 0; i <= yItemCount; i++) {
            if (i < yItemCount) {
                String yText = MathUtil.getFormatNumber(min + 0.1 * i, 1);
                if (!yText.endsWith(".0")) {
                    yText = yText.substring(yText.indexOf("."));
                }
                canvas.drawText(yText, padding + yLabelWidth, baseLineHeight - i * itemHeight + textSize / 3.0f, textPaint);
            }
            canvas.drawLine(padding + yLabelWidth + labelsOff, baseLineHeight - i * itemHeight, bitmapWidth - padding, baseLineHeight - i * itemHeight, gridLinePaint);
        }
        //额外标识
        String[] extrasLabel = {"doctor", "sex", "blood"};
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(padding + yLabelWidth + labelsOff, padding + itemHeight * i, bitmapWidth - padding, padding + itemHeight * i, gridLinePaint);
            canvas.drawText(extrasLabel[i], padding + yLabelWidth, padding + itemHeight * i + itemHeight / 2.0f + textSize / 3.0f, textPaint);
        }
        //X轴
        canvas.drawLine(padding + yLabelWidth + labelsOff, baseLineHeight, bitmapWidth - padding, baseLineHeight, gridLinePaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < dayList.size(); i++) {
            DayEntity dayEntity = dayList.get(i);
            float itemX = padding + yLabelWidth + labelsOff + itemWidth * i;
            canvas.drawText(dayEntity.getDay() + "", itemX + itemWidth / 2.0f, bitmapHeight - padding, textPaint);
            canvas.drawLine(itemX + itemWidth, baseLineHeight, itemX + itemWidth, padding, gridLinePaint);
            //额外的参数
            if (!TextUtils.isEmpty(dayEntity.getDoctor())) {
                canvas.drawText(dayEntity.getDoctor(), itemX + itemWidth / 2.0f, padding + itemHeight / 2.0f + textSize / 3.0f, extrasPaint);
            }
            if (!TextUtils.isEmpty(dayEntity.getSexy())) {
                canvas.drawText(ResourcesUtil.getString(this, R.string.symbol_love), itemX + itemWidth / 2.0f, padding + itemHeight + itemHeight / 2.0f + textSize / 3.0f, extrasPaint);
            }
            if (!TextUtils.isEmpty(dayEntity.getBlood())) {
                canvas.drawText(dayEntity.getBlood(), itemX + itemWidth / 2.0f, padding + itemHeight * 2 + itemHeight / 2.0f + textSize / 3.0f, extrasPaint);
                Path path = new Path();
                path.moveTo(itemX, baseLineHeight);
                path.lineTo(itemX + itemWidth, baseLineHeight);
                path.lineTo(itemX + itemWidth, padding + itemHeight * 3);
                path.lineTo(itemX, padding + itemHeight * 3);
                path.close();
                canvas.drawPath(path, bloodPaint);
            }
            //绘制点
            double perY = itemHeight / 0.1;
            float itemValue;
            if (TextUtils.isEmpty(dayEntity.getTemperature())) {
                itemValue = 0;
            } else {
                itemValue = Float.parseFloat(dayEntity.getTemperature());
            }
            int itemY;
            if (itemValue == 0) {
                itemY = baseLineHeight;
            } else {
                itemY = (int) (baseLineHeight - (itemValue - min) * perY);
            }
            if (itemValue == 0) {
                canvas.drawCircle(itemX + itemWidth / 2.0f, itemY, 0, pointPaint);
            } else {
                canvas.drawCircle(itemX + itemWidth / 2.0f, itemY, pointSize, pointPaint);
                textPaint.setTextAlign(Paint.Align.CENTER);
                //TODO 绘制详细温度
                //canvas.drawText(dayEntity.getTemperature() + " " + dayEntity.getExtrasStr(), itemX, itemY - WindowUtil.dip2px(this, 10), textPaint);
            }
            //折线
            if (i < dayList.size() - 1) {
                int itemXNext = (int) (itemX + itemWidth);
                float itemValueNext;
                if (TextUtils.isEmpty(dayList.get(i + 1).getTemperature())) {
                    itemValueNext = 0;
                } else {
                    itemValueNext = Float.parseFloat(dayList.get(i + 1).getTemperature());
                }
                int itemYNext;
                if (itemValueNext == 0) {
                    itemYNext = baseLineHeight;
                } else {
                    itemYNext = (int) (baseLineHeight - (itemValueNext - min) * perY);
                }
                canvas.drawLine(itemX + itemWidth / 2.0f, itemY, itemXNext + itemWidth / 2.0f, itemYNext, linePaint);
            }
        }
        //绘制日期
        TextPaint datePaint = new TextPaint();
        datePaint.setAntiAlias(true);
        datePaint.setTextSize(16 * scale);
        datePaint.setColor(Color.BLACK);
        datePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(DateUtil.selectDateStart + " ~ " + DateUtil.selectDateEnd, bitmapWidth - padding, 30 * scale, datePaint);
        int activityHeight = WindowUtil.getActivityHeight(this) - WindowUtil.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = (int) (activityHeight * 1.0 / bitmapHeight * bitmapWidth);
        imageView.setLayoutParams(layoutParams);
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
        findViewById(R.id.iconNative).setOnClickListener(this);
    }

    private void saveNative() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 没有权限。
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    // 申请授权
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                }
            } else {
                // 有权限 直接保存图片
                shareContinue();
            }
        } else {
            shareContinue();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限已申请
                shareContinue();
            } else {
                //权限已拒绝
                //textDialog.show("失败", "因为您拒绝了此权限请求将导致无法存储图片");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void shareContinue() {
        new PictureAsyncTask().execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iconNative) {
            saveNative();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class PictureAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return FileUtil.saveBmp2Gallery(PrintActivity.this, bitmap, "dahuoji_bbt_" + System.currentTimeMillis());
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(PrintActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
//            if (!bitmap.isRecycled()) {
//                bitmap.recycle();
//            }
        }
    }
}