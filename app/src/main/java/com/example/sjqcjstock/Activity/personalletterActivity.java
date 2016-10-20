package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.facelibaryAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class personalletterActivity extends Activity {

    // 获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;

    private LinearLayout goback1;

    // 获取控件
    private EditText editcommentforweibo1;
    private ImageView facelibrary1;
    private CheckBox selected;
    private ImageView atsbicon1;
    private LinearLayout addweicomment1;

    // 定义适配器
    facelibaryAdapter facelibaryAdapter;
    private GridView facegridView1;
    // 定义于数据库同步的字段集合
    // private String[] name;
    ArrayList<HashMap<String, Object>> listData;

    // 判断弹出获取图片操作的标识 0代表显示，1代表隐藏
    private String isdisplaygetimg = "0";

    // 判断弹出表情库的标识 0代表显示，1代表隐藏
    private String isdisplayfacelibrary = "0";

    // 获取发送给某人的id
    String touidstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.personalletter);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {

        // 获取意图数据
        Intent intent = getIntent();
        touidstr = intent.getStringExtra("uid");

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        editcommentforweibo1 = (EditText) findViewById(R.id.editcommentforweibo1);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        selected = (CheckBox) findViewById(R.id.selected);
        atsbicon1 = (ImageView) findViewById(R.id.atsbicon1);
        addweicomment1 = (LinearLayout) findViewById(R.id.addweicomment1);

        goback1.setOnClickListener(new goback1_listener());
        editcommentforweibo1
                .setOnClickListener(new editcommentforweibo1_listener());
        facelibrary1.setOnClickListener(new facelibrary1_listener());
        addweicomment1.setOnClickListener(new addweicomment1_listener());
        atsbicon1.setOnClickListener(new atsbicon1_listener());

        facegridView1 = (GridView) findViewById(R.id.facegridView1);

        listData = new ArrayList<HashMap<String, Object>>();
        facelibaryAdapter = new facelibaryAdapter(personalletterActivity.this,
                listData);
        final int[] imagestr = {R.drawable.aoman, R.drawable.baiyan,
                R.drawable.bishi, R.drawable.bizui, R.drawable.cahan,
                R.drawable.caidao, R.drawable.chajin, R.drawable.cheer,
                R.drawable.chong, R.drawable.ciya, R.drawable.da,
                R.drawable.dabian, R.drawable.dabing, R.drawable.dajiao,
                R.drawable.daku, R.drawable.dangao, R.drawable.danu,
                R.drawable.dao, R.drawable.deyi, R.drawable.diaoxie,
                R.drawable.e, R.drawable.fadai, R.drawable.fadou,
                R.drawable.fan, R.drawable.fanu, R.drawable.feiwen,
                R.drawable.fendou, R.drawable.gangga, R.drawable.geili,
                R.drawable.gouyin, R.drawable.guzhang, R.drawable.haha,
                R.drawable.haixiu, R.drawable.haqian, R.drawable.hua,
                R.drawable.huaixiao, R.drawable.hufen, R.drawable.huishou,
                R.drawable.huitou, R.drawable.jidong, R.drawable.jingkong,
                R.drawable.jingya, R.drawable.kafei, R.drawable.keai,
                R.drawable.kelian, R.drawable.ketou, R.drawable.kiss,
                R.drawable.ku, R.drawable.kuaikule, R.drawable.kulou,
                R.drawable.kun, R.drawable.lanqiu, R.drawable.lenghan,
                R.drawable.liuhan, R.drawable.liulei, R.drawable.liwu,
                R.drawable.love, R.drawable.ma, R.drawable.meng,
                R.drawable.nanguo, R.drawable.no, R.drawable.ok,
                R.drawable.peifu, R.drawable.pijiu, R.drawable.pingpang,
                R.drawable.pizui, R.drawable.qiang, R.drawable.qinqin,
                R.drawable.qioudale, R.drawable.qiu, R.drawable.quantou,
                R.drawable.ruo, R.drawable.se, R.drawable.shandian,
                R.drawable.shengli, R.drawable.shenma, R.drawable.shuai,
                R.drawable.shuijiao, R.drawable.taiyang, R.drawable.tiao,
                R.drawable.tiaopi, R.drawable.tiaosheng, R.drawable.tiaowu,
                R.drawable.touxiao, R.drawable.tu, R.drawable.tuzi,
                R.drawable.wabi, R.drawable.weiqu, R.drawable.weixiao,
                R.drawable.wen, R.drawable.woshou, R.drawable.xia,
                R.drawable.xianwen, R.drawable.xigua, R.drawable.xinsui,
                R.drawable.xu, R.drawable.yinxian, R.drawable.yongbao,
                R.drawable.youhengheng, R.drawable.youtaiji,
                R.drawable.yueliang, R.drawable.yun, R.drawable.zaijian,
                R.drawable.zhadan, R.drawable.zhemo, R.drawable.zhuakuang,
                R.drawable.zhuanquan, R.drawable.zhutou,
                R.drawable.zuohengheng, R.drawable.zuotaiji, R.drawable.zuqiu,

        };

        final String[] facestrs = {"[aoman]", "[baiyan]", "[bishi]",
                "[bizui]", "[cahan]", "[caidao]", "[chajin]", "[cheer]",
                "[chong]", "[ciya]", "[da]", "[dabian]", "[dabing]",
                "[dajiao]", "[daku]", "[dangao]", "[danu]", "[dao]", "[deyi]",
                "[diaoxie]", "[e]", "[fadai]", "[fadou]", "[fan]", "[fanu]",
                "[feiwen]", "[fendou]", "[gangga]", "[geili]", "[gouyin]",
                "[guzhang]", "[haha]", "[haixiu]", "[haqian]", "[hua]",
                "[huaixiao]", "[hufen]", "[huishou]", "[huitou]", "[jidong]",
                "[jingkong]", "[jingya]", "[kafei]", "[keai]", "[kelian]",
                "[ketou]", "[kiss]", "[ku]", "[kuaikule]", "[kulou]", "[kun]",
                "[lanqiu]", "[lenghan]", "[liuhan]", "[liulei]", "[liwu]",
                "[love]", "[ma]", "[meng]", "[nanguo]", "[no]", "[ok]",
                "[peifu]", "[pijiu]", "[pingpang]", "[pizui]", "[qiang]",
                "[qinqin]", "[qioudale]", "[qiu]", "[quantou]", "[ruo]",
                "[se]", "[shandian]", "[shengli]", "[shenma]", "[shuai]",
                "[shuijiao]", "[taiyang]", "[tiao]", "[tiaopi]", "[tiaosheng]",
                "[tiaowu]", "[touxiao]", "[tu]", "[tuzi]", "[wabi]", "[weiqu]",
                "[weixiao]", "[wen]", "[woshou]", "[xia]", "[xianwen]",
                "[xigua]", "[xinsui]", "[xu]", "[yinxian]", "[yongbao]",
                "[youhengheng]", "[youtaiji]", "[yueliang]", "[yun]",
                "[zaijian]", "[zhadan]", "[zhemo]", "[zhuakuang]",
                "[zhuanquan]", "[zhutou]", "[zuohengheng]", "[zuotaiji]",
                "[zuqiu]",

        };

        // int[]
        // arrowimagestr={R.drawable.twodimension1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,
        // R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1
        // ,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,
        // R.mipmap.gracearrow1,};
        for (int i = 0; i < 110; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("friend_image", imagestr[i]);
            // map.put("friend_username", name[i]);
            // map.put("arrow_image", arrowimagestr[i]);
            // map.put("friend_id",id[i]);
            map.put("facestr", facestrs[i]);

            // 添加数据
            listData.add(map);
        }

        facegridView1.setAdapter(facelibaryAdapter);

        facegridView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                int index = editcommentforweibo1.getSelectionStart();
                Editable editable = editcommentforweibo1.getText();
                editable.insert(index, facestrs[position]);

                /**
                 * ImageGetter imageGetter = new ImageGetter() { public Drawable
                 * getDrawable(String source) { int id =
                 * Integer.parseInt(source); Drawable d =
                 * getResources().getDrawable(id); d.setBounds(0, 0,
                 * d.getIntrinsicWidth(), d.getIntrinsicHeight()); return d; }
                 * };
                 *
                 * CharSequence cs = Html.fromHtml("<img src='"+
                 * imagestr[position]+ "'/>",imageGetter, null);
                 * editcommentforweibo1.getText().append(cs); String faceContent
                 * =FilterHtml(Html.toHtml(editcommentforweibo1.getText()));
                 *
                 *
                 */

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // at返回值
        if (resultCode == REQUEST_CODE_UNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
            // myTextView.setText("恭喜您，注册成功。您的用户名是："+Name);

            int index = editcommentforweibo1.getSelectionStart();
            Editable editable = editcommentforweibo1.getText();
            editable.insert(index, "@" + Name + " ");

            // editcommentforweibo1.setText("@"+Name+" ");
        }

    }

    //发送私信
    class addweicomment1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {

            //发送私信到某个客户
            new SendInfoTaskaddpersonalletter().execute(new TaskParams(
                    Constants.Url + "?app=public&mod=AppFeedList&act=AppdoPost",
                    new String[]{"mid", Constants.staticmyuidstr},
                    new String[]{"login_password", Constants.staticpasswordstr},
                    new String[]{"content", editcommentforweibo1.getText().toString()},
                    //目标用户的id
                    new String[]{"to", touidstr}
            ));
            finish();
        }
    }

    class atsbicon1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(personalletterActivity.this, atfriendActivity.class);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UNAME);
        }
    }

    class facelibrary1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if ("0".equals(isdisplayfacelibrary)) {
                facegridView1.setVisibility(View.VISIBLE);
                isdisplayfacelibrary = "1";

                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        editcommentforweibo1.getWindowToken(), 0);
                // if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
                // {
                // 隐藏软键盘
                // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                // }
            } else {
                editcommentforweibo1.setFocusable(true);
                editcommentforweibo1.setFocusableInTouchMode(true);
                facegridView1.setVisibility(View.GONE);
                isdisplayfacelibrary = "0";
            }

        }

    }

    class editcommentforweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            facegridView1.setVisibility(View.GONE);
        }

    }

    class goback1_listener implements OnClickListener {

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
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "发评论成功", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.makeText(getApplicationContext(), "发评论失败,请重试", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
