package com.example.sjqcjstock.netutil;

import java.security.MessageDigest;

public class Md5Util {
    private static MessageDigest md5 = null;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用于获取一个String的md5值
     *
     * @param string
     * @return
     */
    public static String getMd5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }


    /**
     * 截取 md5的前6位
     *
     * @param string
     * @return
     */
    public static String getuidstrMd5(String uidmd5) {
        StringBuilder uidimgurl = new StringBuilder();
        String uidmidimg = uidmd5.substring(0, 6);
        uidimgurl.append("http://www.sjqcj.com/data/upload/avatar/");
        uidimgurl.append(uidmidimg.substring(0, 2));
        uidimgurl.append("/");
        uidimgurl.append(uidmidimg.substring(2, 4));
        uidimgurl.append("/");
        uidimgurl.append(uidmidimg.substring(4, 6));
        uidimgurl.append("/");
        uidimgurl.append("original_200_200.jpg");
        return uidimgurl.toString();
    }
}
