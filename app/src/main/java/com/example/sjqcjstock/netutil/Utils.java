package com.example.sjqcjstock.netutil;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static long lastClickTime;

    /**
     * 防止暴力点击 1秒
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {       //1000毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 防止暴力点击  0.5秒
     */
    public static boolean isFastDoubleClick2() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {       //500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 防止暴力点击 1秒
     */
    public static boolean isFastDoubleClick4() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 4000) {       //1000毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //定义ListView的高度

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition    
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    //性能测试,计算耗时


    //判断是否离开edit区域的方法
    public static boolean isineditarea(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置  
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

    //将时间戳转换成date
    public static String getStringtoDate(String publish_timestr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(Long.parseLong(publish_timestr) * 1000);
        //Date curDate =new Date(System.currentTimeMillis());
        publish_timestr = formatter.format(curDate);
        return publish_timestr;
    }

    //将时间戳转换成date
    public static String getStringtoDate1(String publish_timestr) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd HH:mm");
        Date curDate = new Date(Long.parseLong(publish_timestr) * 1000);
        //Date curDate =new Date(System.currentTimeMillis());
        publish_timestr = formatter1.format(curDate);
        return publish_timestr;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String publish_timestr = formatter.format(curDate);
        return publish_timestr;
    }

    /**
     * 获取当前的时间yyMMdd
     *
     * @return
     */
    public static String getNowDate1() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String publish_timestr = formatter.format(curDate);
        return publish_timestr;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断是否是数字
     *
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(number);
        return m.matches();
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if ("".equals(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 格式化保留两位小数
     */
    public static String getNumberFormat(String str) {
        String format = "########0.00";
        if (str == null || "".equals(str) || "null".equals(str)) {
            return "0.00";
        }
        str = str.replace("%", "");
        double DStr = Double.valueOf(str);
        DecimalFormat df = new DecimalFormat(format);
        return df.format(DStr);
    }

    /**
     * 格式化小數
     */
    public static String getNumberFormat1(String str) {
        String format = "#########.##";
        if (str == null || "".equals(str) || "null".equals(str)) {
            return "0";
        }
        double DStr = Double.valueOf(str);
        DecimalFormat df = new DecimalFormat(format);
        return df.format(DStr);
    }

    /**
     * 把List<Map> 转成String
     *
     * @return
     */
    public static String getListMapStr(ArrayList<HashMap<String, String>> listMap) {
        if (listMap == null || listMap.size() < 1) {
            return "";
        }
        Gson gson = new Gson();
        String str = gson.toJson(listMap);
        return str;
    }

    /**
     * 把String转成List<Map>
     */
    public static ArrayList<HashMap<String, String>> getListMap(String str) {
        if (str == null || "".equals(str.trim())) {
            return new ArrayList<HashMap<String, String>>();
        }
        ArrayList<HashMap<String, String>> listMap = null;
        Gson gson = new Gson();
        listMap = gson.fromJson(str, new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType());
        return listMap;
    }


    /**
     * 往字符串中查入冒号
     *
     * @param str 1021
     * @return 10:21
     */
    public static String setInsertMark(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(3, ":");
        return sb.toString();
    }

    /**
     * 往字符串中查入斜杠
     *
     * @param str 1021
     * @return 10/21
     */
    public static String setInsertMark1(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(2, "/");
        return sb.toString();
    }

    /**
     * 除以一万后再进行格式化
     * 格式化保留两位小数
     */
    public static String getNumberFormatW(String str) {
        String format = "#########.00";
        if (str == null || str == "") {
            return "";
        }
        double DStr = Double.valueOf(str);
        DStr = DStr / 10000;
        DecimalFormat df = new DecimalFormat(format);
        return df.format(DStr);
    }

    /**
     * 传入时间进行拼接成 YYYY-MM-DD
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getStringDate(int year, int month, int day) {
        String str = year + "-";
        if (month < 10) {
            str += "0" + month + "-";
        } else {
            str += month + "-";
        }
        if (day < 10) {
            str += "0" + day;
        } else {
            str += day + "";
        }
        return str;
    }

    /**
     * 判断是否大于万 大于亿
     * 格式化保留两位小数
     */
    public static String getNumberFormatWY(String str) {
        String format = "#########.##";
        if (str == null || str == "") {
            return "";
        }
        double DStr = Double.valueOf(str);
        if (DStr >= 100000000) {
            DStr = DStr / 100000000;
            DecimalFormat df = new DecimalFormat(format);
            str = df.format(DStr) + "亿";
        } else if (DStr >= 10000) {
            DStr = DStr / 10000;
            DecimalFormat df = new DecimalFormat(format);
            str = df.format(DStr) + "万";
        }
        return str;
    }

    /**
     * 格式化传入的时间
     *
     * @return
     */
    public static String getFormatDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
        String date = "";
        try {
            date = formatter1.format(formatter.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 给传入两位数的年份减去一 并且只传回后两位
     *
     * @return
     */
    public static String getYearFormat(String str) {
        int year = Integer.valueOf(str);
        if (year == 0) {
            str = "99";
        } else {
            str = (year - 1) + "";
        }
        if (str.length() < 2) {
            str = "0" + str;
        }
        return str;
    }

    /**
     * 判断当前是否可以追加新的数据（如果）
     *
     * @return
     */
    public static Boolean isTimeOne(String time) {
        time = time.substring(0, 8);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        // 当前年月日
        String timestr = formatter.format(curDate);
        // 如果当前时间大于获取到的时间那么久不追加
        if (Double.valueOf(timestr) > Double.valueOf(time)) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        if (hour > 9 || (hour == 9 && minute > 30)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前是否是星期一
     *
     * @return
     */
    public static Boolean isWeekOne(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            // 传入时间
            calendar.setTime(format.parse("20" + date));
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int mouth = calendar.get(Calendar.MONTH);
            // JDK think 2015-12-31 as 2016 1th week
            //如果月份是12月，且求出来的周数是第一周，说明该日期实质上是这一年的第53周，也是下一年的第一周
            if (mouth >= 11 && week <= 1) {
                week += 52;
            }
            // 当前时间
            calendar.setTime(new Date());
            int weekNew = calendar.get(Calendar.WEEK_OF_YEAR);
            mouth = calendar.get(Calendar.MONTH);
            // JDK think 2015-12-31 as 2016 1th week
            //如果月份是12月，且求出来的周数是第一周，说明该日期实质上是这一年的第53周，也是下一年的第一周
            if (mouth >= 11 && week <= 1) {
                weekNew += 52;
            }
            // 如果不为同一周就然后true进行追加数据
            if (week != weekNew) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断当前是否当月数据
     * date 当最后第一个日期
     *
     * @return
     */
    public static Boolean isDayOne(String date) {
        int days = Integer.valueOf(date.substring(4, 2));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (days > day) {
            return true;
        }
        return false;
    }

    /**
     * 替换字符串中的网址添加A标签
     *
     * @param str
     * @return
     */
    public static String replaceWebUrl(List<String> strList, String str) {
        if (null == str || "".equals(str)) return "";
        if (strList == null) {
            return str;
        }
        for (String url : strList) {
            str = str.replace(url, "<a href = \"" + url + "\">网址</a>");
        }
        return str;
    }

    /**
     * 替换字符串中替换网址为文字
     *
     * @param str
     * @return
     */
//    public static String replaceWebUrlFont(JSONArray strList, String str) throws JSONException {
//        if (null == str || "".equals(str)) return "";
//        if (strList == null){
//            return str;
//        }
//        String url = "";
//        for (int i =0 ;i<strList.length() ;i++){
//            url = strList.getString(i);
//            str = str.replace(url,"<a href = \"" + url + "\">网址</a>");
//        }
//        return str;
//    }

}
