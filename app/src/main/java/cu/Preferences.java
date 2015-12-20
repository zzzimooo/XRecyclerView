package cu;

/**
 * Created by Wang on 2015/10/28.
 */
public class Preferences {

    // DEBUG调试模式
//    public static boolean DEBUG = true;

    public static final String UUID = "uuid";

    public static final String KEYS = "keys"; // 设备注册后的标识
    //public static final String APPKEY = "appkey"; // header中固定参数，由服务端产生
    public static final String TOKEN = "token"; // 登录成功后服务器返回token,每次请求放在URL的后面

    public static final String PUNCH_REMIND_TIME = "punc_remind_time"; // 每日打卡提醒

    // 首次使用
    public static final String NOT_FIRST_USE_APP = "not_first_use_app";

    // setActiveDeviceFlag这个接口，是一次性调用的，这个接口是当用户正式登录进去应用程序，第一次正式使用的时候调用。
    public static final String SET_ACTIVE_DEVICE_FLAG = "set_active_device_flag";


}
