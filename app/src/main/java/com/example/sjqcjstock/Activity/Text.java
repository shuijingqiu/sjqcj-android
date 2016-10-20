package com.example.sjqcjstock.Activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/9.
 */
public class Text {
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

        String str = "你的房间就是你的 https://a5242.com/abd/d/aa.png 不和爱人争对错,生命状态 http://t.cn/RVLWazx 不 http://a.b.c.com 和爱人争对错，不和朋友争高低。http://t.cn/RVLWazJ你好这是网页";
//        Pattern pattern = Pattern.compile("(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$");

        String sstr = str.replaceAll("[\\u4e00-\\u9fa5]", " ");

        System.out.println("ur1l:" + sstr);

        Pattern pattern = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
        Matcher matcher = pattern.matcher(str);
        String url = "";
        while (matcher.find()) {
            url = matcher.group();
            System.out.println("url:" + url);
            str = str.replace(url, "<a href = \"" + url + "\">" + url + "</a>");
        }
//       str = Utils.replaceWebUrl(str);
        System.out.println("url:" + str);
    }
}
