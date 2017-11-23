package com.example.sjqcjstock.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.MainActivity;
import com.example.sjqcjstock.Activity.user.RegisterActivity;
import com.example.sjqcjstock.app.CarApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // 获取微信 openid
    private String openidstr;
    private String access_tokenstr;
    private String weixinNickname;
    // 登陆接口返回数据
    private String loginStr;
    private Handler mHandler;

    private Context context = WXEntryActivity.this;

    private void handleIntent(Intent paramIntent) {
        // TODO Auto-generated method stub

        CarApplication.api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        CarApplication.api.registerApp(Constants.APP_ID);

        CarApplication.api.handleIntent(paramIntent, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onReq(BaseReq arg0) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        // TODO Auto-generated method stub

        String code = "";

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
                    CustomToast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                code = ((SendAuth.Resp) resp).code;
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0) {
                            try{
                                JSONObject jsonObject = new JSONObject(loginStr);
                                String code = jsonObject.getString("code");
                                if (Constants.successCode.equals(code)){
                                    // 登录成功
                                    // 密码未修改
                                    Constants.isDefault = false;
                                    JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                                    String userId =  jsonData.getString("uid");
                                    // 获得编辑器
                                    Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器
                                    editor.putString("userid", userId);
                                    editor.commit();
                                    //已经登陆系统
                                    Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
                                    editorIsLogin.putString("isLogin", "1");
                                    editorIsLogin.putString("uidstr", userId);
                                    editorIsLogin.putString("openidstr", openidstr);
                                    editorIsLogin.putString("loginType", "weixin");
                                    editorIsLogin.commit();

                                    Constants.setStaticmyuidstr("");
                                    Constants.setStaticuname("");
                                    Constants.setStaticpasswordstr("");
                                    Constants.setStaticmyuidstr(userId);
                                    Constants.setStatictokeystr(openidstr);
                                    Constants.setStaticLoginType("weixin");
                                    // 登陆成功跳转
                                    // 修改登陆状态
                                    Constants.isLogin = true;
                                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    // 第三方未绑定
                                    // 未绑定时跳转到绑定页面
                                    Intent intent = new Intent(WXEntryActivity.this, RegisterActivity.class);
                                    intent.putExtra("name",weixinNickname);
                                    intent.putExtra("type","binding");
                                    intent.putExtra("registerType","weixin");
                                    intent.putExtra("oauth_token",openidstr);
                                    intent.putExtra("oauth_token_secret",access_tokenstr);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                // 将code发送至微信服务器，根据code返回 openid、access_token
                new SendInfoTask()
                        .execute(new TaskParams(
                                "https://api.weixin.qq.com/sns/oauth2/access_token", "",
                                new String[]{"appid", Constants.APP_ID},
                                new String[]{"secret", Constants.App_Secret},
                                new String[]{"code", code},
                                new String[]{"grant_type", "authorization_code"}));

            case BaseResp.ErrCode.ERR_USER_CANCEL:

                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:

                break;


            default:
                break;
        }

        finish();

    }

    // 将code发送至微信服务器，根据code返回 openid、access_token
    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "用户名密码错误", Toast.LENGTH_SHORT).show();
            } else {
                // 解析json字符串获得List<Map<String,Object>>
                result = "[" + result + "]";
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    access_tokenstr = map.get("access_token").toString();
                    openidstr = map.get("openid").toString();
                    // 根据openid和access_token获取微信用户的昵称
                    new SendInfoTaskForweixinNickname().execute(new TaskParams(
                            "https://api.weixin.qq.com/sns/userinfo", "",
                            new String[]{"access_token", access_tokenstr},
                            new String[]{"openid", openidstr}

                    ));

//                    new SendInfoTaskweixinthirdlogin()
//                            .execute(new TaskParams(
//                                    Constants.Url + "?app=index&mod=Index&act=app_w3g_no_register_display",
//                                    new String[]{"type", "weixin"},
//                                    new String[]{"tokey", openidstr}));
                    // 第三分登陆
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List dataList = new ArrayList();
                            // 第三分token
                            dataList.add(new BasicNameValuePair("tokey", openidstr));
                            // 类型
                            dataList.add(new BasicNameValuePair("type", "weixin"));
                            loginStr = HttpUtil.restHttpPost(Constants.newUrl+"/api/login/thirdParty",dataList);
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();

                }
            }
        }
    }

//    private class SendInfoTaskweixinthirdlogin extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // 解析json字符串获得List<Map<String,Object>>
//                result = "[" + result + "]";
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//
//                for (Map<String, Object> map : lists) {
//                    String statusstr = map.get("status").toString();
//
//                    String datastr = map.get("data").toString();
//                    List<Map<String, Object>> datastrlists = JsonTools
//                            .listKeyMaps("[" + datastr + "]");
//                    //未绑定
//                    if ("0".equals(statusstr)) {
//                        // 微信账户与水晶球账户进行绑定
////                        new SendInfoTaskForNickName()
////                                .execute(new TaskParams(
////                                        Constants.Url + "?app=index&mod=Index&act=AppdoOtherStep",
////                                        new String[]{
////                                                "email",
////                                                openidstr.substring(0, 9)
////                                                        + "@qq.com"},
////                                        new String[]{"password",
////                                                openidstr.substring(0, 9)},
////                                        new String[]{"uname", weixinNickname},
////                                        new String[]{"type", "weixin"},
////                                        new String[]{"tokey", openidstr},
////                                        new String[]{"access_token",
////                                                access_tokenstr}));
//
//                        // 未绑定时跳转到绑定页面
//                        Intent intent = new Intent(WXEntryActivity.this, RegisterActivity.class);
//                        intent.putExtra("name",weixinNickname);
//                        intent.putExtra("type","binding");
//                        intent.putExtra("registerType","weixin");
//                        intent.putExtra("oauth_token",openidstr);
//                        intent.putExtra("oauth_token_secret",access_tokenstr);
//                        startActivity(intent);
//                    } else {
//                        for (Map<String, Object> datastrmap : datastrlists) {
//                            String midstr = datastrmap.get("mid").toString();
//                            // 设置密码
//                            Constants.isDefault = false;
//                            /** 向SharedPreferemces中存储数据 */
//                            // 获得编辑器
//                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器
//
//                            // 存储数据
//                            editor.putString("userid", midstr);
//                            // editor.putString("login_email",loginusername1.getText().toString());
//                            // editor.putString("login_password",
//                            // loginpassword1.getText().toString());
//                            editor.commit();
//
//                            //已经登陆系统
//                            Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
//                            editorIsLogin.putString("isLogin", "1");
//                            editorIsLogin.putString("uidstr", midstr);
//                            editorIsLogin.putString("openidstr", openidstr);
//                            editorIsLogin.putString("loginType", "weixin");
//                            editorIsLogin.commit();
//
//
//                            Constants.setStaticmyuidstr("");
//                            Constants.setStaticuname("");
//                            Constants.setStaticpasswordstr("");
//                            Constants.setStatictokeystr("");
//                            Constants.setStaticLoginType("");
//                            Constants.setStaticmyuidstr(midstr);
//                            Constants.setStatictokeystr(openidstr);
//                            Constants.setStaticLoginType("weixin");
//                            // 登陆成功跳转
//                            // 修改登陆状态
//                            Constants.isLogin = true;
//                            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                }
//            }
//        }
//    }

    private class SendInfoTaskForweixinNickname extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // 解析json字符串获得List<Map<String,Object>>

                result = "[" + result + "]";
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    weixinNickname = map.get("nickname").toString();
                }
            }
        }
    }

//    private class SendInfoTaskForNickName extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//
//                // 解析服务器返回的数据，根据数据做出相应的判断
//                for (Map<String, Object> map : lists) {
//                    // 判断是否登录成功 1代表成功，其他代表失败
//
//                    String statusstr = map.get("status").toString();
//                    String datastr = map.get("data").toString();
//                    List<Map<String, Object>> datastrlists = JsonTools
//                            .listKeyMaps("[" + datastr + "]");
//
//                    // 昵称有重名，跳转到昵称设置界面
//                    if ("0".equals(statusstr)) {
//
//                        Intent intent = new Intent(WXEntryActivity.this, UpdateNicknameActivity.class);
//                        intent.putExtra("email", openidstr.substring(0, 9) + "@qq.com");
//                        intent.putExtra("type", "weixin");
//                        intent.putExtra("tokey", openidstr);
//                        intent.putExtra("access_token", access_tokenstr);
//                        startActivity(intent);
//
//                        // 已绑定，直接跳转到主界面
//                    } else {
//                        for (Map<String, Object> datastrmap : datastrlists) {
//
//                            String midstr = datastrmap.get("mid").toString();
//
//                            /** 向SharedPreferemces中存储数据 */
//                            // 获得编辑器
//                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();
//                            editor.putString("userid", midstr);
//                            // editor.putString("login_email",loginusername1.getText().toString());
//                            // editor.putString("login_password",loginpassword1.getText().toString());
//                            editor.commit();
//
//                            //已经登陆系统
//                            Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
//                            editorIsLogin.putString("isLogin", "1");
//                            editorIsLogin.putString("uidstr", midstr);
//                            editorIsLogin.putString("openidstr", openidstr);
//                            editorIsLogin.putString("loginType", "weixin");
//                            editorIsLogin.commit();
//
//                            //登录成功后需要保存的参数
//                            Constants.setStaticmyuidstr("");
//                            Constants.setStaticuname("");
//                            Constants.setStaticpasswordstr("");
//                            Constants.setStatictokeystr("");
//                            Constants.setStaticLoginType("");
//                            Constants.setStaticmyuidstr(midstr);
//                            Constants.setStatictokeystr(openidstr);
//                            Constants.setStaticLoginType("weixin");
//                            //Constants.setStaticpasswordstr(openidstr.substring(0, 9));
//                            //Constants.setStaticuname(weixinNickname);
//
//                            // 设置密码
//                            Constants.isDefault = false;
//                            // 登陆成功跳转
//                            // 修改登陆状态
//                            Constants.isLogin = true;
//                            // 已经绑定微信，直接跳转到主界面
//                            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                }
//            }
//        }
//    }
}
