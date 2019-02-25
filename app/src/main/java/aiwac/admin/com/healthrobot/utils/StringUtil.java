package aiwac.admin.com.healthrobot.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import aiwac.admin.com.healthrobot.common.Constant;

/**       字符串操作工具类
 * Created by luwang on 2017/10/17.
 */

public class StringUtil {

    /**
     * 是否是有效字符串  null，空串，只有空格的字符串等不是有效字符串
     * @param str   字符串
     * @return  true ：有效， false无效
     */
    public static boolean isValidate(String str){
        if(str != null && !str.trim().equals(""))
            return true;
        return false;
    }

    /**
     * 检查一个字符串是否是手机号码
     * @param str   字符串
     * @return  true ：是手机号码， false不是手机号码
     */
    public static boolean isNumber(String str){
        String pattern = "^1\\d{10}$";
        boolean isMatch = Pattern.matches(pattern, str);
        return isMatch;
    }

    /**
     * 检查一个字符串是否是邮箱号码
     * @param str   字符串
     * @return  true ：是，邮箱地址， false不是
     */
    public static boolean isEmail(String str){
        String pattern = "^.+@.+\\..+$";
        boolean isMatch = Pattern.matches(pattern, str);
        return isMatch;
    }

    /**
     * 检查一个字符串是否是有效密码
     * @param str   字符串
     * @return  true ：是， false不是
     */
    public static boolean isPassword(String str){
        String pattern = "^.{6,20}$";
        boolean isMatch = Pattern.matches(pattern, str);
        return isMatch;
    }


    /**
     *  验证码是否有效
     * @param userCheckcode  服务器接受到的验证码
     * @param inputCheckcode  用户收入的验证码
     * @return  true 有效
     */
    public static boolean isCheckcodeValidate(String userCheckcode, String inputCheckcode){
        if(isValidate(inputCheckcode) && isValidate(userCheckcode) && userCheckcode.equals(inputCheckcode)){
            //检验验证码是否有效
            return true;
        }
        return false;
    }


    public static String joinButtonText(String text, long time){
        String baseStr = text.replaceAll("\\(.*\\)", "");
        if(time == 0){
            return baseStr;
        }
        return baseStr + "("+ time + ")";
    }

    /**
     *  对给定字符串进行md5加密，以便用于网络传输
     * @param str
     * @return
     */
    public static String md5(String str){
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest messageDigest = MessageDigest.getInstance(Constant.SECURITY_MD5);
            //添加要进行计算摘要的信息
            messageDigest.update(str.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, messageDigest.digest()).toString(16);

        } catch (Exception e) {
            LogUtil.d(Constant.SECURITY_MD5_FAILURE);
            e.printStackTrace();
            throw new aiwac.admin.com.healthrobot.exception.HttpException(Constant.SECURITY_MD5_FAILURE, e);
        }
    }

    public static String getTimerStrategy(String activationMode){
        if(activationMode.equals("0000000"))
            return "只响一次";

        if(activationMode.equals("1111111"))
            return "每天";

        if(activationMode.equals("1100000"))
            return "周末";

        if(activationMode.equals("0011111"))
            return "周一到周五";

        if(activationMode.equals("0000001"))
            return "周一";

        if(activationMode.equals("0000010"))
            return "周二";

        if(activationMode.equals("0000100"))
            return "周三";

        if(activationMode.equals("0001000"))
            return "周四";

        if(activationMode.equals("0010000"))
            return "周五";

        if(activationMode.equals("0100000"))
            return "周六";

        if(activationMode.equals("1000000"))
            return "周日";

        return "只响一次";
    }

    public static int getHour(String activatedTime){
        try{
            String[] time = activatedTime.split(":");
            return Integer.parseInt(time[0]);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int getMinute(String activatedTime){
        try{
            String[] time = activatedTime.split(":");
            return Integer.parseInt(time[1]);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    private static Date stringToDate(String strTime, String formatType) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = formatter.parse(strTime);
        return date;
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    private static Date longToDate(long currentTime, String formatType) throws Exception{
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    private static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    private static String longToString(long currentTime, String formatType) throws Exception{
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    public static String longToString(long currentTime){
        try {
            return longToString(currentTime, "yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String longToString(String currentTime){
        try {
            return longToString(Long.parseLong(currentTime));
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String nowDateToString(){
        return dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
    }


}


