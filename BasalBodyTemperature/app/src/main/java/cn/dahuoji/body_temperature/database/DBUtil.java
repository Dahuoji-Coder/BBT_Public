package cn.dahuoji.body_temperature.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.dahuoji.body_temperature.DayEntity;
import cn.dahuoji.body_temperature.LocalApplication;

/**
 * Created by 10732 on 2018/5/22.
 */

public class DBUtil {

    private final DBHelper dbHelper;
    private final SQLiteDatabase db;
    private static volatile DBUtil instance = null;

    public static synchronized DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil(LocalApplication.getContext());
        }
        return instance;
    }

    private DBUtil(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void createTable(String tableName, ColumnEntity[] columns) throws SQLException {
        StringBuilder sqlColumns = new StringBuilder();
        for (ColumnEntity column : columns) {
            sqlColumns.append(column.getDbName()).append(" ").append(column.getDbType()).append(",");
        }
        String sql = "create table if not exists " + formatTableName(tableName) + " (" +
                sqlColumns.substring(0, sqlColumns.length() - 1) +
                ");";
        db.execSQL(sql);
    }

    public void beginTransaction() {
        db.beginTransaction();
    }

    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    public void endTransaction() {
        if (db.inTransaction()) {
            db.endTransaction();
        }
    }

    public void deleteData(String tableName) throws SQLException {
        /*
         * 如果表存在则删除数据
         * 如果不存在则创建一个新表
         * */
        String tableNameFormat = formatTableName(tableName);
        if (isExists(tableName)) {
            String sql = "delete from " + tableNameFormat + ";";
            String sql2 = "delete from sqlite_sequence where name='" + tableName + "';";
            db.execSQL(sql);
            db.execSQL(sql2);
        }
    }

    public void insertData(String table, String nullColumnHack, ContentValues values) {
        long insert = db.insert(formatTableName(table), nullColumnHack, values);
        //Log.d(">>>DBUtil", "rowID ===> " + insert);
    }

    public Cursor queryData(String table, String[] columns, String selection,
                            String[] selectionArgs, String groupBy, String having,
                            String orderBy, String limit) {
        return db.query(formatTableName(table), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public void updateData(String table, ContentValues values, String whereClause, String[] whereArgs) {
        db.update(formatTableName(table), values, whereClause, whereArgs);
    }

    public void deleteData(String table, String whereClause, String[] whereArgs) {
        db.delete(formatTableName(table), whereClause, whereArgs);
    }

    private String formatTableName(String tableName) {
        if (tableName.startsWith("`")) {
            return tableName;
        } else {
            return "`" + tableName + "`";
        }
    }

    public boolean isExists(String tableName) {
        //String tableNameFormat = formatTableName(tableName);
        boolean isExists = false;

        String sql = "select name from sqlite_master where type='table';";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            if (tableName != null && name != null && tableName.equals(name)) {
                isExists = true;
            }
        }
        cursor.close();

        return isExists;
    }

    public void close() {
        dbHelper.close();
    }

    public static void saveTemperature(String date, String value, String sexy, String blood, String doctor) {
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (dbUtil.isExists(tableName)) {
            Cursor cursor = dbUtil.queryData(tableName, null, DBConstant.DATE + "=?", new String[]{date}, null, null, null, null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstant.DATE, date);
            contentValues.put(DBConstant.VALUE, value);
            contentValues.put(DBConstant.SEXY, sexy);
            contentValues.put(DBConstant.BLOOD, blood);
            contentValues.put(DBConstant.DOCTOR, doctor);
            if (cursor.moveToNext()) {
                dbUtil.updateData(tableName, contentValues, DBConstant.DATE + "=?", new String[]{date});
            } else {
                dbUtil.insertData(tableName, null, contentValues);
            }
        }
    }

    public static DayEntity queryData(String date) {
        String[] split = date.split("-");
        DayEntity dayEntity = new DayEntity(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]));
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (dbUtil.isExists(tableName)) {
            Cursor cursor = dbUtil.queryData(tableName, null, DBConstant.DATE + "=?", new String[]{date}, null, null, null, null);
            if (cursor.moveToNext()) {
                String value = cursor.getString(cursor.getColumnIndex(DBConstant.VALUE));
                String sexy = cursor.getString(cursor.getColumnIndex(DBConstant.SEXY));
                String blood = cursor.getString(cursor.getColumnIndex(DBConstant.BLOOD));
                String doctor = cursor.getString(cursor.getColumnIndex(DBConstant.DOCTOR));
                dayEntity.setTemperature(value);
                dayEntity.setSexy(sexy);
                dayEntity.setBlood(blood);
                dayEntity.setDoctor(doctor);
            }
        }
        return dayEntity;
    }

    public static void deleteRaw(String date) {
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (dbUtil.isExists(tableName)) {
            dbUtil.deleteData(tableName, DBConstant.DATE + "=?", new String[]{date});
        }
    }

    public List<DayEntity> queryMonthData(int year, int month) {
        List<DayEntity> list = new ArrayList<>();
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (dbUtil.isExists(tableName)) {
            String keyword = String.valueOf(month);
            if (keyword.length() == 1) {
                keyword = "0" + keyword;
            }
            keyword = year + "-" + keyword + "-";
            String[] selectioinArgs = {"%" + keyword + "%"};
            String sql = "select * from " + tableName + " where " + DBConstant.DATE + " like ? ";
            Cursor cursor = db.rawQuery(sql, selectioinArgs);
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(DBConstant.DATE));
                String value = cursor.getString(cursor.getColumnIndex(DBConstant.VALUE));
                String sexy = cursor.getString(cursor.getColumnIndex(DBConstant.SEXY));
                String blood = cursor.getString(cursor.getColumnIndex(DBConstant.BLOOD));
                String doctor = cursor.getString(cursor.getColumnIndex(DBConstant.DOCTOR));
                String[] split = date.split("-");
                DayEntity dayEntity = new DayEntity(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2]));
                dayEntity.setTemperature(value);
                dayEntity.setSexy(sexy);
                dayEntity.setBlood(blood);
                dayEntity.setDoctor(doctor);
                list.add(dayEntity);
            }
        }
        return list;
    }

    public List<DayEntity> queryPeriodData(String startDate, String endDate) {
        List<DayEntity> list = new ArrayList<>();
        DBUtil dbUtil = DBUtil.getInstance();
        String tableName = DBConstant.BODY_TEMPERATURE_ + "me";
        if (dbUtil.isExists(tableName)) {
            String[] args = new String[]{startDate, endDate};
            String sql = "select * from " + tableName + " where " + DBConstant.DATE + " >= ? and " + DBConstant.DATE + " <= ?";
            Cursor cursor = db.rawQuery(sql, args);
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(DBConstant.DATE));
                String value = cursor.getString(cursor.getColumnIndex(DBConstant.VALUE));
                String sexy = cursor.getString(cursor.getColumnIndex(DBConstant.SEXY));
                String blood = cursor.getString(cursor.getColumnIndex(DBConstant.BLOOD));
                String doctor = cursor.getString(cursor.getColumnIndex(DBConstant.DOCTOR));
                String[] split = date.split("-");
                DayEntity dayEntity = new DayEntity(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2]));
                dayEntity.setTemperature(value);
                dayEntity.setSexy(sexy);
                dayEntity.setBlood(blood);
                dayEntity.setDoctor(doctor);
                list.add(dayEntity);
            }
        }
        return list;
    }
}