package com.example.sjqcjstock.fragment.Tomlive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.Tomlive.SendTomliveActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.tomlive.tomliveChatAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 直播显示页面
 * Created by Administrator on 2017/1/10.
 */
public class FragmentTomlive extends Fragment{

    // 问房主字体
    private TextView quizTv;
    // 父级页面用于回掉
    private DirectBroadcastingRoomActivity dbra;
    //定义List集合容器
    private tomliveChatAdapter adapter;
    //数据集合
    private ArrayList<HashMap<String, String>> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int page = 1;
    // 播主的uid
    private String uid;
    // 房间Id
    private String roomId;
    // 接口返回的数据
    private String resstr;
    // 最后的消息id
    private String lastId;
    // 最后的消息id1
    private String lastId1;
    // 定时器
    private Timer timer;
    // 是否是付费直播
    private String type;

    public FragmentTomlive(){}
    public FragmentTomlive(DirectBroadcastingRoomActivity dbra,String roomId,String uid){
        this.dbra = dbra;
        this.roomId = roomId;
        this.uid = uid;
    }

    /**
     * 设置是否是付费订阅
      * @param type
     */
    public void setType(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomlive, container, false);
        findView(view);
        getData();
        // 每隔30秒刷新数据
        if (timer == null) {
            timer = new Timer();
            // 开定时器获取数据
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    getData1();
                }
            };
            timer.schedule(task, 30000, 30000); // 30s后执行task,经过30s再次执行
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isTomlive){
            getData1();
            // 如果是刷新就回到顶部
            listView.smoothScrollToPosition(0);
            Constants.isTomlive = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            // 关闭掉定时器
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        listData = new ArrayList<HashMap<String, String>>();
        quizTv = (TextView) view.findViewById(R.id.quiz_tv);
        listData = new ArrayList<HashMap<String, String>>();
        adapter = new tomliveChatAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.jd_list_view);
        listView.setAdapter(adapter);

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下拉刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                lastId1 = "";
                //清空列表重载数据
                getData1();
            }
            // 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                getData();
            }
        });
        view.findViewById(R.id.quiz_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick3()){
                    return;
                }
                if (Constants.staticmyuidstr.equals(uid)){
                    Intent intent = new Intent(getActivity(), SendTomliveActivity.class);
                    intent.putExtra("roomId",roomId);
                    intent.putExtra("type",type);
                    startActivity(intent);
                }else{
                    // 跳转选项卡显示输入框
                    dbra.switchChat();
                }
            }
        });
        if (Constants.staticmyuidstr.equals(uid)){
            // 当前用户为房主
            quizTv.setText("发送直播文");
        }
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        // 获取直播间历史信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lastId == null || "".equals(lastId)){
                    resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live"+"&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&room_id="+roomId+"&p="+page);
                }else{
                    resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live"+"&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&room_id="+roomId+"&p="+page+"&last_id="+lastId);
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 获取网络数据
     */
    private void getData1() {
        // 获取直播间最新信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lastId1 == null || "".equals(lastId1)){
                    resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live/index/new"+"&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&room_id="+roomId);
                }else{
                    resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live/index/new"+"&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&room_id="+roomId+"&last_id="+lastId1);
                }
                handler.sendEmptyMessage(1);
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
                        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        lastId = jsonObject.getString("last_id");
                        JSONArray jsonArray = JSON.parseArray(jsonObject.getString("data"));
                        HashMap<String, String> maps;
                        String contentType = "";
                        String content = "";
                        for (int i=0;i<jsonArray.size();i++){
                            maps = new HashMap<String, String>();
                            contentType = jsonArray.getJSONObject(i).getString("content_type");
                            content = jsonArray.getJSONObject(i).getString("content");
                            if ("3".equals(contentType)){
                                content = "<img src=\""+content+"\"/>";
                            }
                            if (lastId1 == null){
                                lastId1 = jsonArray.getJSONObject(i).getString("id");
                            }

                            // 这个IOS 新发布版本后不要的
//                            content = content.replace("<div class=\"live-msg\">","");
//                            content = content.replace("</div>","");

                            maps.put("content",content);
                            maps.put("time", Utils.InterceptDate(jsonArray.getJSONObject(i).getString("time")));
                            list.add(maps);
                        }
                        if (list.size()>0){
                            listData.addAll(listData.size(),list);
                            adapter.setlistData(listData);
                        }
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        lastId1 = jsonObject.getString("last_id");
                        JSONArray jsonArray = JSON.parseArray(jsonObject.getString("data"));
                        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> maps;
                        for (int i=0;i<jsonArray.size();i++){
                            maps = new HashMap<String, String>();
                            maps.put("content", jsonArray.getJSONObject(i).getString("content"));
                            maps.put("time", Utils.InterceptDate(jsonArray.getJSONObject(i).getString("time")));
                            list.add(maps);
                        }
                        if (list.size()>0){
                            listData.addAll(0,list);
                            adapter.setlistData(listData);
                        }
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}