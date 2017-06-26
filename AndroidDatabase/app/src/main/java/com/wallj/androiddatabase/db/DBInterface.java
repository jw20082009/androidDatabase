
package com.wallj.androiddatabase.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：数据库外部调用接口
 *
 * @author walljiang
 * @since 2017/06/26 11:07
 */

public interface DBInterface<TableEntity> {
    long insert(TableEntity entity);

    boolean insert(ArrayList<TableEntity> entities);

    long update(String id, TableEntity entity);

    int delete(String id);

    List<TableEntity> query(String id);

    List<TableEntity> query();
}
