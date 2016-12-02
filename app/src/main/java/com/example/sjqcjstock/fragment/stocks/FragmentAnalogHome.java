package com.example.sjqcjstock.fragment.stocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.advertUrlActivity;
import com.example.sjqcjstock.Activity.stocks.ExpertListsActivity;
import com.example.sjqcjstock.Activity.stocks.MyDealAccountActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.SimulationGameActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.ADInfo;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.CycleViewPager;
import com.example.sjqcjstock.view.SoListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟炒股首页
 * Created by Administrator on 2016/8/16.
 */
public class FragmentAnalogHome extends Fragment {

    // 广告滚动的加载
    private CycleViewPager cycleViewPager;
    // 获取广告的接口的类
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    // 图片的加载
    private List<ImageView> views = new ArrayList<ImageView>();
    // 股票的List
    private SoListView listView;
    // 行情List的Adapter
    private DynamicExpertAdapter listAdapter;
    // 滚动控件
    private MyScrollView myScrollView;
    // 需要加载的牛人动态数据
    private ArrayList<GeniusEntity> geniusList;
    // 网络请求提示
    private ProgressDialog dialog;
    // 牛人动态返回的数据
    private String resstr;
    // 缓存用的类
    private ACache mCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_analog_home, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        findView(view);
        initData();
        return view;
    }

    /**
     * 控件的绑定
     */
    private void findView(View view) {
        if (cycleViewPager == null) {
            cycleViewPager = new CycleViewPager();
            this.getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_picture_imgs, cycleViewPager).commit();
        }

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();

        myScrollView = (MyScrollView) view.findViewById(R.id.myScrollView);
        listAdapter = new DynamicExpertAdapter(getActivity());
        listView = (SoListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("code", geniusList.get(position).getStock());
                inten.putExtra("name", geniusList.get(position).getStock_name());
                inten.setClass(getActivity(), SharesDetailedActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 常胜牛人
         */
        view.findViewById(R.id.csnr_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("type", 0);
                inten.setClass(getActivity(), ExpertListsActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 人气牛人
         */
        view.findViewById(R.id.rqnr_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent inten = new Intent();
//                inten.putExtra("type", 1);
//                inten.setClass(getActivity(), ExpertListsActivity.class);
//                startActivity(inten);
                Toast.makeText(getActivity(), "功能完善中，敬请期待！", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 总收益榜
         */
        view.findViewById(R.id.zsyb_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("type", 2);
                inten.setClass(getActivity(), ExpertListsActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 股票牛人
         */
        view.findViewById(R.id.gpnr_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("type", 3);
                inten.setClass(getActivity(), ExpertListsActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 模拟比赛
         */
        view.findViewById(R.id.mnbs_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SimulationGameActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 模拟交易
         */
        view.findViewById(R.id.mnjy_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyDealAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 数据的加载
     */
    private void initData() {
        // 开线程获取广告图片
        SendImageLoder taskSl = new SendImageLoder();
        taskSl.execute(new TaskParams(Constants.globalSwf));
        String data = mCache.getAsString("orders");
        if (data != null){
            geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(data,GeniusEntity.class);
            if (geniusList != null && geniusList.size()>0) {
                listAdapter.setlistData(geniusList);
                dialog.dismiss();
            }
        }
        // 开线程获牛人动态数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/orders");
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 广告图片的一些加载适配
     */
    private void setImageLoader() {
        // 将最后一个ImageView添加进来
        views.add(ImageUtil.getImageView(this.getActivity(), infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ImageUtil.getImageView(this.getActivity(), infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ImageUtil.getImageView(this.getActivity(), infos.get(0).getUrl()));
        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        // 设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        // 设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private class SendImageLoder extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                dialog.dismiss();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = null;
                    String status = jsonObject.getString("status");
                    if ("1".equals(status)) {
                        jsonArray = jsonObject.getJSONArray("data");
                    }
                    if (jsonArray != null && jsonArray.length() > 0) {
                        ADInfo info = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = (JSONObject) jsonArray.get(i);
                            info = new ADInfo();
                            info.setUrl(jsonObject.getString("pic"));
                            info.setContent(jsonObject.getString("url"));
                            infos.add(info);
                        }
                    }
                } catch (JSONException e) {
                    // mh 应该是要去找缓存的
                    e.printStackTrace();
                    return;
                }
                if (infos.size() > 0) {
                    setImageLoader();
                }
            }
        }
    }

    /**
     * 点击广告图片的单机事件
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Intent intent = new Intent(getActivity(), advertUrlActivity.class);
                intent.putExtra("url", info.getContent());
                startActivity(intent);
            }
        }
    };

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
                            CustomToast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        }
                        String data = jsonObject.getString("data");
                        // 牛人动态做缓存
                        mCache.put("orders", data);
                        geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"),GeniusEntity.class);
                        listAdapter.setlistData(geniusList);
                        ViewUtil.setListViewHeightBasedOnChildren(listView);
                        // 滚动到顶部
                        myScrollView.scrollTo(0, 0);
                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    break;
            }
        }
    };

}
