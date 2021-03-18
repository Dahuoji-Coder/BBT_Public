package cn.dahuoji.body_temperature.linechart;

public class ChartConfig {
    private int offTop;
    private int offBottom;
    private int offLeft;
    private int offRight;
    private int offXAxis;
    private int offYAxis;
    private int labelTextSize;
    private int sidesBlankWidth;
    private int lineColorId;
    private int fadePathStartColorId;
    private int fadePathEndColorId;
    private boolean needNegativeNumber = false;

    public ChartConfig(int offTop, int offBottom, int offLeft, int offRight, int offXAxis, int offYAxis, int labelTextSize, int sidesBlankWidth, int lineColorId, int fadePathStartColorId, int fadePathStartEndId, boolean needNegativeNumber) {
        this.offTop = offTop;
        this.offBottom = offBottom;
        this.offLeft = offLeft;
        this.offRight = offRight;
        this.offXAxis = offXAxis;
        this.offYAxis = offYAxis;
        this.labelTextSize = labelTextSize;
        this.sidesBlankWidth = sidesBlankWidth;
        this.lineColorId = lineColorId;
        this.fadePathStartColorId = fadePathStartColorId;
        this.fadePathEndColorId = fadePathStartEndId;
        this.needNegativeNumber = needNegativeNumber;
    }

    public boolean isNeedNegativeNumber() {
        return needNegativeNumber;
    }

    public void setNeedNegativeNumber(boolean needNegativeNumber) {
        this.needNegativeNumber = needNegativeNumber;
    }

    public int getLineColorId() {
        return lineColorId;
    }

    public void setLineColorId(int lineColorId) {
        this.lineColorId = lineColorId;
    }

    public int getFadePathStartColorId() {
        return fadePathStartColorId;
    }

    public void setFadePathStartColorId(int fadePathStartColorId) {
        this.fadePathStartColorId = fadePathStartColorId;
    }

    public int getFadePathEndColorId() {
        return fadePathEndColorId;
    }

    public void setFadePathEndColorId(int fadePathEndColorId) {
        this.fadePathEndColorId = fadePathEndColorId;
    }

    public int getOffTop() {
        return offTop;
    }

    public void setOffTop(int offTop) {
        this.offTop = offTop;
    }

    public int getOffBottom() {
        return offBottom;
    }

    public void setOffBottom(int offBottom) {
        this.offBottom = offBottom;
    }

    public int getOffLeft() {
        return offLeft;
    }

    public void setOffLeft(int offLeft) {
        this.offLeft = offLeft;
    }

    public int getOffRight() {
        return offRight;
    }

    public void setOffRight(int offRight) {
        this.offRight = offRight;
    }

    public int getOffYAxis() {
        return offYAxis;
    }

    public void setOffYAxis(int offYAxis) {
        this.offYAxis = offYAxis;
    }

    public int getOffXAxis() {
        return offXAxis;
    }

    public void setOffXAxis(int offXAxis) {
        this.offXAxis = offXAxis;
    }

    public int getLabelTextSize() {
        return labelTextSize;
    }

    public void setLabelTextSize(int labelTextSize) {
        this.labelTextSize = labelTextSize;
    }

    public void setSidesBlankWidth(int sidesBlankWidth) {
        this.sidesBlankWidth = sidesBlankWidth;
    }

    public int getSidesBlankWidth() {
        return sidesBlankWidth;
    }

    public static float calculateMaxValue(double maxValue, float space) {
        double step = maxValue / space;
        float trueStep;
        float multiple = 1.0f;
        if (step == 0) {
            step = 1f;
        }
        if (step < 1) {
            do {
                step = step * 10;
                multiple = multiple / 10;
            } while (step < 1);
        } else {
            if (step > 1000) {
                while (step > 100) {
                    step = step / 10;
                    multiple = multiple * 10;
                }
            } else {
                while (step > 10) {
                    step = step / 10;
                    multiple = multiple * 10;
                }
            }
        }
        int intStep = (int) step;
        if (step > intStep) {
            intStep++;
        }
        trueStep = intStep * multiple;
        if (step < 1.5 && step > 1.0) {
            trueStep = 1.5f * multiple;
        }

        return trueStep * 10;
    }

    public static float calculateMinValue(double minValue, float space) {
        return -calculateMaxValue(-minValue, space);
    }
}
