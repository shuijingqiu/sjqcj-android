package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class replyinfoAdapter extends BaseAdapter {


    private Context context;
    private com.example.sjqcjstock.Activity.forumnotedetailActivity forumnotedetailActivity;
    private ArrayList<HashMap<String, Object>> listData;

    public replyinfoAdapter(Context context, com.example.sjqcjstock.Activity.forumnotedetailActivity forumnotedetailActivity, ArrayList<HashMap<String, Object>> listData) {
        super();
        this.context = context;
        this.listData = listData;
        this.forumnotedetailActivity = forumnotedetailActivity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_replyinfo, null);
        }
        TextView createtime1 = (TextView) convertView.findViewById(R.id.createtime1);
        createtime1.setText((String) listData.get(position).get("ctime"));

        CharSequence html = (CharSequence) listData.get(position).get("content");
//        html = html.replace("__THEME__", "http://www.sjqcj.com/addons/theme/stv1/_static");
//        html = html.replace("imgsrc", "img src");
//        CharSequence contentHtml = Html.fromHtml(html);
        TextView wv_ad = (TextView) convertView.findViewById(R.id.wv_ad);
        wv_ad.setText(html);

//        WebView wv_ad = (WebView) convertView.findViewById(R.id.wv_ad);
//        //设置WebView背景色
//        //wv_ad.setBackgroundColor(0x00000000);
////        wv_ad.setBackgroundColor(0xffffffff);
//        //wv_ad.setBackgroundColor(0); // 设置背景色
//        wv_ad.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
//        wv_ad.setVisibility(View.VISIBLE); // 加载完之后进行设置显示，以免加载时初始化效果不好看
//        wv_ad.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        TextView storey1 = (TextView) convertView.findViewById(R.id.storey1);
        storey1.setText((String) listData.get(position).get("storey"));

        TextView username1 = (TextView) convertView.findViewById(R.id.username1);
        username1.setText((String) listData.get(position).get("uname"));

        ImageView image = (ImageView) convertView.findViewById(R.id.userimg2);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middle"),
                image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

//        image.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                try {
//                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
//                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                } finally {
//                }
//            }
//        });

        LinearLayout pickcommentreply2 = (LinearLayout) convertView.findViewById(R.id.pickcommentreply2);
        pickcommentreply2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //回调
                forumnotedetailActivity.commentoptions(position);
            }
        });

        final ImageView vipImg = (ImageView) convertView
                .findViewById(R.id.vip_img);
        String isVip = listData.get(position).get(
                "isVip") + "";
        ViewUtil.setUpVip(isVip, vipImg);
        return convertView;

    }
}
