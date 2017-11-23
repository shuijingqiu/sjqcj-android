package com.example.sjqcjstock.Activity.qa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 写提问和回答的页面
 * Created by Administrator on 2017/3/6.
 */
public class QuestionAnswerActivity extends Activity {

    // 网络请求提示
    private CustomProgress dialog;
    private EditText contentEdi;
    // 写摘要还能输入多少字
    private TextView inputNumberWords;
    // 标题
    private TextView titleTv;
    // 1 为提问 2为回答
    private String type;
    // 接口返回数据
    private String resstr;
    // 问题id 或 回答者uid
    private String auid;
    // 提问水晶币个数
    private String sjbCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 页面空间的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentEdi = (EditText) findViewById(R.id.content_edi);
        // 绑定摘要输入状态的变化
        contentEdi.addTextChangedListener(watcher);
        titleTv = (TextView) findViewById(R.id.title_tv);
        inputNumberWords = (TextView) findViewById(R.id.input_number_words);
        type = getIntent().getStringExtra("type");
        auid = getIntent().getStringExtra("id");
        sjbCount = getIntent().getStringExtra("sjbCount");
        if ("2".equals(type)) {
            titleTv.setText("回答");
            contentEdi.setHint("输入你要回答的内容");
        }
    }

    /**
     * 提交或者回答问题的按钮
     *
     * @param view
     */
    public void SubmitClick(View view) {
        final String content = contentEdi.getText().toString().trim();
        if (content.length() > 200) {
            Toast.makeText(getApplicationContext(), "对不起字数超出了限制", Toast.LENGTH_SHORT).show();
            return;
        }
        final String contentStr = content;
        final List dataList = new ArrayList();
        // 用户ID
        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
        if (type != null && type.equals("1")) {
            // 弹出提示框
            new AlertDialog.Builder(QuestionAnswerActivity.this).setTitle("提问确认").setMessage("打赏" + sjbCount + "水晶币提问")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogs, int which) {
                            dialog.showDialog();
                            // 问的提交
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dataList.add(new BasicNameValuePair("auid", auid));
                                    // 提问
                                    dataList.add(new BasicNameValuePair("content", contentStr));
                                    resstr = HttpUtil.restHttpPost(Constants.moUrl + "/ask/index/question", dataList);
                                    handler.sendEmptyMessage(0);
                                }
                            }).start();
                        }
                    })
                    .show();
        } else {
            dialog.showDialog();
            // 答的提交
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataList.add(new BasicNameValuePair("id", auid));
                    // 回答
                    dataList.add(new BasicNameValuePair("content", contentStr));
                    resstr = HttpUtil.restHttpPost(Constants.moUrl + "/ask/index/answer", dataList);
                    handler.sendEmptyMessage(0);
                }
            }).start();
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
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        dialog.dismissDlog();
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        Constants.qaIsrn = true;
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    /**
     * 绑定提问或者回答输入字数事件
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int number = 200 - s.length();
            if (number >= 0) {
                if (number == 200) {
                    inputNumberWords.setText("你可以输入" + number + "字");
                } else {
                    inputNumberWords.setText("你还可以输入" + number + "字");
                }
                inputNumberWords.setTextColor(inputNumberWords.getResources().getColor(R.color.color_999999));
            } else {
                inputNumberWords.setTextColor(inputNumberWords.getResources().getColor(R.color.red));
                inputNumberWords.setText("已经超出" + Math.abs(number) + "字");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

}
