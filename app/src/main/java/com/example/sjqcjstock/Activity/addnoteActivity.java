package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;

import java.util.List;
import java.util.Map;

public class addnoteActivity extends Activity {

    private LinearLayout addshortweibo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.addshortweibo);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {
        addshortweibo1 = (LinearLayout) findViewById(R.id.addshortweibo1);
        addshortweibo1.setOnClickListener(new addshortweibo1_listener());

    }

    class addshortweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            new SendInfoTask()
                    .execute(new TaskParams(
                                    Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed", new String[]{
                                    "mid", Constants.staticmyuidstr},
                                    new String[]{"login_password",
                                            Constants.staticpasswordstr},
                                    new String[]{"app_name", "public"},
                                    new String[]{"body", "今日行情"}, new String[]{
                                    "type", "post"}

                            )

                    );
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

        }

    }

}
