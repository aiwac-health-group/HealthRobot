package aiwac.admin.com.healthrobot.common;

/**
 * Created by 14750 on 2019/2/23.
 */

public class Constant {

    public final static String TEST_IP = "101.132.192.66:80";

    //WebSocket 和讲座 健康检测结果  相关的

    public final static String WEBSOCKET_MESSAGE_ACCOUNT = "account";
    public final static String WEBSOCKET_MESSAGE_CODE = "code";
    public final static String WEBSOCKET_MESSAGE_LECTUREID = "lectureID";
    public final static String WEBSOCKET_MESSAGE_HEALTH_TEST_RESULT = "resultID";

    public final static String WEBSOCKET_MESSAGE_DATA  = "data";


    public final static String WEBSOCKET_LECTURE_ABSTRACT  = "lectureAbstract";
    public final static String WEBSOCKET_LECTURE_COURSE_UPDATETIME  = "updateTime";
    public final static String WEBSOCKET_LECTURE_COURSE_DESCRIPTION = "description";
    public final static String WEBSOCKET_LECTURE_COURSE_COVER  = "cover";
    public final static String WEBSOCKET_LECTURE_COURSE_DURATION = "duration";
    public final static String WEBSOCKET_LECTURE_COURSE_NAME = "name";
    public final static String WEBSOCKET_MESSAGE_LECTURE_AV_LINK = "link";
    public final static String WEBSOCKET_MESSAGE_LECTURE_CONTEXT = "lectureContext";
    public final static String WEBSOCKET_MESSAGE_RESULTID = "resultID";
    public final static String WEBSOCKET_MESSAGE_UPDATETIME = "updateTime";
    public final static String WEBSOCKET_MESSAGE_RESULT_CONTEXT = "resultContext";
    public final static String WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE = "robot";
    public final static String WEBSOCKET_TIMER_STATUS = "status";
    public final static String WEBSOCKET_BUSINESS_DOWNLOAD_LECTURE = "正在加载讲座资源，稍等...";


    //和安全相关的
    public final static String SECURITY_MD5 = "MD5";
    public final static String SECURITY_MD5_FAILURE = SECURITY_MD5 + "算法加密失败";

    //和定时任务有关的
    public final static String TIMER_ATTENTIONTYPE_EAT = "01";
    public final static String TIMER_ATTENTIONTYPE_EAT_VALUE = "该吃饭了";
    public final static String TIMER_ATTENTIONTYPE_SLEEP = "02";
    public final static String TIMER_ATTENTIONTYPE_EDUCATION = "03";
    public final static String TIMER_ATTENTIONTYPE_CUSTOM = "99";
    public final static int TIMER_OPERATIONTYPE_ADD = 1;
    public final static int TIMER_OPERATIONTYPE_UPDATE = 2;
    public final static int TIMER_OPERATIONTYPE_DELETE = 3;
    public final static int TIMER_OPEN = 1;
    public final static int TIMER_CLOSE = 2;
    public final static int TIMER_COMMIT = 1;
    public final static int TIMER_UNCOMMIT = 2;
    public final static String TIMER_OPEN_VALUE = "开启";
    public final static String TIMER_CLOSE_VALUE = "未开启";
    public final static String TIMER_UPDATE = "更新定时任务";



    //文件相关操作
    public final static String FILE_FOLD_VOICE ="voice"; //语音文件默认文件夹
    public final static String FILE_FILE_VOICE ="aiwac_voice.wav"; //语音文件默认名
    public final static String FILE_FILE_ANSWER_VOICE ="aiwacAnswerVoice.wav"; //语音文件默认名
    public final static String FILE_FILE_QUESTION_VOICE ="aiwacQuestionVoice.wav"; //语音文件默认名
    public final static String FILE_FILE_MONITOR_QUESTION_VOICE ="aiwacMonitorQuestionVoice.wav"; //监控模式语音文件默认名
    public final static String FILE_OPERATOR_EXCEPTION ="文件操作异常";
    public final static String FILE_OPERATOR_SUCCESS ="文件操作成功";
    public final static String FILE_FILE_EXIST ="文件已经存在";
    public final static String FILE_FOLD_EXIST ="文件夹已经存在";
    public final static String FILE_CREATE_FILE_SUCCESS ="创建文件成功";
    public final static String FILE_CREATE_FOLD_SUCCESS ="创建文件夹成功";
    public final static String FILE_CREATE_FILE_EXCEPTION ="创建文件异常";
    public final static String FILE_CREATE_FOLD_EXCEPTION ="创建文件夹异常";
    public final static String FILE_DELETE_FILE_EXCEPTION ="删除文件异常";
    public final static String FILE_DELETE_FILE_SUCCESS ="删除文件成功";
    public final static String FILE_DELETE_FOLD_EXCEPTION ="删除文件夹异常";
    public final static String FILE_DELETE_FOLD_SUCCESS="删除文件夹成功";
    public final static String FILE_CLOSE_FILEINPUTSTREAM_EXCEPTION="关闭文件输入流异常";
    public final static String FILE_CLOSE_FILEINPUTSTREAM_SUCCESS="关闭文件输入流成功";
    public final static String FILE_CLOSE_FILEOUTPUTSTREAM_EXCEPTION="关闭文件输出流异常";
    public final static String FILE_CLOSE_FILEOUTPUTSTREAM_SUCCESS="关闭文件输出流成功";
    public final static String FILE_READ_WRITE_AUTHORITY_UNABLED = "没有读写文件权限，请开通";


    //和WebSocket相关的
    public final static String WEBSOCKET_BASE_URL = "ws://" + TEST_IP;
    public final static String WEBSOCKET_USER_IDENTITY = "number";
    public final static String WEBSOCKET_URL = WEBSOCKET_BASE_URL + "/ws?token=";
    public final static String WEBSOCKET_MESSAGE_BUSSINESSTYPE = "code";
    public final static String WEBSOCKET_MESSAGE_CLIENTID = "account";
    public final static String WEBSOCKET_MESSAGE_UUID = "uniqueID";
    public final static String WEBSOCKET_MESSAGE_CLIENTTYPE = "clientType";
    public final static String WEBSOCKET_MESSAGE_COMMANDPRIORITY = "commandPriority";
    public final static String WEBSOCKET_MESSAGE_PERSONALITYTYPE = "personalityType";
    public final static String WEBSOCKET_MESSAGE_ERRORDESC = "message";
    public final static String WEBSOCKET_MESSAGE_DESCRIPTION = "description";
    public final static String WEBSOCKET_MESSAGE_ITEMS = "items";



    public final static String MESSAGE_ERRORCODE_2000 = "2000";
    public final static String MESSAGE_ERRORCODE_2001 = "2001";

    public final static String WEBSOCKET_TIMER_OPERATIONTYPE = "operationType";
    public final static String WEBSOCKET_TIMER_ATTENTIONTYPE = "attentionType";
    public final static String WEBSOCKET_TIMER_ATTENTIONCONTENT = "attentionContent";
    public final static String WEBSOCKET_TIMER_ACTIVATIONMODE = "activationMode";
    public final static String WEBSOCKET_TIMER_ACTIVATEDTIME = "activatedTime";
    public final static String WEBSOCKET_TIMER_ERRORCODE = "errorCode";
    public final static String WEBSOCKET_BUSINESS_DATA = "data";
    public final static String WEBSOCKET_MESSAGE_TIME = "time";


    public final static String WEBSOCKET_USER_NAME = "userName";
    public final static String WEBSOCKET_USER_SEX = "sex";
    public final static String WEBSOCKET_USER_BIRTHDAY = "birthday";
    public final static String WEBSOCKET_USER_ADDRESS = "address";
    public final static String WEBSOCKET_USER_WECHAT = "wechat";
    public final static String WEBSOCKET_USERDATA_TOKEN = "token";


    public final static String WEBSOCKET_REGISTERINFO_PROVINCE = "province";
    public final static String WEBSOCKET_REGISTERINFO_CITY = "city";
    public final static String WEBSOCKET_REGISTERINFO_HOSPITAL = "hospital";
    public final static String WEBSOCKET_REGISTERINFO_DEPARTMENT = "department";
    public final static String WEBSOCKET_REGISTERINFO_STATUS = "registerStatus";
    public final static String WEBSOCKET_REGISTERINFO_DESCRIPTION = "description";
    public final static String WEBSOCKET_REGISTERINFO_CREATETIME = "createTime";
    public final static String WEBSOCKET_REGISTERINFO_UPDATETIME = "updateTime";

    //测肤结果
    public final static String WEBSOCKET_SKINRESULT_FACE = "face";
    public final static String WEBSOCKET_SKINRESULT_SCORE = "score";
    public final static String WEBSOCKET_SKINRESULT_HEITOU = "heitou";
    public final static String WEBSOCKET_SKINRESULT_DOU = "dou";
    public final static String WEBSOCKET_SKINRESULT_BAN = "ban";
    public final static String WEBSOCKET_SKINRESULT_FUSE = "fuse";

    public final static String WEBSOCKET_SKINRESULT_BODY = "body";
    public final static String WEBSOCKET_SKINRESULT_DIET = "diet";
    public final static String WEBSOCKET_SKINRESULT_MEDICINE = "medicine";
    public final static String WEBSOCKET_SKINRESULT_DRUG = "drug";
    public final static String WEBSOCKET_SKINRESULT_RESULT = "result";

    //体检推荐的参数
    public final static String WEBSOCKET_EXAM_ID = "examID";//体检推荐的id
    public final static String WEBSOCKET_EXAM_NAME="name";//体检推荐的名字
    public final static String WEBSOCKET_EXAM_COVER = "cover";//体检的图片
    public final static String WEBSOCKET_EXAM_DESCRIPTION="description";//体检的描述
    public final static String WEBSOCKET_EXAM_UPDATETIME="updateTime";//体检的时间
    public final static String WEBSOCKET_EXAM_CONTEXT="examContext";//体检的详情
    public final static String WEBSOCKET_EXAM_MENU_LINK="link";//体检套餐的文件link



    //消息通知相关
    public final static String WEBSOCKET_NOTIFICTION_MESSAGETYPE="messageType";
    public final static String WEBSOCKET_NOTIFICTION_MESSAGEID="messageID";


    public final static String WEBSOCKET_MESSAGE_ID = "messageID";//新消息通知
    public final static String WEBSOCKET_RESULT_ID = "resultID";//健康周报结果查询
    public final static String WEBSOCKET_HEALTH_WEEKLY_REPORT_RESULT_CONTEXT="resultContext";//健康周报结果查询返回的json里保存doc的link的key。

    public final static String WEBSOCKET_MESSAGE_CLIENTTYPE_NUMBER = "2";
    public final static String WEBSOCKET_QUERYPERSONINFO_BUSSINESSTYPE_CODE = "0005";
    public final static String WEBSOCKET_PERSONAL_INFOMATION_BUSSINESSTYPE_CODE = "0006";
    public final static String WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE = "0009";   //类型查询 讲座音频摘要
    public final static String WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE = "0010";   //类型查询 讲座视频摘要
    public final static String WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE = "0011";   //类型查询 讲座视频和音频 详情
    public final static String WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE = "0012";   //类型查询 讲座文章摘要
    public final static String WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE = "0013";   //类型查询 讲座文章 详情
    public final static String WEBSOCKET_SKIN_RESULT_CODE = "0016";
    public final static String WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE = "0017";
    public final static String WEBSOCKET_VOICEREGISTER_BUSSINESSTYPE_CODE = "0019";//挂号医院信息
    public final static String WEBSOCKET_REGISTERHISTORY_BUSSINESSTYPE_CODE = "0020";//挂号历史纪录
    public final static String WEBSOCKET_REGISTERRESULT_BUSSINESSTYPE_CODE = "0021";//挂号结果
    public final static String WEBSOCKET_NEW_MESSAGE_BUSSINESSTYPE_CODE = "0018";//新消息通知
    public final static String WEBSOCKET_HANGUPCHAT_BUSSINESSTYPE_CODE = "0017"; //挂断在线问诊
    public final static String WEBSOCKET_THREE_EXAM_BUSSINESSTYPE_CODE = "0023"; //挂断在线问诊
    public final static String WEBSOCKET_MEDICAL_EXAM_DETAIL_BUSSINESSTYPE_CODE="0008";//体检推荐详细信息查询
    public final static String WEBSOCKET_MEDICAL_EXAM_SUMMARY_BUSSINESSTYPE_CODE="0007";//体检推荐摘要查询
    public final static String WEBSOCKET_MEDICAL_EXAM_MENU_CODE="0022";//体检套餐查询
    public final static String WEBSOCKET_HEALTH_WEEKLY_REPORT_CODE="0015";//健康周报，健康检查结果


    public final static String WEBSOCKET_USER_IDENTITY_EXCEPTION = "没有用户异常";
    public final static String WEBSOCKET_CONNECTION_EXCEPTION = "WebSocket连接异常";
    public final static String WEBSOCKET_CONNECTION_SUCCESS = "WebSocket连接成功";
    public final static String WEBSOCKET_CONNECTION_CLOSE = "关闭WebSocket连接";
    public final static String WEBSOCKET_CONNECTION_OPEN = "开启WebSocket连接";
    public final static String WEBSOCKET_URI_EXCEPTION = "WebSocket构造URI异常或者创建WebSocketClient异常";
    public final static String WEBSOCKET_MESSAGE_FROM_SERVER = "有来自服务器的消息";
    public final static String WEBSOCKET_BUSINESS_DOWNLOAD_PICVOICE = "正在加载图片和声音...";
    public final static String WEBSOCKET_BUSINESS_DOWNLOAD_INFO = "正在加载...";
    public final static String WEBSOCKET_SERVER_TIMER_WEBSOCKET = "开启一分钟的定时任务检测Websocket连接";

    //应用程序相关
    public final static String APPLICATION_AIWAC = "Aiwac";
    public final static String APPLICATION_ERROR= "应用程序权限不够或者安装错误，请赋予权限或重新安装";
    public final static String APLLICATION_CODE= "utf-8"; //统一的编码方式
    public final static String APLLICATION_CLEAN= "用户清理了应用";
    public final static int APLLICATION_THREADPOOL_SIZE = 20; // 线程池大小

    //Sqlite数据库相关的常量
    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "aiwac";
    public final static String DB_CREATE_FAILURE = "数据库创建失败";
    public final static String DB_CREATE_SUCCESS = "数据库创建成功";
    public final static String DB_CREATING = "正在创建数据库";

    public final static String DB_USER_TABLENAME = "user";
    public final static String DB_TIMER_TABLENAME = "timer";
    public final static String DB_EDUCATION_NAME= "education";
    public final static String DB_Notification="notification";//本地消息列表
    public final static String DB_CREATE_TABLE_FAILURE = "数据表创建失败";
    public final static String DB_CREATE_TABLE_SUCCESS = "数据表创建成功";
    public final static String DB_CREATE_TABLE_EXIST = "数据表已经存在，无需再次创建";
    public final static String DB_OPEN_READ_CONNECTION = "打开读数据库连接";
    public final static String DB_OPEN_WRITE_CONNECTION = "打开写数据库连接";
    public final static String DB_CLOSE_READ_CONNECTION = "关闭读数据库连接";
    public final static String DB_CLOSE_WRITE_CONNECTION = "关闭写数据库连接";
    public final static String DB_INSERT = "插入数据";
    public final static String DB_UPDATE = "更新数据";
    public final static String DB_DELETE = "删除数据";
    public final static String DB_UPDATE_EXCEPTIOIN = "更新数据异常";
    public final static String DB_INSERT_EXCEPTIOIN = "插入数据异常";
    public final static String DB_DELETE_EXCEPTIOIN = "删除数据异常";
    public final static String DB_UPDATE_COMMIT = "确定数据提交到后台";
    public final static String DB_UPDATE_COMMIT_EXCEPTION = "确定数据提交到后台异常";

    public final static String DB_QUERY_EXCEPTIOIN = "查询数据异常";
    public final static String DB_NUMBER_EXCEPTION = "手机号码已经被注册";

    //wifi相关常量
    public final static String AIWAC_CONFIG_ING = "正在配置AIWAC机器人";
    public final static String AIWAC_CONFIG_SUCCESS = "配置AIWAC机器人成功";
    public final static String AIWAC_CONFIG_EXCEPTION = "配置AIWAC机器人异常";
    public final static String AIWAC_CONFIG_FAILURE= "配置AIWAC机器人失败";
    public final static String AIWAC_CONFIG_PASSWORD = "请输入wifi密码";
    public final static String AIWAC_CONFIG_PASSWORD_ERROR = "wifi密码不得少于8位";
    public final static String AIWAC_CONFIG_SELECT = "请选择AIWAC机器人连接的Wifi";
    public final static String AIWAC_CONFIG_PASSWORD_FAILURE = "Wifi密码错误或无权访问Wifi，请重新配置";

    public final static String WIFI_CLOSE = "Wifi没有打开,请打开Wifi";
    public final static String WIFI_NO_CONNECTABLE = "附近没有可用Wifi，请到有wifi环境下重试";
    public final static String WIFI_CONNECTABLE = "有可用Wifi";
    public final static String WIFI_UNAVAILABILITY = "你手机的Wifi模块不可用，请检查是否打开Wifi模块或赋予权限！";
    public final static String WIFI_AVAILABILITY = "你手机的Wifi模块可用";
    public final static String WIFI_SELECT = "请选择连接的无线网";
    public final static String WIFI_USE_MOBILE = "使用移动流量";
    public final static String WIFI_USE_WIFI = "使用无线流量";
    public final static String WIFI_USE_NO = "无网络连接";
    public static final String WIFI_CONNECTED_OPERRATOR = "请联网后再操作";
    public final static String WIFI_GET_WIFIMANAGER_EXCEPTION = "获取WifiManager系统服务异常";
    public final static String WIFI_GET_CONNECTIVITYMANAGER_EXCEPTION = "获取ConnectivityManager系统服务异常";
    public final static String WIFI_STATE_DISABLED_DESC = "WLAN已经关闭";
    public final static String WIFI_STATE_DISABLING_DESC = "WLAN正在关闭";
    public final static String WIFI_STATE_ENABLED_DESC = "WLAN已经打开";
    public final static String WIFI_STATE_ENABLING_DESC = "WLAN正在打开";
    public final static String WIFI_STATE_UNKNOWN_DESC = "WLAN状态未知";
    public static final String WIFI_SIGNAL_VERYHIGH = "Wifi信号很强"; //信号很强
    public static final String WIFI_SIGNAL_HIGH =  "Wifi信号强"; //信号强
    public static final String WIFI_SIGNAL_NORMAL = "Wifi信号一般"; //信号一般
    public static final String WIFI_SIGNAL_LOW = "Wifi信号弱"; //信号较差
    public static final String WIFI_SIGNAL_NO = "无Wifi信号"; //无信号
    public static final int WIFI_TYPE_NO = 0;
    public static final int WIFI_TYPE_MOBILE = 1;
    public static final int WIFI_TYPE_WIFI = 2;
    public static final int WIFI_SIGNAL_LEVEL_VERYHIGH = 1; //信号很强
    public static final int WIFI_SIGNAL_LEVEL_HIGH = 2; //信号强
    public static final int WIFI_SIGNAL_LEVEL_NORMAL = 3; //信号一般
    public static final int WIFI_SIGNAL_LEVEL_LOW = 4; //信号较差
    public static final int WIFI_SIGNAL_LEVEL_NO = 5; //无信号


    //和activity相关常量
    public final static String ACTIVITY_SKIP= "页面跳转";
    public final static String ACTIVITY_ERROR = "Error";

    //编码相关的
    public final static String CODE_ENCODE_EXCEPTION = "编码异常";
    public final static String CODE_DECODE_EXCEPTION = "解码异常";

    //和service相关的常量
    public final static String SERVICE_CREATE = "服务创建";
    public final static String SERVICE_DESTROY= "服务销毁";
    public final static String SERVICE_BINDER= "绑定服务";
    public final static String SERVICE_TIMER_TYPE = "timer"; // 定时任务类型   1:表示检测websocket连接
    public final static int SERVICE_TIMER_TYPE_WEBSOCKET = 1;
    public final static String SERVICE_STOP_SELF= "服务自我停止";


    //和网络有关的
    public final static String HTTP_CLOSE_STREAM_EXCEPTION = "关闭网络流异常";
    public final static String HTTP_STREAM2STRING_EXCEPTION = "网络流转换层字符串异常";
    public final static String HTTP_METHOD_POST  = "POST"; //post请求方式
    public final static String HTTP_METHOD_POST_SUCCESS  = "POST方式请求成功";
    public final static String HTTP_METHOD_POST_FAILURE  = "POST方式请求失败";
    public final static String HTTP_METHOD_POST_EXCEPTION  = "POST方式请求异常";
    public final static String HTTP_METHOD_GET  = "GET"; //get请求方式
    public final static String HTTP_METHOD_GET_SUCCESS  = "GET方式请求成功";
    public final static String HTTP_METHOD_GET_FAILURE  = "GET方式请求失败";
    public final static String HTTP_METHOD_GET_EXCEPTION  = "GET方式请求异常";


    //和json解析相关的
    public final static String JSON_PARSE_SUCCESS = "Json解析成功";
    public final static String JSON_PARSE_EXCEPTION = "Json解析异常";
    public final static String JSON_GENERATE_EXCEPTION = "对象生成Json异常";
    public final static String JSON_EXCEPTION = "Json解析/生成异常";
    public final static String JSON_GENERATE_SUCCESS = "对象生成Json成功";
    public final static String JSON_OPT = "opt";  //json字符串操作指令
    public final static String JSON_RESULT= "result";  //json字符串的key
    public final static String JSON_RESULT_TRUE = "true";
    public final static String JSON_JSON = "json";  //json字符串的key
    public final static String JSON_OBJECT_USER_NAME = "user";  //json字符串操作对象user名


    //和默认设置相关的
    public final static String DEFAULT_POSITIVE_BUTTON = "确定";
    public final static String DEFAULT_NEGATIVE_BUTTON = "取消";

    //和用户有关
    public final static String USER_INPUT_NUMBER_ERROR = "手机号码无效，请输入正确的号码";
    public final static String USER_REGISTER = "该手机号码已被注册";
    public final static String USER_REGISTING = "用户正在注册";
    public final static String USER_CHECKCODE_MESSAGE_KEY= "checkcode";
    public final static int USER_REGISTER_CODE = 1;
    public final static int USER_LOGIN_CODE = 2;
    public final static int USER_GET_CHECKCODE = 0;
    public final static int USER_MODIFY_PASSWORD = 10;
    public final static int USER_MODIFY_PASSWORD_EXCEPTION = 101;
    public final static int USER_GET_CHECKCODE_EXCEPTION = 100;
    public final static String USER_GET_CHECKCODE_EXCEPTION_MESSAGE = "网络信号不好，请重新获取验证码";
    public final static int USER_POST_CHECKCODE_EXCEPTION = 101;
    public final static String USER_POST_CHECKCODE_EXCEPTION_MESSAGE = "网络信号不好，请重新注册/登录";
    public final static int USER_JSON_EXCEPTION = 102;
    public final static String USER_JSON_EXCEPTION_MESSAGE = "服务器繁忙，等下再试";
    public final static int USER_CHECKCODE_ERROR_EXCEPTION = 103;
    public final static String USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE = "验证码输入错误";
    public final static int USER_CHECKCODE_SUCCESS = 104;
    public final static String USER_CHECKCODE_SUCCESS_MESSAGE = "验证码输入正确";
    public final static String USER_UNKNOW_EXCEPTION = "未知错误";
    public final static String USER_PASSWORD_MODIFY_EXCEPTION_MESSAGE = "服务器繁忙，密码修改失败，请重试";
    public final static String USER_PASSWORD_MODIFY_SUCCESS_MESSAGE = "密码修改成功，请记住修改后的密码";
    public final static String USER_IS_LOGIN  = "判断用户是否登录";
    public final static int USER_CHECKCODE_COUNTDOWNINTERVAL = 1000; //没一秒更新一次
    public final static int USER_CHECKCODE_MILLISINFUTURE = 60 * USER_CHECKCODE_COUNTDOWNINTERVAL; //要倒计时的总时间


    public final static String USER_MODIFY_PASSWORD_OPT  = "modifyPasswd";
    public final static String USER_LOGIN_PASSWORD_OPT  = "loginWithPasswd";
    public final static String USER_GET_CHECKCODE_OPT  = "getIdentifyCode";
    public final static String USER_REGISTER_OPT = "identifyingCodeVerified";

    public final static String HTTP_BASE_URL = "http://" + TEST_IP;

    public final static String HTTP_CHECKCODE_URL = HTTP_BASE_URL + "/web/login/getIdentifyCode";
    public final static String HTTP_USER_REGISTER_BASEURL = HTTP_BASE_URL + "/web/register/registerWithPasswd";
    public final static String HTTP_USER_MODIFY_PASSWORD_BASEURL = HTTP_BASE_URL + "/web/register/modifyPasswd";
    public final static String HTTP_USER_LOGIN_PASSWORD_BASEURL = HTTP_BASE_URL + "/web/login/" + USER_LOGIN_PASSWORD_OPT;
    public final static String HTTP_USER_LOGIN_IDENTIFYCODE_BASEURL = HTTP_BASE_URL + "/web/login/loginWithIdentifyCode";

    public final static String USER_REGISTER_NUMBER ="account";
    public final static String USER_REGISTER_CHECKCODE ="identifyCode";
    public final static String USER_REGISTER_PHONENUMBER_EXIST ="isPhoneNumberExist";
    public final static String USER_REGISTER_PHONENUMBER_EXIST_YES ="1";
    public final static String USER_REGISTER_ISSUCCESS = "result";
    public final static String USER_REGISTER_SUCCESS = "1";
    public final static String USER_DATA_FIELD_REGISTER = "register";
    public final static String USER_DATA_FIELD_PASSWORD = "password";
    public final static String USER_DATA_FIELD_NUMBER = "number";
    public final static String USER_DATA_FIELD_TOKEN = "token";
    public final static String USER_DATA_FIELD_TOKENTIME = "time";//获取token的时间
    public final static String USER_DATA_PERSISTENCE = "利用SharedPreferences持久化用户数据";
    public final static String USER_DATA_PERSISTENCE_CLEAN = "清空SharedPreferences用户数据";



}
