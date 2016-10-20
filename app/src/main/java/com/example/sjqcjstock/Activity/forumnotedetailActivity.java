package com.example.sjqcjstock.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.HeadImageAdapter;
import com.example.sjqcjstock.adapter.replyinfoAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.javaScriptfaces.JavascriptInterface;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.ShareUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.ImageWebViewClient;
import com.example.sjqcjstock.view.SoListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微博详细显示页面（正文页）
 */
public class forumnotedetailActivity extends Activity implements
        OnClickListener {

    //弹出对话框的组件
    private Button bt_ok;
    private EditText et_inputCount;
    private TextView tv_restCount;
    private View view;
    // 判断转发的原帖微博是否点赞
    private String surceis_digg;
    // 判断微博是否点赞
    private String is_digg;
    private String Collectionstr;
    // 获得微博id
    private String weiboidstr;
    // 获取微博点赞数
    private String digg_countstr;
    // 获取uid
    private String uidstr;
    // 获取微博id
    private String feed_idstr;
    // 获取转发id
    private String source_feed_idstr;
    // 获取转发用户id
    private String sourceuidstr;
    // 获取微博类型
    private String typestr;
    // 获取微博介绍
    private Object introduction;
    // 获取查看的水晶币
    private String rewardStr;
    // 判断文章类型是否受付费文章（0：不是）
    private String state;
    // 判断是否是打赏过的文章
    private Object payState;
    // 被打赏用户的ID
    private String touid;
    // 主体控件
    private LinearLayout mailTextLl;
    // 获取控件
    // 付费文章的说明tv
    private TextView rewardExplain;
    // 摘要的TextView
    private TextView coment;
    private TextView username1;
    private TextView create_time1;
    private TextView repost_count1;
    private TextView digg_count1;
    private TextView comment_count1;
    private ImageView headimg1;
    // 标题的tv
    private TextView feedtitle;
    // 打赏的水晶币
    private TextView rewardTv;
    private ImageView praise1;
    private ImageView cellectloge1_img;
    private LinearLayout transpond1;
    private LinearLayout collectnote1;
    private TextView cellectloge1;
    private LinearLayout commentweibo1;
    private WebView wv_ad_tv;
    private LinearLayout goback1;
    private TextView isdiggsign1;
    private LinearLayout pickpraise1;
    private RelativeLayout repostcounts;
    private TextView repostrepost_count1;
    private TextView repostdigg_count1;
    private TextView repostcomment_count1;
    private TextView repostusername1;
    private TextView repostweibocomment1;
    private TextView reward1;
    private LinearLayout repostuser1;
    private LinearLayout repostlin1;
    private ImageView praiseimageView1;
    private LinearLayout share1;
    private LinearLayout pickuserinfo2;
    private RelativeLayout pickcommentoptions1;
    private RelativeLayout cancelcommentoptions1;
    private RelativeLayout pickreplycomment1;
    private RelativeLayout pickdeletecomment1;
    private ImageView iv_sang;
    private RelativeLayout rl_dialogDismiss;
    // 水晶币说明的全部模块
    private RelativeLayout explainRl;
    // 水晶币说明文字
    private TextView explainTv;
    // 水晶币更多
    private TextView moreTv;
    // 水晶币头像显示gv
    private GridView headsGv;
    // gridView的控制器
    private HeadImageAdapter imageAdapter;
    // 打赏水晶的人数
    private String reward_count = "0";
    // 打赏水晶的数量
    private String amount_count = "0";
    // 获取打赏水晶的人的信息集合
    private List<Map<String, Object>> asslist;
    // 打赏的水晶币个数
    private String sjbCount = "0";
    // 定义XList集合容器
    private replyinfoAdapter replyinfoAdapter;
    private ArrayList<HashMap<String, Object>> listreplyinfoData;
    // 回复列表
    private SoListView replyinfolistview;
    // 网络请求提示
    private ProgressDialog dialog;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 转发数
    private String comment_countstr;
    private String repost_countstr;
    // 当前list下标
    private int currentposition;
    // 标题
    private String titelStr = "";
    private String contentSt = "";
    // 加载更多的控件
    private RelativeLayout xlistviewFooterContent;
    private ProgressBar xlistviewFooterProgressbar;
    private TextView xlistviewFooterHintTextview;
    private ImageView vipImg;
    private ImageView vipImgSource;
    private ScrollView myScrollView;

    public void commentoptions(int position) {
        // 4表示禁言用户不能回复评论
        if (Constants.userType.equals("4")) {
            return;
        }
        currentposition = position;
        pickcommentoptions1.setVisibility(View.VISIBLE);
        if (Constants.staticmyuidstr.equals(listreplyinfoData.get(
                currentposition).get("uid"))
                || Constants.staticmyuidstr.equals(listreplyinfoData
                .get(currentposition).get("feeduid"))) {
            pickdeletecomment1.setVisibility(View.VISIBLE);
        } else {
            pickdeletecomment1.setVisibility(View.GONE);

        }
        pickcommentoptions1.getBackground().setAlpha(150);
    }

    //更新微博评论的方法
    private void refercommentreply() {
        // TODO Auto-generated method stub
        listreplyinfoData.clear();
        current = 1;
        geneItems();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemdetail_forumnotedetail);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        // 可以在主线程中使用http网络访问
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 当评论或者回复后进行页面刷新
        refercommentreply();
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();

        Intent intent = getIntent();
        uidstr = intent.getStringExtra("uid");
        weiboidstr = intent.getStringExtra("weibo_id");
        source_feed_idstr = intent.getStringExtra("sourceweibo_id");
        sourceuidstr = intent.getStringExtra("sourceuid");
        typestr = intent.getStringExtra("type");
        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        mailTextLl = (LinearLayout) findViewById(R.id.mail_text_ll);
        explainRl = (RelativeLayout) findViewById(R.id.shuijinbi_explain_rl);
        explainTv = (TextView) findViewById(R.id.shuijinbi_explain_tv);
        moreTv = (TextView) findViewById(R.id.shuijinbi_more_tv);
        moreTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶打赏列表页面
                Intent intent = new Intent(forumnotedetailActivity.this,
                        RewardListAcitivity.class);
                intent.putExtra("feed_id", weiboidstr);
                startActivity(intent);
            }
        });

        headsGv = (GridView) findViewById(R.id.shuijinbi_head_gv);
        headsGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击头像跳转到个人中心
                String userId = ((TextView) view.findViewById(R.id.head_uid_tv)).getText() + "";
                Intent intent = new Intent(forumnotedetailActivity.this,
                        UserDetailNewActivity.class);
                intent.putExtra("uid", userId);
                startActivity(intent);
            }
        });

        iv_sang = (ImageView) findViewById(R.id.iv_sang);
        iv_sang.setOnClickListener(new iv_sang_listener());

        coment = (TextView) findViewById(R.id.coment);
        rewardExplain = (TextView) findViewById(R.id.reward_explain);
        username1 = (TextView) findViewById(R.id.username1);
        create_time1 = (TextView) findViewById(R.id.create_time1);
        repost_count1 = (TextView) findViewById(R.id.repost_count1);
        digg_count1 = (TextView) findViewById(R.id.digg_count1);
        comment_count1 = (TextView) findViewById(R.id.comment_count1);
        headimg1 = (ImageView) findViewById(R.id.headimg1);
        feedtitle = (TextView) findViewById(R.id.feedtitle);
        rewardTv = (TextView) findViewById(R.id.reward);
        praise1 = (ImageView) findViewById(R.id.praise1);
        cellectloge1_img = (ImageView) findViewById(R.id.cellectloge1_img);
        transpond1 = (LinearLayout) findViewById(R.id.transpond1);
        collectnote1 = (LinearLayout) findViewById(R.id.collectnote1);
        cellectloge1 = (TextView) findViewById(R.id.cellectloge1);
        commentweibo1 = (LinearLayout) findViewById(R.id.commentweibo1);
        wv_ad_tv = (WebView) findViewById(R.id.wv_ad);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        isdiggsign1 = (TextView) findViewById(R.id.isdiggsign1);
        pickpraise1 = (LinearLayout) findViewById(R.id.pickpraise1);
        repostlin1 = (LinearLayout) findViewById(R.id.repostlin1);
        repostcounts = (RelativeLayout) findViewById(R.id.repostcounts);
        pickuserinfo2 = (LinearLayout) findViewById(R.id.pickuserinfo2);
        pickcommentoptions1 = (RelativeLayout) findViewById(R.id.pickcommentoptions1);
        cancelcommentoptions1 = (RelativeLayout) findViewById(R.id.cancelcommentoptions1);
        pickreplycomment1 = (RelativeLayout) findViewById(R.id.pickreplycomment1);
        pickdeletecomment1 = (RelativeLayout) findViewById(R.id.pickdeletecomment1);

        repostrepost_count1 = (TextView) findViewById(R.id.repostrepost_count1);
        repostdigg_count1 = (TextView) findViewById(R.id.repostdigg_count1);
        repostcomment_count1 = (TextView) findViewById(R.id.repostcomment_count1);
        repostuser1 = (LinearLayout) findViewById(R.id.repostuser1);

        repostusername1 = (TextView) findViewById(R.id.repostusername1);
        repostweibocomment1 = (TextView) findViewById(R.id.repostweibocomment1);
        reward1 = (TextView) findViewById(R.id.reward1);
        praiseimageView1 = (ImageView) findViewById(R.id.praiseimageView1);
        share1 = (LinearLayout) findViewById(R.id.share1);

        //加载更多的控件
        xlistviewFooterContent = (RelativeLayout) findViewById(R.id.xlistview_footer_content);
        xlistviewFooterProgressbar = (ProgressBar) findViewById(R.id.xlistview_footer_progressbar);
        xlistviewFooterHintTextview = (TextView) findViewById(R.id.xlistview_footer_hint_textview);
        xlistviewFooterContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                xlistviewFooterProgressbar.setVisibility(View.VISIBLE);
                xlistviewFooterHintTextview.setVisibility(View.GONE);
                geneItems();
            }
        });
        transpond1.setOnClickListener(new transpond1_listener());
        collectnote1.setOnClickListener(new collectnote1_listener());
        commentweibo1.setOnClickListener(new commentweibo1_listener());
        headimg1.setOnClickListener(new headimg1_listener());
        goback1.setOnClickListener(new goback1_listener());
        pickpraise1.setOnClickListener(new pickpraise1_listener());
        repostuser1.setOnClickListener(new repostuser1_listener());
        repostlin1.setOnClickListener(new repostlin1_listener());
        praiseimageView1.setOnClickListener(new praiseimageView1_listener());
        share1.setOnClickListener(new share1_listener());
        pickuserinfo2.setOnClickListener(new pickuserinfo2_listener());
        cancelcommentoptions1.setOnClickListener(new cancelcommentoptions1_listener());
        pickreplycomment1.setOnClickListener(new pickreplycomment1_listener());
        pickdeletecomment1.setOnClickListener(new pickdeletecomment1_listener());

        pickcommentoptions1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击自身空白部分就消失
                pickcommentoptions1.setVisibility(View.GONE);
            }
        });

        // 开线程获取水晶币数量
        new SendInfoTaskmywealth().execute(new TaskParams(Constants.appUserMoneyUrl,
                new String[]{"mid", Constants.staticmyuidstr}
        ));

        // 获取微博详细信息
        new SendInfoTasknotedetail().execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=Appfeed",
                new String[]{"feed_id", weiboidstr},
                new String[]{"mid", Constants.staticmyuidstr}
        ));
//        geneItems();
        replyinfolistview = (SoListView) findViewById(R.id.replyinfolist2);
        // 存储数据的数组列表
        listreplyinfoData = new ArrayList<HashMap<String, Object>>();
        // 为ListView 添加适配器
        replyinfoAdapter = new replyinfoAdapter(forumnotedetailActivity.this, this, listreplyinfoData);

        replyinfolistview.setAdapter(replyinfoAdapter);
        vipImg = (ImageView) findViewById(R.id.vip_img);
        vipImgSource = (ImageView) findViewById(R.id.vip_img_source);
    }


    //删除微博，这部分内容暂时取消
    class pickdeletecomment1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            // 删除微博评论
            new AlertDialog.Builder(forumnotedetailActivity.this)
                    .setMessage("确认删除吗")
                    .setPositiveButton("是",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    pickcommentoptions1.setVisibility(View.GONE);
                                    new SendInfoTaskdeleteweibo()
                                            .execute(new TaskParams(
                                                    Constants.Url + "?app=public&mod=AppFeedList&act=Appdelcomment",
                                                    new String[]{"mid", Constants.staticmyuidstr},
                                                    new String[]{"login_password", Constants.staticpasswordstr},
                                                    new String[]{"comment_id", (String) listreplyinfoData.get(currentposition).get("comment_id")}
                                            ));
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss(); //关闭alertDialog
                                }
                            }).show();
        }
    }

    //回复微博，这部分内容隐藏
    class pickreplycomment1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            pickcommentoptions1.setVisibility(View.GONE);
            try {
                Intent intent = new Intent(getApplicationContext(), addreplycommentweiboActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("feed_id", (String) listreplyinfoData.get(currentposition).get("feed_id"));
                intent.putExtra("feeduid", (String) listreplyinfoData.get(currentposition).get("feeduid"));
                intent.putExtra("to_uid", (String) listreplyinfoData.get(currentposition).get("uid"));
                intent.putExtra("comment_id", (String) listreplyinfoData.get(currentposition).get("comment_id"));
                intent.putExtra("oldname", (String) listreplyinfoData.get(currentposition).get("uname"));
                startActivity(intent);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {

            }
        }
    }

    class cancelcommentoptions1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            pickcommentoptions1.setVisibility(View.GONE);
        }

    }

    class pickuserinfo2_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(forumnotedetailActivity.this,
                    UserDetailNewActivity.class);
            intent.putExtra("uid", uidstr);
            startActivity(intent);
        }

    }

    /**
     * 分享的单机事件
     */
    class share1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            ShareUtil.showShare(forumnotedetailActivity.this, weiboidstr, titelStr, contentSt);
        }
    }

    // 对原帖微博进行点赞
    class praiseimageView1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    // 对微博进行点赞
    class pickpraise1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 通过网络范围进行点赞操作
            if ("0".equals(is_digg)) {
                // 进行点赞操作

                if (Utils.isFastDoubleClick()) {
                    return;
                }

                // geneItems();
                // mAdapter.notifyDataSetChanged();
                new SendInfoTaskpraise().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AddDigg",
                        new String[]{"mid", Constants.staticmyuidstr},
                        new String[]{"login_password", Constants.staticpasswordstr},
                        new String[]{"feed_id", weiboidstr}
                ));
            } else {
                // 进行取消点赞操作
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                new SendInfoTaskcancelpraise().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=DelDigg", new String[]{"mid",
                        Constants.staticmyuidstr},
                        new String[]{"login_password",
                                Constants.staticpasswordstr}, new String[]{
                        "feed_id", weiboidstr}
                ));
            }
        }
    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }
    }

    class repostlin1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(), forumnotedetailActivity.class);
            intent.putExtra("weibo_id", source_feed_idstr);
            intent.putExtra("uid", sourceuidstr);
            startActivity(intent);
        }
    }

    class repostuser1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(), UserDetailNewActivity.class);
            intent.putExtra("uid", sourceuidstr);
            startActivity(intent);
        }
    }

    class headimg1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(forumnotedetailActivity.this,
                    UserDetailNewActivity.class);
            if (uidstr == null || "".equals(uidstr) || "1".equals(uidstr)) {
                intent.putExtra("uid", touid);
            } else {
                intent.putExtra("uid", uidstr);
            }
            startActivity(intent);
        }

    }

    class commentweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(forumnotedetailActivity.this,
                    addcommentweiboActivity.class);
            intent.putExtra("feed_id", weiboidstr);
            intent.putExtra("feeduid", uidstr);

            startActivity(intent);

        }

    }

    class iv_sang_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (Constants.staticmyuidstr.equals(touid)) {
                // 自己不能给自己打赏
                CustomToast.makeText(getApplicationContext(), "对不起不能给自己打赏。", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            view = LayoutInflater.from(forumnotedetailActivity.this).inflate(R.layout.dialog_sang, null);
            rl_dialogDismiss = (RelativeLayout) view.findViewById(R.id.rl_dialogDismiss);
            et_inputCount = (EditText) view.findViewById(R.id.et_inputCount);

            // 判断是否是付费文章在判断是否是被打赏过的如果打赏过就可以随意输入否则只能默认最低数不能输入
            if ("1".equals(state) && payState != null && payState.toString().equals("0")) {
                // 设置当前打赏的水晶币
                et_inputCount.setText(rewardStr);
                // 禁止手机软键盘
                et_inputCount.setInputType(InputType.TYPE_NULL);
            } else {
                // 开启软键盘
                et_inputCount.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            // 显示水晶币数量的控件
            tv_restCount = (TextView) view.findViewById(R.id.tv_restCount);
            tv_restCount.setText(Constants.shuijinbiCount);
            bt_ok = (Button) view.findViewById(R.id.bt_ok);

            final AlertDialog alertDialog = new AlertDialog.Builder(forumnotedetailActivity.this).setView(view).show();

            rl_dialogDismiss.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    //销毁弹出框
                    alertDialog.dismiss();
                }
            });
            bt_ok.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    sjbCount = et_inputCount.getText() + "";
                    if ("".equals(sjbCount.trim())) {
                        CustomToast.makeText(getApplicationContext(), "请输入打赏水晶币数。", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                        CustomToast.makeText(getApplicationContext(), "对不起你金币不足。", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    if (Integer.valueOf(sjbCount) < 1) {
                        CustomToast.makeText(getApplicationContext(), "对不起打赏不能为零。", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    if (!((CheckBox) view.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                        CustomToast.makeText(getApplicationContext(), "请阅读付费阅读及打赏协议", Toast.LENGTH_LONG).show();
                        return;
                    }

                    alertDialog.dismiss();
                    //提交数据到服务器
                    new SendInfoTaskReward().execute(new TaskParams(Constants.apprewardUrl,
                            new String[]{"mid", Constants.staticmyuidstr},
                            new String[]{"reward_coin", sjbCount},// 打赏金额
                            new String[]{"weibo_id", weiboidstr},// 打赏微博ID
                            new String[]{"touid", touid},// 被打赏用户ID
                            new String[]{"state", state}////微博状态0标识免费微博，1标识付费微博
                    ));
                }
            });

            view.findViewById(R.id.sang_agreement_tv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到水晶币协议页面
                    Intent intent = new Intent(forumnotedetailActivity.this, AgreementActivity.class);
                    startActivity(intent);
                }
            });
            view.findViewById(R.id.bt_cz).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到充值页面
                    Intent intent = new Intent(forumnotedetailActivity.this, RechargeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    class transpond1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(forumnotedetailActivity.this, transpondweiboActivity.class);
            intent.putExtra("feed_id", feed_idstr);
            intent.putExtra("feeduid", uidstr);
            startActivity(intent);
        }
    }

    class collectnote1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            if ("0".equals(Collectionstr)) {
                new SendInfoTaskcellectweibo()
                        .execute(new TaskParams(
                                        Constants.Url + "?app=public&mod=AppFeedList&act=AddCollection", new String[]{
                                        "mid", Constants.staticmyuidstr},
                                        new String[]{"login_password",
                                                Constants.staticpasswordstr},
                                        new String[]{"feed_id", weiboidstr}
                                )
                        );
                Collectionstr = "1";
            } else {
                new SendInfoTaskcancelcellectweibo()
                        .execute(new TaskParams(
                                        Constants.Url + "?app=public&mod=AppFeedList&act=DelCollection", new String[]{
                                        "mid", Constants.staticmyuidstr},
                                        new String[]{"login_password",
                                                Constants.staticpasswordstr},
                                        new String[]{"feed_id", weiboidstr}
                                )
                        );
                Collectionstr = "0";
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            /**
             * case R.id.show_more: { if (mState == SPREAD_STATE) {
             * coment.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
             * coment.requestLayout(); mImageShrinkUp.setVisibility(View.GONE);
             * mImageSpread.setVisibility(View.VISIBLE); mState = SHRINK_UP_STATE; }
             * else if (mState == SHRINK_UP_STATE) {
             * coment.setMaxLines(Integer.MAX_VALUE); coment.requestLayout();
             * mImageShrinkUp.setVisibility(View.VISIBLE);
             * mImageSpread.setVisibility(View.GONE); mState = SPREAD_STATE; }
             * break; } default: { break; }
             */

        }
    }

    private class SendInfoTasknotedetail extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // 作者名字
            String unamestr = "";
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";

                List<Map<String, Object>> lists = null;
                String statusstr = null;
                List<Map<String, Object>> weibolists = null;

                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if ("0".equals(map.get("status").toString())) {
                        CustomToast.makeText(getApplicationContext(), map.get("info").toString(), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        return;
                    }
                    if (statusstr == null) {
                        statusstr = map.get("data").toString();
                        weibolists = JsonTools.listKeyMaps("[" + statusstr + "]");
                    }
                    for (Map<String, Object> weibomap : weibolists) {
                        //该字段用于获取微博详情
                        String feedinfostr = weibomap.get("feedinfo") + "";
                        List<Map<String, Object>> feedinfoList = JsonTools.listKeyMaps("[" + feedinfostr + "]");
                        JSONObject jsonuserinfo = null;
                        try {
                            jsonuserinfo = new JSONObject(weibomap.get("userinfo") + "");
                            // 被打赏微博的用户ID
                            touid = jsonuserinfo.getString("uid");
                            unamestr = jsonuserinfo.get("uname") + "";
                            String avatar_middlestr = jsonuserinfo.get("avatar_middle")
                                    + "";
                            username1.setText(unamestr);
                            ImageLoader.getInstance().displayImage(avatar_middlestr,
                                    headimg1, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                            String userGroup = jsonuserinfo.getString("user_group");
                            userGroup = userGroup.replace("\\", "");
                            // 显示Vip标识的
                            ViewUtil.setUpVip(userGroup + "", vipImg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (Map<String, Object> userCreditmap : feedinfoList) {

                            String feed_titlestr;
                            //微博每个字段的内容
                            feed_idstr = userCreditmap.get("feed_id") + "";
                            String typestr = userCreditmap.get("type") + "";
                            String publish_timestr = userCreditmap.get("publish_time") + "";

                            comment_countstr = userCreditmap.get("comment_count") + "";
                            repost_countstr = userCreditmap.get("repost_count") + "";
                            digg_countstr = userCreditmap.get("digg_count") + "";
                            Collectionstr = userCreditmap.get("Collection") + "";
                            is_digg = userCreditmap.get("is_digg") + "";
                            introduction = userCreditmap.get("introduction");
                            //判断是否已经收藏
                            if ("1".equals(Collectionstr)) {
                                cellectloge1.setText("已收藏");
                                cellectloge1_img.setImageResource(R.mipmap.soucang2_l);
                            }
                            //判断是否已经点赞
                            if ("1".equals(is_digg)) {
                                isdiggsign1.setText("已点赞");
                                praise1.setImageResource(R.mipmap.praise6_l);
                            }

                            //StringBuilder拼接“内容+图片地址”作为webview的内容
                            StringBuilder postimgsbdstr = new StringBuilder();

                            //正文页中含有的图片
                            if ("postimage".equals(typestr)) {
                                //获取图片的地址，http://www.sjqcj.com/data/upload/  +  解析attach_url得到的地址，即可获得图片
                                if (userCreditmap.get("attach_url") != null) {
                                    String attach_url = userCreditmap.get("attach_url") + "";
                                    // 解析短微博图片地址
                                    attach_url = attach_url.substring(1, attach_url.length() - 1);
                                    String[] attach_urlstrs = attach_url.split(",");
                                    for (int i = 0; i < attach_urlstrs.length; i++) {
                                        String attachurl = attach_urlstrs[i];
                                        attachurl = attachurl.substring(1, attachurl.length() - 1);
                                        postimgsbdstr.append("<img onclick=\"getimg(this);\" src=\"");
                                        postimgsbdstr.append("http://www.sjqcj.com/data/upload/");
                                        postimgsbdstr.append(attachurl);
                                        postimgsbdstr.append("\"  height=\"150px\" width=\"150px\" />&nbsp;&nbsp;");
                                    }
                                }
                            }


                            //微博的内容
                            String contentstr = userCreditmap.get("feed_content_android") + "";
                            //微博中添加的网址
                            List<String> contentUrl = (List<String>) userCreditmap.get("feed_content_android_url");

                            contentSt = Html.fromHtml(contentstr) + "";

                            contentstr = " <html><head><style type=\"text/css\">body{font-size:16px;}</style>   <script type=\"text/javascript\">         	  function getimg(th) { 	var btsrc=th.src;		getimage.getImageView(btsrc); 	  }	 </script></head><body>" + contentstr;
                            //处理微博内容的标题，【】里面的内容为#4471BC色
                            contentstr = contentstr.replace("<feed-titlestyle='display:none'>", "<font color=\"#4471BC\" >【");
                            contentstr = contentstr.replace("</feed-title>", "】</font><br/>");
                            contentstr = contentstr.replace("/data/upload", "http://www.sjqcj.com/data/upload");
                            contentstr = contentstr.replace("http://www.sjqcj.comhttp://www.sjqcj.com/data/upload", "http://www.sjqcj.com/data/upload");
                            contentstr = contentstr.replace("<img", "<img onclick=\"getimg(this);\" style=\"max-width: 100%;height:auto\"  ");
                            contentstr = contentstr.replace("[", "<img src=\"http://www.sjqcj.com/addons/theme/stv1/_static/image/expression/miniblog/");
                            contentstr = contentstr.replace("]", ".gif\"  style=\"width:20px;height:20px;padding-top:0px;margin-top:0px\" > ");

                            int count = contentstr.indexOf("【") + 1;
                            if (count > 0) {
                                titelStr = contentstr.substring(count, contentstr.indexOf("】"));
                                contentSt = Html.fromHtml(contentstr.substring(contentstr.indexOf("】") + 1)) + "";
                            } else {
                                titelStr = unamestr + ":";
                            }

                            // 将时间戳转换成date
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(Long.parseLong(publish_timestr) * 1000); // 获取当前时间
                            publish_timestr = formatter.format(curDate);

                            publish_timestr = CalendarUtil.formatDateTime(publish_timestr);

                            // username1.setText(uidstr);
                            create_time1.setText(publish_timestr);
                            repost_count1.setText(repost_countstr);
                            digg_count1.setText(digg_countstr);
                            comment_count1.setText(comment_countstr);
                            Object stateObj = userCreditmap.get("state");
                            stateObj = stateObj == null ? 0 : stateObj;
                            state = stateObj + "";
                            // 是否是打赏了的文章 0:未打赏 1：打赏
                            payState = userCreditmap.get("PayState");
                            if (!"0".equals(state)) {
                                rewardExplain.setVisibility(View.VISIBLE);
                                rewardStr = userCreditmap.get("reward") + "";
                                feed_titlestr = contentstr.substring(contentstr.indexOf("<font color=\"#4471BC\" >【"), contentstr.indexOf("】</font>") + 8);
                                feed_titlestr = feed_titlestr.replaceFirst("【", " ");
                                feed_titlestr = feed_titlestr.replaceFirst("】", " ");
                                // 是付费文章
                                feedtitle.setText(Html.fromHtml(feed_titlestr));
                                titelStr = feedtitle.getText() + "";
                                // 显示需要打赏水晶币数量
                                rewardTv.setText(rewardStr);
                                rewardTv.setVisibility(View.VISIBLE);
                                if (Constants.staticmyuidstr.equals(touid)) {
                                    // 如果是自己的文章就直接显示已经阅读过了
                                    payState = "1";
                                }
                                contentSt = introduction + "";
                                contentstr = contentstr.substring(0, contentstr.indexOf("】</font><br/>") + 13) + "摘要：" + introduction + "<br/>" + contentstr.substring(contentstr.indexOf("】</font><br/>") + 13);
                                if (payState == null || "0".equals(payState.toString())) {

                                    // --------- 给管理人员用的不用进行打赏就可以阅读文章（注释掉是給管理员用的）
                                    wv_ad_tv.setVisibility(View.GONE);

                                    // 显示摘要
                                    coment.setText("摘要：" + introduction);
                                    coment.setVisibility(View.VISIBLE);
                                    feedtitle.setVisibility(View.VISIBLE);
                                } else {
                                    wv_ad_tv.setVisibility(View.VISIBLE);
                                    coment.setVisibility(View.GONE);
                                    feedtitle.setVisibility(View.GONE);
                                    // 修改说明语句
                                    rewardExplain.setText("本文是付费文章，您已阅读");
                                }
                            } else {
                                wv_ad_tv.setVisibility(View.VISIBLE);
                                rewardTv.setVisibility(View.GONE);
                                rewardExplain.setVisibility(View.GONE);
                            }
                            contentstr = contentstr.replaceFirst("【", "");
                            contentstr = contentstr.replaceFirst("】", "");
                            //内容+图片的地址拼接，作为WebView的内容
                            if (postimgsbdstr != null) {
                                contentstr = contentstr + "<br/><br/>" + postimgsbdstr + "";
                            }
                            contentstr = contentstr + "</body></html>";
                            //全文

                            wv_ad_tv.getSettings().setJavaScriptEnabled(true);

                            // 给WebView添加js交互接口类(用于图片点击放大)
                            wv_ad_tv.addJavascriptInterface(new JavascriptInterface(forumnotedetailActivity.this), "imagelistner");

                            wv_ad_tv.setWebViewClient(new ImageWebViewClient(wv_ad_tv, forumnotedetailActivity.this));

                            if (contentUrl != null) {
                                contentstr = Utils.replaceWebUrl(contentUrl, contentstr);
                            }

                            wv_ad_tv.loadDataWithBaseURL(null, contentstr, "text/html", "utf-8", null);

                            wv_ad_tv.setWebChromeClient(new WebChromeClient() {
                                public void onProgressChanged(WebView view, int newProgress) {
                                    if (newProgress > 95) {
                                        findViewById(R.id.web_jiazai_xianshi).setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            // 打赏水晶的人数
                            reward_count = userCreditmap.get("reward_count") + "";
                            // 打赏水晶的数量
                            amount_count = userCreditmap.get("amount_count") + "";
                            // 获取打赏水晶的人的信息集合
                            asslist = JsonTools.listKeyMaps(userCreditmap.get("asslist").toString());
                            if (Constants.staticmyuidstr.equals(touid) || "0".equals(state)) {
                                explainRl.setVisibility(View.VISIBLE);
                                if (asslist.size() > 0) {
                                    explainTv.setText("已有 " + reward_count + " 人共打赏 " + amount_count + " 水晶币");
                                    // 更多显示
                                    moreTv.setVisibility(View.VISIBLE);
                                    imageAdapter = new HeadImageAdapter(asslist, forumnotedetailActivity.this);
                                    headsGv.setAdapter(imageAdapter);
                                }
                            }
                        }

                        //该字段用于获取转发微博详情
                        Object repostStr = weibomap.get("repost");
                        if (repostStr != null && !"".equals(repostStr)) {
                            repostlin1.setVisibility(View.VISIBLE);
                            repostcounts.setVisibility(View.VISIBLE);
                            try {
                                JSONObject jsonObject = new JSONObject(repostStr.toString());
                                String comment_countstr = jsonObject.getString(
                                        "comment_count");
                                String repost_countstr = jsonObject.getString(
                                        "repost_count");
                                digg_countstr = jsonObject.getString("digg_count");
                                Collectionstr = jsonObject.getString("Collection");
                                surceis_digg = jsonObject.getString("is_digg");
                                if ("1".equals(surceis_digg)) {
                                    praiseimageView1
                                            .setImageResource(R.mipmap.praise6_l);
                                }
                                String contentstr = jsonObject.getString(
                                        "feed_content_android");

                                // 正则表达式处理 去Html代码
                                String regex = "\\<[^\\>]+\\>";
                                contentstr = contentstr.replaceAll(regex, "");
                                contentstr = contentstr.replace("\n\n\n", "");
                                contentstr = contentstr.replace("\t", "");
                                contentstr = contentstr.replace("\n", "");
                                contentstr = contentstr.replace("&nbsp;", "");
                                contentstr = contentstr.replace("　", "");
                                contentstr = contentstr.replace("<feed-titlestyle='display:none'>", "<font color=\"#4471BC\" >【");
                                contentstr = contentstr.replace("<feed-title style='display:none'>", "<font color=\"#4471BC\" >【");
                                contentstr = contentstr.replace("</feed-title>", "】</font><Br/>");

                                repostrepost_count1.setText(repost_countstr);
                                repostdigg_count1.setText(digg_countstr);
                                repostcomment_count1.setText(comment_countstr);
                                repostweibocomment1.setText(Html.fromHtml(contentstr));

                                int count = contentstr.indexOf("【") + 1;
                                if (count > 0) {
                                    titelStr = contentstr.substring(count, contentstr.indexOf("】"));
                                    contentSt = Html.fromHtml(contentstr.substring(contentstr.indexOf("】") + 1)) + "";
                                }

                                String introductionObj = jsonObject.optString("introduction");
                                if (!"".equals(introductionObj.trim()) && !"null".equals(introductionObj)) {
                                    contentstr = "摘要：" + introductionObj + "";
                                    repostweibocomment1.setText(contentstr);
                                    reward1.setText(jsonObject.getString(
                                            "reward"));
                                    reward1.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // 该字段用于获取转发微博用户详情
                        Object repostUserinfoStr = weibomap.get("repost_userinfo");
                        if (repostUserinfoStr != null && !"".equals(repostUserinfoStr)) {
                            try {
                                JSONObject jsonObject = new JSONObject(repostUserinfoStr.toString());
                                repostusername1.setText(jsonObject.getString("uname"));
                                String userGroup = jsonObject.getString("user_group");
                                userGroup = userGroup.replace("\\", "");
                                ViewUtil.setUpVip(userGroup, vipImgSource);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            mailTextLl.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    private class SendInfoTasknotereplylist extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";

                List<Map<String, Object>> lists2 = null;
                String statusstr2 = null;
                int count = 0;
                List<Map<String, Object>> datalists2 = null;
                String datastr2 = null;
                List<Map<String, Object>> weibolists2 = null;

                // 解析json字符串获得List<Map<String,Object>>
                if (lists2 == null) {
                    lists2 = JsonTools.listKeyMaps(result);
                }

                for (Map<String, Object> map : lists2) {
                    if (statusstr2 == null) {
                        statusstr2 = map.get("data") + "";
                        if ("".equals(statusstr2)) {
                            // 表示没有评论
                            return;
                        }
                        datalists2 = JsonTools.listKeyMaps("[" + statusstr2
                                + "]");
                    }
                    for (Map<String, Object> datamap : datalists2) {
                        if (datastr2 == null) {
                            datastr2 = datamap.get("data") + "";
                            if (datamap.get("count") != null) {
                                count = Integer.valueOf(datamap.get("count").toString());
                            }
                            if ("".equals(datastr2.trim())) {
                                datastr2 = "[]";
                            }
                            weibolists2 = JsonTools.listKeyMaps(datastr2);
                        }
                        if (weibolists2 == null) {
                            weibolists2 = new ArrayList<Map<String, Object>>();
                        }
                        for (Map<String, Object> weibomap : weibolists2) {

                            String ctimestr = weibomap.get("ctime") + "";
                            String contentstr = weibomap.get("content")
                                    + "";
                            String storeystr = weibomap.get("storey")
                                    + "";

                            String comment_idstr = weibomap.get("comment_id")
                                    + "";

                            contentstr = contentstr.replace("'__THEME__/image/expression/miniblog/", "\"");
                            contentstr = contentstr.replace(".gif'", "\"");
                            contentstr = contentstr.replace("imgsrc", "img src");

                            CharSequence contentstrcharSequence = Html
                                    .fromHtml(contentstr, ImageUtil.getImageGetter(getResources()), null);

                            ctimestr = CalendarUtil.formatDateTime(Utils
                                    .getStringtoDate(ctimestr));

                            HashMap<String, Object> map3 = new HashMap<String, Object>();
                            map3.put("ctime", ctimestr);
                            map3.put("content", contentstrcharSequence);
                            map3.put("storey", storeystr);
                            map3.put("feed_id", feed_idstr);
                            map3.put("feeduid", uidstr);
                            map3.put("comment_id", comment_idstr);

                            String user_infostr = weibomap.get("user_info")
                                    + "";
                            List<Map<String, Object>> user_infolists = JsonTools
                                    .listKeyMaps("[" + user_infostr + "]");

                            for (Map<String, Object> user_infomap : user_infolists) {
                                String unamestr2;
                                String avatar_middlestr2;
                                String uidstr;
                                if (user_infomap.get("uid") == null) {
                                    uidstr = "";
                                } else {
                                    uidstr = user_infomap.get("uid") + "";
                                }
                                if (user_infomap.get("uname") == null) {
                                    unamestr2 = "暂无";
                                } else {
                                    unamestr2 = user_infomap.get("uname")
                                            + "";
                                }
                                if (user_infomap.get("avatar_middle") == null) {
                                    avatar_middlestr2 = "http://www.sjqcj.com/data/upload/avatar/2a/2d/16/original_100_100.jpg?v1446619149";
                                } else {
                                    avatar_middlestr2 = user_infomap.get(
                                            "avatar_middle") + "";
                                }

                                String userGroup = user_infomap.get("user_group") + "";
                                map3.put("isVip", userGroup);
                                map3.put("uid", uidstr);
                                map3.put("uname", unamestr2);
                                map3.put("avatar_middle", avatar_middlestr2);
                            }
                            listreplyinfoData.add(map3);
                        }
                    }
                }
                replyinfoAdapter.notifyDataSetChanged();
//                if (current == 1) {
                    // 评论大于两页就不用滚动到顶部去
                    // 滚动到顶部
                    ViewUtil.setListViewHeightBasedOnChildren(replyinfolistview);
                    myScrollView.smoothScrollTo(0, 0);
//                }
                if (listreplyinfoData.size() >= count) {
                    xlistviewFooterContent.setVisibility(View.GONE);
                }
                xlistviewFooterProgressbar.setVisibility(View.INVISIBLE);
                xlistviewFooterHintTextview.setVisibility(View.VISIBLE);
            }
        }
    }

    private class SendInfoTaskpraise extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {

                super.onPostExecute(result);
                // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String infostr = map.get("info") + "";
                    String statusstr = map.get("status") + "";
                    String datastr = map.get("data") + "";

                    is_digg = "1";
                }

                int diggcount2 = Integer.parseInt(digg_count1.getText()
                        .toString());

                digg_count1.setText(String.valueOf(diggcount2 + 1));
                isdiggsign1.setText("已点赞");
                praise1.setImageResource(R.mipmap.praise6_l);

            }
        }

    }

    private class SendInfoTaskcancelpraise extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String infostr = map.get("info") + "";
                    String statusstr = map.get("status") + "";

                    if ("1".equals(statusstr)) {
                        is_digg = "0";
                    }
                }

                int diggcount2 = Integer.parseInt(digg_count1.getText()
                        .toString());
                digg_count1.setText(String.valueOf(diggcount2 - 1));
                isdiggsign1.setText("点赞");
                praise1.setImageResource(R.mipmap.praise6);

            }
        }

    }

    //onResume()时，调用该方法获取微博的数据
    private void geneItemsnotedetail() {
        new SendInfoTasknotedetail().execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=Appfeed",
                new String[]{"feed_id", weiboidstr},
                new String[]{"mid", Constants.staticmyuidstr}
        ));
    }

    //获取评论列表信息
    private void geneItems() {
        new SendInfoTasknotereplylist().execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=AppComment&p="
                        + current,
                new String[]{"feed_id", weiboidstr}
        ));
        current++;
    }

    //搜藏微博
    private class SendInfoTaskcellectweibo extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                cellectloge1.setText("已收藏");
                cellectloge1_img.setImageResource(R.mipmap.soucang2_l);
            }
        }
    }

    private class SendInfoTaskcancelcellectweibo extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {

            } else {
                super.onPostExecute(result);
                cellectloge1.setText("收藏");
                cellectloge1_img.setImageResource(R.mipmap.soucang2);

            }
        }

    }

    /**
     * 调用打赏水晶币接口的异步
     */
    private class SendInfoTaskReward extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                // 返回是否成功标识
                String status = "0";
                String msg = "对不起打赏失败,请重试";
                super.onPostExecute(result);
                try {
                    JSONObject jsonParser = new JSONObject(result);
                    status = jsonParser.getString("status");
                    msg = jsonParser.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    CustomToast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
                            .show();
                }
                // 如果显示打赏信息打赏后就更新打赏信息
                if (explainRl.getVisibility() == View.VISIBLE) {
                    listreplyinfoData.clear();
                    current = 1;
                    geneItemsnotedetail();
                    return;
                }
                CustomToast.makeText(getApplicationContext(), "打赏成功！", Toast.LENGTH_LONG)
                        .show();
                if (status.equals("1")) {
                    // 成功
                    payState = "1";
                    // 显示全文
                    wv_ad_tv.setVisibility(View.VISIBLE);
                    coment.setVisibility(View.GONE);
                    feedtitle.setVisibility(View.GONE);
                    // 修改说明语句
                    rewardExplain.setText("本文是付费文章，您已阅读");
                } else {
                    // 失败
                    CustomToast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    private class SendInfoTaskdeleteweibo extends AsyncTask<TaskParams, Void, String> {

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
                    refercommentreply();
                }
            }
        }
    }

    // 获取用户财富设置水晶币个数
    private class SendInfoTaskmywealth extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(forumnotedetailActivity.this, "", Toast.LENGTH_LONG).show();
            } else {
                String count = "0";
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    // 获取水晶币
                    count = jsonObj.getJSONObject("data").getJSONObject("credit").getJSONObject("shuijingbi").getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Constants.shuijinbiCount = count;
            }
        }
    }
}
