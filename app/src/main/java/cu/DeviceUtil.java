package cu;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 设备信息
 */
public class DeviceUtil {
  private String manufacturer;
  private String model;
  private String releaseVersion;
  private String sdkVersion;

  private int displayWidth;
  private int displayHeight;

  private String deviceId;
  private String networkOperatorName;
  private String networkOperator;
  private String networkCountryISO;
  private String simOperator;
  private String simCountryISO;
  private String simSerialNumber;
  private String subscriberId;
  // private String phoneNumber;
  private String androidId;
  private int dpi;

  private String localMacAddress;

  public DeviceUtil(Context context) {
    getSysinfo(context);
  }

  private void getSysinfo(Context context) {
    this.manufacturer = Build.MANUFACTURER;
    this.model = Build.MODEL;
    this.releaseVersion = Build.VERSION.RELEASE;
    this.sdkVersion = Build.VERSION.SDK;
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display d = wm.getDefaultDisplay();
    this.displayWidth = d.getWidth();
    this.displayHeight = d.getHeight();
    this.dpi = context.getResources().getDisplayMetrics().densityDpi;

    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    this.deviceId = tm.getDeviceId();
    this.networkOperatorName = tm.getNetworkOperatorName();
    this.networkOperator = tm.getNetworkOperator();
    this.networkCountryISO = tm.getNetworkCountryIso();
    this.simOperator = tm.getSimOperator();
    this.simCountryISO = tm.getSimCountryIso();
    this.simSerialNumber = tm.getSimSerialNumber();
    this.subscriberId = tm.getSubscriberId();
    // this.phoneNumber = tm.getLine1Number();

    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo info = wifi.getConnectionInfo();
    this.localMacAddress = info.getMacAddress();

    this.androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
  }

  /**
   * @return 厂商
   */
  public String getManufacturer() {
    return manufacturer;
  }

  /**
   * @return 产品型号
   */
  public String getModel() {
    return model;
  }

  /**
   * @return 系统版本
   */
  public String getReleaseVersion() {
    return releaseVersion;
  }

  /**
   * @return SDK版本
   */
  public String getSdkVersion() {
    return sdkVersion;
  }

  /**
   * @return 屏幕宽
   */
  public int getDisplayWidth() {
    return displayWidth;
  }

  /**
   * @return 屏幕高
   */
  public int getDisplayHeight() {
    return displayHeight;
  }

  /**
   * @return IMEI
   */
  public String getDeviceId() {
    return deviceId;
  }

  /**
   * @return 网络服务提供名称
   */
  public String getNetworkOperatorName() {
    return networkOperatorName;
  }

  /**
   * @return 网络服务提供
   */
  public String getNetworkOperator() {
    return networkOperator;
  }

  /**
   * @return 网络服务提供商国家编码
   */
  public String getNetworkCountryISO() {
    return networkCountryISO;
  }

  /**
   * @return SIM服务提供商 中国移动：46000&46002 中国联通：46001 中国电信：46003
   */
  public String getSimOperator() {
    if (TextUtils.isEmpty(simOperator)) {
      return "46000";
    }
    return simOperator;
  }

  /**
   * @return SIM国家编码
   */
  public String getSimCountryISO() {
    return simCountryISO;
  }

  /**
   * @return SIM 序列号
   */
  public String getSimSerialNumber() {
    if (TextUtils.isEmpty(simSerialNumber)) {
      return "000000";
    }
    return simSerialNumber;
  }

  /**
   * @return IMSI
   */
  public String getSubscriberId() {
    if (TextUtils.isEmpty(subscriberId)) {
      return "9";
    }
    return "9" + subscriberId;
  }

  /**
   * @return 手机号码
   */
  /*
   * public String getPhoneNumber() { return phoneNumber; }
   */

  /**
   * 获取wifi mac地址
   * 
   * @return wifi mac地址(xx:xx:xx:xx:xx:xx)
   */
  public String getLocalMacAddress() {
    // if(TextUtils.isEmpty(localMacAddress)){
    // return localMacAddress;//mac[new Random().nextInt(10)];
    // }
    // .replace(":", "")
    return localMacAddress;
  }

  public String buildUUID() {
    //TODO 最后添加了时间
    String pid =
        "86" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
            + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
            + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
            + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
            + Build.USER.length() % 10 + System.currentTimeMillis();
    String lid = getDeviceId() + pid + getAndroidId() + getLocalMacAddress();

    MessageDigest m = null;
    try {
      m = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    m.update(lid.getBytes(), 0, lid.length());
    byte p_md5Data[] = m.digest();

    String uniqueID = new String();
    for (int i = 0; i < p_md5Data.length; i++) {
      int b = (0xFF & p_md5Data[i]);
      // if it is a single digit, make sure it have 0 in front (proper padding)
      if (b <= 0xF) uniqueID += "0";
      // add number to string
      uniqueID += Integer.toHexString(b);
    }
    uniqueID = uniqueID.toUpperCase();
    if (TextUtils.isEmpty(uniqueID) || "null".equals(uniqueID)) return buildUUID2();
    return uniqueID;
  }

  private String buildUUID2() {
    String pid = "86";

    if (Build.BOARD != null) {
      pid = pid + Build.BOARD.length() % 10;
    }
    if (Build.BRAND != null) {
      pid = pid + Build.BRAND.length() % 10;
    }
    if (Build.CPU_ABI != null) {
      pid = pid + Build.CPU_ABI.length() % 10;
    }
    if (Build.DEVICE != null) {
      pid = pid + Build.DEVICE.length() % 10;
    }
    if (Build.DISPLAY != null) {
      pid = pid + Build.DISPLAY.length() % 10;
    }
    if (Build.HOST != null) {
      pid = pid + Build.HOST.length() % 10;
    }
    if (Build.ID != null) {
      pid = pid + Build.ID.length() % 10;
    }
    if (Build.MANUFACTURER != null) {
      pid = pid + Build.MANUFACTURER.length() % 10;
    }
    if (Build.MODEL != null) {
      pid = pid + Build.MODEL.length() % 10;
    }
    if (Build.PRODUCT != null) {
      pid = pid + Build.PRODUCT.length() % 10;
    }
    if (Build.TAGS != null) {
      pid = pid + Build.TAGS.length() % 10;
    }
    if (Build.TYPE != null) {
      pid = pid + Build.TYPE.length() % 10;
    }
    if (Build.USER != null) {
      pid = pid + Build.USER.length() % 10;
    }

    String lid = getDeviceId() + pid + getAndroidId() + getLocalMacAddress();

    MessageDigest m = null;

    try {
      m = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    m.update(lid.getBytes(), 0, lid.length());
    byte p_md5Data[] = m.digest();

    String uniqueID = new String();
    for (int i = 0; i < p_md5Data.length; i++) {
      int b = (0xFF & p_md5Data[i]);
      // if it is a single digit, make sure it have 0 in front (proper padding)
      if (b <= 0xF) uniqueID += "0";
      // add number to string
      uniqueID += Integer.toHexString(b);
    }

    uniqueID = uniqueID.toUpperCase();

    return uniqueID;
  }

  public String getAndroidId() {
    return androidId;
  }

  public void setAndroidId(String androidId) {
    this.androidId = androidId;
  }

  @Override
  public String toString() {
    return "DeviceId: " + getDeviceId() + " || DisplayHeight: " + getDisplayHeight()
        + " || DisplayWidth: " + getDisplayWidth() + " || Manufacturer: " + getManufacturer()
        + " || Model: " + getModel() + " || NetworkCountryISO: " + getNetworkCountryISO()
        + " || NetworkOperator: "
        + getNetworkOperator()
        + " || NetworkOperatorName: "
        + getNetworkOperatorName()
        // +" || PhoneNumber: "+getPhoneNumber()
        + " || ReleaseVersion: " + getReleaseVersion() + " || SdkVersion: " + getSdkVersion()
        + " || SimCountryISO: " + getSimCountryISO() + " || SimOperator: " + getSimOperator()
        + " || SimSerialNumber: " + getSimSerialNumber() + " || SubscriberId: " + getSubscriberId()
        + " || LocalMacAddress: " + getLocalMacAddress();
  }

  public int getDpi() {
    return dpi;
  }

  public void setDpi(int dpi) {
    this.dpi = dpi;
  }


}
