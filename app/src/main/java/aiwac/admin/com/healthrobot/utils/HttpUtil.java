package aiwac.admin.com.healthrobot.utils;

import android.content.SharedPreferences;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.HttpException;

import static android.content.Context.MODE_PRIVATE;

/**     封装访问服务器细节
 * Created by luwang on 2017/10/24.
 */

public class HttpUtil {

    public static String requestGet(String baseUrl, HashMap<String, String> params) {

        HttpURLConnection urlConn = null;
        try {
            StringBuilder requestParam = new StringBuilder();
            if(params != null) {
                int pos = 0;
                for (String key : params.keySet()) {
                    if (pos > 0) {
                        requestParam.append("&");
                    }
                    requestParam.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), Constant.APLLICATION_CODE)));
                    pos++;
                }
            }
            String requestUrl = baseUrl + requestParam.toString();

            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod(Constant.HTTP_METHOD_GET);
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                LogUtil.d(Constant.HTTP_METHOD_POST_SUCCESS + result);
                return result;
            } else {
                LogUtil.d(Constant.HTTP_METHOD_GET_FAILURE);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(Constant.HTTP_METHOD_GET_EXCEPTION);
            throw new HttpException(Constant.HTTP_METHOD_GET_EXCEPTION, e);
        }finally {
            if(urlConn != null) {
                // 关闭连接
                urlConn.disconnect();
            }
        }
    }


    public static String requestPost(String baseUrl, HashMap<String, String> params) {
        DataOutputStream dos = null;
        HttpURLConnection urlConn = null;
        try {
            //合成参数
            StringBuilder requestParam = new StringBuilder();
            int pos = 0;
            for (String key : params.keySet()) {
                if (pos > 0) {
                    requestParam.append("&");
                }
                requestParam.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key),Constant.APLLICATION_CODE)));
                pos++;
            }
            // 请求的参数转换为byte数组
            byte[] postData = requestParam.toString().getBytes();

            //连接网络
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod(Constant.HTTP_METHOD_POST);
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();

            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                LogUtil.d(Constant.HTTP_METHOD_POST_SUCCESS + result);
                return result;
            } else {
                LogUtil.d(Constant.HTTP_METHOD_POST_FAILURE);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(Constant.HTTP_METHOD_POST_EXCEPTION);
            throw new HttpException(Constant.HTTP_METHOD_POST_EXCEPTION, e);
        }finally {
            if(dos != null){
                try{
                    dos.close();
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d(Constant.HTTP_CLOSE_STREAM_EXCEPTION);
                }
            }
            if(urlConn != null) {
                // 关闭连接
                urlConn.disconnect();
            }
        }
    }

    public static String requestPostJson(String baseUrl, String jsonRequest) {
        DataOutputStream dos = null;
        HttpURLConnection urlConn = null;
        try {

            // 请求的参数转换为byte数组
            byte[] postData = jsonRequest.toString().getBytes();

            //连接网络
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod(Constant.HTTP_METHOD_POST);
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();

            LogUtil.d(urlConn.getResponseCode()+"@@@@");
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                LogUtil.d(Constant.HTTP_METHOD_POST_SUCCESS );
                String result = streamToString(urlConn.getInputStream());
                LogUtil.d(Constant.HTTP_METHOD_POST_SUCCESS + result);
                if(result == null || result.equals("")){
                    return "200";
                }else{
                    return result;
                }

            } else {
                LogUtil.d(Constant.HTTP_METHOD_POST_FAILURE);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(Constant.HTTP_METHOD_POST_EXCEPTION);
            throw new HttpException(Constant.HTTP_METHOD_POST_EXCEPTION, e);
        }finally {
            if(dos != null){
                try{
                    dos.close();
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d(Constant.HTTP_CLOSE_STREAM_EXCEPTION);
                }
            }
            if(urlConn != null) {
                // 关闭连接
                urlConn.disconnect();
            }
        }
    }

    public static String requestTokenString(String baseUrl, String jsonRequest) {
        DataOutputStream dos = null;
        HttpURLConnection urlConn = null;
        try {

            // 请求的参数转换为byte数组
            byte[] postData = jsonRequest.toString().getBytes();

            //连接网络
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            SharedPreferences pref = HealthRobotApplication.getContext().getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
            String token = pref.getString(Constant.USER_DATA_FIELD_TOKEN, "");
            String tokenStr = "Bearer " + token;
            urlConn.addRequestProperty("Authorization", tokenStr);

            // 设置为Post请求
            urlConn.setRequestMethod(Constant.HTTP_METHOD_POST);
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();

            LogUtil.d(urlConn.getResponseCode()+"@@@");
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                LogUtil.d(Constant.HTTP_METHOD_POST_SUCCESS + result);
                return result;
            } else {
                LogUtil.d(Constant.HTTP_METHOD_POST_FAILURE);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(Constant.HTTP_METHOD_POST_EXCEPTION);
            throw new HttpException(Constant.HTTP_METHOD_POST_EXCEPTION, e);
        }finally {
            if(dos != null){
                try{
                    dos.close();
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d(Constant.HTTP_CLOSE_STREAM_EXCEPTION);
                }
            }
            if(urlConn != null) {
                // 关闭连接
                urlConn.disconnect();
            }
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    private static String streamToString(InputStream is) {
        if(is == null) {
            return null;
        }

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(Constant.HTTP_STREAM2STRING_EXCEPTION);
            throw new HttpException(Constant.HTTP_STREAM2STRING_EXCEPTION, e);
        }finally {
            if(baos != null){
                try {
                    baos.close();
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d(Constant.HTTP_CLOSE_STREAM_EXCEPTION);
                }

            }
            if(is != null){
                try {
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d(Constant.HTTP_CLOSE_STREAM_EXCEPTION);
                }

            }
        }

    }

}
