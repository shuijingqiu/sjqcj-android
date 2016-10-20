package com.example.sjqcjstock.netutil;


/*
 * jsontoObject   Objecttojson
 * */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用 fastJson来解析数据
 * <p/>
 * FastJson是一个Json处理工具包，包括"序列化"和"反序列化"两个部分，它具备如下特征:
 * <p/>
 * FastJson解析Json字符串速度最快，测试表明，fastjson具有极快的性能，超越任何其他的
 * Java Json parser。 包括自称最快的JackJson 。
 * <p/>
 * 功能强大，完全支持Java Bean、集合、日期、Enum、支持泛型，支持自省
 * <p/>
 * 无依赖，能够直接运行在Java SE 5.0 以上版本
 */
public class JsonTools {

    //解析json字符串,获得person对象

    /**
     * 完成对单个javaBean的解析
     */
    //支持json字符串获得person对象
    //支持使用反射机制来赋值
    public static <T> T getPerson(String jsonString, Class<T> cls) {
        T t = null;

        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }

    /**
     * 使用fastJson进行解析  List<Person>
     *
     * @param <T>
     * @param jsonString 要解析的字符串
     * @param cls
     */

    public static <T> List<T> getPersons(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();

        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;

    }

    /**
     * 使用fastJson进行解析  List<Map<String,Object>>
     *
     * @param jsonString 要解析的字符串
     */

    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 生成一个json字符串
     */

    public static String createJsonString(Object value) {
        //获取Gson对象
        Gson gson = new Gson();
        //获取json字符串
        String gString = gson.toJson(value);
        return gString;

    }

}
