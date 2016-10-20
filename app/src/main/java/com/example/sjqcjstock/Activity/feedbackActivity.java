package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.sjqcjstock.app.ExitApplication;

public class feedbackActivity extends Activity {

//	//获取控件
//	private ImageView goback5;
// 	private ImageView addfeedback1;
//
// 	//获取列表控件
//
// 	private ListView listView1;
//
//	ArrayList<HashMap<String,Object>> listfeedbackData;
//
//	feedbackAdapter feedbackAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//		setContentView(R.layout.feedbacklist);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

//		initView();
    }
//
//	private void initView() {
//		// TODO Auto-generated method stub
//		goback5=(ImageView)findViewById(R.id.goback5);
//		addfeedback1=(ImageView)findViewById(R.id.addfeedback1);
//		goback5.setOnClickListener(new goback5_listener());
//		addfeedback1.setOnClickListener(new addfeedback1_listener());
//
//
//		/*意见反馈列表*/
//		listView1=(ListView)findViewById(R.id.listView1);
//		 //存储数据的数组列表
//		listfeedbackData=new ArrayList<HashMap<String,Object>>();
//
//
//
//        for(int i=0;i<20;i++){
//        	HashMap<String,Object> map=new HashMap<String, Object>();
//
//        	//添加数据
//        	listfeedbackData.add(map);
//        }
//        feedbackAdapter=new feedbackAdapter(feedbackActivity.this, listfeedbackData);
//
//        listView1.setAdapter(feedbackAdapter);
//
//
//	}
//
//	class goback5_listener implements OnClickListener{
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			finish();
//		}
//
//	}
//
//	class addfeedback1_listener implements OnClickListener{
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Intent intent=new Intent(feedbackActivity.this,addfeedbackActivity.class);
//			startActivity(intent);
//		}
//
//	}

}
