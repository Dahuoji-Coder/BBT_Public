package cn.dahuoji.body_temperature;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import cn.dahuoji.body_temperature.database.ColumnEntity;
import cn.dahuoji.body_temperature.database.DBConstant;
import cn.dahuoji.body_temperature.database.DBUtil;
import cn.dahuoji.body_temperature.skinview.SkinRelativeLayout;
import cn.dahuoji.body_temperature.skinview.TTFTextView;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class MonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<DayEntity> days = new ArrayList<>();
    private final LunarCalendar lunarCalendar = new LunarCalendar();

    public MonthAdapter(Context context, int year, int month) {
        this.context = context;
        int monthLastDay = DateUtil.getMonthLastDay(year, month);
        int firstWeekOfMonth = DateUtil.getFirstWeekOfMonth(year, month);
        if (firstWeekOfMonth > 0) {
            //需要补足上个月的几天
            int yearPre;
            int monthPre;
            if (month == 1) {
                yearPre = year - 1;
                monthPre = 12;
            } else {
                yearPre = year;
                monthPre = month - 1;
            }
            int preMonthLastDay = DateUtil.getMonthLastDay(yearPre, monthPre);
            for (int i = 0; i < firstWeekOfMonth; i++) {
                DayEntity dayEntity = new DayEntity(yearPre, monthPre, preMonthLastDay - firstWeekOfMonth + i + 1);
                dayEntity.setPosition(DayEntity.POSITION_PREVIOUS);
                days.add(dayEntity);
            }
        }
        for (int i = 1; i <= monthLastDay; i++) {
            DayEntity dayEntity = new DayEntity(year, month, i);
            dayEntity.setPosition(DayEntity.POSITION_CURRENT);
            days.add(dayEntity);
        }
        int lastDays = days.size() % 7;
        if (lastDays > 0) {
            //需要补足下个月的几天
            int yearNext;
            int monthNext;
            if (month == 12) {
                monthNext = 1;
                yearNext = year + 1;
            } else {
                monthNext = month + 1;
                yearNext = year;
            }
            for (int i = 1; i <= 7 - lastDays; i++) {
                DayEntity dayEntity = new DayEntity(yearNext, monthNext, i);
                dayEntity.setPosition(DayEntity.POSITION_NEXT);
                days.add(dayEntity);
            }
        }
        notifyTemperatureDataChanged();
    }

    public void notifyTemperatureDataChanged() {
        //数据库取出数据
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
            String sexy = cursor.getString(cursor.getColumnIndex(DBConstant.SEXY));
            String blood = cursor.getString(cursor.getColumnIndex(DBConstant.BLOOD));
            String doctor = cursor.getString(cursor.getColumnIndex(DBConstant.DOCTOR));
            for (int i = 0; i < days.size(); i++) {
                DayEntity dayEntity = days.get(i);
                if (date.equals(dayEntity.getDateString())) {
                    dayEntity.setTemperature(value);
                    dayEntity.setSexy(sexy);
                    dayEntity.setBlood(blood);
                    dayEntity.setDoctor(doctor);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dayView = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(dayView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder = (DayViewHolder) holder;
        ((DayViewHolder) holder).rootView.setHeight(WindowUtil.dip2px(context, 60));
        DayEntity dayEntity = days.get(position);
        ((DayViewHolder) holder).rootView.setBackgroundResource(0);
        ((DayViewHolder) holder).numberText.setText(String.valueOf(dayEntity.getDay()));
        ((DayViewHolder) holder).numberText.setTextColorById(R.color.pool_text_color_hard);
        if (TextUtils.isEmpty(dayEntity.getTemperature())) {
            ((DayViewHolder) holder).lunarText.setText(lunarCalendar.getLunarString(dayEntity.getYear(), dayEntity.getMonth(), dayEntity.getDay()).split(" ")[1]);
            ((DayViewHolder) holder).lunarText.setTextColorById(R.color.pool_text_color_light);
        } else {
            ((DayViewHolder) holder).lunarText.setText(dayEntity.getTemperature());
            ((DayViewHolder) holder).lunarText.setTextColorById(R.color.common_red);
        }
        if (dayEntity.getPosition() == DayEntity.POSITION_CURRENT) {
            ((DayViewHolder) holder).rootView.setAlpha(1.0f);
        } else {
            ((DayViewHolder) holder).rootView.setAlpha(0.4f);
        }
        ((DayViewHolder) holder).symbolText.setTextColorById(R.color.pool_text_color_light);
        if (dayEntity.getDateString().equals(DateUtil.selectDateStart)) {
            if (TextUtils.isEmpty(DateUtil.selectDateEnd)) {
                ((DayViewHolder) holder).rootView.setBackgroundResource(R.drawable.current_date_back);
            } else {
                ((DayViewHolder) holder).rootView.setBackgroundResource(R.drawable.select_date_back_start);
            }
            ((DayViewHolder) holder).numberText.setTextColorById(R.color.text_color_white);
            ((DayViewHolder) holder).lunarText.setTextColorById(R.color.text_color_white);
            ((DayViewHolder) holder).symbolText.setTextColorById(R.color.text_color_white);
        } else {
            if (!TextUtils.isEmpty(DateUtil.selectDateEnd)) {
                if (dayEntity.getDateString().equals(DateUtil.selectDateEnd)) {
                    ((DayViewHolder) holder).rootView.setBackgroundResource(R.drawable.select_date_back_end);
                    ((DayViewHolder) holder).numberText.setTextColorById(R.color.text_color_white);
                    ((DayViewHolder) holder).lunarText.setTextColorById(R.color.text_color_white);
                    ((DayViewHolder) holder).symbolText.setTextColorById(R.color.text_color_white);
                } else if (dayEntity.getDateString().compareTo(DateUtil.selectDateStart) > 0 && dayEntity.getDateString().compareTo(DateUtil.selectDateEnd) < 0) {
                    ((DayViewHolder) holder).rootView.setBackgroundResource(R.drawable.select_date_back_in);
                }
            }
        }
        ((DayViewHolder) holder).symbolText.setText(
                (TextUtils.isEmpty(dayEntity.getSexy()) ? "" : dayEntity.getSexy()) +
                        (TextUtils.isEmpty(dayEntity.getBlood()) ? "" : dayEntity.getBlood()) +
                        (TextUtils.isEmpty(dayEntity.getDoctor()) ? "" : dayEntity.getDoctor())
        );
        ((DayViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int compare = dayEntity.getDateString().compareTo(DateUtil.selectDateStart);
                if (compare > 0) {
                    if (TextUtils.isEmpty(DateUtil.selectDateStart)) {
                        DateUtil.selectDateStart = dayEntity.getDateString();
                    } else {
                        DateUtil.selectDateEnd = dayEntity.getDateString();
                    }
                } else if (compare < 0) {
                    DateUtil.selectDateEnd = DateUtil.selectDateStart;
                    DateUtil.selectDateStart = dayEntity.getDateString();
                } else {
                    //如果是点击的是开始日期，判断是要清除区间还是清除开始日期
                    if (!TextUtils.isEmpty(DateUtil.selectDateEnd)) {
                        DateUtil.selectDateStart = dayEntity.getDateString();
                        DateUtil.selectDateEnd = "";
                    } else {
                        DateUtil.selectDateStart = "";
                    }
                }
                if (onCalendarEventListener != null) {
                    onCalendarEventListener.onItemClicked(dayEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        private final SkinRelativeLayout rootView;
        private final TTFTextView symbolText;
        private final TTFTextView numberText;
        private final TTFTextView lunarText;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            symbolText = itemView.findViewById(R.id.symbolText);
            numberText = itemView.findViewById(R.id.numberText);
            lunarText = itemView.findViewById(R.id.lunarText);
        }
    }

    private OnCalendarEventListener onCalendarEventListener;

    public void setOnCalendarEventListener(OnCalendarEventListener onCalendarEventListener) {
        this.onCalendarEventListener = onCalendarEventListener;
    }

    public interface OnCalendarEventListener {
        void onItemClicked(DayEntity dayEntity);
    }

}
