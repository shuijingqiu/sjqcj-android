package com.example.sjqcjstock.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.gwzbmatchAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.StarcraftEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 股王争霸赛每日榜单Fragemtn
 * Created by Administrator on 2016/6/13.
 */
public class FragmentGwzbsList extends Fragment {

    // 网络请求提示
    private CustomProgress dialog;
    // 定义List集合容器
    private gwzbmatchAdapter adapter;
    // 定义于数据库同步的字段集合
    private ArrayList<StarcraftEntity> listessenceData;
    private ListView listView;
    // 1股王争霸 0股王比赛
    private String type;
    // 接口返回数据
    private String jsonStr;

    public FragmentGwzbsList(){}
    public FragmentGwzbsList(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gwzb_bd, container, false);
        findView(view);
        geneItems();
        return view;
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        dialog = new CustomProgress(getActivity());
        if ("0".equals(type)){
            dialog.showDialog();
        }
        adapter = new gwzbmatchAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.gwzb_bd_list);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                try {
//                    Intent intent = new Intent(getActivity(), forumnotedetailActivity.class);
//                    intent.putExtra("weibo_id", (String) listessenceData.get(arg2).get("weibo_idstr"));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

//    /**
//     * 为页面赋值
//     *
//     * @param list
//     */
//    public void setData(ArrayList<HashMap<String, Object>> list) {
//        listessenceData = list;
//        gwzbmatchAdapter.setlistData(listessenceData);
//    }


    private void geneItems() {
        // 获取股王比赛
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/starcraft/list?type=" + type);
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        dialog.dismissDlog();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listessenceData = (ArrayList<StarcraftEntity>) JSON.parseArray(jsonObject.getString("data"), StarcraftEntity.class);
                        adapter.setlistData(listessenceData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
