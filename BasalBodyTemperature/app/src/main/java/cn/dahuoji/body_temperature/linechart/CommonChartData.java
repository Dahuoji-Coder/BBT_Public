package cn.dahuoji.body_temperature.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cn.dahuoji.body_temperature.R;
import cn.dahuoji.body_temperature.util.ThemeUtil;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class CommonChartData extends View {

    private Context context;
    private ChartConfig chartConfig;
    private Paint mLinePaint, mFillPaint, mHighLightPaint, mHighLightLinePaint;
    private Path fadePath, linePath;
    private List<Double> values;
    private double maxValue, minValue;
    private int width, height;
    private float touchedX = -1;

    public CommonChartData(Context context, ChartConfig chartConfig, List<Double> values, double maxValue, double minValue) {
        super(context);
        this.context = context;
        this.chartConfig = chartConfig;
        this.values = values;
        this.maxValue = maxValue;
        this.minValue = minValue;
        //折线画笔
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(WindowUtil.dip2px(context, 2f));
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setColor(ThemeUtil.getThemeColorById(chartConfig.getLineColorId()));
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        //渐变背景画笔
        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        //高亮点画笔
        mHighLightPaint = new Paint();
        mHighLightPaint.setStyle(Paint.Style.FILL);
        mHighLightPaint.setColor(Color.WHITE);
        mHighLightPaint.setAntiAlias(true);
        //高亮点下面的线画笔
        mHighLightLinePaint = new Paint();
        mHighLightLinePaint.setStyle(Paint.Style.STROKE);
        mHighLightLinePaint.setStrokeWidth(WindowUtil.dip2px(context, 1.5f));
        mHighLightLinePaint.setAntiAlias(true);
        //渐变路径
        fadePath = new Path();
        //折线路径
        linePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        //为了展开动画，能把0的线展示完整，height实际大chartConfig.getOffXAxis()
        this.height = h - chartConfig.getOffXAxis();
        LinearGradient xFillBack = new LinearGradient(0, 0, 0, h,
                ThemeUtil.getThemeColorById(chartConfig.getFadePathStartColorId()),
                ThemeUtil.getThemeColorById(chartConfig.getFadePathEndColorId()),
                Shader.TileMode.CLAMP);
        mFillPaint.setShader(xFillBack);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.size() == 0) return;

        double per = 1.0f * height / (maxValue - minValue);
        fadePath.reset();
        fadePath.moveTo(chartConfig.getSidesBlankWidth(), height);
        linePath.reset();
        if (values.size() > 1) {
            float itemXBehind = 0f;
            float itemYBehind = 0f;
            for (int i = 0; i < values.size() - 1; i++) {
                float itemX = chartConfig.getSidesBlankWidth() + 1.0f * i * (width - chartConfig.getSidesBlankWidth() * 2) / (values.size() - 1);
                float itemY = (float) (height - (values.get(i) - minValue) * per);
                itemXBehind = chartConfig.getSidesBlankWidth() + 1.0f * (i + 1) * (width - chartConfig.getSidesBlankWidth() * 2) / (values.size() - 1);
                itemYBehind = (float) (height - (values.get(i + 1) - minValue) * per);
                if (i == 0) {
                    linePath.moveTo(itemX, itemY);
                    //绘制路径做渐变
                    fadePath.lineTo(itemX, itemY);
                }
                linePath.lineTo(itemXBehind, itemYBehind);
                fadePath.lineTo(itemXBehind, itemYBehind);
            }
            fadePath.lineTo(width - chartConfig.getSidesBlankWidth(), height);
            fadePath.close();
            if (!chartConfig.isNeedNegativeNumber()) {
                canvas.drawPath(fadePath, mFillPaint);
            }
            canvas.drawPath(linePath, mLinePaint);
        } else {
            float cx = width / 2.0f;
            float cy = (float) (height - values.get(0) * per);
            mHighLightPaint.setColor(Color.WHITE);
            canvas.drawCircle(cx, cy, WindowUtil.dip2px(context, 2), mHighLightPaint);
        }
        //触摸时绘制高亮点
        drawHighlight(canvas);
    }

    private void drawHighlight(Canvas canvas) {
        if (touchedX == -1 || values == null || values.size() == 0) return;

        if (values.size() > 1) {
            float itemXSpace = 1.0f * (width - chartConfig.getSidesBlankWidth() * 2) / (values.size() - 1);
            int temp = (int) Math.floor((touchedX - chartConfig.getSidesBlankWidth()) / itemXSpace);
            float judgeTemp = touchedX - chartConfig.getSidesBlankWidth() - temp * itemXSpace;
            boolean isHalfPassed = false;
            if (judgeTemp > itemXSpace / 2) {
                temp++;
                isHalfPassed = true;
            }
            if (temp >= 0 && temp <= values.size() - 1) {
                float per = (float) (1.0f * height / (maxValue - minValue));
                float highLightX = chartConfig.getSidesBlankWidth() + temp * itemXSpace;
                float highLightY = (float) (height - (values.get(temp) - minValue) * per);
                //计算Y值, 平滑移动
                float circleY = 0f;
                float highLightAnotherX, highLightAnotherY;
                if (isHalfPassed) {
                    //证明 temp++ 处理过, another是前面的
                    if (temp == 0) return;
                    highLightAnotherX = chartConfig.getSidesBlankWidth() + (temp - 1) * itemXSpace;
                    highLightAnotherY = (float) (height - (values.get(temp - 1) - minValue) * per);
                    circleY = highLightAnotherY + 1.0f * (touchedX - highLightAnotherX) / itemXSpace * (highLightY - highLightAnotherY);
                } else {
                    // another 是后面的
                    if (temp == values.size() - 1) {
                        circleY = (float) (height - (values.get(temp) - minValue) * per);
                    } else {
                        highLightAnotherX = chartConfig.getSidesBlankWidth() + (temp + 1) * itemXSpace;
                        highLightAnotherY = (float) (height - (values.get(temp + 1) - minValue) * per);
                        circleY = highLightY + 1.0f * (touchedX - highLightX) / itemXSpace * (highLightAnotherY - highLightY);
                    }
                }
                //画高亮线
                LinearGradient xLineFade = new LinearGradient(highLightX, highLightY, highLightX, height,
                        Color.parseColor("#BB6666"),
                        Color.parseColor("#BB6666"),
                        Shader.TileMode.CLAMP);
                mHighLightLinePaint.setShader(xLineFade);
                float stopY = height;
                if (stopY > circleY) {
                    canvas.drawLine(touchedX, circleY, touchedX, stopY, mHighLightLinePaint);
                }
                //画高亮点
                mHighLightPaint.setColor(Color.parseColor("#BB6666"));
                canvas.drawCircle(touchedX, circleY, WindowUtil.dip2px(context, 6), mHighLightPaint);
                mHighLightPaint.setColor(ThemeUtil.getThemeColorById(R.color.text_color_white));
                canvas.drawCircle(touchedX, circleY, WindowUtil.dip2px(context, 3.5f), mHighLightPaint);
                //回调
                if (onCommonChartDataListener != null) {
                    onCommonChartDataListener.onHighLightStatusUpdate(temp, touchedX, circleY);
                }
            }
        } else {
            float cx = width / 2.0f;
            float cy = (float) (height - (values.get(0) - minValue) * (1.0f * height / (maxValue - minValue)));
            //画高亮线
            LinearGradient xLineFade = new LinearGradient(cx, cy, cx, height,
                    ThemeUtil.getThemeColorById(R.color.chart_line_highlight_line_start_color),
                    ThemeUtil.getThemeColorById(R.color.chart_line_highlight_line_end_color),
                    Shader.TileMode.CLAMP);
            mHighLightLinePaint.setShader(xLineFade);
            float stopY = height;
            if (stopY > cy) {
                canvas.drawLine(cx, cy, cx, stopY, mHighLightLinePaint);
            }
            //画高亮点
            mHighLightPaint.setColor(ThemeUtil.getThemeColorById(R.color.chart_line_highlight_circle_color));
            canvas.drawCircle(cx, cy, WindowUtil.dip2px(context, 6), mHighLightPaint);
            mHighLightPaint.setColor(ThemeUtil.getThemeColorById(R.color.text_color_white));
            canvas.drawCircle(cx, cy, WindowUtil.dip2px(context, 3.5f), mHighLightPaint);
            //回调
            if (onCommonChartDataListener != null) {
                onCommonChartDataListener.onHighLightStatusUpdate(0, cx, cy);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //这行代码的意思是告诉父控件,这次touch事件由我自己处理
        getParent().requestDisallowInterceptTouchEvent(true);
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (currentX < chartConfig.getSidesBlankWidth())
                    currentX = chartConfig.getSidesBlankWidth();
                if (currentX > width - chartConfig.getSidesBlankWidth())
                    currentX = width - chartConfig.getSidesBlankWidth();
                setHighLight(currentX);
                break;
            default:
                performClick();
                //取消高亮
                setHighLight(-1);
                if (onCommonChartDataListener != null) {
                    onCommonChartDataListener.onHighLightStatusUpdate(-1, 0, 0);
                }
                break;
        }

        return true;
    }

    public void setHighLight(float touchedX) {
        this.touchedX = touchedX;
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private OnCommonChartDataListener onCommonChartDataListener;

    public void setOnCommonChartDataListener(OnCommonChartDataListener onCommonChartDataListener) {
        this.onCommonChartDataListener = onCommonChartDataListener;
    }

    public interface OnCommonChartDataListener {
        void onHighLightStatusUpdate(int index, float positionX, float positionY);
    }
}
