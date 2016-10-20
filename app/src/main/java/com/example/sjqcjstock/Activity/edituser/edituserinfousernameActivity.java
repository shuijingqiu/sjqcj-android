package com.example.sjqcjstock.Activity.edituser;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;

import java.util.List;
import java.util.Map;

public class edituserinfousernameActivity extends Activity {

    private LinearLayout goback1;
    private ImageView clearedit1;
    private EditText editname1;
    private TextView isexist1;
    private LinearLayout editusername1;


    // 获取原用户名
    String unamestr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edituserinfousername);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        // 获取intent的数据
        Intent intent = getIntent();
        unamestr2 = intent.getStringExtra("unamestr");


        goback1 = (LinearLayout) findViewById(R.id.goback1);
        isexist1 = (TextView) findViewById(R.id.isexist1);
        clearedit1 = (ImageView) findViewById(R.id.clearedit1);
        editname1 = (EditText) findViewById(R.id.editname1);
        editusername1 = (LinearLayout) findViewById(R.id.editusername1);

        editname1.setText(unamestr2);

        goback1.setOnClickListener(new goback1_listener());
        clearedit1.setOnClickListener(new clearedit1_listener());
        editusername1.setOnClickListener(new editusername1_listener());

        // 无效的失去焦点
        // editname1.setOnFocusChangeListener(new OnFocusChangeListener() {
        //
        // @Override
        // public void onFocusChange(View arg0, boolean arg1) {
        // // TODO Auto-generated method stub
        // //if(arg1==false){
        // // CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1)
        // // .show();
        // //}
        //
        // if(arg1){//获得焦点
        //
        // }else{//失去焦点
        // new SendInfoTasktesting().execute(new
        // TaskParams(Constants.Url+"?app=public&mod=Register&act=IsUnameAvailable",
        // //new String[] { "login_email", "1061550505@qq.com" },
        // //new String[] { "login_password", "12345678" },
        // new String[] { "old_name", "dfsfsdfsd"},
        // new String[] { "uname", "dsffdgdfgdfgdfgdf"}
        //
        //
        // )
        //
        // );
        // }
        // }
        // });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            // if (isineditarea(editcommentforweibo1, ev)) {
            // //CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1)
            // // .show();
            // LinearLayout01.setVisibility(View.GONE);
            //
            // }

            if (isineditarea(editname1, ev)) {
                // LinearLayout01.setVisibility(View.VISIBLE);

                // CustomToast.makeText(getApplicationContext(), "已离开区域", 1).show();
                // unamestr2
                AsyncTask<TaskParams, Void, String> edituserinfotask3 = new SendInfoTasktesting();

                edituserinfotask3
                        .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=Register&act=IsUnameAvailable",
                                // new String[] { "login_email",
                                // "1061550505@qq.com" },
                                // new String[] { "login_password", "12345678"
                                // },
                                new String[]{"old_name", "dfsdf"},
                                new String[]{"uname", editname1.getText().toString()}

                        ));
            }

            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    // 判断是否离开edit区域的方法
    public boolean isineditarea(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }

    class editusername1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 将参数传回请求的Activity
            Intent intent = getIntent();
            Bundle unameBundle = new Bundle();
            unameBundle.putString("unamestr", editname1.getText().toString());
            // zhuceIntent.putExtra("username", zhuceEdit.getText().toString());
            intent.putExtras(unameBundle);
            // 结果码，以及上一个页面传递过来的intent
            setResult(5, intent);
            finish();
        }

    }

    class clearedit1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            editname1.setText(null);
        }

    }

    private class SendInfoTasktesting extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            // old_name=dfsfsdfsd&uname=dsfdf

            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String infostr;
                String statusstr = map.get("status") + "";

                if (map.get("info") == null) {
                    infostr = "该用户名不存在";
                } else {
                    infostr = map.get("info") + "";
                }
                isexist1.setText(infostr);


            }

        }

    }

}
