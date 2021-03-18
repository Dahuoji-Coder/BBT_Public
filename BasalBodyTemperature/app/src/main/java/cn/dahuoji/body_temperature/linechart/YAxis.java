package cn.dahuoji.body_temperature.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;

import cn.dahuoji.body_temperature.LocalApplication;
import cn.dahuoji.body_temperature.R;
import cn.dahuoji.body_temperature.util.MathUtil;
import cn.dahuoji.body_temperature.util.ThemeUtil;

/**
 * Created by 10732 on 2018/6/15.
 */

public class YAxis extends View {

    private TextPaint mLabelsPaint;
    private double maxValue = 1.0f;
    private double minValue = 0;
    private int height, width;
    private ChartConfig chartConfig;

    public YAxis(Context context, ChartConfig chartConfig) {
        super(context);
        this.chartConfig = chartConfig;
        init();
    }

    private void init() {
        mLabelsPaint = new TextPaint();
        mLabelsPaint.setAntiAlias(true);
        mLabelsPaint.setTextSize(chartConfig.getLabelTextSize());
        mLabelsPaint.setTextAlign(Paint.Align.RIGHT);
        mLabelsPaint.setColor(ThemeUtil.getThemeColorById(R.color.pool_text_color_hard));
        //mLabelsPaint.setTypeface(LocalApplication.getPf_normal());
    }

    public TextPaint getLabelsPaint() {
        return mLabelsPaint;
    }

    public void setMaxValue(double maxValue, double minValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int labelsCount = 5;
        double itemSpace = (maxValue - minValue) / labelsCount;
        for (int i = 0; i <= labelsCount; i++) {
            float itemY = 1.0f * (height - chartConfig.getOffTop() - chartConfig.getOffBottom() - chartConfig.getLabelTextSize() - chartConfig.getOffXAxis()) / labelsCount * i + chartConfig.getOffTop();
            double value = minValue + itemSpace * (labelsCount - i);
            canvas.drawText(MathUtil.getFormatNumber(value, 2), width, itemY + chartConfig.getLabelTextSize() * 1.0f / 3, mLabelsPaint);
        }
    }

}
