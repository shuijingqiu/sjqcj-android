package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.List;
import java.util.Map;

public class UpdateNicknameActivity extends Activity {

    Button bt_ok;
    EditText updateNickname_et_input_nickname;
    String email, type, openidstr, access_tokenstr, nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_nickname);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        type = intent.getStringExtra("type");
        openidstr = intent.getStringExtra("tokey");
        access_tokenstr = intent.getStringExtra("access_token");
    }

    private void initListener() {
        // TODO Auto-generated method stub
        bt_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 点击登录向服务器发送昵称判断
                isNickNameSame();
            }
        });
    }

    protected void isNickNameSame() {
        // TODO Auto-generated method stub
        nickname = updateNickname_et_input_nickname.getText() + "";
        if (nickname == null || nickname.equals("")) {
            CustomToast.makeText(UpdateNicknameActivity.this, "用户昵称不能为空!", Toast.LENGTH_LONG).show();
        } else {
            // qq账户与水晶球账户进行绑定
            new SendInfoTaskForNickName().execute(new TaskParams(
                    Constants.Url + "?app=index&mod=Index&act=AppdoOtherStep",
                    new String[]{"email", email},
                    new String[]{"password", openidstr.substring(0, 9)},
                    new String[]{"uname", nickname},
                    new String[]{"type", type},
                    new String[]{"tokey", openidstr},
                    new String[]{"access_token", access_tokenstr}));
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        updateNickname_et_input_nickname = (EditText) findViewById(R.id.updateNickname_et_input_nickname);
        bt_ok = (Button) findViewById(R.id.bt_ok);
    }

    // 判断用户名是否重复
    private class SendInfoTaskForNickName extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", 1).show();
            } else {
                super.onPostExecute(result);
                // CustomToast.makeText(loginActivity.this, result, 1).show();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                // 解析服务器返回的数据，根据数据做出相应的判断
                for (Map<String, Object> map : lists) {
                    // 判断是否登录成功 1代表成功，其他代表失败

                    String statusstr = map.get("status") + "";
                    String datastr = map.get("data") + "";

                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");

                    Log.d("1000", statusstr);

                    // 昵称有重名，跳转到昵称设置界面
                    if ("0".equals(statusstr)) {

                        CustomToast.makeText(UpdateNicknameActivity.this, "该昵称已被使用，请更换昵称", Toast.LENGTH_LONG).show();

                        // 已绑定，直接跳转到主界面
                    } else {
                        for (Map<String, Object> datastrmap : datastrlists) {

                            String midstr = datastrmap.get("mid") + "";

                            /** 向SharedPreferemces中存储数据 */
                            // 获得编辑器
                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器
                            editor.putString("userid", midstr);
                            editor.commit();

                            //已经登陆系统
                            Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
                            editorIsLogin.putString("isLogin", "1");
                            editorIsLogin.putString("uidstr", midstr);
                            editorIsLogin.putString("openidstr", openidstr);
                            editorIsLogin.putString("loginType", type);
                            editorIsLogin.commit();

                            Constants.setStaticmyuidstr("");
                            Constants.setStaticuname("");
                            Constants.setStaticpasswordstr("");
//							Constants.setStatictokeystr("");
//							Constants.setStaticLoginType("");
                            Constants.setStaticmyuidstr(midstr);
                            Constants.setStaticLoginType(type);
                            Constants.setStatictokeystr(openidstr);

                            // 设置密码
                            Constants.isDefault = false;
                            Intent intent = new Intent(UpdateNicknameActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        }
    }
}
