
package com.wallj.androiddatabase.entities;

import com.wallj.androiddatabase.db.BaseEntity;

/**
 * 描述：
 *
 * @author walljiang
 * @since 2017/06/26 13:09
 */

public class UserEntity extends BaseEntity {
    String username;

    int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
