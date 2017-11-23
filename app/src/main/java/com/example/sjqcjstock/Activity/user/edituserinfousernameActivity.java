package com.example.sjqcjstock.Activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改用户名称
 */
public class edituserinfousernameActivity extends Activity {

    private LinearLayout goback1;
    private ImageView clearedit1;
    private EditText editname1;
    private TextView isexist1;
    private LinearLayout editusername1;
    private String nameStr = "";
    // 获取原用户名
    private String unamestr2;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edituserinfousername);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();

    }

    private void initView() {
        dialog = new CustomProgress(this);
        // 获取intent的数据
        unamestr2 = getIntent().getStringExtra("unamestr");
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        isexist1 = (TextView) findViewById(R.id.isexist1);
        clearedit1 = (ImageView) findViewById(R.id.clearedit1);
        editname1 = (EditText) findViewById(R.id.editname1);
        editusername1 = (LinearLayout) findViewById(R.id.editusername1);
        editname1.setText(unamestr2);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearedit1.setOnClickListener(new clearedit1_listener());
        editusername1.setOnClickListener(new editusername1_listener());

    }

    class editusername1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            dialog.showDialog();
            // 判断用户名是否重复
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    // 用户名
                    dataList.add(new BasicNameValuePair("uname", editname1.getText().toString()));
                    nameStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/register/checkUname", dataList);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }

    }

    class clearedit1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            editname1.setText(null);
        }
    }


    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(nameStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {
                            // 将参数传回请求的Activity
                            Intent intent = getIntent();
                            Bundle unameBundle = new Bundle();
                            unameBundle.putString("unamestr", editname1.getText().toString());
                            intent.putExtras(unameBundle);
                            // 结果码，以及上一个页面传递过来的intent
                            setResult(5, intent);
                            finish();
                        } else {
                            editname1.requestFocus();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
