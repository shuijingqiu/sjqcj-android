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
import com.example.sjqcjstock.adapter.totalrankingfamousAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.IncomeStatementEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 总收益榜精英组
 * Created by Administrator on 2016/6/14.
 */
public class FragmentTotalELite extends Fragment{

    // 定义List集合容器
    private totalrankingfamousAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<IncomeStatementEntity> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 展示类容 1 为总收益榜 2为长胜牛人
    private String type = "1";
    // 接口传入类型 1：名人总收益榜 2：精英总收益榜 3：名人周均收益 4：精英周均收益
    private String typeValue = "2";
    // 接口返回数据
    private String jsonStr;
    // 网络请求提示
    private CustomProgress dialog;

    public  FragmentTotalELite(String type){
        this.type = type;
        if ("2".equals(type)){
            typeValue = "4";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_jy, container, false);
        findView(view);
        getData();
        return view;
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        dialog = new CustomProgress(getActivity());
        dialog.showDialog();
        adapter = new totalrankingfamousAdapter(getActivity(),type);
        listView = (ListView) view.findViewById(
                R.id.jy_list_view);
        listView.setAdapter(adapter);
        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (listData != null) {
                    //清空列表重载数据
                    listData.clear();
                }
                current = 1;
                getData();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                getData();
            }
        });
    }

    private void getData() {
//        new SendInfoTaskfamousranking()
//                .execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AppTopBallot&type="+typeValue+"&p=" + current
//                        )
//                );
        // 行榜信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl+"/api/ballot/top?type="+typeValue+"&p="+current);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

//    private class SendInfoTaskfamousranking extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                List<Map<String, Object>> lists = null;
//                String statusstr = null;
//                List<Map<String, Object>> statusstrlists = null;
//                String status2str = null;
//                List<Map<String, Object>> statusstr2lists = null;
//                String ballot_jifenstr;
//                String weeklystr;
//                String unamestr;
//                String uidstr;
//                // 解析json字符串获得List<Map<String,Object>>
//                if (lists == null) {
//                    lists = JsonTools.listKeyMaps(result);
//                }
//                for (Map<String, Object> map : lists) {
//                    if (statusstr == null) {
//                        statusstr = map.get("data")+"";
//                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr
//                                + "]");
//                    }
//                    for (Map<String, Object> statusstrmap : statusstrlists) {
//                        if (status2str == null) {
//                            status2str = statusstrmap.get("data")+"";
//                            statusstr2lists = JsonTools.listKeyMaps(status2str);
//                        }
//                        for (Map<String, Object> statusstrlists2map : statusstr2lists) {
//                            uidstr = statusstrlists2map.get("uid")+"";
//                            if ("2".equals(type)){
//                                ballot_jifenstr = statusstrlists2map.get(
//                                        "avg_jefen").toString();
//                            }else{
//                                ballot_jifenstr = statusstrlists2map.get(
//                                        "ballot_jifen").toString();
//                            }
//                            weeklystr = statusstrlists2map.get("weekly")+"";
//                            unamestr = statusstrlists2map.get("uname")+"";
//                            // 取小数点后两位
//                            java.text.DecimalFormat df = new java.text.DecimalFormat(
//                                    "#.##");
//                            HashMap<String, Object> map2 = new HashMap<String, Object>();
//                            map2.put("uname", unamestr);
//                            map2.put("uid", uidstr);
//                            map2.put(
//                                    "ballot_jifen",
//                                    df.format(Double
//                                            .parseDouble(ballot_jifenstr))
//                                            + "%");
//                            map2.put("weekly", weeklystr);
//                            map2.put("uidimg", Md5Util.getuidstrMd5(Md5Util
//                                    .getMd5(uidstr)));
//                            listData.add(map2);
//                        }
//                    }
//                }
//                adapter.setlistData(listData);
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
//    }

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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<IncomeStatementEntity> incomeStatementEntitys = (ArrayList<IncomeStatementEntity>) JSON.parseArray(jsonObject.getString("data"),IncomeStatementEntity.class);
                        if (1 == current){
                            listData = incomeStatementEntitys;
                        }else{
                            listData.addAll(incomeStatementEntitys);
                        }
                        adapter.setlistData(listData);
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