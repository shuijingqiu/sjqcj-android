package com.example.sjqcjstock.Activity;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/9.
 */
public class Text {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void main(String args[]) {
//        String str = "你的房间就是你的http://5242.com/abd/d/aa.png不和爱人争对错,生命状态http://t.cn/RVLWazx不http://a.b.c.com和爱人争对错，不和朋友争高低。http://t.cn/RVLWazJ你好这是网页";
////        String str ="http://www.abidu.com哈哈这个是http://weixn.co.cn哈哈";
//        Pattern pattern = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
//        Matcher matcher = pattern.matcher(str);
//        while(matcher.find()){
//            System.out.println(matcher.group());
//        }
//        str = str.replaceAll("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?", "123456");
//        System.out.println(str);
//        System.out.println("Hello World!");

//        String str = "你的房间就是你的 https://a5242.com/abd/d/aa.png 不和爱人争对错,生命状态 http://t.cn/RVLWazx 不 http://a.b.c.com 和爱人争对错，不和朋友争高低。http://t.cn/RVLWazJ你好这是网页";
////        Pattern pattern = Pattern.compile("(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$");
//
//        String sstr = str.replaceAll("[\\u4e00-\\u9fa5]", " ");
//
//        System.out.println("ur1l:" + sstr);
//
//        Pattern pattern = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
//        Matcher matcher = pattern.matcher(str);
//        String url = "";
//        while (matcher.find()) {
//            url = matcher.group();
//            System.out.println("url:" + url);
//            str = str.replace(url, "<a href = \"" + url + "\">" + url + "</a>");
//        }
////       str = Utils.replaceWebUrl(str);
//        System.out.println("url:" + str);

//        System.out.println(HttpUtil.restHttpGet("http://192.168.2.107/orders"));
//        System.out.println("160503".substring(4)+"     ");
//        String str ="160503";
//        System.out.println(str.substring(2,4)+"   -*+");
//        double number = 123000;
//        System.out.println(number%100+"");
//        System.out.println((int)(number/100)+"00");
//        String code = "var hq_str_sz000750=阿道夫";
//        System.out.println(code.indexOf("hq_str_s"));
//        System.out.println(code.substring(13,19));
//        code = code.substring(code.indexOf("hq_str_s"));
//        System.out.println(code);
//        String str = "2016-12-15 13:09:06";
//        System.out.println(str.substring(5,10));

//        String str = "ST萌黄";
//        System.out.println(str.toLowerCase().indexOf("ST"));
//        System.out.println(str.toLowerCase().indexOf("st"));

//        String str = "sh600001";
//        System.out.println(str.substring(2));

//        try {
//            System.out.println(Utils.CalculatedDays("2016-12-29 23:59"));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//        System.out.println(Utils.GetSysDate("yyyy-MM-dd HH:mm","2016-12-28 23:59",0,1,-1));
//        System.out.println(Utils.FormattingTime("yyyy-MM-dd HH:mm","2016-12-28 23:59:59"));
//        String str = "123456789";
//        System.out.print(str.substring(0,str.length()-1));
//
//        int width = 1080;
//        int height = 1920;
//        Double proportion = 1.0d;
//
//        if (width>480.0){
//            proportion = 480.0/width;
//        }
//        System.out.println(480.0/width);
//        System.out.println(proportion);
//        if (proportion*height>800.0){
//            proportion = 800.0/height;
//        }
//        System.out.println((int)(width*proportion)+"");
//        System.out.println(proportion);

//        String url = "http://www.sjqcj.com/weibo/815457/";
//        String feedId = url.substring(url.indexOf("weibo/"));
//        feedId=feedId.replace("weibo/","");
//        feedId=feedId.replace("/","");
//        System.out.println(feedId);

//        HashMap<String, Object> hashMap = new HashMap<String, Object>();
//        hashMap.put("mh1","mh1");
//        hashMap.put("mh2","mh2");
//        hashMap.put("mh3","mh3");
//        hashMap.put("mh4","mh4");
//        for (int i=0;i<hashMap.size();i++){
//            System.out.println(hashMap.get(i));
//        }

//        String str = "http:\\/\\/www.sjqcj.com\\/addons\\/theme\\/stv1\\/_static\\/image\\/usergroup\\/v_07.png";
//        str = str.replace("\\/","/");
//        System.out.println(str);
//        String str = "20|2.6.8|http://a.app.qq.com/o/simple.jsp?pkgname=com.example.sjqcjstock";
//        String[] strs = str.split("\\|");
//        System.out.println(strs[1]);
//        System.out.println(strs[2]);
//        String url  = "http://www.sjqcj.com/live/28993?rid=29";
//        url =  url.substring(url.indexOf("live/"));
//        url = url.replace("live/","");
//        url = url.replace("rid=","");
//        String[] strs = url.split("\\?");
//        System.out.println(strs[0]+"uid="+strs[1]);
//        String str  = "你好[ddf啊这里是[是]拿了[ci]倒萨sfasf12[haha]adf哦s]哦";
//        Pattern pattern = Pattern.compile("(\\[[a-z]+?\\])");
//        Matcher matcher = pattern.matcher(str);
//        str = matcher.replaceAll("+++++++");
//        System.out.println(str);

//        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("0","menghuan");
//        map.put("1","123321");
//        mapList.add(map);
//
//        for (int i = 0;i< mapList.size();i++){
//            System.out.println(mapList.get(i).values());
//        }




//        ArrayList<Integer> arrayList = new ArrayList<Integer>();
//        arrayList.add(23);
//        arrayList.add(24);
//        arrayList.add(30);
//        arrayList.add(18);
//        arrayList.add(41);
//        arrayList.add(5);
//        arrayList.add(2);
//        arrayList.add(35);
//        arrayList.add(9);
//        arrayList.add(19);
//        arrayList.add(8);
//        arrayList.add(6);
//        arrayList.add(11);
//        arrayList.add(17);
//        arrayList.add(31);
//        arrayList.add(43);
//
//        Map<String ,Integer> list = new HashMap<String,Integer>();
//        int jieguo = 24;
//        int number = 0;
//        for (int i=0;i<arrayList.size();i++){
//            number = arrayList.get(i);
//            if (number > jieguo){
//                continue;
//            }
//            for (int j=i;j<arrayList.size();j++){
//                number += arrayList.get(j);
//                if (number > jieguo){
//                    continue;
//                }
//                for (int k=j;k<arrayList.size();k++){
//                    number += arrayList.get(k);
//                    if (number > jieguo){
//                        continue;
//                    }
//                    for (int n=k;n<arrayList.size();n++){
//                        number += arrayList.get(k);
//                        if (number > jieguo){
//                            continue;
//                        }
//                        if (number == jieguo){
//                            System.out.println(arrayList.get(i) +"  "+arrayList.get(j) +"  "+arrayList.get(k) +"  "+arrayList.get(n) +"  ");
//                        }
//                    }
//                }
//            }
//        }

//        String source ="http://spare.sjqcj.com/addons/theme/stv1/_static/image/expression/miniblog/cheer.gif";
//        source = source.substring(source.lastIndexOf("/" )+1,source.indexOf(".gif"));
//        String source = "sh600321";
//        System.out.println(source.substring(2)+"  -*-*-  ");

        String str = "645564是否sdfad.3a-+5%sd|f.a45s|4_-5df";
        str = str.replaceAll("[^0-9,|]", "");
        System.out.println(str);

    }

    private String datas(ArrayList<Integer> arrayList,int i,ArrayList<Integer> jgList,int number){
        int jieguo  = 0;
        for (int n=i;n<arrayList.size();n++){
            for (int k=0;k<jgList.size();k++){
                jieguo += jgList.get(k);
            }
            jgList.add(arrayList.get(i));
            jieguo += arrayList.get(i);
            if (jieguo == number){
                System.out.println(jgList.toString());
            }else if (jieguo > number){
                System.out.println(jgList.toString());
            }
        }
        return "";
    }



    public static Boolean isDayOne(String date) {
        int days = Integer.valueOf(date.substring(4, 1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (days > day) {
            return true;
        }
        return false;
    }
}
