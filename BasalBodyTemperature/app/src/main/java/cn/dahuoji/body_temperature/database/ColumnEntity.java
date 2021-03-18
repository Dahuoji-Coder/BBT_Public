package cn.dahuoji.body_temperature.database;

public class ColumnEntity {
    /*
    1.NULL：空值。
    2.INTEGER：带符号的整型，具体取决有存入数字的范围大小。
    3.REAL：浮点数字，存储为8-byte IEEE浮点数。
    4.TEXT：字符串文本。
    5.BLOB：二进制对象。
    */
    public static final String TYPE_NULL = "NULL";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_REAL = "REAL";
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_BLOB = "BLOB";

    private String dbName;
    private String dbType;
    private String fieldName;

    public ColumnEntity(String dbName, String dbType, String fieldName) {
        this.dbName = dbName;
        this.dbType = dbType;
        this.fieldName = fieldName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
