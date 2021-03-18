package cn.dahuoji.body_temperature.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.view.View;

import java.util.List;

import cn.dahuoji.body_temperature.LocalApplication;
import cn.dahuoji.body_temperature.R;
import cn.dahuoji.body_temperature.util.ThemeUtil;

public class CommonChartBack extends View {

    private int width, height;
    private List<String> values;
    private ChartConfig chartConfig;
    private Paint mXAxisPaint, mLabelsPaint, mXGridLinePaint, mYGridLinePaint;

    public CommonChartBack(Context context, ChartConfig chartConfig, List<String> values) {
        super(context);
        this.chartConfig = chartConfig;
        this.values = values;
        init(context);
    }

    private void init(Context context) {
        // X轴画笔
        mXAxisPaint = new Paint();
        mXAxisPaint.setStrokeWidth(1f);
        mXAxisPaint.setColor(ThemeUtil.getThemeColorById(R.color.pool_text_color_hard));
        mXAxisPaint.setAlpha(180);
        // labels 画笔
        mLabelsPaint = new TextPaint();
        mLabelsPaint.setAntiAlias(true);
        mLabelsPaint.setTextSize(chartConfig.getLabelTextSize());
        mLabelsPaint.setTextAlign(Paint.Align.CENTER);
        mLabelsPaint.setColor(ThemeUtil.getThemeColorById(R.color.pool_text_color_hard));
        //mLabelsPaint.setTypeface(LocalApplication.getPf_normal());
        // X轴 GridLines
        mXGridLinePaint = new Paint();
        mXGridLinePaint.setStrokeWidth(2f);
        // Y轴 GridLines
        mYGridLinePaint = new Paint();
        mYGridLinePaint.setStrokeWidth(2f);
        mYGridLinePaint.setColor(ThemeUtil.getThemeColorById(R.color.pool_text_color_hard));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        //X轴从下向上颜色渐浅的分割线
        LinearGradient xGirdLineBack = new LinearGradient(
                0, h - chartConfig.getOffBottom() - chartConfig.getLabelTextSize() - chartConfig.getOffXAxis(),
                0, chartConfig.getOffTop(), Color.parseColor("#6634495E"),
                Color.parseColor("#0634495E"), Shader.TileMode.CLAMP);
        mXGridLinePaint.setShader(xGirdLineBack);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null || values.size() == 0) return;

        float xAxisPositionY = height - chartConfig.getOffBottom() - chartConfig.getLabelTextSize() - chartConfig.getOffXAxis();
        //绘制X轴线
        //canvas.drawLine(chartConfig.getOffYAxis(), xAxisPositionY, width - chartConfig.getOffRight(), xAxisPositionY, mXAxisPaint);
        //绘制Label
        float labelY = xAxisPositionY + chartConfig.getOffXAxis() + chartConfig.getLabelTextSize();
        if (values.size() == 1) {
            //只有一个点时, 画在中间一个圈
            float labelX = chartConfig.getOffYAxis() + (width - chartConfig.getOffYAxis() - chartConfig.getOffRight()) / 2.0f;
            canvas.drawText(values.get(0), labelX, labelY, mLabelsPaint);
            canvas.drawLine(labelX, xAxisPositionY, labelX, chartConfig.getOffTop(), mXGridLinePaint);
        } else if (values.size() == 2 || values.size() == 3 || values.size() == 4) {
            //二或三或者四个点时, 平均分配
            float separateLength = (width - chartConfig.getOffYAxis() - chartConfig.getSidesBlankWidth() * 2 - chartConfig.getOffRight() * 1.0f) / (values.size() - 1);
            for (int i = 0; i < values.size(); i++) {
                float labelX = chartConfig.getOffYAxis() + chartConfig.getSidesBlankWidth() + separateLength * i;
                canvas.drawText(
                        values.get(i),
                        labelX,
                        labelY,
                        mLabelsPaint);
                canvas.drawLine(labelX, xAxisPositionY, labelX, chartConfig.getOffTop(), mXGridLinePaint);
            }
        } else {
            //五个点及以上, 中间画三条横向距离相同的竖线
            int separateCount = (int) Math.floor(values.size() * 1.0f / 4);
            float separateLength = (width - chartConfig.getOffYAxis() - chartConfig.getSidesBlankWidth() * 2 - chartConfig.getOffRight()) / (values.size() - 1.0f);
            //绘制除了最后一个点之外的点
            int xGridLinesCount = 0;
            for (int i = 0; i < values.size() - 1 && xGridLinesCount < 4; i++) {
                if (i % separateCount == 0) {
                    float labelX = chartConfig.getOffYAxis() + chartConfig.getSidesBlankWidth() + separateLength * i;
                    canvas.drawText(
                            values.get(i),
                            labelX,
                            labelY,
                            mLabelsPaint);
                    canvas.drawLine(labelX, xAxisPositionY, labelX, chartConfig.getOffTop(), mXGridLinePaint);
                    xGridLinesCount++;
                }
            }
            //最后一个点在最右边
            float lastLabelX = width - chartConfig.getOffRight() - chartConfig.getSidesBlankWidth();
            canvas.drawText(
                    values.get(values.size() - 1),
                    lastLabelX,
                    labelY,
                    mLabelsPaint);
            canvas.drawLine(lastLabelX, xAxisPositionY, lastLabelX, chartConfig.getOffTop(), mXGridLinePaint);
        }

        //Y轴 gridLines
        for (int i = 0; i <= 5; i++) {
            float itemY = 1.0f * (xAxisPositionY - chartConfig.getOffTop()) / 5 * i + chartConfig.getOffTop();
            mYGridLinePaint.setAlpha((int) (itemY / height * 80 + 6));
            canvas.drawLine(chartConfig.getOffYAxis(), itemY, width - chartConfig.getOffRight(), itemY, mYGridLinePaint);
        }
    }
}
