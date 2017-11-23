package com.example.sjqcjstock.adapter.qa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.qa.QuestionAnswerEntity;
import com.example.sjqcjstock.fragment.qa.FragmentAllInterlocution;
import com.example.sjqcjstock.fragment.qa.FragmentAllQuiz;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 问答列表的adapter
 */
public class QuizAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuestionAnswerEntity> listData;
    // 调用接口返回的数据
    private String resstr = "";
    private FragmentAllQuiz fragmentAllQuiz;
    private FragmentAllInterlocution fragmentAllInterlocution;
    private UserDetailNewActivity userDetailNewActivity;


    public QuizAdapter(Context context) {
        super();
        this.context = context;
    }

    public QuizAdapter(Context context,UserDetailNewActivity userDetailNewActivity) {
        super();
        this.context = context;
        this.userDetailNewActivity = userDetailNewActivity;
    }

    public QuizAdapter(Context context,FragmentAllQuiz fragmentAllQuiz) {
        super();
        this.context = context;
        this.fragmentAllQuiz = fragmentAllQuiz;
    }

    public QuizAdapter(Context context,FragmentAllInterlocution fragmentAllInterlocution) {
        super();
        this.context = context;
        this.fragmentAllInterlocution = fragmentAllInterlocution;
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
            convertView = mInflater.inflate(R.layout.list_item_quiz, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.userImage = (ImageView) convertView.findViewById(R.id.user_image);
            holder.quizContenTv = (TextView) convertView.findViewById(R.id.quiz_conten_tv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.abstractTv = (TextView) convertView.findViewById(R.id.abstract_tv);
            holder.subscribeAnswerTv = (TextView) convertView.findViewById(R.id.subscribe_answer_tv);
            holder.answerTv = (TextView) convertView.findViewById(R.id.answer_tv);
            holder.answerRl = (RelativeLayout) convertView.findViewById(R.id.answer_rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QuestionAnswerEntity questionAnswerEntity = listData.get(position);

        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                .getMd5(questionAnswerEntity.getUid())),
                holder.userImage, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                .getMd5(questionAnswerEntity.getAnswerer())),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人中心
                Intent intent = new Intent(context,UserDetailNewActivity.class);
                intent.putExtra("uid", questionAnswerEntity.getAnswerer());
                intent.putExtra("type", "3");
                context.startActivity(intent);
            }
        });
        holder.userImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人中心
                Intent intent = new Intent(context,UserDetailNewActivity.class);
                intent.putExtra("uid", questionAnswerEntity.getUid());
                intent.putExtra("type", "3");
                context.startActivity(intent);
            }
        });

        if ("2".equals(questionAnswerEntity.getStatus())){
            holder.answerRl.setVisibility(View.VISIBLE);
        }else{
            holder.answerRl.setVisibility(View.GONE);
        }

        holder.quizContenTv.setText(questionAnswerEntity.getQuestion());
        holder.nameTv.setText(questionAnswerEntity.getAuname());
        holder.timeTv.setText(questionAnswerEntity.getUpdate_time());
        holder.answerTv.setText(questionAnswerEntity.getAnswer());
        holder.abstractTv.setText(questionAnswerEntity.getA_intro());
        if ("1".equals(questionAnswerEntity.getValid())){
            holder.subscribeAnswerTv.setVisibility(View.GONE);
            holder.answerTv.setVisibility(View.VISIBLE);
        }else{
            holder.subscribeAnswerTv.setVisibility(View.VISIBLE);
            holder.answerTv.setVisibility(View.GONE);
            holder.subscribeAnswerTv.setText("打赏"+questionAnswerEntity.getWatch_price()+"水晶币可见回答");
            holder.subscribeAnswerTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    // menghuan 不用登陆也可以用
                    // 如果未登陆跳转到登陆页面
                    if (!Constants.isLogin){
                        Intent intent = new Intent(context, loginActivity.class);
                        context.startActivity(intent);
                        return;
                    }

                    // 弹出提示框
                    new AlertDialog.Builder(context).setMessage("    打赏"+listData.get(position).getWatch_price()+"水晶币查看回答")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (fragmentAllQuiz!=null){
                                        fragmentAllQuiz.Refresh(questionAnswerEntity.getId());
                                    }else if(fragmentAllInterlocution != null){
                                        fragmentAllInterlocution.Refresh(questionAnswerEntity.getId());
                                    }else if(userDetailNewActivity !=null){
                                        userDetailNewActivity.Refresh(questionAnswerEntity.getId());
                                    }
                                }})
                            .show();
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        // 回答者头像
        ImageView image;
        // 提问人头像
        ImageView userImage;
        // 提出的问题
        TextView quizContenTv;
        // 名称
        TextView nameTv;
        // 时间
        TextView timeTv;
        // 打赏提示按钮
        TextView subscribeAnswerTv;
        // 个人简介
        TextView abstractTv;
        // 答案
        TextView answerTv;
        // 回答显示主体
        RelativeLayout answerRl;
    }
}
