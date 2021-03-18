package cn.dahuoji.body_temperature;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectValueView extends LinearLayout {

    private ValueAdapter.OnValueItemEventListener onValueItemEventListener;
    private ValueAdapter adapter;

    public void setOnValueItemEventListener(ValueAdapter.OnValueItemEventListener onValueItemEventListener) {
        this.onValueItemEventListener = onValueItemEventListener;
        if (adapter != null) adapter.setOnValueItemEventListener(onValueItemEventListener);
    }

    public SelectValueView(Context context) {
        super(context);
        init(context);
    }

    public SelectValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_select_value, this, true);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //super.getItemOffsets(outRect, view, parent, state);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if ((childAdapterPosition + 1) % 10 <= 5 && (childAdapterPosition + 1) % 10 != 0) {
                    outRect.top = 25;
                } else {
                    outRect.top = 0;
                }
            }
        });
        adapter = new ValueAdapter(context);
        if (onValueItemEventListener != null) {
            adapter.setOnValueItemEventListener(onValueItemEventListener);
        }
        recyclerView.setAdapter(adapter);
    }
}
