package com.bs.teachassistant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.entity.User;
import com.bs.teachassistant.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by limh on 2017/4/25.
 * 登陆
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.edit_user_name)
    EditText editName;
    @ViewInject(R.id.edit_user_password)
    EditText editPass;

    private User user = null;
    private ProgressDialog dialog;

    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(userPreference.getString("useInfo", ""))) {
            user = GsonUtils.GsonToBean(userPreference.getString("useInfo", ""), User.class);
            editName.setText(user.getUserName());
            editPass.setText(user.getPassword());
            Log.w("Login", user.toString());
        }
    }

    @Event(value = {R.id.btn_login, R.id.txt_register}, type = View.OnClickListener.class)
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginUser();
                break;
            case R.id.txt_register:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void loginUser() {
        if (null == user) {
            Toast.makeText(this, "请先注册", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editName.getText().toString())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editPass.getText().toString())) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((editName.getText().toString().equals(user.getUserName()) || editName.getText().toString().equals(user.getPhone())) &&
                editPass.getText().toString().equals(user.getPassword())) {
            if (null == dialog)
                dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("正在登陆，请稍后...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0x123);
                }
            }).start();*/
           // login()
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (dialog.isShowing())
                dialog.dismiss();
            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }
    });

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }
    public void login(String name,String password) {
        OkHttpClient client = new OkHttpClient();
        //构造Request对象
        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
        Request request = new Request.Builder().get().url("http://140.143.19.16:8080/teacher/teacher/login.action?name=" +
                name + "&password=" + password)
                .build();
        Call call = client.newCall(request);
        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               //ToastUtil.showToast(this, "Get 失败");
                Toast.makeText(getApplicationContext(), "Get 失败",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tv_result.setText(responseStr);
                    }
                });
            }
        });
    }

}
