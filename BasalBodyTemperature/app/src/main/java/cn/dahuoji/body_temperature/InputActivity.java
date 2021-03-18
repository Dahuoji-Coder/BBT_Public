package cn.dahuoji.body_temperature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.dahuoji.body_temperature.database.DBConstant;
import cn.dahuoji.body_temperature.database.DBUtil;

public class InputActivity extends AppCompatActivity {

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Bundle extras = getIntent().getExtras();
        date = extras.getString("date", "");
//        Log.d(">>>InputActivity", "===date: " + date);
        EditText editText = findViewById(R.id.editText);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    saveTemperature(editText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }

    private void saveTemperature(String value) {
        if (TextUtils.isEmpty(value)) return;
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (dbUtil.isExists(tableName)) {
            Cursor cursor = dbUtil.queryData(tableName, null, DBConstant.DATE + "=?", new String[]{date}, null, null, null, null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstant.DATE, date);
            contentValues.put(DBConstant.VALUE, value);
            if (cursor.moveToNext()) {
                dbUtil.updateData(tableName, contentValues, DBConstant.DATE + "=?", new String[]{date});
            } else {
                dbUtil.insertData(tableName, null, contentValues);
            }
        }
        setResult(199);
        finish();
    }
}