package com.example.sjqcjstock.Activity.Article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.RechargeActivity;
import com.example.sjqcjstock.Activity.RewardListAcitivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.transpondweiboActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.HeadImageAdapter;
import com.example.sjqcjstock.adapter.replyinfoAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.CommentListEntity;
import com.example.sjqcjstock.entity.Article.FeedEntity;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.javaScriptfaces.JavascriptInterface;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ShareUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.ImageWebViewClient;
import com.example.sjqcjstock.view.SoListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 20170705 修改
 * 微博详细显示页面（三级页面）
 */
public class ArticleDetailsActivity extends Activity {

    //弹出对话框的组件
    private Button bt_ok;
    private EditText et_inputCount;
    private TextView tv_restCount;
    private View view;
    // 判断转发的原帖微博是否点赞
//    private String surceis_digg;
    // 判断微博是否点赞
    private String is_digg;
    private String Collectionstr;
    // 获得微博id
    private String weiboidstr;
    //    // 获取微博点赞数
//    private String digg_countstr;
    // 获取uid
    private String uidstr;
    // 获取微博id
    private String feed_idstr;
    // 获取转发id(2017-06-21 改后可以不用上一个页面传回来了)
    private String source_feed_idstr;
    // 获取转发用户id
    private String sourceuidstr;
    //    // 获取微博类型
//    private String typestr;
//    // 获取微博介绍
//    private Object introduction;
    // 获取查看的水晶币
    private String rewardStr;
    // 判断文章类型是否受付费文章（0：不是）
    private String state = "0";
    // 判断是否是打赏过的文章
    private String payState;
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
    private List<FeedEntity.asslist> asslist;
    // 打赏的水晶币个数
    private String sjbCount = "0";
    // 定义XList集合容器
    private replyinfoAdapter replyinfoAdapter;
    private ArrayList<CommentListEntity> commentListEntities;
    // 回复列表
    private ListView replyinfolistview;
    // 网络请求提示
    private CustomProgress dialog;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 转发数
//    private String comment_countstr;
//    private String repost_countstr;
    // 当前list下标
    private int currentposition;
    // 标题
    private String titelStr = "";
    // 分享文章的主题内容
    private String contentSt = "";
    // 加载更多的控件
    private RelativeLayout xlistviewFooterContent;
    private ProgressBar xlistviewFooterProgressbar;
    private TextView xlistviewFooterHintTextview;
    private ImageView vipImg;
    private ImageView vipImgSource;
    private ScrollView myScrollView;
    // 微博接口返回数据
    private String feedStr;
    // 评论的接口数据
    private String commentStr;
    // 操作接口返回数据
    private String jsonStr;

//    private GoogleApiClient client;

    /**
     * 回复评论的处理事件
     *
     * @param position
     */
    public void commentoptions(int position) {
        // menghuan 不用登陆也可以用
        // 如果未登陆跳转到登陆页面
        if (!Constants.isLogin) {
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
//        // 4表示禁言用户不能回复评论
//        if (Constants.userType.equals("4")) {
//            return;
//        }
        currentposition = position;
        pickcommentoptions1.setVisibility(View.VISIBLE);
        if (Constants.staticmyuidstr.equals(commentListEntities.get(position).getUid())
                || Constants.staticmyuidstr.equals(uidstr)) {
            pickdeletecomment1.setVisibility(View.VISIBLE);
        } else {
            pickdeletecomment1.setVisibility(View.GONE);

        }
        pickcommentoptions1.getBackground().setAlpha(150);
    }

    //更新微博评论的方法
    private void refercommentreply() {
        commentListEntities.clear();
        current = 1;
        geneItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemdetail_forumnotedetail);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 当评论或者回复后进行页面刷新
        refercommentreply();
    }

    //    @Override
//    protected void onStop() {
//        super.onStop();
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "ArticleDetails Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.sjqcjstock.Activity.Article/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        if (dialog != null) {
//            dialog.dismissDlog();
//        }
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.disconnect();
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "ArticleDetails Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.sjqcjstock.Activity.Article/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//删除微博，这部分内容暂时取消
    private class pickdeletecomment1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // 删除微博评论
            new AlertDialog.Builder(ArticleDetailsActivity.this)
                    .setMessage("确认删除吗")
                    .setPositiveButton("是",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    pickcommentoptions1.setVisibility(View.GONE);
//                                    new SendInfoTaskdeleteweibo()
//                                            .execute(new TaskParams(
//                                                    Constants.Url + "?app=public&mod=AppFeedList&act=Appdelcomment",
//                                                    new String[]{"mid", Constants.staticmyuidstr},
//                                                    new String[]{"login_password", Constants.staticpasswordstr},
//                                                    new String[]{"comment_id", commentListEntities.get(currentposition).getComment_id()}
//                                            ));
                                    // 删除评论
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/comment/del?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&comment_id=" + commentListEntities.get(currentposition).getComment_id());
                                            handler.sendEmptyMessage(6);
                                        }
                                    }).start();
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss(); //关闭alertDialog
                                }
                            }).show();
        }
    }

    //    }
    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();

        Intent intent = getIntent();
//        uidstr = intent.getStringExtra("uid");
        weiboidstr = intent.getStringExtra("weibo_id");
//        source_feed_idstr = intent.getStringExtra("sourceweibo_id");
//        sourceuidstr = intent.getStringExtra("sourceuid");
//        typestr = intent.getStringExtra("type");

        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        mailTextLl = (LinearLayout) findViewById(R.id.mail_text_ll);
        explainRl = (RelativeLayout) findViewById(R.id.shuijinbi_explain_rl);
        explainTv = (TextView) findViewById(R.id.shuijinbi_explain_tv);
        moreTv = (TextView) findViewById(R.id.shuijinbi_more_tv);
        moreTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶打赏列表页面
                Intent intent = new Intent(ArticleDetailsActivity.this,
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
                Intent intent = new Intent(ArticleDetailsActivity.this,
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
                current++;
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

//        // 开线程获取水晶币数量
//        new SendInfoTaskmywealth().execute(new TaskParams(Constants.appUserMoneyUrl,
//                new String[]{"mid", Constants.staticmyuidstr}
//        ));

        // 开线程获取水晶币数量
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/credit/info?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(8);
            }
        }).start();

        // 获取微博详细信息
        geneItemsnotedetail();

        replyinfolistview = (SoListView) findViewById(R.id.replyinfolist2);
        // 存储数据的数组列表
        commentListEntities = new ArrayList<CommentListEntity>();
        // 为ListView 添加适配器
        replyinfoAdapter = new replyinfoAdapter(ArticleDetailsActivity.this);
        replyinfolistview.setAdapter(replyinfoAdapter);
        vipImg = (ImageView) findViewById(R.id.vip_img);
        vipImgSource = (ImageView) findViewById(R.id.vip_img_source);
        replyinfolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                commentoptions(position);
            }
        });
    }

//    }

    //回复微博，这部分内容隐藏
    class pickreplycomment1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            pickcommentoptions1.setVisibility(View.GONE);
            try {
                CommentListEntity commentListEntity = commentListEntities.get(currentposition);
                Intent intent = new Intent(getApplicationContext(), addreplycommentweiboActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("feed_id", feed_idstr);
                intent.putExtra("feeduid", uidstr);
                intent.putExtra("to_uid", commentListEntity.getUid());
                intent.putExtra("comment_id", commentListEntity.getComment_id());
                intent.putExtra("oldname", commentListEntity.getUname());
//                intent.putExtra("feed_id", (String) listreplyinfoData.get(currentposition).get("feed_id"));
//                intent.putExtra("feeduid", (String) listreplyinfoData.get(currentposition).get("feeduid"));
//                intent.putExtra("to_uid", (String) listreplyinfoData.get(currentposition).get("uid"));
//                intent.putExtra("comment_id", (String) listreplyinfoData.get(currentposition).get("comment_id"));
//                intent.putExtra("oldname", (String) listreplyinfoData.get(currentposition).get("uname"));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    class cancelcommentoptions1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            pickcommentoptions1.setVisibility(View.GONE);
        }
    }

    class pickuserinfo2_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(ArticleDetailsActivity.this,
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
            ShareUtil.showShare(ArticleDetailsActivity.this, weiboidstr, titelStr, contentSt);
        }
    }

    // 对原帖微博进行点赞
    class praiseimageView1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    /**
     * 对微博进行点赞
     */
    class pickpraise1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (Utils.isFastDoubleClick()) {
                return;
            }
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(ArticleDetailsActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }
            // 添加点赞 add 取消点赞 cancel
            // 通过网络范围进行点赞操作
            if ("0".equals(is_digg)) {
                // 进行点赞
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/digg?feed_id=" + weiboidstr + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr + "&type=add");
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            } else {
                // 取消点赞
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/digg?feed_id=" + weiboidstr + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr + "&type=cancel");
                        handler.sendEmptyMessage(2);
                    }
                }).start();
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
            Intent intent = new Intent(getApplicationContext(), ArticleDetailsActivity.class);
            intent.putExtra("weibo_id", source_feed_idstr);
            intent.putExtra("uid", sourceuidstr);
            startActivity(intent);
        }
    }

    class repostuser1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getApplicationContext(), UserDetailNewActivity.class);
            intent.putExtra("uid", sourceuidstr);
            startActivity(intent);
        }
    }

    class headimg1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(ArticleDetailsActivity.this,
                    UserDetailNewActivity.class);
            intent.putExtra("uid", uidstr);
            startActivity(intent);
        }
    }

    /**
     * 评论的单机事件
     */
    class commentweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(ArticleDetailsActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(ArticleDetailsActivity.this,
                    addcommentweiboActivity.class);
            intent.putExtra("feed_id", weiboidstr);
            intent.putExtra("feeduid", uidstr);
            startActivity(intent);
        }
    }

    /**
     * 付费或者打赏水晶币
     */
    class iv_sang_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(ArticleDetailsActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }
            if (Constants.staticmyuidstr.equals(uidstr)) {
                // 自己不能给自己打赏
                CustomToast.makeText(getApplicationContext(), "对不起不能给自己打赏。", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            view = LayoutInflater.from(ArticleDetailsActivity.this).inflate(R.layout.dialog_sang, null);
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


            final AlertDialog alertDialog = new AlertDialog.Builder(ArticleDetailsActivity.this).setView(view).show();
            rl_dialogDismiss.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    //销毁弹出框
                    alertDialog.dismiss();
                }
            });
            bt_ok.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    sjbCount = et_inputCount.getText().toString().trim();
                    if ("".equals(sjbCount)) {
                        CustomToast.makeText(getApplicationContext(), "请输入打赏水晶币数。", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                        CustomToast.makeText(getApplicationContext(), "对不起你水晶币不足。", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    if (Integer.valueOf(sjbCount) < 1) {
                        CustomToast.makeText(getApplicationContext(), "对不起打赏不能为零。", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    if (!((CheckBox) view.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                        CustomToast.makeText(getApplicationContext(), "请阅读《付费阅读协议》", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    alertDialog.dismiss();

                    //提交数据到服务器
//                    new SendInfoTaskReward().execute(new TaskParams(Constants.apprewardUrl,
//                            new String[]{"mid", Constants.staticmyuidstr},
//                            new String[]{"reward_coin", sjbCount},// 打赏金额
//                            new String[]{"weibo_id", weiboidstr},// 打赏微博ID
//                            new String[]{"touid", uidstr},// 被打赏用户ID
//                            new String[]{"state", state}////微博状态0标识免费微博，1标识付费微博
//                    ));
                    // 打赏水晶币
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List dataList = new ArrayList();
                            dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                            dataList.add(new BasicNameValuePair("feed_id", weiboidstr));
                            dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                            dataList.add(new BasicNameValuePair("reward_coin", sjbCount));
                            dataList.add(new BasicNameValuePair("touid", uidstr));
                            jsonStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/credit/feedReward", dataList);
                            handler.sendEmptyMessage(7);
                        }
                    }).start();
                }
            });

            view.findViewById(R.id.sang_agreement_tv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到水晶币协议页面
                    Intent intent = new Intent(ArticleDetailsActivity.this, AgreementActivity.class);
                    startActivity(intent);
                }
            });

            view.findViewById(R.id.bt_cz).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到充值页面
                    Intent intent = new Intent(ArticleDetailsActivity.this, RechargeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    /**
     * 转发的单机事件
     */
    class transpond1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(ArticleDetailsActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(ArticleDetailsActivity.this, transpondweiboActivity.class);
            intent.putExtra("feed_id", feed_idstr);
            intent.putExtra("feeduid", uidstr);
            startActivity(intent);
        }
    }

    /**
     * 收藏文章的单机事件
     */
    class collectnote1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(ArticleDetailsActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }
            if ("0".equals(Collectionstr)) {
                // 添加收藏
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/collection?feed_id=" + weiboidstr + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr + "&type=add");
                        handler.sendEmptyMessage(3);
                    }
                }).start();
                Collectionstr = "1";
            } else {
                // 取消收藏
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/collection?feed_id=" + weiboidstr + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr + "&type=cancel");
                        handler.sendEmptyMessage(4);
                    }
                }).start();
                Collectionstr = "0";
            }
        }
    }

    //调用该方法获取微博的数据
    private void geneItemsnotedetail() {
        // 获取微博详细id
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                // 微博id
                dataList.add(new BasicNameValuePair("feed_id", weiboidstr));
                dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                feedStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/feed/info", dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    //获取评论列表信息
    private void geneItems() {
        // 文章评论列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                commentStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/comment/getList?feed_id=" + weiboidstr + "&p=" + current);
                handler.sendEmptyMessage(5);
            }
        }).start();
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(feedStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject dataObject = new JSONObject(jsonObject.getString("data"));
                        FeedEntity feedEntity = JSON.parseObject(dataObject.getString("feedinfo"), FeedEntity.class);
                        UserEntity userEntity = JSON.parseObject(dataObject.getString("userinfo"), UserEntity.class);
                        // 发微博的用户id
                        uidstr = userEntity.getUid();
                        // 微博类型
                        String feeddType = feedEntity.getType();
                        if ("repost".equals(feeddType)) {
                            // 当为转发微博的时候
                            FeedEntity repostFeedEntity = JSON.parseObject(dataObject.getString("repost"), FeedEntity.class);
                            UserEntity repostUserEntity = JSON.parseObject(dataObject.getString("repost_userinfo"), UserEntity.class);
                            repostlin1.setVisibility(View.VISIBLE);
                            repostcounts.setVisibility(View.VISIBLE);
                            // 转发数
                            repostrepost_count1.setText(repostFeedEntity.getRepost_count());
                            // 点赞数
                            repostdigg_count1.setText(repostFeedEntity.getDigg_count());
                            // 评论数
                            repostcomment_count1.setText(repostFeedEntity.getComment_count());
                            // 转发微博id
                            source_feed_idstr = repostFeedEntity.getFeed_id();
                            // 转发用户id
                            sourceuidstr = repostUserEntity.getUid();

                            if ("1".equals(repostFeedEntity.getIs_digg())) {
                                praiseimageView1
                                        .setImageResource(R.mipmap.praise6_l);
                            }
                            // 转发微博的类型
                            String repostFeeddType = repostFeedEntity.getType();
                            // 转发微博的主体内容
                            String repostContentStr = repostFeedEntity.getFeed_content();
                            // 转发微博的标题
                            String repostTitleStr = repostFeedEntity.getTitle();
                            // 转发微博的简介
                            String repostIntroductionStr = repostFeedEntity.getIntroduction();

                            if ("long_post".equals(repostFeeddType)) {
                                // 长微博
                                repostContentStr = "<font color=\"#4471BC\" >" + repostTitleStr + "</font><br/>" + repostContentStr;
                            } else if ("paid_post".equals(repostFeeddType)) {
                                // 付费微博
                                // 长微博
                                repostContentStr = "<font color=\"#4471BC\" >" + repostTitleStr + "</font><br/>" + repostIntroductionStr + "<br/>" + repostContentStr;
                                reward1.setText(feedEntity.getReward());
                                reward1.setVisibility(View.VISIBLE);
                            }
                            CharSequence charSequence = Html.fromHtml(repostContentStr, ImageUtil.getImageGetter(ArticleDetailsActivity.this.getResources()), null);
                            repostweibocomment1.setText(charSequence);
                            // 用户认证标识
                            ViewUtil.setUpVipNew(repostUserEntity.getUser_group_icon_url(), vipImgSource);
                            repostusername1.setText(repostUserEntity.getUname());
                        }
                        // 被打赏微博的用户ID
                        uidstr = userEntity.getUid();
                        username1.setText(userEntity.getUname());
                        // 头像
                        ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                                headimg1, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                        // 用户认证标识
                        ViewUtil.setUpVipNew(userEntity.getUser_group_icon_url(), vipImg);
                        // 微博id
                        feed_idstr = feedEntity.getFeed_id();
                        // 发布时间
                        String feedTime = CalendarUtil.formatDateTime(Utils
                                .getStringtoDate(feedEntity.getPublish_time()));
                        create_time1.setText(feedTime);
                        // 转发数
                        repost_count1.setText(feedEntity.getRepost_count());
                        // 点赞数
                        digg_count1.setText(feedEntity.getDigg_count());
                        // 评论数
                        comment_count1.setText(feedEntity.getComment_count());
                        // 是否收藏
                        Collectionstr = feedEntity.getCollection();
                        // 是否点赞
                        is_digg = feedEntity.getIs_digg();

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
                        // 微博内容
                        String contentStr = feedEntity.getFeed_content();
                        if ("paid_post".equals(feeddType)) {
                            // 设置为付费标识
                            state = "1";
                            // 微博简介
                            String introductionStr = feedEntity.getIntroduction() + "";
                            // 是打赏文分享内容为微博简介
                            contentSt = Html.fromHtml(introductionStr).toString();
                            // 如果该微博为付费微博
                            // 是否是打赏了的文章 0:未打赏 1：打赏
                            payState = feedEntity.getPayState();

                            // 微博以打赏
                            rewardExplain.setVisibility(View.VISIBLE);
                            rewardStr = feedEntity.getReward();
                            // 显示需要打赏水晶币数量
                            rewardTv.setText(rewardStr);
                            rewardTv.setVisibility(View.VISIBLE);
                            if (Constants.staticmyuidstr.equals(uidstr)) {
                                // 如果是自己的文章就直接显示已经阅读过了
                                payState = "1";
                            }

                            if ("1".equals(payState)) {
                                // 拼接微博内容
                                contentStr = "摘要：" + introductionStr + "<br/><br/>" + contentStr;
//                                if (payState == null || "0".equals(payState.toString())) {
//                                    // --------- 给管理人员用的不用进行打赏就可以阅读文章（注释掉是給管理员用的）
//                                    wv_ad_tv.setVisibility(View.GONE);
//                                    // 显示摘要
//                                    coment.setText("摘要：" + introductionStr);
//                                    coment.setVisibility(View.VISIBLE);
//                                } else {
                                wv_ad_tv.setVisibility(View.VISIBLE);
                                coment.setVisibility(View.GONE);
                                // 修改说明语句
                                rewardExplain.setText("本文是订阅文章，您已订阅打赏");
                                // 禁止复制
                                wv_ad_tv.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        return true;
                                    }
                                });
//                                }
                            } else {
                                // --------- 给管理人员用的不用进行打赏就可以阅读文章（注释掉是給管理员用的）
                                wv_ad_tv.setVisibility(View.GONE);
                                // 显示摘要
                                coment.setText("摘要：" + introductionStr);
                                coment.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // 未打赏
                            wv_ad_tv.setVisibility(View.VISIBLE);
                            rewardTv.setVisibility(View.GONE);
                            rewardExplain.setVisibility(View.GONE);
                            // 不是打赏文分享内容为正文
                            contentSt = Html.fromHtml(contentStr).toString();
                        }

                        //StringBuilder拼接“内容+图片地址”作为webview的内容
                        StringBuilder postimgsbdstr = new StringBuilder();

                        // 这下面这个测试了再决定改不改
                        //正文页中含有的图片
                        if ("postimage".equals(feeddType)) {
                            //获取图片的地址，http://www.sjqcj.com/data/upload/  +  解析attach_url得到的地址，即可获得图片
                            String attachUrl = feedEntity.getAttach_url();
                            if (!"".equals(attachUrl) && attachUrl != null) {
//                                List<Map<String, Object>> feedinfoList = JsonTools.listKeyMaps(attachUrl);
                                // 解析短微博图片地址
//                                    attachUrl = attachUrl.substring(1, attachUrl.length() - 1);
//                                    String[] attach_urlstrs = attachUrl.split(",");
                                JSONArray jsonArray = new JSONArray(attachUrl);
                                int count = jsonArray.length();
                                for (int i = 0; i < count; i++) {
                                    postimgsbdstr.append("<img onclick=\"getimg(this)\" src=\"");
                                    postimgsbdstr.append(Constants.newUrl + "/data/upload/" + jsonArray.getString(i));
                                    postimgsbdstr.append("\"  height=\"150px\" width=\"150px\" />&nbsp;&nbsp;");
                                }
                            }
                        }

                        //内容+图片的地址拼接，作为WebView的内容
                        if (postimgsbdstr != null) {
                            contentStr = contentStr + "<br/>" + postimgsbdstr + "";
                        }
                        // 设置图片不超出屏幕
                        contentStr = contentStr.replace("<img", "<img onclick=\"getimg(this);\" style=\"max-width: 100%;height:auto\"  ");
                        //全文
                        wv_ad_tv.getSettings().setJavaScriptEnabled(true);
                        // 给WebView添加js交互接口类(用于图片点击放大)
                        wv_ad_tv.addJavascriptInterface(new JavascriptInterface(ArticleDetailsActivity.this), "imagelistner");
                        wv_ad_tv.setWebViewClient(new ImageWebViewClient(wv_ad_tv, ArticleDetailsActivity.this));
                        wv_ad_tv.loadDataWithBaseURL(null, contentStr, "text/html", "utf-8", null);
                        dialog.dismissDlog();
                        wv_ad_tv.setWebChromeClient(new WebChromeClient() {
                            public void onProgressChanged(WebView view, int newProgress) {
                                if (newProgress > 95) {
                                    findViewById(R.id.web_jiazai_xianshi).setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        String titleStr = feedEntity.getTitle();
                        if ("".equals(titleStr) || null == titleStr) {
                            feedtitle.setVisibility(View.GONE);
                        } else {
                            // 是付费文章
                            feedtitle.setText(Html.fromHtml(titleStr));
                            feedtitle.setVisibility(View.VISIBLE);
                            titelStr = feedtitle.getText() + "";
                        }

                        if (Constants.staticmyuidstr.equals(uidstr) || !"paid_post".equals(feeddType)) {
                            // 获取打赏水晶的人的信息集合 （自己的和不为付费微博才显示）
                            // 打赏水晶的人数
                            reward_count = feedEntity.getReward_count();
                            // 打赏水晶的数量
                            amount_count = feedEntity.getAmount_count();
                            asslist = feedEntity.getAsslist();
                            explainRl.setVisibility(View.VISIBLE);
                            if (asslist != null && asslist.size() > 0) {
                                explainTv.setText("已有 " + reward_count + " 人共打赏 " + amount_count + " 水晶币");
                                // 更多显示
                                moreTv.setVisibility(View.VISIBLE);
                                imageAdapter = new HeadImageAdapter(asslist, ArticleDetailsActivity.this);
                                headsGv.setAdapter(imageAdapter);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(ArticleDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            is_digg = "1";
                            int diggcount2 = Integer.parseInt(digg_count1.getText()
                                    .toString());
                            digg_count1.setText(String.valueOf(diggcount2 + 1));
                            isdiggsign1.setText("已点赞");
                            praise1.setImageResource(R.mipmap.praise6_l);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(ArticleDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            is_digg = "0";
                            if ("".equals(digg_count1.getText())) {
                                digg_count1.setText("0");
                            } else {
                                int diggcount2 = Integer.parseInt(digg_count1.getText() + "");
                                digg_count1.setText(String.valueOf(diggcount2 - 1));
                            }
                            isdiggsign1.setText("点赞");
                            praise1.setImageResource(R.mipmap.praise6);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(ArticleDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            cellectloge1.setText("已收藏");
                            cellectloge1_img.setImageResource(R.mipmap.soucang2_l);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(ArticleDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            cellectloge1.setText("收藏");
                            cellectloge1_img.setImageResource(R.mipmap.soucang2);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    // 评论列表
                    try {
                        JSONObject jsonObject = new JSONObject(commentStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        List<CommentListEntity> commentlist = JSON.parseArray(jsonObject.getString("data"), CommentListEntity.class);
                        // 评论数
                        comment_count1.setText(commentlist.size() + "");
                        commentListEntities.addAll(commentlist);
                        replyinfoAdapter.setlistData(commentListEntities);
                        if (current == 1) {
                            // 评论大于两页就不用滚动到顶部去
                            // 滚动到顶部
                            ViewUtil.setListViewHeightBasedOnChildren(replyinfolistview);
                            myScrollView.smoothScrollTo(0, 0);
                        }
                        if (commentListEntities.size() >= commentlist.size()) {
                            xlistviewFooterContent.setVisibility(View.GONE);
                        }
                        xlistviewFooterProgressbar.setVisibility(View.INVISIBLE);
                        xlistviewFooterHintTextview.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    // 删除评论
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(ArticleDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            refercommentreply();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(ArticleDetailsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 如果显示打赏信息打赏后就更新打赏信息
                            if (explainRl.getVisibility() == View.VISIBLE) {
                                geneItemsnotedetail();
                                return;
                            }
                            // 成功
                            payState = "1";
                            // 显示全文
                            wv_ad_tv.setVisibility(View.VISIBLE);
                            coment.setVisibility(View.VISIBLE);
                            // 禁止复制
                            wv_ad_tv.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    return true;
                                }
                            });
                            // 修改说明语句
                            rewardExplain.setText("本文是订阅文章，您已订阅打赏");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String shuijingbi = jsonData.getString("shuijingbi");
                            Constants.shuijinbiCount = shuijingbi;
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
