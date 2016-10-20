package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.CrystalBwater;
import com.example.sjqcjstock.netutil.Utils;

import java.util.List;

/**
 * 加载水晶币流动的控制器
 * Created by Administrator on 2016/5/6.
 */
public class CrystalBwaterAdapter extends BaseAdapter {
    // 要加载的数据集合
    private CrystalBwater crystalBwater;
    private Context context;

    public CrystalBwaterAdapter(Context context) {
        this.context = context;
    }

    public void setCrystalBwater(CrystalBwater crystalBwater) {
        this.crystalBwater = crystalBwater;
        notifyDataSetChanged();
    }

    // 追加数据
    public void setAddList(List<CrystalBwater.msgs> msg) {
        crystalBwater.getMsg().addAll(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return crystalBwater == null ? 0 : crystalBwater.getMsg().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CrystalBwater.msgs msg = crystalBwater.getMsg().get(position);
        return makeItemView(msg);
    }

    // 绘制Item的函数
    public View makeItemView(CrystalBwater.msgs msg) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater
                .inflate(R.layout.list_item_crystal_bwater, null);
        TextView time = (TextView) itemView.findViewById(R.id.bwater_time_tv);
        TextView name = (TextView) itemView.findViewById(R.id.bwater_name_tv);
        TextView typeTv = (TextView) itemView.findViewById(R.id.bwater_type_tv);
        TextView id = (TextView) itemView.findViewById(R.id.bwater_id_tv);
        name.setText(msg.getUname());
        time.setText(Utils.getStringtoDate1(msg.getTime()));
        id.setText(msg.getAssist_uid());
        String type = msg.getType();
        if ("0".equals(type)) {
            typeTv.setText("充值：" + msg.getAmount());
            typeTv.setTextColor(Color.RED);
        } else if ("1".equals(type)) {
            typeTv.setText("打赏：-" + msg.getAmount());
        } else if ("2".equals(type)) {
            typeTv.setText("被打赏：" + msg.getAmount());
            typeTv.setTextColor(Color.RED);
        }
        return itemView;
    }
}
