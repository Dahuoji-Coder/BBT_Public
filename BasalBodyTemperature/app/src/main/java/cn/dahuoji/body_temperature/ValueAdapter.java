package cn.dahuoji.body_temperature;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.dahuoji.body_temperature.skinview.TTFTextView;
import cn.dahuoji.body_temperature.util.MathUtil;

public class ValueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final int start = 35;
    private final int end = 38;

    public ValueAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_value_layout, parent, false);
        return new ValueHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String value = MathUtil.getFormatNumberForElectricity(position * 0.1 + start, 1);
        holder = (ValueHolder) holder;
        if (value.endsWith(".0")) {
            ((ValueHolder) holder).valueText.setText(value + " °c");
            ((ValueHolder) holder).valueText.setTextSize(17);
            ((ValueHolder) holder).valueText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            ((ValueHolder) holder).valueText.setText(value);
            ((ValueHolder) holder).valueText.setTextSize(15);
            ((ValueHolder) holder).valueText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        ((ValueHolder) holder).valueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onValueItemEventListener != null) {
                    onValueItemEventListener.onValueItemClicked(value + "0");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10 * (end - start);
    }

    public static class ValueHolder extends RecyclerView.ViewHolder {

        private final TTFTextView valueText;

        public ValueHolder(@NonNull View itemView) {
            super(itemView);
            valueText = itemView.findViewById(R.id.valueText);
        }
    }

    private OnValueItemEventListener onValueItemEventListener;

    public void setOnValueItemEventListener(OnValueItemEventListener onValueItemEventListener) {
        this.onValueItemEventListener = onValueItemEventListener;
    }

    public interface OnValueItemEventListener {
        void onValueItemClicked(String value);
    }
}
