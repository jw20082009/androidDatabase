
package com.wallj.androiddatabase.db.table;

import com.wallj.androiddatabase.entities.UserEntity;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 12:14
 */

public class UserTable extends BaseTable<UserEntity> {

    static UserTable mInstance;

    final String TABLE_NAME = "user";

    final String AGE = "age";

    final String USERNAME = "username";

    public static UserTable getInstance() {
        if (mInstance == null) {
            mInstance = new UserTable();
        }
        return mInstance;
    }

    private UserTable() {
        addColumnInteger(AGE);
        addColumnText(USERNAME);
    }

    @Override
    String getTableName() {
        return TABLE_NAME;
    }

    @Override
    ContentValues getCVFromEntity(UserEntity userEntity) {
        if (userEntity != null) {
            ContentValues values = new ContentValues();
            values.put(AGE, userEntity.getAge());
            values.put(USERNAME, userEntity.getUsername());
            return values;
        }
        return null;
    }

    @Override
    List<UserEntity> getEntityFromCursor(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            return null;
        }
        ArrayList<UserEntity> userEntities = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                UserEntity entity = new UserEntity();
                entity.setAge(cursor.getInt(cursor.getColumnIndex(AGE)));
                entity.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                entity.set_id(cursor.getInt(cursor.getColumnIndex(_ID)));
                userEntities.add(entity);
            }
        } finally {
            cursor.close();
        }
        return userEntities;
    }
}
