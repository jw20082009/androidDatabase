
package com.wallj.androiddatabase;

import com.wallj.androiddatabase.app.BaseWorkerActivity;
import com.wallj.androiddatabase.db.table.UserTable;
import com.wallj.androiddatabase.entities.UserEntity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends BaseWorkerActivity implements View.OnClickListener {

    private final String TAG = "DBActivity";

    ListView listview;

    ArrayList<UserEntity> users = new ArrayList<>();

    UserAdapter userAdapter;

    EditText etUserName, etAge;

    TextView tvAddUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        listview = (ListView) findViewById(R.id.listview);
        userAdapter = new UserAdapter();
        listview.setAdapter(userAdapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                    long id) {
                UserTable.getInstance().delete(users.get(position).get_id() + "");
                sendEmptyUiMessage(MSG_UI_REFRESH_LIST);
                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserEntity entity = users.get(position);
                etUserName.setText(entity.getUsername());
                etAge.setText(entity.getAge() + "");
                tvAddUser.setText("修改");
                tvAddUser.setTag(entity);
            }
        });
        etUserName = (EditText) findViewById(R.id.et_username);
        etAge = (EditText) findViewById(R.id.et_age);
        tvAddUser = (TextView) findViewById(R.id.tv_addUser);
        tvAddUser.setOnClickListener(this);
        refreshList();
    }

    private final int MSG_UI_REFRESH_LIST = 0x01;

    @Override
    protected void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MSG_UI_REFRESH_LIST:
                if (users.size() > 0) {
                    users.clear();
                }
                users.addAll(UserTable.getInstance().query());
                userAdapter.notifyDataSetChanged();
                break;
        }
    }

    private final int MSG_BACK_INSERT_USER = 0x02;

    private final int MSG_BACK_UPDATE_USER = 0x03;

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        switch (msg.what) {
            case MSG_BACK_UPDATE_USER: {
                Object obj = msg.obj;
                if (obj != null) {
                    UserEntity user = (UserEntity) obj;
                    long result = UserTable.getInstance().update(user.get_id() + "", user);
                    if (result >= 0) {
                        sendEmptyUiMessage(MSG_UI_REFRESH_LIST);
                    } else {
                        showToast("更新失败：result = " + result);
                    }
                }
            }
                break;
            case MSG_BACK_INSERT_USER: {
                Object obj = msg.obj;
                if (obj != null) {
                    UserEntity user = (UserEntity) obj;
                    long result = UserTable.getInstance().insert(user);
                    if (result >= 0) {
                        sendEmptyUiMessage(MSG_UI_REFRESH_LIST);
                    } else {
                        showToast("插入失败：result = " + result);
                    }
                }
            }
                break;
        }
    }

    private void refreshList() {
        sendEmptyUiMessage(MSG_UI_REFRESH_LIST);
    }

    private boolean checkValid() {
        if (etUserName.getText() == null || etUserName.getText().toString().trim().length() <= 0) {
            etUserName.requestFocus();
            showToast("请输入用户名");
            return false;
        }

        if (etAge.getText() == null || etAge.getText().toString().trim().length() <= 0) {
            etAge.requestFocus();
            showToast("请输入年龄");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addUser:
                Object tag = v.getTag();
                if (checkValid()) {
                    if (tag == null) {
                        String username = etUserName.getText().toString();
                        String age = etAge.getText().toString();
                        UserEntity userEntity = new UserEntity();
                        if (username != null && age != null) {
                            userEntity.setUsername(username);
                            userEntity.setAge(Integer.parseInt(age));
                        }
                        Message backMsg = obtainBackgroundMessage(MSG_BACK_INSERT_USER);
                        backMsg.obj = userEntity;
                        backMsg.sendToTarget();
                    } else {
                        UserEntity userEntity = (UserEntity) tag;
                        String username = etUserName.getText().toString();
                        String age = etAge.getText().toString();
                        if (username != null && username.trim().length() > 0) {
                            userEntity.setUsername(username);
                        }
                        if (age != null && age.trim().length() > 0) {
                            userEntity.setAge(Integer.parseInt(age));
                        }
                        Message backMsg = obtainBackgroundMessage(MSG_BACK_UPDATE_USER);
                        backMsg.obj = userEntity;
                        backMsg.sendToTarget();
                    }
                    etUserName.setText("");
                    etAge.setText("");
                    tvAddUser.setText("增加");
                }
                break;
        }
    }

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public UserEntity getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder cache;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_user,
                        null);
                cache = new ViewHolder();
                cache.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
                cache.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
                cache.tvId = (TextView) convertView.findViewById(R.id.tv_id);
                convertView.setTag(R.id.tag_item_cache, cache);
            } else {
                cache = (ViewHolder) convertView.getTag(R.id.tag_item_cache);
            }
            UserEntity user = getItem(position);
            cache.tvUserName.setText(user.getUsername() == null ? "" : user.getUsername());
            cache.tvAge.setText(user.getAge() + "");
            cache.tvId.setText(user.get_id() + "");
            return convertView;
        }
    }

    class ViewHolder {
        TextView tvUserName;

        TextView tvAge;

        TextView tvId;
    }
}
