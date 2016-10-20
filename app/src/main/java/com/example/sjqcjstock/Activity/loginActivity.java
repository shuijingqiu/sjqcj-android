package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.CarApplication;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class loginActivity extends Activity {
    private static final String TAG = loginActivity.class.getName();
    // 获取微信code的好友
    private static final int REQUEST_CODE_WEXINCODE = 8;
    public static loginActivity instance = null;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    // 获取按钮控件
    private Button login;
    private LinearLayout register1;
    private LinearLayout register2;
    private EditText fillphone1;
    private EditText fillpassword1;

    private Handler mHandler;

    private ImageView qqthirdlogin1;
    private ImageView weixinlogin1;

    // 第三方登录用的实例
//	private IUiListener listener;
//	private IUiListener userInfoListener; // 获取用户信息监听器

    // 获取qq的openid信息
    private String reponsestr;

    public static String mAppid;
    public static QQAuth mQQAuth;
    private UserInfo mInfo;
    private Tencent mTencent;
    private final String APP_ID = "1105186318";// 测试时使用，真正发布的时候要换成自己的APP_ID

    // token信息 openid
    private String access_tokenstr;
    private String openidstr;
    private String nicknamestr;

    @Override
    protected void onStart() {
        final Context context = loginActivity.this;
        final Context ctxContext = context.getApplicationContext();
        mAppid = APP_ID;
        mQQAuth = QQAuth.createInstance(mAppid, ctxContext);
        mTencent = Tencent.createInstance(mAppid, loginActivity.this);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        initData();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    JSONObject response = (JSONObject) msg.obj;
                    reponsestr = "[" + reponsestr + "]";
                    List<Map<String, Object>> reponsestrlists = JsonTools.listKeyMaps(reponsestr);
                    access_tokenstr = "";
                    openidstr = "";

                    for (Map<String, Object> map : reponsestrlists) {
                        access_tokenstr = map.get("access_token") + "";
                        openidstr = map.get("openid") + "";
                    }

                    new SendInfoTaskqqthirdlogin()
                            .execute(new TaskParams(
                                    Constants.Url + "?app=index&mod=Index&act=app_w3g_no_register_display",
                                    new String[]{"type", "qq"},
                                    new String[]{"tokey", openidstr}
                            ));

                    if (response.has("nickname")) {
                        try {
                            String responsestr = "[" + response.toString() + "]";
                            List<Map<String, Object>> lists = JsonTools.listKeyMaps(responsestr);
                            for (Map<String, Object> map : lists) {
                                // 获取qq昵称
                                nicknamestr = map.get("nickname") + "";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (msg.what == 1) {

                }
            }

        };
    }

    private void initView() {

        qqthirdlogin1 = (ImageView) findViewById(R.id.qqthirdlogin1);
        weixinlogin1 = (ImageView) findViewById(R.id.weixinlogin1);
        login = (Button) findViewById(R.id.login);

        register1 = (LinearLayout) findViewById(R.id.register1);
        register2 = (LinearLayout) findViewById(R.id.register2);
        fillpassword1 = (EditText) findViewById(R.id.fillpassword1);
        fillphone1 = (EditText) findViewById(R.id.fillphone1);

    }

    private void initData() {
        //unicode编码转中文
        //String str = unicodeconvertutil.decodeUnicode("\u9677\u9631\u2014\u2014\u5996\u80a1");

        // 保存上一次登录的用户名密码
        String oldlogin_email = getSharedPreferences("userinfo", MODE_PRIVATE).getString("login_email", "");
        String oldlogin_password = getSharedPreferences("userinfo", MODE_PRIVATE).getString("login_password", "");
        fillphone1.setText(oldlogin_email);
        fillpassword1.setText(oldlogin_password);

        //qq三方登录
        qqthirdlogin1.setOnClickListener(new qqthirdlogin1_listener());

        //微信三方登录,通过WXAPIFactory工厂，获取IWXAPI的实例。将该app注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
        CarApplication.getApi(loginActivity.this);
        weixinlogin1.setOnClickListener(new weixinlogin1_listener());

        //普通用户登录
        login.setOnClickListener(new commonUserLogin_listener());

        //用户注册
        register1.setOnClickListener(new userRegister_listener());
        //找回密码
        register2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, ForgotPassWordActivity.class);
                startActivity(intent);
            }
        });

    }

    class userRegister_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            userRegister();
        }

        private void userRegister() {
            Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    class commonUserLogin_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            commonUserLogin();
        }

        private void commonUserLogin() {
            //防止暴力点击
            if (Utils.isFastDoubleClick()) {
                return;
            }
            // 防止二次第三方的影响
            Constants.isDefault = true;
            new SendInfoTaskForCommonUserLogin().execute(new TaskParams(
                    Constants.Url + "?app=public&mod=Passport&act=AppLogin",
                    new String[]{"login_email", fillphone1.getText().toString()},
                    new String[]{"login_password", fillpassword1.getText().toString()},
                    new String[]{"login_remember", "1"}
            ));
        }
    }

    class weixinlogin1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        }
    }

    class qqthirdlogin1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            onClickLogin();
        }
    }

    private void onClickLogin() {
        if (!mQQAuth.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                @Override
                protected void doComplete(JSONObject values) {
                    updateUserInfo();
                }
            };
            mQQAuth.login(this, "all", listener);
            mTencent.login(this, "all", listener);
        } else {
            mQQAuth.logout(this);
            updateUserInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11101) {
        } else if (resultCode == REQUEST_CODE_WEXINCODE) {
            Bundle bundle = data.getExtras();
            String codestr = bundle.getString("codestr");
        }
    }

    private void updateUserInfo() {
        if (mQQAuth != null && mQQAuth.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread() {

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) response;
                            if (json.has("figureurl")) {
                                Bitmap bitmap = null;
                                try {
                                } catch (Exception e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }.start();
                }

                @Override
                public void onCancel() {
                }
            };
            mInfo = new UserInfo(this, mQQAuth.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            reponsestr = response + "";
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }
    }

    //普通用户登录
    private class SendInfoTaskForCommonUserLogin extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("status") + "";

                    if (!"1".equals(statusstr)) {
                        CustomToast.makeText(getApplicationContext(), "用户名或密码错误,请重试", Toast.LENGTH_LONG).show();
                    } else {
                        String unamestr;
                        String infostr = map.get("info") + "";
                        String uidstr = map.get("uid") + "";

                        if (map.get("name") == null) {
                            unamestr = "";
                        } else {
                            unamestr = map.get("name") + "";
                        }
                        /** 向SharedPreferemces中存储数据 */

                        // 存储用户的相关信息，mid、账号、密码
                        Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器
                        editor.putString("userid", uidstr);
                        editor.putString("login_email", fillphone1.getText().toString());
                        editor.putString("login_password", fillpassword1.getText().toString());
                        editor.commit();

                        //已经登陆系统
                        Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
                        editorIsLogin.putString("isLogin", "1");
                        editorIsLogin.putString("loginType", "common");
                        editorIsLogin.putString("uidstr", uidstr);
                        editorIsLogin.putString("unamestr", unamestr);
                        editorIsLogin.putString("loginPwd", fillpassword1.getText().toString());
                        editorIsLogin.commit();

                        Constants.setStaticmyuidstr("");
                        Constants.setStaticuname("");
                        Constants.setStaticpasswordstr("");
                        Constants.setStatictokeystr("");
                        Constants.setStaticLoginType("");

                        Constants.setStaticmyuidstr(uidstr);
                        Constants.setStaticuname(unamestr);
                        Constants.setStaticpasswordstr(fillpassword1.getText().toString());

                        if (Integer.parseInt(statusstr) == 1) {
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        }
    }

    private class SendInfoTaskqqthirdlogin extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                // 解析json字符串获得List<Map<String,Object>>
                result = "[" + result + "]";
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("status") + "";
                    String datastr = map.get("data") + "";
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");
                    // 0，未绑定
                    if ("0".equals(statusstr)) {
                        //注册的时候一下参数都需要
                        new SendInfoTaskForNickName()
                                .execute(new TaskParams(
                                        Constants.Url + "?app=index&mod=Index&act=AppdoOtherStep",
                                        new String[]{"email", openidstr.substring(0, 9) + "@qq.com"},
                                        new String[]{"password", openidstr.substring(0, 9)},
                                        new String[]{"uname", nicknamestr},
                                        new String[]{"type", "qq"},
                                        new String[]{"tokey", openidstr},
                                        new String[]{"access_token", access_tokenstr}));
                        // 已经绑定
                    } else {
                        for (Map<String, Object> datastrmap : datastrlists) {
                            String midstr = datastrmap.get("mid") + "";
                            // 密码未修改
                            Constants.isDefault = false;
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
                            editorIsLogin.putString("loginType", "qq");
                            editorIsLogin.commit();

                            Constants.setStaticmyuidstr("");
                            Constants.setStaticuname("");
                            Constants.setStaticpasswordstr("");
                            Constants.setStaticmyuidstr(midstr);
                            Constants.setStatictokeystr(openidstr);
                            Constants.setStaticLoginType("qq");
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        }
    }

    // 判断用户名是否重复
    private class SendInfoTaskForNickName extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
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
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");

                    // 昵称有重名，跳转到昵称设置界面
                    if ("0".equals(statusstr)) {

                        Intent intent = new Intent(loginActivity.this, UpdateNicknameActivity.class);
                        intent.putExtra("email", openidstr.substring(0, 9) + "@qq.com");
                        intent.putExtra("password", openidstr.substring(0, 9));
                        intent.putExtra("type", "qq");
                        intent.putExtra("tokey", openidstr);
                        intent.putExtra("access_token", access_tokenstr);
                        startActivity(intent);

                        // 昵称没有重名，则直接跳转主界面
                    } else {
                        for (Map<String, Object> datastrmap : datastrlists) {

                            String midstr = datastrmap.get("mid") + "";

                            /** 向SharedPreferemces中存储数据 */
                            // 获得编辑器
                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器
                            editor.putString("userid", midstr);
                            // editor.putString("login_email",loginusername1.getText().toString());
                            // editor.putString("login_password",loginpassword1.getText().toString());
                            editor.commit();

                            //已经登陆系统
                            Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
                            editorIsLogin.putString("isLogin", "1");
                            editorIsLogin.putString("uidstr", midstr);
                            editorIsLogin.putString("openidstr", openidstr);
                            editorIsLogin.putString("loginType", "qq");
                            editorIsLogin.commit();

                            Constants.setStaticmyuidstr("");
                            Constants.setStaticuname("");
                            Constants.setStaticpasswordstr("");
//                            Constants.setStatictokeystr("");
//                            Constants.setStaticLoginType("");
                            //登录成功后需要保存的参数
                            Constants.setStaticmyuidstr(midstr);
                            Constants.setStatictokeystr(openidstr);
                            Constants.setStaticLoginType("qq");
                            // 设置密码
                            Constants.isDefault = false;
                            // 已经绑定qq，直接跳转到主界面
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        }
    }

    private long exitTime = 0;

    //两次按下间隔时间小于两秒就退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                CustomToast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ExitApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        finish();
    }

}
