package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 私信聊天页面
 */
public class personalnewsdetail extends Activity {

    // 获取控件
    private EditText et_sendmessage;
    private TextView youname1;
    private Button btn_send;
    private RelativeLayout rl_bottom;
    private LinearLayout btn_back;

    // 获取列表数据
    private ListView chatlistview;

    private com.example.sjqcjstock.adapter.personalnewsdetailAdapter personalnewsdetailAdapter;

    ArrayList<HashMap<String, Object>> listpersonalnewsData;
    // 从intent 获取list_id数据
    String list_idstr;
    String uidstr;
    String unamestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // setContentView(layoutResID)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        // 获取私信列表传递过来的数据，list_idstr是根据全局的用户id通过异步任务获取
        Intent intent = getIntent();
        // 私信列表传递过来的数据
        list_idstr = intent.getStringExtra("list_id");
        unamestr = intent.getStringExtra("unamestr");
        uidstr = intent.getStringExtra("uidstr");

        et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        // et_sendmessage.clearFocus();
        // InputMethodManager imm = (InputMethodManager)
        // getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);

        // 目标用户名
        youname1 = (TextView) findViewById(R.id.youname1);
        btn_send = (Button) findViewById(R.id.btn_send);

        btn_back = (LinearLayout) findViewById(R.id.goback1);
        btn_back.setOnClickListener(new btn_back_listener());
        et_sendmessage.setOnClickListener(new et_sendmessage_listener());
        btn_send.setOnClickListener(new btn_send_listener());

        // 标题栏，用户名
        youname1.setText(unamestr);

        // 聊天内容列表
        chatlistview = (ListView) findViewById(R.id.chatlistview);

        // 存储数据的数组列表
        listpersonalnewsData = new ArrayList<HashMap<String, Object>>();

        /**
         * String[] strs={"me","you","me","you"};
         *
         * for(int i=0;i<4;i++){ HashMap<String,Object> map=new HashMap<String,
         * Object>(); map.put("meoryou", strs[i]); //添加数据
         * listpersonalnewsData.add(map); }
         */

        // 为ListView 添加适配器
        personalnewsdetailAdapter = new com.example.sjqcjstock.adapter.personalnewsdetailAdapter(
                personalnewsdetail.this, listpersonalnewsData);

        chatlistview.setAdapter(personalnewsdetailAdapter);

        // listView 的点击事件，隐藏键盘
        chatlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
            }
        });
        /**
         * 进入聊天界面，需要请求的数据 参数： 全局用户id， list_id进行请求
         */
        new SendInfoTask().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=MessageDetail&mid="
                        + Constants.getStaticmyuidstr()
                        + "&id=" + list_idstr));
    }

    class btn_send_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            /**
             * 发送私信 参数： 列表id 用户id 密码 tokey 内容 目标用户id
             */
            if (list_idstr == null || list_idstr.equals("")) {
                // 通过异步任务获取list_idstr
                // list_id为空的时候，调用另外的接口发送数据
                new SendInfoTaskaddpersonalletter()
                        .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AppdoPost",
                                new String[]{"mid", Constants.staticmyuidstr},
                                new String[]{"login_password", Constants.staticpasswordstr},
                                new String[]{"content", et_sendmessage.getText().toString()},
                                new String[]{"to", uidstr}));
                //直接将数据添加进集合
                HashMap<String, Object> mapItem = new HashMap<String, Object>();
                mapItem.put("contentstr", et_sendmessage.getText().toString());
                mapItem.put("uidstr", Constants.staticmyuidstr);
                mapItem.put("avatar_middlestr", Constants.headImg);
                mapItem.put("mtimestr", Utils.getNowDate());
                listpersonalnewsData.add(listpersonalnewsData.size(), mapItem);
                personalnewsdetailAdapter.notifyDataSetChanged();

            } else {
                new SendInfoTaskreplypersonalletter().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppdoPost",
                        new String[]{"id", list_idstr},
                        new String[]{"mid", Constants.staticmyuidstr},
                        new String[]{"login_password", Constants.staticpasswordstr},
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"content", et_sendmessage.getText().toString()},
                        new String[]{"to", uidstr}));

                //刷新列表参数：用户id，列表id
                listpersonalnewsData.clear();
                new SendInfoTaskrefresh().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=MessageDetail&mid="
                                + Constants.getStaticmyuidstr()
                                + "&id="
                                + list_idstr));
            }
        }
    }

    class et_sendmessage_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // SOFT_INPUT_STATE_UNSPECIFIED
            if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                // 隐藏软键盘
                // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                // listView自动滚动到底部
                // CustomToast.makeText(getApplicationContext(), "键盘已弹出",1).show();
                chatlistview.setSelection(chatlistview.getBottom());
            }
            et_sendmessage.setFocusableInTouchMode(true);
            // listView自动滚动到底部
            chatlistview.setSelection(chatlistview.getBottom());
        }
    }

    class btn_back_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }
    }

    private class SendInfoTaskaddpersonalletter extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "发送私信成功", Toast.LENGTH_LONG).show();
                    et_sendmessage.setText("");
                } else {

                }
            }
        }
    }

    // 刷新列表
    private class SendInfoTaskrefresh extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();

            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("data") + "";
                List<Map<String, Object>> statusstrlists = JsonTools.listKeyMaps("[" + statusstr + "]");

                for (Map<String, Object> statusstrmap : statusstrlists) {
                    String data2str = statusstrmap.get("data") + "";
                    List<Map<String, Object>> data2strlists = JsonTools.listKeyMaps(data2str);

                    for (Map<String, Object> data2strmap : data2strlists) {

                        String list_idstr = data2strmap.get("list_id") + "";
                        String from_uidstr = data2strmap.get("from_uid") + "";
                        String contentstr = data2strmap.get("content") + "";
                        String mtimestr = data2strmap.get("mtime") + "";

                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("list_idstr", list_idstr);
                        map2.put("from_uidstr", from_uidstr);
                        map2.put("contentstr", contentstr);
                        map2.put("mtimestr", Utils.getStringtoDate(mtimestr));

                        String user_infostr = data2strmap.get("user_info") + "";
                        List<Map<String, Object>> user_infostrlists = JsonTools.listKeyMaps("[" + user_infostr + "]");
                        for (Map<String, Object> user_infostrmap : user_infostrlists) {
                            String uidstr = user_infostrmap.get("uid") + "";
                            String unamestr = user_infostrmap.get("uname") + "";
                            String avatar_middlestr = user_infostrmap.get("avatar_middle") + "";

                            map2.put("uidstr", uidstr);
                            map2.put("unamestr", unamestr);
                            map2.put("avatar_middlestr", avatar_middlestr);

                        }
                        // 从头部插入
                        listpersonalnewsData.add(0, map2);
                    }
                }
            }
            personalnewsdetailAdapter.notifyDataSetChanged();
            // listView自动滚动到底部
            chatlistview.setSelection(chatlistview.getBottom());
        }
    }

    List<Map<String, Object>> lists = null;

    String statusstr = null;

    List<Map<String, Object>> statusstrlists = null;

    String data2str = null;

    List<Map<String, Object>> data2strlists = null;

    // 进入聊天页面时，请求数据
    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            if (lists == null) {
                lists = JsonTools.listKeyMaps(result);
            }
            for (Map<String, Object> map : lists) {
                if (statusstr == null) {
                    statusstr = map.get("data") + "";
                    statusstrlists = JsonTools.listKeyMaps("[" + statusstr + "]");
                }
                for (Map<String, Object> statusstrmap : statusstrlists) {

                    if (data2str == null) {
                        data2str = statusstrmap.get("data") + "";
                        data2strlists = JsonTools.listKeyMaps(data2str);
                    }
                    for (Map<String, Object> data2strmap : data2strlists) {

                        String list_idstr = data2strmap.get("list_id")
                                + "";
                        String from_uidstr = data2strmap.get("from_uid")
                                + "";
                        String contentstr = data2strmap.get("content")
                                + "";
                        String mtimestr = data2strmap.get("mtime") + "";

                        // String from_uidstr= map.get("from_uid")+"";
                        // String mtimestr= map.get("mtime")+"";
                        // String mtimestr= map.get("mtime")+"";

                        // 将时间戳转换成date
                        // SimpleDateFormat formatter=new
                        // SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        // Date curDate =new
                        // Date(Long.parseLong(mtimestr)*1000); //获取当前时间
                        // //Date curDate =new Date(System.currentTimeMillis());
                        // mtimestr=formatter.format(curDate);
                        mtimestr = CalendarUtil.formatDateTime(Utils
                                .getStringtoDate(mtimestr));

                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("list_idstr", list_idstr);
                        map2.put("from_uidstr", from_uidstr);
                        map2.put("contentstr", contentstr);
                        map2.put("mtimestr", mtimestr);

                        String user_infostr = data2strmap.get("user_info")
                                + "";
                        List<Map<String, Object>> user_infostrlists = JsonTools
                                .listKeyMaps("[" + user_infostr + "]");
                        for (Map<String, Object> user_infostrmap : user_infostrlists) {
                            String uidstr = user_infostrmap.get("uid")
                                    + "";
                            String unamestr = user_infostrmap.get("uname")
                                    + "";
                            String avatar_middlestr = user_infostrmap.get(
                                    "avatar_middle") + "";

                            map2.put("uidstr", uidstr);
                            map2.put("unamestr", unamestr);
                            map2.put("avatar_middlestr", avatar_middlestr);

                        }
                        // 从头部插入
                        listpersonalnewsData.add(0, map2);
                        personalnewsdetailAdapter.notifyDataSetChanged();
                        // listView自动滚动到底部
                        chatlistview.setSelection(chatlistview.getBottom());

                        // map2.put("weibo_titlestr", weibo_titlestr);
                        // map2.put("comment_countstr", comment_countstr);
                        // map2.put("unamestr", unamestr);

                        // listpersonalnewsData.add(map2);

                        // from_uid
                    }
                }
            }
        }
    }

    // 发送私信
    private class SendInfoTaskreplypersonalletter extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";

                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "发送私信成功", Toast.LENGTH_LONG).show();
                    et_sendmessage.setText("");
                } else {

                }
            }
        }
    }

    //发送请求，判断私信是否存在
    private class SendInfoTaskForChatIsExist extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                String datastr = map.get("data") + "";
                if ("1".equals(statusstr)) {
                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {
                        list_idstr = datastrmap.get("list_id") + "";
                    }
                }
                if ("0".equals(statusstr)) {
                    CustomToast.makeText(personalnewsdetail.this, "没有list_id", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // 判断控件的点击区域是否在本身，用户隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isineditarea(rl_bottom, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rl_bottom.getWindowToken(), 0);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    // 判断是否离开edit区域的方法
    public boolean isineditarea(View v, MotionEvent event) {
        if (v != null && (v instanceof RelativeLayout)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
