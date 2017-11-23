package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.SimulationGameActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.MatchEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟比赛列表的Adapter
 * Created by Administrator on 2016/8/17.
 */
public class SimulationGameAdapter extends BaseAdapter {

    // 加载用的数据
    private List<MatchEntity> listData;
    private Context context;
    // 调用接口参加比赛返回的数据
    private String resstr = "";
    private SimulationGameActivity simulationGameActivity;

    public SimulationGameAdapter(Context context,SimulationGameActivity simulationGameActivity) {
        super();
        this.context = context;
        this.simulationGameActivity = simulationGameActivity;
    }

    public void setlistData(ArrayList<MatchEntity> listData) {
        if (listData != null) {
            this.listData = (List<MatchEntity>) listData.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_simulation_game, null);
            holder = new ViewHolder();
            holder.rankingLl = (LinearLayout) convertView.findViewById(R.id.ranking_ll);
            holder.titleImg = (ImageView) convertView.findViewById(R.id.title_img_iv);
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.rankingValue = (TextView) convertView.findViewById(R.id.ranking_value_tv);
            holder.joinTv = (TextView) convertView.findViewById(R.id.join_tv);
            holder.joinBut = (TextView) convertView.findViewById(R.id.join_but);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MatchEntity matchEntity = listData.get(position);

        ImageLoader.getInstance().displayImage(matchEntity.getImage(),
                holder.titleImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.title.setText(matchEntity.getName());
        holder.time.setText(matchEntity.getStart_date()+"至"+matchEntity.getEnd_date());
        holder.rankingValue.setText(matchEntity.getRanking());
        holder.joinBut.setVisibility(View.GONE);
        if ("3".equals(matchEntity.getStatus())){
            holder.joinTv.setVisibility(View.VISIBLE);
            holder.joinTv.setBackgroundResource(R.mipmap.yjs);
            if ("0".equals(matchEntity.getJoined())){
                holder.rankingLl.setVisibility(View.INVISIBLE);
            }else {
                holder.rankingLl.setVisibility(View.VISIBLE);
            }
        }else{
            if ("0".equals(matchEntity.getJoined())){
                holder.joinTv.setVisibility(View.GONE);
                holder.joinBut.setVisibility(View.VISIBLE);
                holder.rankingLl.setVisibility(View.INVISIBLE);
            }else{
                holder.joinTv.setVisibility(View.VISIBLE);
                holder.joinBut.setVisibility(View.GONE);
                holder.joinTv.setBackgroundResource(R.mipmap.ycj);
                holder.rankingLl.setVisibility(View.VISIBLE);
            }
        }
        holder.joinBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开线程参加比赛
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // menghuan 不用登陆也可以用
                        // 如果未登陆跳转到登陆页面
                        if (!Constants.isLogin){
                            Intent intent = new Intent(context, loginActivity.class);
                            context.startActivity(intent);
                            return;                                        }
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("id", matchEntity.getId()));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        // 调用接口参加比赛
                        resstr = HttpUtil.restHttpPost(Constants.moUrl+"/match/join",dataList);
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        // 排名的
        LinearLayout rankingLl;
        ImageView titleImg;
        TextView title;
        TextView time;
        // 排名
        TextView rankingValue;
        // 参加
        TextView joinTv;
        // 参加Button
        TextView joinBut;
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject jsonObject = new JSONObject(resstr);
                Toast.makeText(context, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                if ("success".equals(jsonObject.getString("status"))) {
                    simulationGameActivity.refreshPage();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
