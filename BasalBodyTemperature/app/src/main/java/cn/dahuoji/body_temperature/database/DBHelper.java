package cn.dahuoji.body_temperature.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 10732 on 2018/5/22.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "body_temperature_db";
    private static final int DATABASE_VERSION = 1;

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(">>>DBHelper", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d(">>>DBHelper", "onUpgrade oldVersion = " + oldVersion + "; newVersion = " + newVersion);
        dropDB(db, oldVersion);
    }

    private void dropDB(SQLiteDatabase db, int oldVersion) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                /*
                 * 遍历全部表单并删除
                 * */
                db.execSQL("DROP TABLE `" + tableName + "`");
            }
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }

            try {
                String emptySequence = "delete from sqlite_sequence";
                db.execSQL(emptySequence);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}