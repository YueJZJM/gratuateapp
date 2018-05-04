package com.bs.teachassistant.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bs.teachassistant.R;
import com.bs.teachassistant.entity.User;
import com.bs.teachassistant.utils.GsonUtils;

import org.xutils.view.annotation.ViewInject;

public class PersionMsgActivity extends AppCompatActivity {
    private User user;
    public SharedPreferences userPreference;

    @ViewInject(R.id.txt_per_yes)
    private TextView mEditYes;
    @ViewInject(R.id.txt_per_close)
    private TextView mEditClose;
    /*@ViewInject(R.id.edit_per_name)*/
    private EditText mEditName;
    @ViewInject(R.id.per_sex)
    private RadioGroup mSex;
   // @ViewInject(R.id.edit_per_dep)
    private EditText mDep;
   // @ViewInject(R.id.per_man)
    private RadioButton mMan;
   // @ViewInject(R.id.per_woman)
    private RadioButton mWoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion_msg);
        mEditName = (EditText)  findViewById(R.id.edit_per_name);
        mMan = (RadioButton) findViewById(R.id.per_man);
        mWoman = (RadioButton) findViewById(R.id.per_woman);
        mDep = (EditText) findViewById(R.id.edit_per_dep);
        updateUI();
        //Log.d("username",user.getUserName());
    }

    private void updateUI(){
        userPreference=this.getSharedPreferences("user", Context.MODE_PRIVATE);
        user = GsonUtils.GsonToBean(userPreference.getString("useInfo", ""), User.class);
        mEditName.setText(user.getUserName());
        if (user.getSex().equals("boy"))
            mMan.setChecked(true);
        else
            mWoman.setChecked(true);
        mDep.setText("TODO");
    }

}
