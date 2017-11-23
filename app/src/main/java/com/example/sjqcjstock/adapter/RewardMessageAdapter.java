package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.RewardMessage;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 加载打赏消息的控制器
 * Created by Administrator on 2016/5/9.
 */
public class RewardMessageAdapter extends BaseAdapter {

    // 要加载的数据集合
    private RewardMessage rewardMessage;
    private Context context;

    public RewardMessageAdapter(Context context) {
        this.context = context;
    }

    public void setRewardMessage(RewardMessage rewardMessage) {
        this.rewardMessage = rewardMessage;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rewardMessage == null ? 0 : rewardMessage.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return rewardMessage.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_reward_meaage, null);
            holder = new ViewHolder();
            holder.headImg = (ImageView) convertView.findViewById(R.id.user_head_img);
            holder.name = (TextView) convertView.findViewById(R.id.user_name_tv);
            holder.rewardNumber = (TextView) convertView.findViewById(R.id.reward_number_tv);
            holder.uid = (TextView) convertView.findViewById(R.id.user_id_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RewardMessage.datas data = rewardMessage.getData().get(position);
        ImageLoader.getInstance().displayImage(data.getAvatar_tiny(),
                holder.headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.name.setText(data.getUname());
        holder.rewardNumber.setText(data.getAmount());
        holder.uid.setText(data.getUid());
        return convertView;
    }

    // 追加数据
    public void setAddList(List<RewardMessage.datas> data) {
        rewardMessage.getData().addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView name;
        ImageView headImg;
        TextView rewardNumber;
        TextView uid;
    }

}
