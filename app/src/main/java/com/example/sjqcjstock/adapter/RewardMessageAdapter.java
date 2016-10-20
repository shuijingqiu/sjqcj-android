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
        RewardMessage.datas data = rewardMessage.getData().get(position);
        return makeItemView(data);
    }

    // 追加数据
    public void setAddList(List<RewardMessage.datas> data) {
        rewardMessage.getData().addAll(data);
        notifyDataSetChanged();
    }

    // 绘制Item的函数
    public View makeItemView(RewardMessage.datas data) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater
                .inflate(R.layout.list_item_reward_meaage, null);
        ImageView headImg = (ImageView) itemView.findViewById(R.id.user_head_img);
        TextView name = (TextView) itemView.findViewById(R.id.user_name_tv);
        TextView rewardNumber = (TextView) itemView.findViewById(R.id.reward_number_tv);
        TextView uid = (TextView) itemView.findViewById(R.id.user_id_tv);

        ImageLoader.getInstance().displayImage(data.getAvatar_tiny(),
                headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        name.setText(data.getUname());
        rewardNumber.setText(data.getAmount());
        uid.setText(data.getUid());

        return itemView;
    }
}
