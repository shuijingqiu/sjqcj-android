package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.List;
import java.util.Map;

public class modifypasswordActivity extends Activity {

    //获取控件
    private LinearLayout goback1;
    private EditText oldpassword1;
    private EditText newpassword2;
    private EditText renewpassword2;
    private Button modifiedsubmit1;
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modifypassword);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        oldpassword1 = (EditText) findViewById(R.id.oldpassword1);
        newpassword2 = (EditText) findViewById(R.id.newpassword2);
        renewpassword2 = (EditText) findViewById(R.id.renewpassword2);
        modifiedsubmit1 = (Button) findViewById(R.id.modifiedsubmit1);
        goback1.setOnClickListener(new goback3_listener());
        modifiedsubmit1.setOnClickListener(new modifiedsubmit1_listener());
        titleName = (TextView) findViewById(R.id.title_name);
        if (Constants.isDefault) {
            titleName.setText("修改密码");
        } else {
            titleName.setText("设置密码");
            findViewById(R.id.oldpassword_ll).setVisibility(View.GONE);
        }
    }


    class modifiedsubmit1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {

            if (!newpassword2.getText().toString().equals(renewpassword2.getText().toString())) {
                CustomToast.makeText(getApplicationContext(), "对不起两次输入的密码不一致", Toast.LENGTH_LONG).show();
                renewpassword2.setText("");
                return;
            }

            // 第三方登录的Token
            String token = Constants.statictokeystr;
            // 登陆类型
            String type = Constants.getStaticLoginType();
            // 原始密码
            String oldPassWord = oldpassword1.getText() + "";
            if (Constants.isDefault) {
                token = "";
                type = "";
            } else {
                // 第三方登录的时候传一个假密码 不能为空就行
                oldPassWord = "jiade";
            }
            new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppdoModifyPassword",
                            //new String[] { "login_email", "1061550505@qq.com" },
                            //new String[] { "login_password", "12345678" },
                            new String[]{"mid", Constants.staticmyuidstr},
                            new String[]{"login_password", Constants.staticpasswordstr},
                            new String[]{"tokey", token},
                            new String[]{"type", type},
                            new String[]{"oldpassword", oldPassWord},
                            new String[]{"password", newpassword2.getText().toString()},
                            new String[]{"repassword", renewpassword2.getText().toString()}
                    )
            );
            finish();
        }
    }


    //回退按钮
    class goback3_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }
    }


    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";

                if ("1".equals(statusstr)) {
                    if (Constants.isDefault) {
                        CustomToast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_LONG).show();
                    } else {
                        CustomToast.makeText(getApplicationContext(), "设置密码成功", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Constants.isDefault) {
                        CustomToast.makeText(getApplicationContext(), "修改密码失败,请重试", Toast.LENGTH_LONG).show();
                    } else {
                        CustomToast.makeText(getApplicationContext(), "设置密码失败,请重试", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
