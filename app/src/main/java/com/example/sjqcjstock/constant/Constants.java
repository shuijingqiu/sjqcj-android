package com.example.sjqcjstock.constant;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.UnreadCount;

import java.util.HashMap;

public class Constants {

    public final static int NUMBER0 = 0;
    public final static int NUMBER1 = 1;

    //微信app_id                          wx6608522223b1dea0
    public static final String APP_ID = "wx778e68d591bbe9c8";
    public static final String App_Secret = "72b7bdae9cfbfdf768360c628da89c25";
    // 微信支付的订单号
    public static String orderNumber = "";
    //是否刷新股吧列表
    public static String isreferforumlist = "0";//0为刷新，1为不刷新
    //code
    public static String weixincode = "";
    //用户信息持久化
    public static String staticmyuidstr = "";

    // 存储用户头像地址
    public static String headImg = "";
    // 存储用户类别（主要用于是否是禁言用户 如果为4 就为禁言用户）
    public static String userType = "1";

    //public static String staticpasswordstr="3665306";
    public static String staticpasswordstr = "";

    //微信或qqtokey
    public static String statictokeystr = "";
    public static String staticLoginType = "";
    // 判断第三方登录的时候是否修改过密码
    public static Boolean isDefault = true;

    // 没有用
    public static String intentFlag = "";

    // 全局变数水晶币个数(每次进入程序就需要获取一次)
    public static String shuijinbiCount = "";

    // 服务器路径
    public static String Url = "http://www.sjqcj.com/index.php";
    // 分享的前半段路径
    public static String shareArticle = Url + "?app=public&mod=Profile&act=sharemeager&feed_id=";
    // 股吧页面打赏微博的ListView列表接口
    public static String appUserMoneyUrl = Url + "?app=public&mod=Profile&act=AppUserMoney";
    // 股吧页面打赏微博的ListView列表接口
    public static String subscribeListUrl = Url + "?app=public&mod=AppFeedList&act=SubscribeList";
    // 提交打赏水晶币的接口
    public static String apprewardUrl = Url + "?app=public&mod=AppFeedList&act=Appreward";
    // 获取打赏列表消息的接口
    public static String rewardMessagedUrl = Url + "?app=public&mod=AppFeedList&act=SystemMessage";
    // 获取系统消息的接口
    public static String systemMessagedUrl = Url + "?app=public&mod=Index&act=getSysMessage";
    // 我的收藏的的接口
    public static String myCollectionUrl = Url + "?app=public&mod=AppFeedList&act=AppCollection";
    // 我的订阅的的接口
    public static String mySubscribeUrl = Url + "?app=public&mod=FeedListMini&act=MySubscribe";
    // 水晶币流动接口
    public static String crystalBwaterUrl = Url + "?app=public&mod=AppFeedList&act=crystalBwater";
    // 水晶币兑换的协议接口
    public static String agreementUrl = Url + "?app=public&mod=AppFeedList&act=agreement";
    // 流动广告的接口
    public static String globalSwf = Url + "?app=public&mod=AppFeedList&act=GlobalSwf";
    // 文章打赏水晶币人数的详细列表
    public static String playAMan = Url + "?app=public&mod=AppFeedList&act=PlayAMan";
    // 调用统一下单的接口
//	public static String unifiedorder = "http://test.sjqcj.com/index.php?app=public&mod=App&act=test";
    public static String unifiedorder = Url + "?app=public&mod=App&act=wxpay";
    // 查询订单是否成功的接口
    public static String queryOrder = Url + "?app=public&mod=App&act=queryOrder";
    // 未读消息体的结构
    public static String unreadCount = Url + "?app=public&mod=Index&act=getAppUnreadCount";

    // 页面加载是显示的语句
    public static String loadMessage = "正在玩命加载中...";
    // 无网络的时候
    public static String noNetwork = "获取数据失败，网络不给力！";
    // 无数据的时候
    public static String noData = "亲你访问的页面暂时还没有数据！";
    // 微博分享
    public static String microBlogShare = "微博分享";

    // 各种消息条数的实体类
    public static UnreadCount unreadCountInfo;

//	// 图片加载化的一些共通设置
//	public static DisplayImageOptions options=new DisplayImageOptions.Builder().
//		cacheInMemory().cacheOnDisc().
//		showStubImage(R.drawable.ic_stub).
//		showImageForEmptyUri(R.drawable.ic_empty).
//		showImageOnFail(R.drawable.ic_error).
//		//displayer(new RoundedBitmapDisplayer(20)).
//		imageScaleType(ImageScaleType.IN_SAMPLE_INT)//图片显示方式
//		.bitmapConfig(Bitmap.Config.ARGB_4444).build();//設置圖片配置信息  對圖片進行處理防止內存溢出

    public static void setStatictokeystr(String statictokeystr) {
        Constants.statictokeystr = statictokeystr;
    }

    public static String staticuname = "";

    public static String getStaticuname() {
        return staticuname;
    }

    public static void setStaticuname(String staticuname) {
        Constants.staticuname = staticuname;
    }

    //表情持久化
    public static HashMap<String, Object> facemap2 = new HashMap<String, Object>();


    static {
        initfacemap2();

    }


    final int[] imagestr = {R.drawable.aoman, R.drawable.baiyan, R.drawable.bishi,
            R.drawable.bizui, R.drawable.cahan, R.drawable.caidao, R.drawable.chajin,
            R.drawable.cheer, R.drawable.chong, R.drawable.ciya, R.drawable.da, R.drawable.dabian,
            R.drawable.dabing, R.drawable.dajiao, R.drawable.daku, R.drawable.dangao, R.drawable.danu,
            R.drawable.dao, R.drawable.deyi, R.drawable.diaoxie, R.drawable.e, R.drawable.fadai,
            R.drawable.fadou, R.drawable.fan, R.drawable.fanu, R.drawable.feiwen, R.drawable.fendou,
            R.drawable.gangga, R.drawable.geili, R.drawable.gouyin, R.drawable.guzhang, R.drawable.haha,
            R.drawable.haixiu, R.drawable.haqian, R.drawable.hua, R.drawable.huaixiao, R.drawable.hufen,
            R.drawable.huishou, R.drawable.huitou, R.drawable.jidong, R.drawable.jingkong, R.drawable.jingya,
            R.drawable.kafei, R.drawable.keai, R.drawable.kelian, R.drawable.ketou, R.drawable.kiss,
            R.drawable.ku, R.drawable.kuaikule, R.drawable.kulou, R.drawable.kun, R.drawable.lanqiu,
            R.drawable.lenghan, R.drawable.liuhan, R.drawable.liulei, R.drawable.liwu,
            R.drawable.love, R.drawable.ma, R.drawable.meng, R.drawable.nanguo, R.drawable.no,
            R.drawable.ok, R.drawable.peifu, R.drawable.pijiu, R.drawable.pingpang, R.drawable.pizui,
            R.drawable.qiang, R.drawable.qinqin, R.drawable.qioudale, R.drawable.qiu, R.drawable.quantou,
            R.drawable.ruo, R.drawable.se, R.drawable.shandian, R.drawable.shengli, R.drawable.shenma,
            R.drawable.shuai, R.drawable.shuijiao, R.drawable.taiyang, R.drawable.tiao, R.drawable.tiaopi,
            R.drawable.tiaosheng, R.drawable.tiaowu, R.drawable.touxiao, R.drawable.tu, R.drawable.tuzi,
            R.drawable.wabi, R.drawable.weiqu, R.drawable.weixiao, R.drawable.wen, R.drawable.woshou,
            R.drawable.xia, R.drawable.xianwen, R.drawable.xigua, R.drawable.xinsui, R.drawable.xu,
            R.drawable.yinxian, R.drawable.yongbao, R.drawable.youhengheng, R.drawable.youtaiji, R.drawable.yueliang,
            R.drawable.yun, R.drawable.zaijian, R.drawable.zhadan, R.drawable.zhemo, R.drawable.zhuakuang,
            R.drawable.zhuanquan, R.drawable.zhutou, R.drawable.zuohengheng, R.drawable.zuotaiji, R.drawable.zuqiu,

            R.drawable.img0, R.drawable.img1, R.drawable.img2,
            R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11,
            R.drawable.img12, R.drawable.img13, R.drawable.img14, R.drawable.img15, R.drawable.img16,
            R.drawable.img17, R.drawable.img18, R.drawable.img19, R.drawable.img20, R.drawable.img21,
            R.drawable.img22, R.drawable.img23, R.drawable.img24, R.drawable.img25, R.drawable.img26,
            R.drawable.img27, R.drawable.img28, R.drawable.img29, R.drawable.img30, R.drawable.img31,
            R.drawable.img32, R.drawable.img33, R.drawable.img34, R.drawable.img35, R.drawable.img36,
            R.drawable.img37, R.drawable.img38, R.drawable.img39, R.drawable.img40, R.drawable.img41,
            R.drawable.img42, R.drawable.img43, R.drawable.img44, R.drawable.img45, R.drawable.img46,
            R.drawable.img47, R.drawable.img48, R.drawable.img49, R.drawable.img50, R.drawable.img51,
            R.drawable.img52, R.drawable.img53, R.drawable.img54, R.drawable.img55,
            R.drawable.img56, R.drawable.img57, R.drawable.img58, R.drawable.img59, R.drawable.img60,
            R.drawable.img61, R.drawable.img62, R.drawable.img63, R.drawable.img64, R.drawable.img65,
            R.drawable.img66, R.drawable.img67, R.drawable.img68, R.drawable.img69, R.drawable.img70,
            R.drawable.img71, R.drawable.img72, R.drawable.img73, R.drawable.img74, R.drawable.img75,
            R.drawable.img76, R.drawable.img77, R.drawable.img78, R.drawable.img79, R.drawable.img80,
            R.drawable.img81, R.drawable.img82, R.drawable.img83, R.drawable.img84, R.drawable.img85,
            R.drawable.img86, R.drawable.img87, R.drawable.img88, R.drawable.img89, R.drawable.img90,
            R.drawable.img91, R.drawable.img92, R.drawable.img93, R.drawable.img94, R.drawable.img95,
            R.drawable.img96, R.drawable.img97, R.drawable.img98, R.drawable.img99, R.drawable.img100,
            R.drawable.img101, R.drawable.img102, R.drawable.img103, R.drawable.img104, R.drawable.img105,
            R.drawable.img106, R.drawable.img107, R.drawable.img108, R.drawable.img109, R.drawable.img110,
            R.drawable.img111, R.drawable.img112, R.drawable.img113, R.drawable.img114, R.drawable.img115,
            R.drawable.img116, R.drawable.img117, R.drawable.img118, R.drawable.img119, R.drawable.img120,
            R.drawable.img121, R.drawable.img122, R.drawable.img123, R.drawable.img124, R.drawable.img125,
            R.drawable.img126, R.drawable.img127, R.drawable.img128, R.drawable.img129, R.drawable.img130,
            R.drawable.img131, R.drawable.img132, R.drawable.img133, R.drawable.img134
    };


    final String[] facestrs = {"[aoman]", "[baiyan]", "[bishi]",
            "[bizui]", "[cahan]", "[caidao]", "[chajin]",
            "[cheer]", "[chong]", "[ciya]", "[da]", "[dabian]",
            "[dabing]", "[dajiao]", "[daku]", "[dangao]", "[danu]",
            "[dao]", "[deyi]", "[diaoxie]", "[e]", "[fadai]",
            "[fadou]", "[fan]", "[fanu]", "[feiwen]", "[fendou]",
            "[gangga]", "[geili]", "[gouyin]", "[guzhang]", "[haha]",
            "[haixiu]", "[haqian]", "[hua]", "[huaixiao]", "[hufen]",
            "[huishou]", "[huitou]", "[jidong]", "[jingkong]", "[jingya]",
            "[kafei]", "[keai]", "[kelian]", "[ketou]", "[kiss]",
            "[ku]", "[kuaikule]", "[kulou]", "[kun]", "[lanqiu]",
            "[lenghan]", "[liuhan]", "[liulei]", "[liwu]",
            "[love]", "[ma]", "[meng]", "[nanguo]", "[no]",
            "[ok]", "[peifu]", "[pijiu]", "[pingpang]", "[pizui]",
            "[qiang]", "[qinqin]", "[qioudale]", "[qiu]", "[quantou]",
            "[ruo]", "[se]", "[shandian]", "[shengli]", "[shenma]",
            "[shuai]", "[shuijiao]", "[taiyang]", "[tiao]", "[tiaopi]",
            "[tiaosheng]", "[tiaowu]", "[touxiao]", "[tu]", "[tuzi]",
            "[wabi]", "[weiqu]", "[weixiao]", "[wen]", "[woshou]",
            "[xia]", "[xianwen]", "[xigua]", "[xinsui]", "[xu]",
            "[yinxian]", "[yongbao]", "[youhengheng]", "[youtaiji]", "[yueliang]",
            "[yun]", "[zaijian]", "[zhadan]", "[zhemo]", "[zhuakuang]",
            "[zhuanquan]", "[zhutou]", "[zuohengheng]", "[zuotaiji]", "[zuqiu]",

            "[1]", "[2]", "[3]",
            "[4]", "[5]", "[6]", "[7]",
            "[8]", "[9]", "[10]", "[11]", "[12]",
            "[13]", "[14]", "[15]", "[16]", "[17]",
            "[18]", "[19]", "[20]", "[21]", "[22]",
            "[23]", "[24]", "[25]", "[26]", "[27]",
            "[28]", "[29]", "[30]", "[31]", "[32]",
            "[33]", "[34]", "[35]", "[36]", "[37]",
            "[38]", "[39]", "[40]", "[41]", "[42]",
            "[43]", "[44]", "[45]", "[46]", "[47]",
            "[48]", "[49]", "[50]", "[51]", "[52]",
            "[53]", "[54]", "[55]", "[56]",
            "[57]", "[58]", "[59]", "[60]", "[61]",
            "[62]", "[63]", "[64]", "[65]", "[66]",
            "[67]", "[68]", "[69]", "[70]", "[71]",
            "[72]", "[73]", "[74]", "[75]", "[76]",
            "[77]", "[78]", "[79]", "[80]", "[81]",
            "[82]", "[83]", "[84]", "[85]", "[86]",
            "[87]", "[88]", "[89]", "[90]", "[91]",
            "[92]", "[93]", "[94]", "[95]", "[96]",
            "[97]", "[98]", "[99]", "[100]", "[101]",
            "[102]", "[103]", "[104]", "[105]", "[106]",
            "[107]", "[108]", "[109]", "[110]", "[111]",
            "[112]", "[113]", "[114]", "[115]", "[116]",
            "[117]", "[118]", "[119]", "[120]", "[121]",
            "[122]", "[123]", "[124]", "[125]", "[126]",
            "[127]", "[128]", "[129]", "[130]", "[131]",
            "[132]", "[133]", "[134]"
    };


    public Constants() {
        super();
        // TODO Auto-generated constructor stub
        initfacemap2();

    }

    private static void initfacemap2() {
        // TODO Auto-generated method stub
        facemap2.put("aoman", R.drawable.aoman);
        facemap2.put("baiyan", R.drawable.baiyan);
        facemap2.put("bishi", R.drawable.bishi);
        facemap2.put("bizui", R.drawable.bizui);
        facemap2.put("cahan", R.drawable.cahan);
        facemap2.put("caidao", R.drawable.caidao);
        facemap2.put("chajin", R.drawable.chajin);
        facemap2.put("cheer", R.drawable.cheer);
        facemap2.put("chong", R.drawable.chong);
        facemap2.put("ciya", R.drawable.ciya);
        facemap2.put("da", R.drawable.da);
        facemap2.put("dabian", R.drawable.dabian);
        facemap2.put("dabing", R.drawable.dabing);
        facemap2.put("dajiao", R.drawable.dajiao);
        facemap2.put("daku", R.drawable.daku);
        facemap2.put("dangao", R.drawable.dangao);
        facemap2.put("danu", R.drawable.danu);
        facemap2.put("dao", R.drawable.dao);
        facemap2.put("deyi", R.drawable.deyi);
        facemap2.put("diaoxie", R.drawable.diaoxie);
        facemap2.put("e", R.drawable.e);
        facemap2.put("fadai", R.drawable.fadai);
        facemap2.put("fadou", R.drawable.fadou);
        facemap2.put("fan", R.drawable.fan);
        facemap2.put("fanu", R.drawable.fanu);
        facemap2.put("feiwen", R.drawable.feiwen);
        facemap2.put("fendou", R.drawable.fendou);
        facemap2.put("gangga", R.drawable.gangga);
        facemap2.put("geili", R.drawable.geili);
        facemap2.put("gouyin", R.drawable.gouyin);
        facemap2.put("guzhang", R.drawable.guzhang);
        facemap2.put("haha", R.drawable.haha);
        facemap2.put("haixiu", R.drawable.haixiu);
        facemap2.put("haqian", R.drawable.haqian);
        facemap2.put("hua", R.drawable.hua);
        facemap2.put("huaixiao", R.drawable.huaixiao);
        facemap2.put("hufen", R.drawable.hufen);
        facemap2.put("huishou", R.drawable.huishou);
        facemap2.put("huitou", R.drawable.huitou);
        facemap2.put("jidong", R.drawable.jidong);
        facemap2.put("jingkong", R.drawable.jingkong);
        facemap2.put("jingya", R.drawable.jingya);
        facemap2.put("kafei", R.drawable.kafei);
        facemap2.put("keai", R.drawable.keai);
        facemap2.put("kelian", R.drawable.kelian);
        facemap2.put("ketou", R.drawable.ketou);
        facemap2.put("kiss", R.drawable.kiss);
        facemap2.put("ku", R.drawable.ku);
        facemap2.put("kuaikule", R.drawable.kuaikule);
        facemap2.put("kulou", R.drawable.kulou);
        facemap2.put("kun", R.drawable.kun);
        facemap2.put("lanqiu", R.drawable.lanqiu);
        facemap2.put("lenghan", R.drawable.lenghan);
        facemap2.put("liuhan", R.drawable.liuhan);
        facemap2.put("liulei", R.drawable.liulei);
        facemap2.put("liwu", R.drawable.liwu);
        facemap2.put("love", R.drawable.love);
        facemap2.put("ma", R.drawable.ma);
        facemap2.put("meng", R.drawable.meng);
        facemap2.put("nanguo", R.drawable.nanguo);
        facemap2.put("no", R.drawable.no);
        facemap2.put("ok", R.drawable.ok);
        facemap2.put("peifu", R.drawable.peifu);
        facemap2.put("pijiu", R.drawable.pijiu);
        facemap2.put("pingpang", R.drawable.pingpang);
        facemap2.put("pizui", R.drawable.pizui);
        facemap2.put("qiang", R.drawable.qiang);
        facemap2.put("qinqin", R.drawable.qinqin);
        facemap2.put("qioudale", R.drawable.qioudale);
        facemap2.put("qiu", R.drawable.qiu);
        facemap2.put("quantou", R.drawable.quantou);
        facemap2.put("ruo", R.drawable.ruo);
        facemap2.put("se", R.drawable.se);
        facemap2.put("shandian", R.drawable.shandian);
        facemap2.put("shengli", R.drawable.shengli);
        facemap2.put("shenma", R.drawable.shenma);
        facemap2.put("shuai", R.drawable.shuai);
        facemap2.put("shuijiao", R.drawable.shuijiao);
        facemap2.put("taiyang", R.drawable.taiyang);
        facemap2.put("tiao", R.drawable.tiao);
        facemap2.put("tiaopi", R.drawable.tiaopi);
        facemap2.put("tiaopi", R.drawable.tiaopi);
        facemap2.put("tiaosheng", R.drawable.tiaosheng);
        facemap2.put("tiaowu", R.drawable.tiaowu);
        facemap2.put("touxiao", R.drawable.touxiao);
        facemap2.put("tu", R.drawable.tu);
        facemap2.put("tuzi", R.drawable.tuzi);
        facemap2.put("wabi", R.drawable.wabi);
        facemap2.put("weiqu", R.drawable.weiqu);
        facemap2.put("weixiao", R.drawable.weixiao);
        facemap2.put("wen", R.drawable.wen);
        facemap2.put("woshou", R.drawable.woshou);
        facemap2.put("xia", R.drawable.xia);
        facemap2.put("xianwen", R.drawable.xianwen);
        facemap2.put("xigua", R.drawable.xigua);
        facemap2.put("xinsui", R.drawable.xinsui);
        facemap2.put("xu", R.drawable.xu);
        facemap2.put("yinxian", R.drawable.yinxian);
        facemap2.put("yongbao", R.drawable.yongbao);
        facemap2.put("youhengheng", R.drawable.youhengheng);
        facemap2.put("youtaiji", R.drawable.youtaiji);
        facemap2.put("yueliang", R.drawable.yueliang);
        facemap2.put("yun", R.drawable.yun);
        facemap2.put("zaijian", R.drawable.zaijian);
        facemap2.put("zhadan", R.drawable.zhadan);
        facemap2.put("zhemo", R.drawable.zhemo);
        facemap2.put("zhuakuang", R.drawable.zhuakuang);
        facemap2.put("zhuanquan", R.drawable.zhuanquan);
        facemap2.put("zhutou", R.drawable.zhutou);
        facemap2.put("zuohengheng", R.drawable.zuohengheng);
        facemap2.put("zuotaiji", R.drawable.zuotaiji);
        facemap2.put("zuqiu", R.drawable.zuqiu);


        facemap2.put("0", R.drawable.img0);
        facemap2.put("1", R.drawable.img1);
        facemap2.put("2", R.drawable.img2);
        facemap2.put("3", R.drawable.img3);
        facemap2.put("4", R.drawable.img4);
        facemap2.put("5", R.drawable.img5);
        facemap2.put("6", R.drawable.img6);
        facemap2.put("7", R.drawable.img7);
        facemap2.put("8", R.drawable.img8);
        facemap2.put("9", R.drawable.img9);
        facemap2.put("10", R.drawable.img10);
        facemap2.put("11", R.drawable.img11);
        facemap2.put("12", R.drawable.img12);
        facemap2.put("13", R.drawable.img13);
        facemap2.put("15", R.drawable.img15);
        facemap2.put("16", R.drawable.img16);
        facemap2.put("17", R.drawable.img17);
        facemap2.put("18", R.drawable.img18);
        facemap2.put("19", R.drawable.img19);
        facemap2.put("20", R.drawable.img20);
        facemap2.put("21", R.drawable.img21);
        facemap2.put("22", R.drawable.img22);
        facemap2.put("23", R.drawable.img23);
        facemap2.put("24", R.drawable.img24);
        facemap2.put("25", R.drawable.img25);
        facemap2.put("26", R.drawable.img26);
        facemap2.put("27", R.drawable.img27);
        facemap2.put("28", R.drawable.img28);
        facemap2.put("29", R.drawable.img29);
        facemap2.put("30", R.drawable.img30);
        facemap2.put("31", R.drawable.img31);
        facemap2.put("32", R.drawable.img32);
        facemap2.put("33", R.drawable.img33);
        facemap2.put("34", R.drawable.img34);
        facemap2.put("35", R.drawable.img35);
        facemap2.put("36", R.drawable.img36);
        facemap2.put("37", R.drawable.img37);
        facemap2.put("38", R.drawable.img38);
        facemap2.put("39", R.drawable.img39);
        facemap2.put("40", R.drawable.img40);
        facemap2.put("41", R.drawable.img41);
        facemap2.put("42", R.drawable.img42);
        facemap2.put("43", R.drawable.img43);
        facemap2.put("44", R.drawable.img44);
        facemap2.put("45", R.drawable.img45);

        facemap2.put("46", R.drawable.img46);
        facemap2.put("47", R.drawable.img47);
        facemap2.put("48", R.drawable.img48);
        facemap2.put("49", R.drawable.img49);
        facemap2.put("50", R.drawable.img50);
        facemap2.put("51", R.drawable.img51);
        facemap2.put("52", R.drawable.img52);
        facemap2.put("53", R.drawable.img53);
        facemap2.put("54", R.drawable.img54);
        facemap2.put("55", R.drawable.img55);
        facemap2.put("56", R.drawable.img56);
        facemap2.put("57", R.drawable.img57);
        facemap2.put("58", R.drawable.img58);
        facemap2.put("59", R.drawable.img59);
        facemap2.put("60", R.drawable.img60);
        facemap2.put("61", R.drawable.img61);
        facemap2.put("62", R.drawable.img62);
        facemap2.put("63", R.drawable.img63);
        facemap2.put("64", R.drawable.img64);
        facemap2.put("65", R.drawable.img65);
        facemap2.put("66", R.drawable.img66);
        facemap2.put("67", R.drawable.img67);
        facemap2.put("68", R.drawable.img68);
        facemap2.put("69", R.drawable.img69);
        facemap2.put("70", R.drawable.img70);
        facemap2.put("71", R.drawable.img71);
        facemap2.put("72", R.drawable.img72);
        facemap2.put("73", R.drawable.img73);
        facemap2.put("74", R.drawable.img74);
        facemap2.put("75", R.drawable.img75);
        facemap2.put("76", R.drawable.img76);
        facemap2.put("77", R.drawable.img77);
        facemap2.put("78", R.drawable.img78);
        facemap2.put("79", R.drawable.img79);
        facemap2.put("80", R.drawable.img80);
        facemap2.put("81", R.drawable.img81);
        facemap2.put("82", R.drawable.img82);
        facemap2.put("83", R.drawable.img83);
        facemap2.put("84", R.drawable.img84);
        facemap2.put("85", R.drawable.img85);
        facemap2.put("86", R.drawable.img86);
        facemap2.put("87", R.drawable.img87);
        facemap2.put("88", R.drawable.img88);
        facemap2.put("89", R.drawable.img89);
        facemap2.put("90", R.drawable.img90);
        facemap2.put("91", R.drawable.img91);
        facemap2.put("92", R.drawable.img92);

        facemap2.put("93", R.drawable.img93);
        facemap2.put("94", R.drawable.img94);
        facemap2.put("95", R.drawable.img95);
        facemap2.put("96", R.drawable.img96);
        facemap2.put("97", R.drawable.img97);
        facemap2.put("98", R.drawable.img98);
        facemap2.put("99", R.drawable.img99);
        facemap2.put("100", R.drawable.img100);
        facemap2.put("101", R.drawable.img101);
        facemap2.put("102", R.drawable.img102);
        facemap2.put("103", R.drawable.img103);
        facemap2.put("104", R.drawable.img104);
        facemap2.put("105", R.drawable.img105);
        facemap2.put("106", R.drawable.img106);
        facemap2.put("107", R.drawable.img107);
        facemap2.put("108", R.drawable.img108);
        facemap2.put("109", R.drawable.img109);
        facemap2.put("110", R.drawable.img110);
        facemap2.put("111", R.drawable.img111);
        facemap2.put("112", R.drawable.img112);
        facemap2.put("113", R.drawable.img113);
        facemap2.put("114", R.drawable.img114);
        facemap2.put("115", R.drawable.img115);
        facemap2.put("116", R.drawable.img116);
        facemap2.put("117", R.drawable.img117);
        facemap2.put("118", R.drawable.img118);
        facemap2.put("119", R.drawable.img119);
        facemap2.put("120", R.drawable.img120);
        facemap2.put("121", R.drawable.img121);
        facemap2.put("122", R.drawable.img122);
        facemap2.put("123", R.drawable.img123);
        facemap2.put("124", R.drawable.img124);
        facemap2.put("125", R.drawable.img125);
        facemap2.put("126", R.drawable.img126);
        facemap2.put("127", R.drawable.img127);
        facemap2.put("128", R.drawable.img128);
        facemap2.put("129", R.drawable.img129);
        facemap2.put("130", R.drawable.img130);
        facemap2.put("131", R.drawable.img131);
        facemap2.put("132", R.drawable.img132);
        facemap2.put("133", R.drawable.img133);
        facemap2.put("134", R.drawable.img134);


    }


    public static class userinfoKeys {
        public final static String Content = "Content";
        public final static String Id = "id";
    }

    public static String getStaticmyuidstr() {
        return staticmyuidstr;
    }

    public static void setStaticmyuidstr(String staticmyuidstr) {
        Constants.staticmyuidstr = staticmyuidstr;
    }

    public static String getStaticpasswordstr() {
        return staticpasswordstr;
    }

    public static void setStaticpasswordstr(String staticpasswordstr) {
        Constants.staticpasswordstr = staticpasswordstr;
    }

    public static String getStaticLoginType() {
        return staticLoginType;
    }

    public static void setStaticLoginType(String staticLoginType) {
        Constants.staticLoginType = staticLoginType;
    }

}
