package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.Article.CommentActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;

/**
 * 消息的主体页面
 */
public class ActivityMessage extends Activity implements OnClickListener {
    // 收到的评论
    private LinearLayout pickrecivecomment1;
    // 发出的评论
    private LinearLayout picksendcomment1;
    // 打赏列表消息
    private LinearLayout rewardMessage;
    // 系统消息
    private LinearLayout systemMessage;
    // 提到我的
    private LinearLayout pickpersonalnews1;
    // 我的私信
    private LinearLayout pickatmecomment1;
    // 未读消息条数
    private TextView messageCountTv1, messageCountTv2, messageCountTv3, messageCountTv4, messageCountTv5, messageCountTv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_message3);
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pickrecivecomment1 = (LinearLayout) findViewById(R.id.pickrecivecomment1);
        picksendcomment1 = (LinearLayout) findViewById(R.id.picksendcomment1);
        pickpersonalnews1 = (LinearLayout) findViewById(R.id.pickpersonalnews1);
        systemMessage = (LinearLayout) findViewById(R.id.system_message);
        rewardMessage = (LinearLayout) findViewById(R.id.reward_message);
        pickatmecomment1 = (LinearLayout) findViewById(R.id.pickatmecomment1);
        messageCountTv1 = (TextView) findViewById(R.id.message1_count_tv);
        messageCountTv2 = (TextView) findViewById(R.id.message2_count_tv);
        messageCountTv3 = (TextView) findViewById(R.id.message3_count_tv);
        messageCountTv4 = (TextView) findViewById(R.id.message4_count_tv);
        messageCountTv5 = (TextView) findViewById(R.id.message5_count_tv);
        messageCountTv6 = (TextView) findViewById(R.id.message6_count_tv);
        // 绑定点击事件
        pickrecivecomment1.setOnClickListener(this);
        picksendcomment1.setOnClickListener(this);
        rewardMessage.setOnClickListener(this);
        systemMessage.setOnClickListener(this);
        pickpersonalnews1.setOnClickListener(this);
        pickatmecomment1.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.unreadCountInfo != null) {
            int comment = Constants.unreadCountInfo.getData().getUnread_comment();
            String commentStr = comment + "";
            if (comment > 99) {
                commentStr = "99+";
            }
            messageCountTv1.setText(commentStr);
            if (commentStr.equals("0")) {
                messageCountTv1.setVisibility(View.GONE);
            } else {
                messageCountTv1.setVisibility(View.VISIBLE);
            }
            int atme = Constants.unreadCountInfo.getData().getUnread_atme();
            String atmeStr = atme + "";
            if (atme > 99) {
                atmeStr = "99+";
            }
            messageCountTv3.setText(atmeStr);
            if (atmeStr.equals("0")) {
                messageCountTv3.setVisibility(View.GONE);
            } else {
                messageCountTv3.setVisibility(View.VISIBLE);
            }
            // 私信消息
            int message = Constants.unreadCountInfo.getData().getUnread_message();
            String messageStr = message + "";
            if (message > 99) {
                messageStr = "99+";
            }
            messageCountTv4.setText(messageStr);
            if (messageStr.equals("0")) {
                messageCountTv4.setVisibility(View.GONE);
            } else {
                messageCountTv4.setVisibility(View.VISIBLE);
            }
            // 打赏消息
            int notify = Integer.valueOf(Constants.unreadCountInfo.getData().getUnread_notify());
            String notifyStr = notify + "";
            if (notify > 99) {
                notifyStr = "99+";
            }
            messageCountTv5.setText(notifyStr);
            if (notifyStr.equals("0")) {
                messageCountTv5.setVisibility(View.GONE);
            } else {
                messageCountTv5.setVisibility(View.VISIBLE);
            }
            // 系统消息
            int sysm = Integer.valueOf(Constants.unreadCountInfo.getData().getSys_message());
            String sysmStr = sysm + "";
            if (sysm > 99) {
                sysmStr = "99+";
            }
            messageCountTv6.setText(sysmStr);
            if (sysmStr.equals("0")) {
                messageCountTv6.setVisibility(View.GONE);
            } else {
                messageCountTv6.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.pickatmecomment1:
                intent = new Intent(this, atmeActivity.class);
                Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getUnread_atme());
                Constants.unreadCountInfo.getData().setUnread_atme(0);
                break;
            case R.id.pickpersonalnews1:
                intent = new Intent(this, personalnewsActivity.class);
                Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getUnread_message());
                Constants.unreadCountInfo.getData().setUnread_message(0);
                break;
            case R.id.pickrecivecomment1:
                // 收到的评论
                intent = new Intent(this, CommentActivity.class);
                intent.putExtra("type","receive");
                Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getUnread_comment());
                Constants.unreadCountInfo.getData().setUnread_comment(0);
                break;
            case R.id.picksendcomment1:
                //  发出的评论
                intent = new Intent(this, CommentActivity.class);
                intent.putExtra("type","send");
                break;
            case R.id.reward_message:
                intent = new Intent(this, rewardMessageActivity.class);
                Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Integer.valueOf(Constants.unreadCountInfo.getData().getUnread_notify()));
                Constants.unreadCountInfo.getData().setUnread_notify(0);
                break;
            case R.id.system_message:
                intent = new Intent(this, systemMessageListActivity.class);
                Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getSys_message());
                Constants.unreadCountInfo.getData().setSys_message(0);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
