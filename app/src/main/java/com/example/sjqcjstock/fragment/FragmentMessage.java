package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.atmeActivity;
import com.example.sjqcjstock.Activity.personalnewsActivity;
import com.example.sjqcjstock.Activity.recivecommentActivity;
import com.example.sjqcjstock.Activity.rewardMessageActivity;
import com.example.sjqcjstock.Activity.sendedcommentActivity;
import com.example.sjqcjstock.Activity.systemMessageListActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;

/**
 * 消息页  以后不要的
 */
public class FragmentMessage extends Fragment implements View.OnClickListener {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message3, null);

        initView(view);
        showMessage();
        return view;
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        pickrecivecomment1 = (LinearLayout) view.findViewById(R.id.pickrecivecomment1);
        picksendcomment1 = (LinearLayout) view.findViewById(R.id.picksendcomment1);
        pickpersonalnews1 = (LinearLayout) view.findViewById(R.id.pickpersonalnews1);
        systemMessage = (LinearLayout) view.findViewById(R.id.system_message);
        rewardMessage = (LinearLayout) view.findViewById(R.id.reward_message);
        pickatmecomment1 = (LinearLayout) view.findViewById(R.id.pickatmecomment1);
        messageCountTv1 = (TextView) view.findViewById(R.id.message1_count_tv);
        messageCountTv2 = (TextView) view.findViewById(R.id.message2_count_tv);
        messageCountTv3 = (TextView) view.findViewById(R.id.message3_count_tv);
        messageCountTv4 = (TextView) view.findViewById(R.id.message4_count_tv);
        messageCountTv5 = (TextView) view.findViewById(R.id.message5_count_tv);
        messageCountTv6 = (TextView) view.findViewById(R.id.message6_count_tv);
        // 绑定点击事件
        pickrecivecomment1.setOnClickListener(this);
        picksendcomment1.setOnClickListener(this);
        rewardMessage.setOnClickListener(this);
        systemMessage.setOnClickListener(this);
        pickpersonalnews1.setOnClickListener(this);
        pickatmecomment1.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showMessage();
    }

    // 显示消息条数的方法
    public void showMessage() {
        if (Constants.unreadCountInfo != null && Constants.unreadCountInfo.getStatus().equals("1")) {
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
            // 发出的评论 没有的
//			String fc = Constants.unreadCountInfo.getUnreadInfo().getUnread_fc();
//			messageCountTv2.setText(fc);
//			if(fc == "0"){
//				messageCountTv2.setVisibility(View.VISIBLE);
//			}else{
//				messageCountTv2.setVisibility(View.GONE);
//			}
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
                intent = new Intent(getActivity(), atmeActivity.class);
                if (Constants.unreadCountInfo != null) {
                    Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getUnread_atme());
                    Constants.unreadCountInfo.getData().setUnread_atme(0);
                }
                break;
            case R.id.pickpersonalnews1:
                intent = new Intent(getActivity(), personalnewsActivity.class);
                if (Constants.unreadCountInfo != null) {
                    Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getUnread_message());
                    Constants.unreadCountInfo.getData().setUnread_message(0);
                }
                break;
            case R.id.pickrecivecomment1:
                // 收到的评论
                intent = new Intent(getActivity(), recivecommentActivity.class);

                if (Constants.unreadCountInfo != null) {
                    Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getUnread_comment());
                    Constants.unreadCountInfo.getData().setUnread_comment(0);
                }
                break;
            case R.id.picksendcomment1:
                //  发出的评论
                intent = new Intent(getActivity(), sendedcommentActivity.class);
                break;
            case R.id.reward_message:
                intent = new Intent(getActivity(), rewardMessageActivity.class);
                if (Constants.unreadCountInfo != null) {
                    Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Integer.valueOf(Constants.unreadCountInfo.getData().getUnread_notify()));
                    Constants.unreadCountInfo.getData().setUnread_notify("0");
                }
                break;
            case R.id.system_message:
                intent = new Intent(getActivity(), systemMessageListActivity.class);
                if (Constants.unreadCountInfo != null) {
                    Constants.unreadCountInfo.getData().setUnread_total(Constants.unreadCountInfo.getData().getUnread_total() - Constants.unreadCountInfo.getData().getSys_message());
                    Constants.unreadCountInfo.getData().setSys_message(0);
                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}

