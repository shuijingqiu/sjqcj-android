package com.example.sjqcjstock.constant;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.entity.UnreadCount;

import java.util.HashMap;
import java.util.Timer;

public class Constants {

    public final static int NUMBER0 = 0;
    public final static int NUMBER1 = 1;
    // 网站自己用的app_key（自己生成的）
    public static final String app_key="2UV6QALXR81FJWNHFEAU9UH54PIPZ6TB";
    //微信app_id                          wx6608522223b1dea0
    public static final String APP_ID = "wx778e68d591bbe9c8";
    public static final String App_Secret = "72b7bdae9cfbfdf768360c628da89c25";
    // 微信支付的订单号
    public static String orderNumber = "";
    //是否刷新股吧列表
    public static String isreferforumlist = "0";//0为刷新，1为不刷新
    // 个人账户是否刷新
    public static boolean isBuy = false;
    // 是否刷新直播页面
    public static boolean isTomlive = false;
    //code
    public static String weixincode = "";
    //用户信息持久化
    public static String staticmyuidstr = "0";
    // 用户是否登录
    public static boolean isLogin = true;

//    // 存储用户头像地址 名称 头像
//    public static String headImg = "";
//    public static String unameStr = "";
//    public static String introStr = "";
//    // 存储用户类别（主要用于是否是禁言用户 如果为4 就为禁言用户）这个不要了  到时候后台控制
//    public static String userType = "1";
    // 用户信息
    public static UserEntity userEntity;

    //public static String staticpasswordstr="3665306";
    public static String staticpasswordstr = "";

    //微信或qqtokey
    public static String statictokeystr = "";
    public static String staticLoginType = "";
    // apptoken
    public static String apptoken = "";
    // 判断第三方登录的时候是否修改过密码
    public static Boolean isDefault = true;
    // 没有用
//    public static String intentFlag = "";
    // 全局变数水晶币个数(每次进入程序就需要获取一次)
    public static String shuijinbiCount = "0";
    // 检索后选中的股票代码和名称
    public static String choiceCode ="";
    public static String choiceName ="";
    // 判断是否延长订阅（用户页面刷新）
    public static boolean isDynamic = false;
    // 判断个人中心问答页面是否刷新
    public static boolean qaIsrn = false;

    // 返回的成功表示的code
    public static String successCode = "00";
    // 服务器路径
    // 最新域名
    public static String newUrl = "https://spare.sjqcj.com";
    public static String newUrls = "http://spare.sjqcj.com";

//     测试服务器
//    public static String newUrl = "http://beta.sjqcj.com";
//    public static String newUrls = "http://beta.sjqcj.com";
    // 模拟炒股的接口路径
    public static String moUrl = "https://moni.sjqcj.com";
    // 测试路径
//    public static String moUrl = "http://match.sjqcj.com";

    // 植入的h5页面
    public static String h5Url = "https://spare.sjqcj.com/dist/";
//    public static String h5Url = "http://test.sjqcj.com/";

//    // 分享的前半段路径
//    public static String shareArticle = Url + "?app=public&mod=Profile&act=sharemeager&feed_id=";18654946018
//    // 水晶币个数
//    public static String appUserMoneyUrl = Url + "?app=public&mod=Profile&act=AppUserMoney";
//    //微博列表接口
//    public static String subscribeListUrl = Url + "?app=public&mod=FeedListMini&act=getList";
//    // 提交打赏水晶币的接口
//    public static String apprewardUrl = Url + "?app=public&mod=AppFeedList&act=Appreward";
//    // 获取打赏列表消息的接口
//    public static String rewardMessagedUrl = Url + "?app=public&mod=AppFeedList&act=SystemMessage";
//    // 获取系统消息的接口
//    public static String systemMessagedUrl = Url + "?app=public&mod=Index&act=getSysMessage";
//    // 水晶币兑换的协议接口
//    public static String agreementUrl = Url + "?app=public&mod=AppFeedList&act=agreement";
//    // 流动广告的接口
//    public static String globalSwf = Url + "?app=public&mod=AppFeedList&act=GlobalSwf";
//    // 文章打赏水晶币人数的详细列表
//    public static String playAMan = Url + "?app=public&mod=AppFeedList&act=PlayAMan";
//    // 调用统一下单的接口
////	public static String unifiedorder = "http://test.sjqcj.com/index.php?app=public&mod=App&act=test";
//    public static String unifiedorder = Url + "?app=public&mod=App&act=wxpay";
//    // 查询订单是否成功的接口
//    public static String queryOrder = Url + "?app=public&mod=App&act=queryOrder";
//    // 未读消息体的结构
//    public static String unreadCount = Url + "?app=public&mod=Index&act=getAppUnreadCount";
    // 页面加载是显示的语句
    public static String loadMessage = "加载中...";
    // 无网络的时候
    public static String noNetwork = "获取数据失败，网络不给力！";
    // 无数据的时候
    public static String noData = "你访问的页面暂时还没有数据！";
    // 微博分享
    public static String microBlogShare = "微博分享";
    // 各种消息条数的实体类
    public static UnreadCount unreadCountInfo = new UnreadCount();
    // 用户获取token 时调用的定时器
    public static Timer timer;

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

    public Constants() {
        super();
        // TODO Auto-generated constructor stub
        initfacemap2();

    }

    private static void initfacemap2() {
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
