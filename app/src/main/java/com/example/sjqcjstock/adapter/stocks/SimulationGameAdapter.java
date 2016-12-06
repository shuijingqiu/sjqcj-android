package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.SimulationGameActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.MatchEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
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
            holder.titleImg = (ImageView) convertView.findViewById(R.id.title_img_iv);
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.ranking = (TextView) convertView.findViewById(R.id.ranking_tv);
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
        holder.ranking.setText(matchEntity.getRanking());
        if ("1".equals(matchEntity.getStatus())){
            if ("0".equals(matchEntity.getJoined())){
                holder.joinTv.setVisibility(View.GONE);
                holder.joinBut.setVisibility(View.VISIBLE);
            }else{
                holder.joinTv.setVisibility(View.VISIBLE);
                holder.joinBut.setVisibility(View.GONE);
                holder.joinTv.setBackgroundResource(R.mipmap.ycj);
            }
        }else{
            holder.joinTv.setBackgroundResource(R.mipmap.yjs);
        }
        holder.joinBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开线程参加比赛
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
        ImageView titleImg;
        TextView title;
        TextView time;
        // 排名
        TextView ranking;
        // 排名字
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
