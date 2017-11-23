package com.example.sjqcjstock.adapter.qa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.qa.QuestionAnswerActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.qa.QuestionAnswerEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 提问列表的adapter
 */
public class QuizMyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuestionAnswerEntity> listData;
    private String uid;

    public QuizMyAdapter(Context context,String uid) {
        super();
        this.context = context;
        this.uid = uid;
    }

    public void setlistData(ArrayList<QuestionAnswerEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<QuestionAnswerEntity>) listData.clone();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_quiz_my, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.quizContenTv = (TextView) convertView.findViewById(R.id.quiz_conten_tv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.abstractTv = (TextView) convertView.findViewById(R.id.abstract_tv);
            holder.answerBt = (Button) convertView.findViewById(R.id.answer_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QuestionAnswerEntity questionAnswerEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                .getMd5(questionAnswerEntity.getUid())),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        if (!Constants.staticmyuidstr.equals(uid)){
            holder.answerBt.setVisibility(View.GONE);
        }
        else{
            holder.answerBt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 跳转或者打开回复方式
                    Intent intent = new Intent(context,QuestionAnswerActivity.class);
                    intent.putExtra("id", questionAnswerEntity.getId());
                    intent.putExtra("type", "2");
                    context.startActivity(intent);
                }
            });
        }
        holder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人中心
                Intent intent = new Intent(context,UserDetailNewActivity.class);
                intent.putExtra("uid", questionAnswerEntity.getUid());
                intent.putExtra("type", "3");
                context.startActivity(intent);
            }
        });
        holder.quizContenTv.setText(questionAnswerEntity.getQuestion());
        holder.nameTv.setText(questionAnswerEntity.getQuname());
        holder.timeTv.setText(questionAnswerEntity.getTime());
        holder.abstractTv.setText(questionAnswerEntity.getQ_intro());
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        // 提出的问题
        TextView quizContenTv;
        // 名称
        TextView nameTv;
        // 时间
        TextView timeTv;
        // 回答按钮
        Button answerBt;
        // 个人简介
        TextView abstractTv;
    }
}
