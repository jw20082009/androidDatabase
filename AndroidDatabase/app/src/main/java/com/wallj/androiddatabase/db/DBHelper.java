
package com.wallj.androiddatabase.db;

import com.wallj.androiddatabase.app.CustomApplication;
import com.wallj.androiddatabase.db.table.UserTable;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 12:04
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "piece_db";

    private DBHelper() {
        super(CustomApplication.getInstance(), DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance() {
        return Builder.INSTANCE;
    }

    static class Builder {
        static DBHelper INSTANCE = new DBHelper();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.getInstance().getTableCreateSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected void upgradeTable(SQLiteDatabase db, String tableName, String columns) {
        try {
            db.beginTransaction();

            // 1, Rename table.
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            db.execSQL(sql);
            // 2, Create table.
            onCreate(db);
            // 3, Load data
            sql = "INSERT INTO " + tableName + " (" + columns + ") " + " SELECT " + columns
                    + " FROM " + tempTableName;

            db.execSQL(sql);

            // 4, Drop the temporary table.
            db.execSQL("DROP TABLE IF EXISTS " + tempTableName);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void close() {
        getWritableDatabase().close();
    }
}
