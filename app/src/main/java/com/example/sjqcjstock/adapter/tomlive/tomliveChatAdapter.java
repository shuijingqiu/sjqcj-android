package com.example.sjqcjstock.adapter.tomlive;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.javaScriptfaces.JavascriptInterface;
import com.example.sjqcjstock.view.HtmlImageGetter;
import com.example.sjqcjstock.view.ImageWebViewClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 直播显示的控制器
 */
public class tomliveChatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public tomliveChatAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, String>>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_tomlive_chat, null);
            holder = new ViewHolder();
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.contentWv = (WebView) convertView.findViewById(R.id.content_wv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.timeTv.setText(listData.get(position).get("time"));
        String content = listData.get(position).get("content");
        if (content.indexOf("<img")!=-1 || content.indexOf("<a")!=-1){
            content =content.replace("<img","<img  style=\"width: 160px;\"");
            // 给WebView添加js交互接口类(用于图片点击放大)
            holder.contentWv.getSettings().setJavaScriptEnabled(true);
            holder.contentWv.addJavascriptInterface(new JavascriptInterface(context), "imagelistner");
            holder.contentWv.setWebViewClient(new ImageWebViewClient(holder.contentWv, context));
//            holder.contentWv.requestFocus();
            holder.contentWv.loadDataWithBaseURL("about:blank",content, "text/html", "utf-8", null);
            holder.contentTv.setVisibility(View.GONE);
            holder.contentWv.setVisibility(View.VISIBLE);
        }else{
            HtmlImageGetter htmlImageGetter = new HtmlImageGetter(context, holder.contentTv);
            holder.contentTv.setText(Html.fromHtml(content,htmlImageGetter,null));
            holder.contentWv.setVisibility(View.GONE);
            holder.contentTv.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView timeTv;
        TextView contentTv;
        WebView contentWv;
    }
}