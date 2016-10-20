package com.example.sjqcjstock.adapter.matchadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class weekrankingfamousAdapter extends BaseAdapter {

    private Context context;
    private DisplayImageOptions options;

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private ArrayList<HashMap<String, Object>> listData;


    public weekrankingfamousAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        super();
        this.context = context;
        this.listData = listData;
        this.options = new DisplayImageOptions.Builder().
                cacheInMemory().cacheOnDisc().
                showStubImage(R.drawable.ic_stub).
                showImageForEmptyUri(R.drawable.ic_empty).
                showImageOnFail(R.drawable.ic_error2).
                displayer(new RoundedBitmapDisplayer(20)). //是否设置为方形或椭圆
                imageScaleType(ImageScaleType.IN_SAMPLE_INT)//图片显示方式
                .bitmapConfig(Bitmap.Config.ARGB_4444).build();//設置圖片配置信息  對圖片進行處理防止內存溢出

    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
    }

    private String[] imagesUrl;

    public void setImagesUrl(String[] imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    @Override
    public int getCount() {
        //return imagesUrl.length;
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_weekrankingfamous, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.user_image1);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                image, options, new AnimateFirstDisplayListener());
        /**

         image.setOnClickListener(new OnClickListener() {

        @Override public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
        if("".equals((String)listData.get(position).get("uidstr"))){

        intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
        context.startActivity(intent);
        }else{
        intent.putExtra("uid",(String)listData.get(position).get("uidstr"));
        context.startActivity(intent);
        }
        }
        });
         */

//		
        TextView famousmanname1 = (TextView) convertView.findViewById(R.id.famousmanname1);
        famousmanname1.setText((String) listData.get(position).get("uname"));

        TextView weekscore1 = (TextView) convertView.findViewById(R.id.weekscore1);
        weekscore1.setText((CharSequence) listData.get(position).get("list_price"));
//		
        TextView totalscore1 = (TextView) convertView.findViewById(R.id.totalscore1);
        totalscore1.setText((CharSequence) listData.get(position).get("ballot_jifen"));
//		


        TextView stockname1 = (TextView) convertView.findViewById(R.id.stockname1);
        stockname1.setText((String) listData.get(position).get("shares_name"));

        TextView shares2_name = (TextView) convertView.findViewById(R.id.stockname2);
        shares2_name.setText((String) listData.get(position).get("shares2_name"));


        TextView stockprice1 = (TextView) convertView.findViewById(R.id.stockprice1);
        stockprice1.setText((String) listData.get(position).get("price"));

        TextView stockprice2 = (TextView) convertView.findViewById(R.id.stockprice2);
        stockprice2.setText((String) listData.get(position).get("price2"));

        TextView uprange1 = (TextView) convertView.findViewById(R.id.uprange1);
        uprange1.setText((String) listData.get(position).get("integration3"));

        TextView uprange2 = (TextView) convertView.findViewById(R.id.uprange2);
        uprange2.setText((String) listData.get(position).get("integration4"));

        TextView rankingcount = (TextView) convertView.findViewById(R.id.rankingcount);
        rankingcount.setText((String) listData.get(position).get("rankingcount"));

        /**
         ImageView sourceuser_image2=(ImageView)convertView.findViewById(R.id.sourceuser_image2);
         // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
         ImageLoader.getInstance().displayImage((String)listData.get(position).
         get("sourceavatar_middlestr"),
         sourceuser_image2,options,new AnimateFirstDisplayListener());

         sourceuser_image2.setOnClickListener(new OnClickListener() {

        @Override public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
        intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
        context.startActivity(intent);

        }
        });
         */
//		CheckBox check=(CheckBox)convertView.findViewById(R.id.selected);

//		//判断用户是否已被选择，如被选择，则复选框为勾选，如未选择则复选框为可选
//		if((Boolean) listData.get(position).get("selected")){
//			state.put(position,true);
//			
//		}

//		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked){
//					state.put(position,isChecked);
//				}else {
//					state.remove(position);
//				}
//			}
//		});

//		check.setChecked((state.get(position)==null?false:true));

        return convertView;
    }


}
