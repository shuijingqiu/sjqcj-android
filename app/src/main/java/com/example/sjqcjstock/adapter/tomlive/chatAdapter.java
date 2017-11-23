package com.example.sjqcjstock.adapter.tomlive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Tomlive.QuestionEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 聊天显示的控制器
 */
public class chatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuestionEntity> listData;
    public chatAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<QuestionEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<QuestionEntity>) listData.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (listData == null){
            return 0;
        }
        return listData.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_chat, null);
            holder = new ViewHolder();
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.nameTv1 = (TextView) convertView.findViewById(R.id.name_tv1);
            holder.timeTv1 = (TextView) convertView.findViewById(R.id.time_tv1);
            holder.contentTv1 = (TextView) convertView.findViewById(R.id.content_tv1);
            holder.nameTv2 = (TextView) convertView.findViewById(R.id.name_tv2);
            holder.timeTv2 = (TextView) convertView.findViewById(R.id.time_tv2);
            holder.contentTv2 = (TextView) convertView.findViewById(R.id.content_tv2);
            holder.replyRl = (RelativeLayout) convertView.findViewById(R.id.reply_rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuestionEntity questionEntity = listData.get(position);
        String answer = questionEntity.getAnswer();
        String uid = questionEntity.getUid();
        if (answer != null && !"".equals(answer)){
            QuestionEntity questionEntity1 = JSON.parseObject(answer,QuestionEntity.class);
            holder.nameTv1.setText(questionEntity1.getUsername());
            holder.timeTv1.setText(Utils.InterceptDate(questionEntity1.getTime()));
            holder.contentTv1.setText(questionEntity1.getContent());
            holder.nameTv2.setText(questionEntity.getUsername());
            holder.timeTv2.setText(Utils.InterceptDate(questionEntity.getTime()));
            holder.contentTv2.setText(questionEntity.getContent());
            holder.replyRl.setVisibility(View.VISIBLE);
            uid = questionEntity1.getUid();
        }else{
            holder.nameTv1.setText(questionEntity.getUsername());
            holder.timeTv1.setText(Utils.InterceptDate(questionEntity.getTime()));
            holder.contentTv1.setText(questionEntity.getContent());
            holder.replyRl.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(uid)),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.headIm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listData.get(position).getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView headIm;
        TextView nameTv1;
        TextView timeTv1;
        TextView contentTv1;
        TextView nameTv2;
        TextView timeTv2;
        TextView contentTv2;
        RelativeLayout replyRl;
    }

}
