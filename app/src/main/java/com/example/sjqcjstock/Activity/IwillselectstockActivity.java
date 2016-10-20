package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;

import java.util.List;
import java.util.Map;

/**
 * 我来选股页面
 */
public class IwillselectstockActivity extends Activity {

    // 获取返回控件
    private LinearLayout goback1;
    // 获取控件
    private Button submitmyselectstock1;
    private EditText onecode1;
    private EditText twocode1;
    private EditText one_vote1;
    private EditText two_vote1;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.iwillselectstock);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        submitmyselectstock1 = (Button) findViewById(R.id.submitmyselectstock1);
        onecode1 = (EditText) findViewById(R.id.onecode1);
        twocode1 = (EditText) findViewById(R.id.twocode1);
        one_vote1 = (EditText) findViewById(R.id.one_vote1);
        two_vote1 = (EditText) findViewById(R.id.two_vote1);
        goback1.setOnClickListener(new goback4_listener());
        submitmyselectstock1
                .setOnClickListener(new submitmyselectstock1_listener());
        // 取消事件的按钮绑定
        findViewById(R.id.cancel_bt).setOnClickListener(new goback4_listener());

    }

    class goback4_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            finish();
        }
    }

    class submitmyselectstock1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (Utils.isFastDoubleClick()) {
                return;
            }
            String one = onecode1.getText() + "";
            String two = twocode1.getText() + "";
            String one_voteStr = one_vote1.getText() + "";
            String two_voteStr = two_vote1.getText() + "";
            if ("".equals(one)) {
                CustomToast.makeText(getApplicationContext(), "请输入第一只股票代码",
                        Toast.LENGTH_LONG).show();
                onecode1.requestFocus();
                return;
            }
            if (one.length() != 6) {
                CustomToast.makeText(getApplicationContext(), "输入的第一只股票代码有误",
                        Toast.LENGTH_LONG).show();
                onecode1.requestFocus();
                return;
            }
            if ("".equals(one_voteStr)) {
                CustomToast.makeText(getApplicationContext(), "请输入第一只选股理由",
                        Toast.LENGTH_LONG).show();
                one_vote1.requestFocus();
                return;
            }
            if ("".equals(two)) {
                CustomToast.makeText(getApplicationContext(), "请输入第二只股票代码",
                        Toast.LENGTH_LONG).show();
                twocode1.requestFocus();
                return;
            }
            if (two.length() != 6) {
                CustomToast.makeText(getApplicationContext(), "输入的第二只股票代码有误",
                        Toast.LENGTH_LONG).show();
                twocode1.requestFocus();
                return;
            }
            if ("".equals(two_voteStr)) {
                CustomToast.makeText(getApplicationContext(), "请输入第二只选股理由",
                        Toast.LENGTH_LONG).show();
                two_vote1.requestFocus();
                return;
            }
            dialog.show();
            new SendInfoTask()
                    .execute(new TaskParams(
                            Constants.Url + "?app=public&mod=AppFeedList&act=Appaddbattol",
                            new String[]{"mid", Constants.staticmyuidstr},
                            new String[]{"login_password", Constants.staticpasswordstr},
                            new String[]{"tokey", Constants.statictokeystr},
                            new String[]{"one", one},
                            new String[]{"two", two},
                            new String[]{"one_vote", one_voteStr},
                            new String[]{"two_vote", two_voteStr}
                    ));
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
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                if ("1".equals(statusstr)) {
                    if (map.get("body") != null) {
                        String bodystr = map.get("body") + "";
                        new SendInfoTaskaddlongweibo()
                                .execute(new TaskParams(
                                        Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
                                        new String[]{"mid", Constants.staticmyuidstr},
                                        new String[]{"login_password", Constants.staticpasswordstr},
                                        new String[]{"tokey", Constants.statictokeystr},
                                        new String[]{"app_name", "public"},
                                        new String[]{"body", bodystr},
                                        new String[]{"channel_id", "15"},
                                        new String[]{"type", "post"}
                                ));
                    }
                } else {
                    if (map.get("info") == null) {
                        CustomToast.makeText(getApplicationContext(), "参赛失败",
                                Toast.LENGTH_LONG).show();
                    } else {
                        String infostr = map.get("info") + "";
                        CustomToast.makeText(getApplicationContext(), infostr,
                                Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            }
        }
    }

    private class SendInfoTaskaddlongweibo extends
            AsyncTask<TaskParams, Void, String> {

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
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "参赛成功",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(IwillselectstockActivity.this, discussareaActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                } else {
                    CustomToast.makeText(getApplicationContext(), "参赛失败",
                            Toast.LENGTH_LONG).show();
                }
            }
            dialog.dismiss();
        }
    }
}
