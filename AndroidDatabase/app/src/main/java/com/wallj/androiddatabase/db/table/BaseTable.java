
package com.wallj.androiddatabase.db.table;

import com.wallj.androiddatabase.db.DBHelper;
import com.wallj.androiddatabase.db.DBInterface;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 12:15
 */

public abstract class BaseTable<TableEntity> implements DBInterface<TableEntity> {

    public final String _ID = "_id";

    private StringBuilder createTable;

    private String sqlBegain = null;

    private ArrayList<String> columns;

    private String sqlEnd = ");";

    protected BaseTable() {
        this.sqlBegain = "CREATE TABLE IF NOT EXISTS " + getTableName() + " (" + _ID
                + " INTEGER PRIMARY KEY";
    }

    abstract String getTableName();

    private StringBuilder getSBCreateTable() {
        if (createTable == null) {
            createTable = new StringBuilder(sqlBegain);
        }
        return createTable;
    }

    private void addColumn(String columnName) {
        if (columnName != null) {
            if (columns == null) {
                columns = new ArrayList<>();
                columns.add(_ID);
            }
            columns.add(columnName);
        }
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    /**
     * 想数据表中添加integer类型字段
     * 
     * @param columnName
     */
    protected void addColumnInteger(String columnName) {
        addColumn(columnName);
        getSBCreateTable().append("," + columnName + " INTEGER");
    }

    /**
     * 向数据表中添加text类型字段
     * 
     * @param columnName
     */
    protected void addColumnText(String columnName) {
        addColumn(columnName);
        getSBCreateTable().append("," + columnName + " TEXT");
    }

    public String getTableCreateSQL() {
        StringBuilder sbCreateTable = getSBCreateTable();
        if (sbCreateTable.length() > 0) {
            sbCreateTable.append(");");
            return sbCreateTable.toString();
        } else {
            return null;
        }
    }

    @Override
    public long insert(TableEntity entity) {
        ContentValues contentValues = getCVFromEntity(entity);
        return DBHelper.getInstance().getWritableDatabase().insert(getTableName(), null,
                contentValues);
    }

    @Override
    public boolean insert(ArrayList<TableEntity> entities) {
        if (entities == null || entities.size() == 0) {
            return false;
        }
        boolean result = false;
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        try {
            db.beginTransaction();
            for (TableEntity entity : entities) {
                insert(entity);
            }
            db.setTransactionSuccessful();
            result = true;
        } finally {
            db.endTransaction();
        }
        return result;
    }

    /**
     * 通过默认_id字段来删除数据，如要删除其他字段，子类可覆盖此方法
     * 
     * @param id 实现类可自行决定使用具体字段
     * @return
     */
    @Override
    public int delete(String id) {
        if (id != null) {
            DBHelper.getInstance().getWritableDatabase().delete(getTableName(), _ID + "= ? ",
                    new String[] {
                            id
                    });
        }
        return 0;
    }

    /**
     * 通过默认_id字段来更新数据，子类可覆盖此方法
     * 
     * @param id
     * @param userEntity
     * @return
     */
    @Override
    public long update(String id, TableEntity userEntity) {
        if (id != null && userEntity != null) {
            return DBHelper.getInstance().getWritableDatabase().update(getTableName(),
                    getCVFromEntity(userEntity), _ID + "= ? ", new String[] {
                            id
                    });
        }
        return 0;
    }

    /**
     * 通过默认_id字段来查询数据，子类可覆盖此方法
     * 
     * @param id 实现类可自行决定使用具体字段
     * @return
     */
    @Override
    public List<TableEntity> query(String id) {
        if (id != null) {
            Cursor cursor = DBHelper.getInstance().getReadableDatabase().query(getTableName(), null,
                    _ID + "= ? ", new String[] {
                            id
                    }, null, null, null);
            return getEntityFromCursor(cursor);
        }
        return null;
    }

    @Override
    public List<TableEntity> query() {
        Cursor cursor = DBHelper.getInstance().getReadableDatabase().query(getTableName(), null,
                null, null, null, null, _ID + " DESC ", 10 + "");
        return getEntityFromCursor(cursor);
    }

    /**
     * 从实体类到ContentValue的转换
     *
     * @param entity
     * @return
     */
    abstract ContentValues getCVFromEntity(TableEntity entity);

    /**
     * 从数据游标中获取实体类
     *
     * @param cursor
     * @return
     */
    abstract List<TableEntity> getEntityFromCursor(Cursor cursor);
}
