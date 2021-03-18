package cn.dahuoji.body_temperature.linechart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import cn.dahuoji.body_temperature.util.MathUtil;

public class CommonLineChart extends FrameLayout {

    private Context context;
    private FrameLayout animateLayout;

    public CommonLineChart(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CommonLineChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setClipChildren(true);
    }

    public void updateValues(List<String> xValues, List<Double> yValues, ChartConfig chartConfig) {
        if (getChildCount() > 0) {
            animateLayout.removeAllViews();
            removeAllViews();
        }
        Double maxTemp = Collections.max(yValues);
        Double minTemp = Collections.min(yValues);
        double max = Math.ceil(maxTemp);
        if (maxTemp == max) max = maxTemp + 0.5;
        double min = Math.floor(minTemp);
        //Y轴
        YAxis yAxis = new YAxis(context, chartConfig);
        String maxValueText = MathUtil.getFormatNumberForElectricity(max, 0), minValueText = MathUtil.getFormatNumberForElectricity(min, 0);
        int labelTextWidth = Math.max((int) yAxis.getLabelsPaint().measureText(maxValueText), (int) yAxis.getLabelsPaint().measureText(minValueText));
        final int yAxisWidth = (int) (chartConfig.getOffLeft() + labelTextWidth);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(yAxisWidth, getMeasuredHeight());
        addView(yAxis, layoutParams);
        yAxis.setMaxValue(max, min);
        //X轴 和 GridLines
        CommonChartBack chartBack = new CommonChartBack(context, chartConfig, xValues);
        LayoutParams chartBackLayoutParams = new LayoutParams(getMeasuredWidth() - yAxisWidth, getMeasuredHeight());
        chartBackLayoutParams.leftMargin = yAxisWidth;
        addView(chartBack, chartBackLayoutParams);
        //图表数据过渡动画布局
        animateLayout = new FrameLayout(context);
        //为了展开动画，能把0的线展示完整，height实际大chartConfig.getOffXAxis()
        final LayoutParams animateLayoutParams = new LayoutParams(
                getMeasuredWidth() - yAxisWidth - chartConfig.getOffYAxis() - chartConfig.getOffRight(),
                getMeasuredHeight() - chartConfig.getOffTop() - chartConfig.getOffBottom() - chartConfig.getLabelTextSize());
        animateLayoutParams.leftMargin = yAxisWidth + chartConfig.getOffYAxis();
        animateLayoutParams.topMargin = chartConfig.getOffTop();
        animateLayout.setLayoutParams(animateLayoutParams);
        //折线 和 填充
        CommonChartData chartData = new CommonChartData(context, chartConfig, yValues, max, min);
        RelativeLayout.LayoutParams layoutParamsChartData = new RelativeLayout.LayoutParams(animateLayoutParams.width, animateLayoutParams.height);
        animateLayout.addView(chartData, layoutParamsChartData);
        addView(animateLayout, animateLayoutParams);
        chartData.setOnCommonChartDataListener(new CommonChartData.OnCommonChartDataListener() {
            @Override
            public void onHighLightStatusUpdate(int index, float positionX, float positionY) {
                if (onCommonLineChartListener != null) {
                    onCommonLineChartListener.onHighLightStatusUpdate(index, animateLayoutParams.leftMargin + positionX, positionY + animateLayoutParams.topMargin);
                }
            }
        });

        //曲线动画
        animateLayout.setAlpha(0f);
        setClipChildren(true);
        animateLayout.setClipChildren(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(0, animateLayoutParams.width / 5, animateLayoutParams.width);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        animateLayoutParams.width = (int) animation.getAnimatedValue();
                        animateLayout.setLayoutParams(animateLayoutParams);
                        if (animateLayoutParams.width > 0) {
                            animateLayout.setAlpha(1f);
                        }
                    }
                });
                valueAnimator.setDuration(1100);
                valueAnimator.start();
            }
        }, 100);
    }

    private OnCommonLineChartListener onCommonLineChartListener;

    public void setOnCommonLineChartListener(OnCommonLineChartListener onCommonLineChartListener) {
        this.onCommonLineChartListener = onCommonLineChartListener;
    }

    public interface OnCommonLineChartListener {
        void onHighLightStatusUpdate(int index, float positionX, float positionY);
    }
}
