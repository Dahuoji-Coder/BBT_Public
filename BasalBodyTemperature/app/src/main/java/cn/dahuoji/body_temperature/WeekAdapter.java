package cn.dahuoji.body_temperature;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.dahuoji.body_temperature.database.ColumnEntity;
import cn.dahuoji.body_temperature.database.DBConstant;
import cn.dahuoji.body_temperature.database.DBUtil;
import cn.dahuoji.body_temperature.skinview.SkinRelativeLayout;
import cn.dahuoji.body_temperature.skinview.TTFTextView;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class WeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<DayEntity> days = new ArrayList<>();
    private final LunarCalendar lunarCalendar = new LunarCalendar();

    public WeekAdapter(Context context, int yearStart, int monthStart, int dayStart) {
        this.context = context;
        int monthLastDay = DateUtil.getMonthLastDay(yearStart, monthStart);
        for (int i = dayStart; i <= monthLastDay && days.size() < 7; i++) {
            DayEntity dayEntity = new DayEntity(yearStart, monthStart, i);
            dayEntity.setPosition(DayEntity.POSITION_CURRENT);
            days.add(dayEntity);
        }
        if (days.size() < 7) {
            //需要补足下一个月的几天
            int lastDays = 7 - days.size();
            for (int i = 0; i < lastDays; i++) {
                int year, month, day;
                if (monthStart == 12) {
                    year = yearStart + 1;
                    month = 1;
                } else {
                    year = yearStart;
                    month = monthStart + 1;
                }
                day = i + 1;
                DayEntity dayEntity = new DayEntity(year, month, day);
                dayEntity.setPosition(DayEntity.POSITION_NEXT);
                days.add(dayEntity);
            }
        }
        notifyTemperatureDataChanged();
    }

    public void notifyTemperatureDataChanged() {
        //数据库取出数据
        for (int i = 0; i < days.size(); i++) {
            DayEntity dayEntity = days.get(i);
            dayEntity.setTemperature(null);
            dayEntity.setSexy(null);
            dayEntity.setBlood(null);
            dayEntity.setDoctor(null);
        }
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
        if (dayEntity.getDateString().equals(DateUtil.getCurrentDate())) {
            ((DayViewHolder) holder).rootView.setBackgroundResource(R.drawable.current_date_back);
            ((DayViewHolder) holder).numberText.setTextColorById(R.color.text_color_white);
            ((DayViewHolder) holder).lunarText.setTextColorById(R.color.text_color_white);
            ((DayViewHolder) holder).symbolText.setTextColorById(R.color.text_color_white);
        } else if (dayEntity.getDateString().equals(DateUtil.selectDate)) {
            ((DayViewHolder) holder).rootView.setBackgroundResource(R.drawable.select_date_back);
        }
        ((DayViewHolder) holder).symbolText.setText(
                (TextUtils.isEmpty(dayEntity.getSexy()) ? "" : dayEntity.getSexy()) +
                        (TextUtils.isEmpty(dayEntity.getBlood()) ? "" : dayEntity.getBlood()) +
                        (TextUtils.isEmpty(dayEntity.getDoctor()) ? "" : dayEntity.getDoctor())
        );
        ((DayViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateUtil.selectDate = dayEntity.getDateString();
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

    public interface OnCalendarEventListener extends MonthAdapter.OnCalendarEventListener {
        void onItemClicked(DayEntity dayEntity);
    }

}
