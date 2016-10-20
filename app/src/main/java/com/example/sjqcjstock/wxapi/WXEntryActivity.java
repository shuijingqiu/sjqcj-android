package com.example.sjqcjstock.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.MainActivity;
import com.example.sjqcjstock.Activity.UpdateNicknameActivity;
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

import java.util.List;
import java.util.Map;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // 获取微信 openid
    String openidstr;
    String access_tokenstr;
    String weixinNickname;

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
        // TODO Auto-generated method stub
        // CustomToast.makeText(getApplicationContext(), "onReq", 1).show();

        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        // TODO Auto-generated method stub

        String code = "";

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
                    CustomToast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
                    break;
                }

                code = ((SendAuth.Resp) resp).code;

                // 将code发送至微信服务器，根据code返回 openid、access_token
                new SendInfoTask()
                        .execute(new TaskParams(
                                "https://api.weixin.qq.com/sns/oauth2/access_token", "",
                                new String[]{"appid", Constants.APP_ID},
//							new String[] { "secret","9205759657863cc68e9fa506efd90f14" },
                                new String[]{"secret", Constants.App_Secret},
                                new String[]{"code", code},
                                new String[]{"grant_type", "authorization_code"}));
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:

                break;

            // String code=((SendAuth.Resp)resp).code;

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
                CustomToast.makeText(getApplicationContext(), "用户名密码错误", Toast.LENGTH_LONG).show();
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

                    new SendInfoTaskweixinthirdlogin()
                            .execute(new TaskParams(
                                    Constants.Url + "?app=index&mod=Index&act=app_w3g_no_register_display",
                                    new String[]{"type", "weixin"},
                                    new String[]{"tokey", openidstr}));
                }
            }
        }
    }

    private class SendInfoTaskweixinthirdlogin extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_LONG)
                        .show();
            } else {
                // 解析json字符串获得List<Map<String,Object>>
                result = "[" + result + "]";
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("status").toString();

                    String datastr = map.get("data").toString();
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");

                    //未绑定
                    if ("0".equals(statusstr)) {

                        // qq账户与水晶球账户进行绑定
                        new SendInfoTaskForNickName()
                                .execute(new TaskParams(
                                        Constants.Url + "?app=index&mod=Index&act=AppdoOtherStep",
                                        new String[]{
                                                "email",
                                                openidstr.substring(0, 9)
                                                        + "@qq.com"},
                                        new String[]{"password",
                                                openidstr.substring(0, 9)},
                                        new String[]{"uname", weixinNickname},
                                        new String[]{"type", "weixin"},
                                        new String[]{"tokey", openidstr},
                                        new String[]{"access_token",
                                                access_tokenstr}));
                    } else {
                        for (Map<String, Object> datastrmap : datastrlists) {
                            String midstr = datastrmap.get("mid").toString();
                            // 设置密码
                            Constants.isDefault = false;
                            /** 向SharedPreferemces中存储数据 */
                            // 获得编辑器
                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器

                            // 存储数据
                            editor.putString("userid", midstr);
                            // editor.putString("login_email",loginusername1.getText().toString());
                            // editor.putString("login_password",
                            // loginpassword1.getText().toString());
                            editor.commit();

                            //已经登陆系统
                            Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
                            editorIsLogin.putString("isLogin", "1");
                            editorIsLogin.putString("uidstr", midstr);
                            editorIsLogin.putString("openidstr", openidstr);
                            editorIsLogin.putString("loginType", "weixin");
                            editorIsLogin.commit();


                            Constants.setStaticmyuidstr("");
                            Constants.setStaticuname("");
                            Constants.setStaticpasswordstr("");
                            Constants.setStatictokeystr("");
                            Constants.setStaticLoginType("");
                            Constants.setStaticmyuidstr(midstr);
                            Constants.setStatictokeystr(openidstr);
                            Constants.setStaticLoginType("weixin");
                            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        }
    }

    private class SendInfoTaskForweixinNickname extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_LONG)
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
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_LONG)
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

                    String statusstr = map.get("status").toString();
                    String datastr = map.get("data").toString();
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");

                    // 昵称有重名，跳转到昵称设置界面
                    if ("0".equals(statusstr)) {

                        // Log.d("1000", "未绑定--->"+weixinNickname);
                        // Log.d("1000",
                        // "\n未绑定--->"+"openid-->"+openidstr.substring(0,
                        // 9)+"@qq.com");
                        // Log.d("1000",
                        // "\n未绑定--->"+"password-->"+openidstr.substring(0, 9));
                        // Log.d("1000", "\n未绑定--->"+"uname-->"+weixinNickname);
                        // Log.d("1000", "\n未绑定--->"+"type-->"+"weixin" );
                        // Log.d("1000", "\n未绑定--->"+"tokey-->"+openidstr);
                        // Log.d("1000",
                        // "\n未绑定--->"+"access_token-->"+access_tokenstr);

                        Intent intent = new Intent(WXEntryActivity.this, UpdateNicknameActivity.class);
                        intent.putExtra("email", openidstr.substring(0, 9) + "@qq.com");
                        intent.putExtra("type", "weixin");
                        intent.putExtra("tokey", openidstr);
                        intent.putExtra("access_token", access_tokenstr);
                        startActivity(intent);

                        // 已绑定，直接跳转到主界面
                    } else {
                        for (Map<String, Object> datastrmap : datastrlists) {

                            String midstr = datastrmap.get("mid").toString();

                            /** 向SharedPreferemces中存储数据 */
                            // 获得编辑器
                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();
                            editor.putString("userid", midstr);
                            // editor.putString("login_email",loginusername1.getText().toString());
                            // editor.putString("login_password",loginpassword1.getText().toString());
                            editor.commit();

                            //已经登陆系统
                            Editor editorIsLogin = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
                            editorIsLogin.putString("isLogin", "1");
                            editorIsLogin.putString("uidstr", midstr);
                            editorIsLogin.putString("openidstr", openidstr);
                            editorIsLogin.putString("loginType", "weixin");
                            editorIsLogin.commit();

                            //登录成功后需要保存的参数
                            Constants.setStaticmyuidstr("");
                            Constants.setStaticuname("");
                            Constants.setStaticpasswordstr("");
                            Constants.setStatictokeystr("");
                            Constants.setStaticLoginType("");
                            Constants.setStaticmyuidstr(midstr);
                            Constants.setStatictokeystr(openidstr);
                            Constants.setStaticLoginType("weixin");
                            //Constants.setStaticpasswordstr(openidstr.substring(0, 9));
                            //Constants.setStaticuname(weixinNickname);

                            // 设置密码
                            Constants.isDefault = false;
                            // 已经绑定微信，直接跳转到主界面
                            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        }
    }
}
