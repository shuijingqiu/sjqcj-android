package com.example.sjqcjstock.netutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.mob.tools.network.SSLSocketFactoryEx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class HttpUtil {

    //客户端访问服务器的网络编程协议工具

    //客户端根据访问的路径得到json字符串内容
    public static String getJsonContent(String url_path) {

        try {
            //获取一个URL的对象
            URL url = new URL(url_path);
            //获得http连接
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            //设置请求的参数
            //设置连接的时间
            connection.setConnectTimeout(3000);
            //设置http网络访问方式
            connection.setRequestMethod("GET");
            //设置能够获取输出流
            connection.setDoInput(true);
            //判断响应的状态码
            int code = connection.getResponseCode();

            if (code == 200) {
                //如果状态码等于200,表示服务器已准备好，可以从服务器取出io流回来
                //将IO流转化成json字符串
                return changeInputStream(connection.getInputStream());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return url_path;

    }

    //将从服务器获取的IO流转化成json字符串   低性能
    public static String changeInputStream(InputStream inputStream) {
        String jsonString = "";
        //获得字节叔祖写入流，准备往内存里面写数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];

        try {
            //通过inputStream 将从服务器获取的输出流,
            //再将输出流通过outputStream写入到内存当中

            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }

            //将写入流中的流转换为字节数组，再将字节数组构建为字符串，最终得到json字符串
            jsonString = new String(outputStream.toByteArray());
        } catch (Exception e) {
            // TODO: handle exception
        }

        return jsonString;

    }


    /**
     * 将从服务器获取的IO流转化成json字符串   高性能
     */
    public static String changeInputStream2(InputStream inputStream) {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //获得字节叔祖写入流，准备往内存里面写数据
        //ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        //int len=0;
        //byte[] data=new byte[1024];
        BufferedReader bufferedReader = null;
        StringBuilder jsonbuilder = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream ,"UTF-8"));
            jsonbuilder = new StringBuilder();
            String jsonString = "";
            //通过inputStream 将从服务器获取的输出流,
            //再将输出流通过outputStream写入到内存当中

            while ((jsonString = bufferedReader.readLine()) != null) {
                //outputStream.write(data,0,len);
                jsonbuilder.append(jsonString);
            }

            //将写入流中的流转换为字节数组，再将字节数组构建为字符串，最终得到json字符串
            //jsonString=new String(outputStream.toByteArray());
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonbuilder.toString();

    }

    //客户端根据访问的路径将json字符串作为参数存储request中传送给服务器
    public static String JsontoServiceContent(String url_path, String jsonString) {


        try {
            //获取一个URL的对象
            URL url = new URL(url_path);
            //获得http连接
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            //设置请求的参数
            //设置连接的时间
            connection.setConnectTimeout(3000);
            //设置http网络访问方式
            connection.setRequestMethod("POST");


            //设置能够获取输出流
            connection.setDoInput(true);
            //connection.setDoOutput(true);
            //判断响应的状态码
            int code = connection.getResponseCode();

            if (code == 200) {
                //如果状态码等于200,表示服务器已准备好，可以从服务器取出io流回来
                //将IO流转化成json字符串
                OutputStream os = connection.getOutputStream();
                os.write(jsonString.getBytes());
                os.close();

                //将IO流转化成json字符串
                return changeInputStream(connection.getInputStream());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return url_path;
    }

    // post 提交
    public static String doInBackground(TaskParams... params) {
        if (params.length == 0)
            return null;
        TaskParams tp = params[0];
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        String resstr = null;
        Log.e("mh-URl:-", "+" + tp.getUrl());
        Log.e("mh-Params:-", "+" + tp.getEncodeParams());
        try {
            conn = (HttpURLConnection) new URL(tp.getUrl()).openConnection();
            // POST GET
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            out = conn.getOutputStream();
//            Log.e("mh-con34n:-", "+" + conn.getRequestMethod());
            String paramsstr = tp.getEncodeParams();
            out.write(paramsstr.getBytes());
            out.flush();
            out.close();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                resstr = HttpUtil.changeInputStream2((in));
            }
            if ("".equals(resstr)) {
                resstr = null;
            }
            Log.e("mh-resstr:-", "+" + resstr);
        } catch (IOException e) {
//            Log.e("网络关闭出错了！IOException", e.getMessage()+"");
            resstr = null;
            e.printStackTrace();
        } catch (Exception e) {
//            Log.e("网络关闭出错了！Exception", e.getMessage()+"");
            resstr = null;
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                resstr = null;
            }
        }
        return resstr;
    }

    // get 提交
    public static String doInBackgroundGet(TaskParams... params) {
        if (params.length == 0)
            return null;
        TaskParams tp = params[0];
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        String resstr = null;
        Log.e("mh-URl:-", "+" + tp.getUrl());
        Log.e("mh-Params:-", "+" + tp.getEncodeParams());
        try {
            conn = (HttpURLConnection) new URL(tp.getUrl()).openConnection();
            // POST GET
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                resstr = HttpUtil.changeInputStream2((in));
            }
            if ("".equals(resstr)) {
                resstr = null;
            }
            Log.e("mh-resstr:-", "+" + resstr);
        } catch (IOException e) {
            resstr = null;
            e.printStackTrace();
        } catch (Exception e) {
            resstr = null;
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                resstr = null;
            }
        }
        return resstr;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
//                    Log.e("mh 网络检查state",i + "===状态===" + networkInfo[i].getState());
//                    Log.e("mh 网络检查name",i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 微信支付调用网络的方法
     *
     * @param url
     * @return
     */
    public static byte[] httpGet(final String url) {
        if (url == null || url.length() == 0) {
//            Log.e("wxzfmh", "httpGet, url is null");
            return null;
        }

        HttpClient httpClient = getNewHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse resp = httpClient.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                Log.e("wxzfmh", "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());

        } catch (Exception e) {
//            Log.e("wxzfmh", "httpGet exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取网络股票信息数据
     *
     * @return
     */
    public static String getIntentData(String url) {
        String strDta = "";
        try {
            Log.e("mhresult-url-123- ", url);
            URL uri = new URL(url);//注意，这里的URL地址必须为网络地址，
            HttpURLConnection ucon = (HttpURLConnection) uri.openConnection();
            ucon.setConnectTimeout(2000);// 设置连接主机超时
            ucon.setReadTimeout(2000);// 设置从主机读取数据超时
            InputStream is = ucon.getInputStream();
//            strDta = changeInputStream2(is);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is ,"GBK"));
            StringBuilder jsonbuilder = new StringBuilder();
            while ((strDta = bufferedReader.readLine()) != null) {
                //outputStream.write(data,0,len);
                jsonbuilder.append(strDta);
            }
            strDta = jsonbuilder.toString();
            Log.e("mhresult--12- ", strDta+"-");
//            reader.close();
        } catch (Exception e) {
            Log.e("mh", e.toString());
        }
        return strDta;
    }

    /**
     * Rest 的get获取http数据的方式
     * @return
     */
    public static String restHttpGet(String url){
        //创建一个http客户端
        HttpClient client = getNewHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        //创建一个GET请求
        HttpGet httpGet=new HttpGet(url);
        Log.e("mh123url:",url + "---");
        String resstr = "";
        try{
            //向服务器发送请求并获取服务器返回的结果
            HttpResponse response=client.execute(httpGet);
            //返回的结果可能放到InputStream，http Header中等。
            InputStream inputStream=response.getEntity().getContent();
            resstr = changeInputStream2((inputStream));
            Log.e("mh123:",resstr + "---");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("mh123:",e.getMessage());
            return "";
        }
        return resstr;
    }

    /**
     * Rest 的Post获取http数据的方式
     * @return
     */
    public static String restHttpPost(String url,List listData){
        Log.e("mhUrlpost:",url);
        //创建一个http客户端
        HttpClient client = getNewHttpClient();
        //创建一个POST请求
        HttpPost httpPost=new HttpPost(url);
        String resstr = "";
        try{
            HttpEntity entity = new UrlEncodedFormEntity(listData, "UTF-8");
            httpPost.setEntity(entity);
            //向服务器发送请求并获取服务器返回的结果
            HttpResponse response=client.execute(httpPost);
            //返回的结果可能放到InputStream，http Header中等。
            InputStream inputStream=response.getEntity().getContent();
            resstr = changeInputStream2((inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("mh123:",e.getMessage());
            return "";
        }
        return resstr;
    }

    /**
     * Rest 的Put获取http数据的方式
     * @return
     */
    public static String restHttpPut(String url,List dataList){
        //创建一个http客户端
        HttpClient client = getNewHttpClient();
        //创建一个GET请求
        HttpPut httpPut=new HttpPut(url);
        String resstr = "";
        try{
            HttpEntity entity = new UrlEncodedFormEntity(dataList, "UTF-8");
            httpPut.setEntity(entity);
            //向服务器发送请求并获取服务器返回的结果
            HttpResponse response=client.execute(httpPut);
            //返回的结果可能放到InputStream，http Header中等。
            InputStream inputStream=response.getEntity().getContent();
            resstr = changeInputStream2((inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("mh123:",e.getMessage());
            return "";
        }
        return resstr;
    }

    /**
     * Rest 的Delete获取http数据的方式
     * @return
     */
    public static String restHttpDelete(String url){
        //创建一个http客户端
        HttpClient client = getNewHttpClient();
        //创建一个GET请求
        HttpDelete  httpDelete=new HttpDelete(url);
        String resstr = "";
        try{
            //向服务器发送请求并获取服务器返回的结果
            HttpResponse response=client.execute(httpDelete);
            //返回的结果可能放到InputStream，http Header中等。
            InputStream inputStream=response.getEntity().getContent();
            resstr = changeInputStream2((inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return resstr;
    }

}
