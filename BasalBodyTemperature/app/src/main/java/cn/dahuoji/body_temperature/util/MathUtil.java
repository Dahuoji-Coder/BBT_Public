package cn.dahuoji.body_temperature.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by 10732 on 2018/6/11.
 */

public class MathUtil {

    public static String getFormatNumberForElectricity(double value, int decimal) {
        Locale enlocale = new Locale("en", "US");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(enlocale);
        switch (decimal) {
            case 0:
                decimalFormat.applyPattern("###########0");
                break;
            case 1:
                decimalFormat.applyPattern("###########0.0");
                break;
            case 2:
                decimalFormat.applyPattern("###########0.00");
                break;
            case 3:
                decimalFormat.applyPattern("###########0.000");
                break;
            case 4:
                decimalFormat.applyPattern("###########0.0000");
                break;
            case 5:
                decimalFormat.applyPattern("###########0.00000");
                break;
            case 6:
                decimalFormat.applyPattern("###########0.000000");
                break;
            case 7:
                decimalFormat.applyPattern("###########0.0000000");
                break;
            case 8:
                decimalFormat.applyPattern("###########0.00000000");
                break;
            case 10:
                decimalFormat.applyPattern("###########0.0000000000");
                break;
        }
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        return decimalFormat.format(value);
    }

    public static String getFormatNumber(double value, int decimal) {
        Locale enlocale = new Locale("en", "US");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(enlocale);
        switch (decimal) {
            case 0:
                decimalFormat.applyPattern("###########0");
                break;
            case 1:
                decimalFormat.applyPattern("###########0.0");
                break;
            case 2:
                decimalFormat.applyPattern("###########0.00");
                break;
            case 3:
                decimalFormat.applyPattern("###########0.000");
                break;
            case 4:
                decimalFormat.applyPattern("###########0.0000");
                break;
            case 5:
                decimalFormat.applyPattern("###########0.00000");
                break;
            case 6:
                decimalFormat.applyPattern("###########0.000000");
                break;
            case 7:
                decimalFormat.applyPattern("###########0.0000000");
                break;
            case 8:
                decimalFormat.applyPattern("###########0.00000000");
                break;
            case 10:
                decimalFormat.applyPattern("###########0.0000000000");
                break;
        }
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        return decimalFormat.format(value);
    }

    public static String getFormatNumberWithThousandPlace(double value, int decimal) {
        try {
            Locale enlocale = new Locale("en", "US");
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(enlocale);
            switch (decimal) {
                case 0:
                    decimalFormat.applyPattern("###,###,###,###,##0");
                    break;
                case 1:
                    decimalFormat.applyPattern("###,###,###,###,##0.0");
                    break;
                case 2:
                    decimalFormat.applyPattern("###,###,###,###,##0.00");
                    break;
                case 3:
                    decimalFormat.applyPattern("###,###,###,###,##0.000");
                    break;
                case 4:
                    decimalFormat.applyPattern("###,###,###,###,##0.0000");
                    break;
            }
            decimalFormat.setRoundingMode(RoundingMode.DOWN);

            return decimalFormat.format(value);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }

    public static int getDecimalNum(double value) {
        int x = 8;
        value /= 10000;
        while (value > 1) {
            x--;
            value /= 10;
        }
        return Math.max(x, 0);
    }

    public static String calcPrice(double price) {
        String tempPrice = "0";
        double abs = Math.abs(price);
        if (abs < 0.0001) {
            tempPrice = "0";
        } else if (abs < 0.1) {
            tempPrice = getFormatNumber(price, 4);
        } else {
            tempPrice = getFormatNumber(price, 2);
        }
        return tempPrice;
    }

    public static String[] getFormatHashRate(double hash_rate, int decimal_type) {
        int index = 0;
        String[] orders = new String[]{"", "K", "M", "G", "T", "P", "E"};
        while (hash_rate > 1000 && index < 6) {
            hash_rate /= 1000;
            index++;
        }
        return new String[]{getFormatNumber(hash_rate, decimal_type), orders[index], String.valueOf(index)};
    }

    public static String convertDoubleToString(double value) {
        // stripTrailingZeros去除末尾的".0"
        // toPlainString表示不使用科学计数法（toString会使用科学计数法）
        // 经测试 1.0 ==> 1, 但是 0.0 ==> 0.0
        String temp;
        BigDecimal bd = new BigDecimal(String.valueOf(value));
        temp = bd.stripTrailingZeros().toPlainString();
        if ("0.0".equals(temp)) temp = "0";
        return temp;
    }
}