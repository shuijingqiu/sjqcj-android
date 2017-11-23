package com.example.sjqcjstock.fragment.Tomlive;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.tomlive.chatAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Tomlive.QuestionEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * 聊天显示页面
 * Created by Administrator on 2017/1/10.
 */
public class FragmentChat extends Fragment {

    //定义List集合容器
    private chatAdapter adapter;
    //数据集合
    private ArrayList<QuestionEntity> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 接口返回的数据
    private String resstr;
    //访问页数控制
    private int page = 1;
    // 提问发送的显示框
    private LinearLayout quizSendLl;
    // 免费问的接口
    private LinearLayout quizll;
    // 输入发送的内容文字
    private EditText quizEt;
    // 房间id
    private String roomId;
    // 1直播消息 2提问 3回答
    private String type = "2";
    // 提问的id
    private String parentId;
    // 房主id
    private String uid;
    // 定时器
    private Timer timer;

    public FragmentChat(){}
    public FragmentChat(String roomId,String uid) {
        this.roomId = roomId;
        this.uid = uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        findView(view);
        getData();
//        // 每隔30秒刷新数据
//        if (timer == null) {
//            timer = new Timer();
//            // 开定时器获取数据
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    getData();
//                }
//            };
//            timer.schedule(task, 0, 30000); // 0s后执行task,经过30s再次执行
//        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (timer!=null) {
//            // 关闭掉定时器
//            timer.cancel();
//            timer.purge();
//            timer = null;
//        }
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        listData = new ArrayList<QuestionEntity>();
        adapter = new chatAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.jd_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isrn = listData.get(position).getAnswer().length()<5;
                if (Constants.staticmyuidstr.equals(uid) && isrn){
                    // 只有房主才能回复消息
                    // 赋值消息ID
                    parentId = listData.get(position).getId();
                    // 回答问题的类型
                    type = "3";
                    showChat();
                    quizEt.setHint("回复："+listData.get(position).getUsername());
                }
            }
        });
        quizEt = (EditText) view.findViewById(R.id.quiz_et);
        quizSendLl = (LinearLayout) view.findViewById(R.id.quiz_send_ll);
        quizSendLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送输入的文字
                quizSendLl.setVisibility(View.GONE);
                quizll.setVisibility(View.VISIBLE);
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(quizEt.getWindowToken(), 0);
            }
        });
        quizll = (LinearLayout) view.findViewById(R.id.quiz_ll);
        if (Constants.staticmyuidstr.equals(uid)){
            quizSendLl.setVisibility(View.GONE);
            quizll.setVisibility(View.GONE);
        }else{
            // 发送输入的文字
            quizSendLl.setVisibility(View.GONE);
            quizll.setVisibility(View.VISIBLE);
        }

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                page = 1;
                getData();
            }
            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                getData();
            }
        });

        view.findViewById(R.id.send_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.staticmyuidstr.equals(uid)){
                    // 发送输入的文字
                    quizSendLl.setVisibility(View.GONE);
                    quizll.setVisibility(View.VISIBLE);
                }else{
                    quizSendLl.setVisibility(View.GONE);
                    quizll.setVisibility(View.GONE);
                }

                // 发送的消息内容
                final String content = quizEt.getText().toString().trim();
                if ("".equals(content.trim())){
                    return;
                }
                // 发送聊天的消息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("room_id", roomId));
                        dataList.add(new BasicNameValuePair("content", content));
                        dataList.add(new BasicNameValuePair("type", type));
                        if ("3".equals(type)){
                            dataList.add(new BasicNameValuePair("parent_id", parentId));
                        }
                        resstr = HttpUtil.restHttpPost(Constants.moUrl+"/live/index/send",dataList);
                        handler.sendEmptyMessage(1);
                    }
                }).start();
                quizEt.clearFocus();
                // 强制关闭软键盘
                InputMethodManager imm = (InputMethodManager) quizEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        quizll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接提问的类型
                type = "2";
                showChat();
            }
        });
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        // 获取直播间历史信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live/index/question"+"&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&room_id="+roomId+"&p="+page);
                handler.sendEmptyMessage(0);
            }
        }).start();
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
                        if ("failed".equals(jsonObject.getString("status"))){
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<QuestionEntity> list = (ArrayList<QuestionEntity>) JSON.parseArray(jsonObject.getString("data"),QuestionEntity.class);
                        if (page == 1){
                            if (list != null){
                                listData = list;
                            }
                        }else{
                            listData.addAll(list);
                        }
                        adapter.setlistData(listData);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        quizEt.setText("");
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getActivity(),jsonObject.getString("data"),Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Toast.makeText(getActivity(),jsonObject.getString("data"),Toast.LENGTH_SHORT).show();
                            // 重新请求数据
                            getData();
                            // 如果是刷新就回到顶部
                            listView.smoothScrollToPosition(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    };

    /**
     * 显示输入聊天的框
     */
    public void showChat(){
        quizSendLl.setVisibility(View.VISIBLE);
        quizll.setVisibility(View.GONE);
        quizEt.requestFocus();
        // 强制开启软键盘
        InputMethodManager imm = (InputMethodManager) quizEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 切换选项卡时重新刷新页面
     */
    public void onHiddenChanged() {
        // 切换选项卡的时候自动刷新
         page = 1;
         getData();
    }

}