package com.bs.teachassistant.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bs.teachassistant.R;
import com.bs.teachassistant.entity.User;
import com.bs.teachassistant.utils.GsonUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
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
 * 用户注册模块
 */
@ContentView(R.layout.activity_regiter)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.edit_regiter_name)
    EditText editName;
    @ViewInject(R.id.edit_regiter_pass1)
    EditText editPass1;
    @ViewInject(R.id.edit_regiter_pass2)
    EditText editPass2;
    @ViewInject(R.id.edit_regiter_phone)
    EditText editPhone;
    @ViewInject(R.id.radio_sex)
    RadioGroup radioSex;

    private ProgressDialog dialog;

    private String sex = "boy";

    @Override
    public void initViews() {
        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_boy:
                        sex = "boy";
                        break;
                    case R.id.radio_girl:
                        sex = "girl";
                        break;
                }
            }
        });
    }

    @Override
    public void initDatas() {

    }

    @Event(value = {R.id.image_regiter_close, R.id.btn_regiter}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_regiter_close:
                finish();
                break;
            case R.id.btn_regiter:
                regiterInfo();
                break;
        }
    }

    private void regiterInfo() {
        if (TextUtils.isEmpty(editName.getText().toString())) {
            Toast.makeText(this, "用户昵称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editPhone.getText().toString())) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editPass1.getText().toString())) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!editPass1.getText().toString().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(editName.getText().toString(), editPass1.getText().toString(), editPhone.getText().toString(), sex);
       // registerHttp(user);
        writeInfo(User user)
       // writeInfo(user);
    }

    private void writeInfo(User user) {
        userPreference.edit().putString("useInfo", GsonUtils.GsonString(user)).apply();
        if (null == dialog)
            dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("正在注册，请稍后...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x123);
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (dialog.isShowing())
                dialog.dismiss();
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
    });
    private void registerHttp(final User user) {
        //final int flag = 0;
        OkHttpClient client = new OkHttpClient();
        //构造Request对象
        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
        Request request = new Request.Builder().get().url("http://140.143.19.16:8080/testd/teacher/register.action?name=" +
                user.getUserName() + "&password=" + user.getPassword() + "&sex=" + user.getSex() + "&dep=" + user.getPhone())
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
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();

                if (TextUtils.isEmpty(responseStr)){
                    //JSONObject jsonObject = new JSONObject(responseStr);
                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                }else {
                    writeInfo(user);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });


            }
        });
        //flag = 1;
       // return flag;
    }
}
