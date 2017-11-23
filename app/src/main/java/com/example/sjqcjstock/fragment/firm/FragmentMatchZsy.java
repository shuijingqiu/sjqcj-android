package com.example.sjqcjstock.fragment.firm;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.RechargeActivity;
import com.example.sjqcjstock.Activity.firm.FirmUserHomePageActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmHallZsyAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmDetailEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 总收益 近5日 当日
 * Created by Administrator on 2017/8/28.
 */
public class FragmentMatchZsy extends Fragment {

    // 网络请求提示
    private CustomProgress dialog;
    //定义List集合容器
    private FirmHallZsyAdapter firmHallZsyAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<FirmDetailEntity> listFirmDetailEntity;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int page = 1;
    // 接口返回数据
    private String jsonStr;
    private String remdStr;
    // 比赛id
    private String firmId;
    // 显示类型 1.总收益 2.近5日 3.当日
    private int type;
    // 比赛状态 1报名中  2进行中  3已结束  4待开始
    private String state;
    // 比赛剩余天数
    private String residue;
    // 比赛id
    private String matchId;
    // 比赛人的uid
    private String matchUid;

    public FragmentMatchZsy(){}
    public FragmentMatchZsy(String firmId,int type,String state,String residue)
    {
        this.firmId = firmId;
        this.type = type;
        this.state = state;
        this.residue = residue;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_zsy, container, false);
        findView(view);
        return view;
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        listFirmDetailEntity = new ArrayList<FirmDetailEntity>();
        dialog = new CustomProgress(getActivity());
        firmHallZsyAdapter = new FirmHallZsyAdapter(getActivity(),this,state);
        listView = (ListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(firmHallZsyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                FirmDetailEntity firmDetailEntity = listFirmDetailEntity.get(arg2);
                if ("2".equals(state) || "3".equals(state)){
                    if ("2".equals(state) && "0".equals(firmDetailEntity.getIs_desert()) && !firmDetailEntity.getUid().equals(Constants.staticmyuidstr)){
                        // 比赛在进行中且没有订阅的时候
                        PayCrystalCoin(firmDetailEntity.getId(),firmDetailEntity.getDesert_reward(),firmDetailEntity.getUid());
                        return;
                    }
                    Intent intent = new Intent(getActivity(), FirmUserHomePageActivity.class);
                    intent.putExtra("id",firmDetailEntity.getId());
                    intent.putExtra("uid",firmDetailEntity.getUid());
                    startActivity(intent);
                }
            }
        });

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
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
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        String url = Constants.newUrl;
        if (type == 1){
            url += "/api/match/detail?match_id="+firmId+"&order=total&p="+page+"&mid="+Constants.staticmyuidstr;
        }else if(type == 2){
            url += "/api/match/detail?match_id="+firmId+"&order=week&p="+page+"&mid="+Constants.staticmyuidstr;
        }else if(type == 3){
            url += "/api/match/detail?match_id="+firmId+"&order=days&p="+page+"&mid="+Constants.staticmyuidstr;
        }
        final String urlStr = url;
        // 获取焦点数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(urlStr);
                handler.sendEmptyMessage(0);
            }
        }).start();
        // 开线程获取水晶币数量
        new Thread(new Runnable() {
            @Override
            public void run() {
                remdStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/credit/info?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(2);
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
                        if (!Constants.successCode.equals(jsonObject.getString("code"))){
                            CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<FirmDetailEntity> list = (ArrayList<FirmDetailEntity>) JSON.parseArray(jsonObject.getString("data"),FirmDetailEntity.class);
                        if(page == 1){
                            listFirmDetailEntity = list;
                        }else{
                            listFirmDetailEntity.addAll(list);
                        }
                        firmHallZsyAdapter.setlistData(listFirmDetailEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))){
                            // 订阅成功后刷新当前数据列表
                            page = 1;
                            getData();
                            // 跳转到实盘比赛的个人主页
                            Intent intent = new Intent(getActivity(), FirmUserHomePageActivity.class);
                            intent.putExtra("id",matchId);
                            intent.putExtra("uid",matchUid);
                            startActivity(intent);
                        }
                        dialog.dismissDlog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(remdStr);
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String shuijingbi = jsonData.getString("shuijingbi");
                            Constants.shuijinbiCount = shuijingbi;
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 订阅水晶币的单机事件
     */
    public void PayCrystalCoin(final String id, final String sjbCount,String matchUid){
        matchId = id;
        this.matchUid = matchUid;
        // menghuan 不用登陆也可以用
        // 只用用户登陆后才能来订阅
        if (!Constants.isLogin){
            Intent intent = new Intent(getActivity(), loginActivity.class);
            startActivity(intent);
            return;
        }
        final View viewSang = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_firm, null);
        RelativeLayout rl_dialogDismiss = (RelativeLayout) viewSang.findViewById(R.id.rl_dialogDismiss);

        //显示需要支付水晶币数量的控件
        TextView inputCount = (TextView) viewSang.findViewById(R.id.tv_inputCount);
        inputCount.setText("打赏："+sjbCount+" 水晶币订阅查看本次比赛，详情见实盘比赛订阅协议。");

        // 显示水晶币数量的控件
        TextView restCount = (TextView) viewSang.findViewById(R.id.tv_restCount);
        restCount.setText(Constants.shuijinbiCount);
        Button bt_ok = (Button) viewSang.findViewById(R.id.bt_ok);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(viewSang).show();
        rl_dialogDismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                alertDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                    CustomToast.makeText(getActivity(), "对不起你水晶币不足。", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!((CheckBox) viewSang.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                    CustomToast.makeText(getActivity(), "请阅读《实盘比赛订阅协议》", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.showDialog();
                // 确认订阅比赛的接口
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("id", id));
                        jsonStr = HttpUtil.restHttpPost(Constants.newUrl+"/api/match/desert",dataList);
                        handler.sendEmptyMessage(1);
                    }
                }).start();
                alertDialog.dismiss();
            }
        });

        viewSang.findViewById(R.id.sang_agreement_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶币协议页面
                Intent intent = new Intent(getActivity(), AgreementActivity.class);
                intent.putExtra("type","27");
                startActivity(intent);
            }
        });
        viewSang.findViewById(R.id.bt_cz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到充值页面
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
            }
        });
    }

}
