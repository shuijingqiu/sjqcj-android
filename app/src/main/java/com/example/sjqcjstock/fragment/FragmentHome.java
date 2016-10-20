package com.example.sjqcjstock.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.EssenceListActivty;
import com.example.sjqcjstock.Activity.advertUrlActivity;
import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.Activity.gwzhmatchActivity;
import com.example.sjqcjstock.Activity.hotstocklistActivity;
import com.example.sjqcjstock.Activity.selectstockmatchActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.supermanlistActivity;
import com.example.sjqcjstock.Activity.todaybullsrankingActivity;
import com.example.sjqcjstock.Activity.weekbullsrankingActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.essenceAdapter;
import com.example.sjqcjstock.adapter.supermanActivityAdapter;
import com.example.sjqcjstock.adapter.todayuprankingAdapter;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.ADInfo;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.userdefined.RoundImageView;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.CycleViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    // 获取控件
    private LinearLayout pickessence;
    private LinearLayout pickhotstock;
    private LinearLayout picksuperman;
    private RoundImageView pickgwzbmatch1;
    private RelativeLayout rl_refresh_layout;
    private RoundImageView pickselectstockmatch1;
    private RoundImageView picktosesencelist1;
    private RoundImageView picktosupermanlist1;
    private LinearLayout picktodayupranking1;
    private LinearLayout pickthisweekupranking1;
    //    private Button totop;
//    private Button tobuttom;
    private MyScrollView myScrollView;
    // 定义List集合容器
    private essenceAdapter essencelistAdapter;
    private supermanActivityAdapter supermanAdapter;
    private todayuprankingAdapter todayuprankingAdapter;
    private com.example.sjqcjstock.adapter.thisweekuprankingAdapter thisweekuprankingAdapter;
    private ArrayList<HashMap<String, String>> listessenceData;
    private ArrayList<HashMap<String, String>> listsupermanData;
    private ArrayList<HashMap<String, String>> listtodayuprankingData;
    private ArrayList<HashMap<String, String>> listthisweekuprankingData;
    // 精华集合
    private ListView essencelistview;
    // 牛人集合
    private ListView supermanlistview;
    // 当日牛股榜集合
    private ListView todayuprankinglist;
    // 当周牛股榜集合
    private ListView thisweekupranking;
    // 广告滚动的加载
    private CycleViewPager cycleViewPager;
    // 获取广告的接口的类
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    // 图片的加载
    private List<ImageView> views = new ArrayList<ImageView>();
    // 网络请求提示
    private ProgressDialog dialog;
    // 缓存用的类
    private ACache mCache;
    // 缓存精华信息用
    private ArrayList<HashMap<String, String>> appindexList;
    // 缓存当日牛股用
    private ArrayList<HashMap<String, String>> appTodayBallotTodayList;
    // 缓存当周牛股用
    private ArrayList<HashMap<String, String>> appTodayBallotWeekList;
    // 缓存牛人推荐
    private ArrayList<HashMap<String, String>> aPPUserSortList;
    // 缓存滚动的图片用
    private String globalSwfStr = "";

    public void test(ArrayList<HashMap<String, String>> listData) {
        listsupermanData = listData;
        supermanAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        initView(view);
        selectRecharge();
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
        return view;
    }

    // 获取用户财富设置水晶币个数
    private class SendInfoTaskmywealth extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            } else {
                String count = "0";
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    // 获取水晶币
                    count = jsonObj.getJSONObject("data").getJSONObject("credit").getJSONObject("shuijingbi").getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Constants.shuijinbiCount = count;
            }
        }
    }

    private class SendInfoTaskmyuserinform extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                try {
                    String avatar_middle = new JSONObject(result).getJSONObject("data").getString("avatar_middle");
                    String userGroup = new JSONObject(result).getJSONObject("data").getString("user_group");
                    if (userGroup.length() > 10) {
                        userGroup = userGroup.substring(userGroup.lastIndexOf("{"));
                        userGroup = userGroup.substring(0, userGroup.indexOf("}") + 1);
                        Constants.userType = new JSONObject(userGroup).getString("user_group_id");
                    } else {
                        Constants.userType = "1";
                    }
                    Constants.headImg = avatar_middle;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void initView(View view) {

        if (cycleViewPager == null) {
            cycleViewPager = new CycleViewPager();
            this.getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_picture, cycleViewPager).commit();
        }
        dialog = new ProgressDialog(getActivity());
        // dialog.setTitle("提示信息");
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        // 加载控件
        rl_refresh_layout = (RelativeLayout) view.findViewById(R.id.rl_refresh_layout);
        pickessence = (LinearLayout) view.findViewById(R.id.pickessence);
        pickhotstock = (LinearLayout) view.findViewById(R.id.pickhotstock);
        picksuperman = (LinearLayout) view.findViewById(R.id.picksuperman);
        pickgwzbmatch1 = (RoundImageView) view.findViewById(R.id.pickgwzbmatch1);
        pickselectstockmatch1 = (RoundImageView) view.findViewById(R.id.pickselectstockmatch1);
        picktosesencelist1 = (RoundImageView) view.findViewById(R.id.picktosesencelist1);
        picktosupermanlist1 = (RoundImageView) view.findViewById(R.id.picktosupermanlist1);
        picktodayupranking1 = (LinearLayout) view.findViewById(R.id.picktodayupranking1);
        pickthisweekupranking1 = (LinearLayout) view.findViewById(R.id.pickthisweekupranking1);
        myScrollView = (MyScrollView) view.findViewById(R.id.myScrollView);

        pickessence.setOnClickListener(new pickessence_listener());
        pickhotstock.setOnClickListener(new pickhotstock_listener());
        picksuperman.setOnClickListener(new picksuperman_listener());
        pickgwzbmatch1.setOnClickListener(new pickgwzbmatch1_listener());
        pickselectstockmatch1.setOnClickListener(new pickselectstockmatch1_listener());
        picktosesencelist1.setOnClickListener(new picktosesencelist1_listener());
        picktosupermanlist1.setOnClickListener(new picktosupermanlist1_listener());
        picktodayupranking1.setOnClickListener(new picktodayupranking1_listener());
        pickthisweekupranking1.setOnClickListener(new pickthisweekupranking1_listener());

        /** 精华集合 */
        essencelistview = (ListView) view.findViewById(R.id.essencelist);

        // 存储数据的数组列表
        listessenceData = new ArrayList<HashMap<String, String>>();
        // 为ListView 添加适配器
        essencelistAdapter = new essenceAdapter(getActivity()
                .getApplicationContext());
        essencelistview.setAdapter(essencelistAdapter);
        essencelistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), forumnotedetailActivity.class);
                    intent.putExtra(
                            "weibo_id",
                            (String) listessenceData.get(position).get(
                                    "weibo_idstr"));
                    intent.putExtra("uid",
                            (String) listessenceData.get(position)
                                    .get("uidstr"));
                    // mh 转发的一些信息没有传
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /** 牛人集合 */
        supermanlistview = (ListView) view.findViewById(R.id.supermanlist);

        // 存储数据的数组列表
        listsupermanData = new ArrayList<HashMap<String, String>>();

        // 为ListView 添加适配器

        supermanAdapter = new supermanActivityAdapter(getActivity()
                .getApplicationContext());

        supermanlistview.setAdapter(supermanAdapter);

        supermanlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listsupermanData.get(position).get("uid").toString());
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });

        // 开线程获取水晶币数量（后台加载可以不用显示提示框）
        new SendInfoTaskmywealth().execute(new TaskParams(Constants.appUserMoneyUrl,
                new String[]{"mid", Constants.staticmyuidstr}
        ));
        // 从网络获取用户详细信息(只是为了获取用户头像在发私信的时候用，新增获取用户级别是否是禁言用户)
        new SendInfoTaskmyuserinform().execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=AppUser",
                new String[]{"mid", Constants.staticmyuidstr}
        ));
        // 开线程获取广告图片
        SendImageLoder taskSl = new SendImageLoder();
        taskSl.execute(new TaskParams(Constants.globalSwf));

        // 牛人推荐
        new SendInfoTasksuperman().execute(new TaskParams(
                Constants.Url + "?app=index&mod=Index&act=APPUserSort",
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"p", "1"}
        ));
        /** 当日牛股榜集合 */
        todayuprankinglist = (ListView) view
                .findViewById(R.id.todayuprankinglist);
        // 存储数据的数组列表
        listtodayuprankingData = new ArrayList<HashMap<String, String>>();
        // 为ListView 添加适配器
        todayuprankingAdapter = new todayuprankingAdapter(getActivity()
                .getApplicationContext());
        todayuprankinglist.setAdapter(todayuprankingAdapter);
        todayuprankinglist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            }
        });
        /** 当周牛股榜集合 */
        thisweekupranking = (ListView) view
                .findViewById(R.id.thisweekupranking);

        // 存储数据的数组列表
        listthisweekuprankingData = new ArrayList<HashMap<String, String>>();
        // 为ListView 添加适配器
        thisweekuprankingAdapter = new com.example.sjqcjstock.adapter.thisweekuprankingAdapter(
                getActivity().getApplicationContext(),
                listthisweekuprankingData);
        thisweekupranking.setAdapter(thisweekuprankingAdapter);
        thisweekupranking.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }

        });
        loadCacheData();
        loadData();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadData();
        } else {
            if (appindexList != null && appindexList.size() > 0) {
                // 做缓存
                mCache.put("Appindexx", Utils.getListMapStr(appindexList));
            }
            if (appTodayBallotTodayList != null && appTodayBallotTodayList.size() > 0) {
                // 做缓存
                mCache.put("AppTodayBallotTodayx", Utils.getListMapStr(appTodayBallotTodayList));
            }
            if (appTodayBallotWeekList != null && appTodayBallotWeekList.size() > 0) {
                // 做缓存
                mCache.put("AppTodayBallotWeekx", Utils.getListMapStr(appTodayBallotWeekList));
            }
            if (aPPUserSortList != null && aPPUserSortList.size() > 0) {
                // 做缓存
                mCache.put("APPUserSortx", Utils.getListMapStr(aPPUserSortList));
            }
            // 缓存滚动图片消息
            mCache.put("globalSwf", globalSwfStr);
        }
    }

    /**
     * 加载缓存数据
     */
    private void loadCacheData() {
        String str = mCache.getAsString("Appindexx");
        listessenceData = Utils.getListMap(str);
        essencelistAdapter.setlistData(listessenceData);
        ViewUtil.setListViewHeightBasedOnChildren(essencelistview);

        str = mCache.getAsString("AppTodayBallotTodayx");
        listtodayuprankingData = Utils.getListMap(str);
        todayuprankingAdapter.setlistData(listtodayuprankingData);
        ViewUtil.setListViewHeightBasedOnChildren(todayuprankinglist);

        str = mCache.getAsString("AppTodayBallotWeekx");
        appTodayBallotWeekList = Utils.getListMap(str);
        thisweekuprankingAdapter.setlistData(appTodayBallotWeekList);
        ViewUtil.setListViewHeightBasedOnChildren(thisweekupranking);

        str = mCache.getAsString("APPUserSortx");
        listsupermanData = Utils.getListMap(str);
        supermanAdapter.setlistData(listsupermanData);
        ViewUtil.setListViewHeightBasedOnChildren(supermanlistview);
    }

    /**
     * 加载焦点，当日牛股，当周牛股的数据
     */
    private void loadData() {
        // 焦点
        new SendInfoTasksesence().execute(new TaskParams(
                Constants.Url + "?app=index&mod=Index&act=Appindex",
                new String[]{"position", "2"}
        ));
        //当日牛股
        new SendInfoTasktodayupranking().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=AppTodayBallot&group=today"));

        // 当周牛股
        new SendInfoTaskthisweekupranking().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=AppTodayBallot&group=week"));
    }

    class pickthisweekupranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(),
                    weekbullsrankingActivity.class);
            startActivity(intent);
        }
    }

    class picktodayupranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(),
                    todaybullsrankingActivity.class);
            startActivity(intent);
        }

    }

    class picktosupermanlist1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(),
                    supermanlistActivity.class);
            startActivity(intent);
        }
    }

    class picktosesencelist1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), EssenceListActivty.class);
            startActivity(intent);
        }
    }

    class pickgwzbmatch1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), gwzhmatchActivity.class);
            startActivity(intent);
        }
    }

    class pickselectstockmatch1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(),
                    selectstockmatchActivity.class);
            startActivity(intent);
        }
    }

    private class SendInfoTaskthisweekupranking extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getActivity(), "网络超时或无数据", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                List<Map<String, Object>> lists2 = null;
                String datastr2 = null;
                List<Map<String, Object>> datastrlists2 = null;

                listthisweekuprankingData.clear();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists2 == null) {
                    lists2 = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists2) {
                    if (datastr2 == null) {
                        datastr2 = map.get("data").toString();
                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
                    }
                    if (datastrlists2 == null) {
                        datastrlists2 = new ArrayList<Map<String, Object>>();
                    }
                    int j = 0;
                    for (Map<String, Object> datastrmap : datastrlists2) {
                        j++;
                        // 涨幅
                        String increasestr = datastrmap.get("increase")
                                .toString();
                        // 最新价
                        String currentPricestr = datastrmap.get("currentPrice")
                                .toString();

                        // 股票名
                        String ballot_namestr;
                        if (datastrmap.get("ballot_name") == null) {
                            ballot_namestr = "暂无";
                        } else {
                            ballot_namestr = datastrmap.get("ballot_name")
                                    .toString();
                        }
                        // 用户名
                        String unamestr = datastrmap.get("uname").toString();
                        String uidstr = datastrmap.get("uid").toString();
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2.put("currentPrice", currentPricestr);
                        map2.put("increase", increasestr);
                        map2.put("ballot_name", ballot_namestr);
                        map2.put("uname", unamestr);
                        map2.put("uidimg",
                                Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
                        map2.put("uid", uidstr);
                        map2.put("i", j + "");
                        listthisweekuprankingData.add(map2);
                        if (j == 3) {
                            break;
                        }
                    }

                }
                thisweekuprankingAdapter.setlistData(listthisweekuprankingData);
                ViewUtil.setListViewHeightBasedOnChildren(thisweekupranking);
                appTodayBallotWeekList = (ArrayList<HashMap<String, String>>) listthisweekuprankingData.clone();
            }
            dialog.dismiss();
            rl_refresh_layout.setVisibility(View.GONE);
        }

    }

    private class SendInfoTasktodayupranking extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;
                listtodayuprankingData.clear();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }

                for (Map<String, Object> map : lists) {
                    if (datastr == null) {
                        datastr = map.get("data").toString();

                        datastrlists = JsonTools.listKeyMaps(datastr);
                    }
                    int i = 0;
                    for (Map<String, Object> datastrmap : datastrlists) {
                        i++;
                        // 涨幅
                        String increasestr = datastrmap.get("increase")
                                .toString();
                        // 最新价
                        String currentPricestr = datastrmap.get("currentPrice")
                                .toString();
                        // 股票名
                        String ballot_namestr;
                        if (datastrmap.get("ballot_name") == null) {
                            ballot_namestr = "暂无";
                        } else {
                            ballot_namestr = datastrmap.get("ballot_name")
                                    .toString();
                        }
                        // 用户名
                        String unamestr = datastrmap.get("uname") + "";
                        String uidstr = datastrmap.get("uid") + "";
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2.put("currentPrice", currentPricestr);
                        map2.put("increase", increasestr);
                        map2.put("ballot_name", ballot_namestr);
                        map2.put("uname", unamestr);
                        map2.put("uidimg",
                                Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
                        map2.put("uid", uidstr);
                        map2.put("i", i + "");
                        listtodayuprankingData.add(map2);
                        if (i == 3) {
                            break;
                        }
                    }
                }
                todayuprankingAdapter.setlistData(listtodayuprankingData);
                ViewUtil.setListViewHeightBasedOnChildren(todayuprankinglist);
                appTodayBallotTodayList = (ArrayList<HashMap<String, String>>) listtodayuprankingData.clone();
            }
            dialog.dismiss();
            rl_refresh_layout.setVisibility(View.GONE);
        }
    }

    private class SendInfoTasksuperman extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                dialog.dismiss();
                rl_refresh_layout.setVisibility(View.GONE);
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                listsupermanData.clear();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    if (map.get("data") == null) {
                        break;
                    }
                    String statusstr = map.get("data").toString();
                    List<Map<String, Object>> supermanlists = JsonTools
                            .listKeyMaps(statusstr);
                    for (int i = 0; i < 3; i++) {
                        String introstr = "";
                        if (supermanlists.get(i).get("intro") != null) {
                            introstr = supermanlists.get(i).get("intro")
                                    .toString();
                        }
                        String uidstr = supermanlists.get(i).get("uid")
                                .toString();
                        String unamestr = supermanlists.get(i).get("uname")
                                .toString();

                        String save_pathstr = supermanlists.get(i)
                                .get("save_path").toString();
                        String followstr;
                        if (supermanlists.get(i).get("follow") == null) {
                            followstr = "";
                        } else {
                            followstr = supermanlists.get(i).get("follow")
                                    .toString();
                        }
                        String followingstr = "";
                        String followerstr = "";
                        List<Map<String, Object>> followingstrlists = JsonTools
                                .listKeyMaps("[" + followstr + "]");
                        for (Map<String, Object> followingstrmap : followingstrlists) {
                            followingstr = followingstrmap.get("following")
                                    .toString();
                            followerstr = followingstrmap.get("follower")
                                    .toString();

                        }
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        String authentication = supermanlists.get(i).get("authentication") + "";
                        map2.put("isVip", authentication);
                        map2.put("uid", uidstr);
                        map2.put("username", unamestr);
                        map2.put("detailcomment", uidstr);
                        map2.put("following", followingstr);
                        map2.put("follower", followerstr);
                        map2.put("intro", introstr);
                        map2.put("i", i + "");
                        map2.put("image_url",
                                "http://www.sjqcj.com/data/upload/avatar"
                                        + save_pathstr + "original_200_200.jpg");
                        listsupermanData.add(map2);
                    }
                }
                supermanAdapter.setlistData(listsupermanData);
                ViewUtil.setListViewHeightBasedOnChildren(supermanlistview);
                aPPUserSortList = (ArrayList<HashMap<String, String>>) listsupermanData.clone();
            }
        }

    }

    private class SendInfoTasksesence extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();

            } else {
                super.onPostExecute(result);
                listessenceData.clear();
                if (result != null) {
                    result = result.replace("\n ", "");
                    result = result.replace("\n", "");
                    result = result.replace(" ", "");
                    result = "[" + result + "]";
                    // 解析json字符串获得List<Map<String,Object>>
                    List<Map<String, Object>> lists = JsonTools
                            .listKeyMaps(result);

                    for (Map<String, Object> map : lists) {
                        String statusstr = map.get("data").toString();
                        List<Map<String, Object>> supermanlists = JsonTools.listKeyMaps(statusstr);
                        for (int i = 0; i < 3; i++) {
                            String namestr = supermanlists.get(i).get("uname")
                                    .toString();
                            String weibo_titlestr = supermanlists.get(i)
                                    .get("weibo_title").toString();
                            String save_pathstr = supermanlists.get(i)
                                    .get("save_path").toString();
                            String save_namestr = supermanlists.get(i)
                                    .get("save_name").toString();
                            String weibo_idstr = supermanlists.get(i)
                                    .get("weibo_id").toString();
                            String uidstr = supermanlists.get(i).get("uid")
                                    .toString();
                            String comment_countstr = supermanlists.get(i)
                                    .get("comment_count").toString();
                            String imageurl = save_pathstr + save_namestr;
                            HashMap<String, String> map2 = new HashMap<String, String>();
                            String authentication = supermanlists.get(i).get("authentication") + "";
                            map2.put("isVip", authentication);
                            map2.put("weibo_titlestr", weibo_titlestr);
                            map2.put("username", namestr);
                            map2.put("image_url",
                                    "http://www.sjqcj.com/data/upload/"
                                            + imageurl);
                            map2.put("weibo_idstr", weibo_idstr);
                            map2.put("uidstr", uidstr);
                            map2.put("comment_countstr", comment_countstr);
                            map2.put("i", i + "");
                            listessenceData.add(map2);
                        }
                    }
                    essencelistAdapter.setlistData(listessenceData);
                    // 重新设置ListView的高度
                    Utils.setListViewHeightBasedOnChildren(essencelistview);
                    appindexList = (ArrayList<HashMap<String, String>>) listessenceData.clone();
                }
            }

            dialog.dismiss();
            rl_refresh_layout.setVisibility(View.GONE);
        }
    }

    // 进入精华列表

    class pickessence_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), EssenceListActivty.class);
            startActivity(intent);

        }

    }

    // 进入热门股列表

    class pickhotstock_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(),
                    hotstocklistActivity.class);
            startActivity(intent);

        }

    }

    // 进入牛人列表
    class picksuperman_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(),
                    supermanlistActivity.class);
            startActivity(intent);
        }
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
            // 先获取缓存
            globalSwfStr = mCache.getAsString("globalSwf");
            if (globalSwfStr == null) {
                globalSwfStr = "";
            }
            if (result == null || "".equals(result)) {
                result = globalSwfStr;
            } else {
                globalSwfStr = result;
            }
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
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (infos.size() > 0) {
                // 加载滚动图片
                setImageLoader();
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
     * 查询充值订单（看订单是否进行过处理）
     */
    private void selectRecharge() {
        String orderType = getActivity().getSharedPreferences("Recharge", getActivity().MODE_PRIVATE).getString("order_type", "");
        if (orderType != null && orderType.equals("1")) {
            // 重新调用后台查询订单增加水晶币
            // 订单号
            String outTradeNo = getActivity().getSharedPreferences("Recharge", getActivity().MODE_PRIVATE).getString("out_trade_no", "");
            new SelectOrder()
                    .execute(new TaskParams(Constants.queryOrder + "&uid=" + Constants.staticmyuidstr + "&out_trade_no=" + outTradeNo));
        }
    }

    private class SelectOrder extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
            } else {
                try {
                    JSONObject json = new JSONObject(result);
                    String status = json.getString("status");
                    String data = json.getString("data");
                    if ("1".equals(status)) {
                        Constants.shuijinbiCount = data;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // 保存订单状态订单号(用于控制订单的二次处理)
            SharedPreferences.Editor editorIsLogin = getActivity().getSharedPreferences("Recharge", getActivity().MODE_PRIVATE).edit();
            // 订单状态1为未处理0为处理
            editorIsLogin.putString("order_type", "0");
            editorIsLogin.commit();
        }
    }
}
