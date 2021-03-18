package cn.dahuoji.body_temperature;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import cn.dahuoji.body_temperature.skinview.TTFTextView;
import cn.dahuoji.body_temperature.util.SPUtil;
import cn.dahuoji.body_temperature.util.WindowUtil;

public class CommonDialog extends Dialog implements View.OnClickListener {

    private TTFTextView titleText;
    private TTFTextView contentText;

    public CommonDialog(@NonNull Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowUtil.getScreenValues()[0];
            dialogWindow.setAttributes(lp);
        }
        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnSure).setOnClickListener(this);
    }

    public void show(String title, String content) {
        super.show();
        this.titleText.setText(title);
        this.contentText.setText(content);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCancel) {
            System.exit(0);
        } else if (id == R.id.btnSure) {
            SPUtil.putBoolean("privacy_agreed", true);
            dismiss();
        }
    }
}
