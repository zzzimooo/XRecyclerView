package cu;

import android.app.Activity;
import android.os.Handler;

import com.example.xrecyclerview.BuildConfig;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import cu.db.config.ConfigUtil;

/**
 * Created by wang on 15/12/20.
 */
public class httphelper {

    private final String TAG = "httphelper";

    private static final String contentType = "application/json;charset=UTF-8";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String DEV_HOST = "api-test.365hr.com";
    public static String RELEASE_HOST = "api.365hr.com";

    //public static String BASE_URL_DEV = "http://m.hrloo.com/hrloo.php?";
    public static String BASE_URL_DEV = "http://eebochina.oicp.net:18080/hrloo.php?";
    public static String BASE_URL_RELEASE = "http://m.hrloo.com/hrloo.php?";
    public static String BASE_URL = "http://m.hrloo.com/hrloo.php?";

    private static httphelper httphelperInstance;
    private OkHttpClient mOkHttpClient;
    private Handler hd;
    private Activity activity;
    private DeviceUtil mDeviceUtil;

    private httphelper() {}

    private httphelper(Activity activity) {
        this.activity = activity;
        this.mDeviceUtil = new DeviceUtil(activity);
        getOkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        hd = new Handler(activity.getMainLooper());
        if (BuildConfig.DEBUG) {
            BASE_URL = BASE_URL_DEV;
        } else {
            BASE_URL = BASE_URL_RELEASE;
        }
    }

    public static httphelper getInstance(Activity activity)
    {
        if (httphelperInstance == null)
        {
            synchronized (httphelper.class)
            {
                if (httphelperInstance == null)
                {
                    httphelperInstance = new httphelper(activity);
                }
            }
        }
        return httphelperInstance;
    }

    private void getOkHttpClient(){
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
    }

    private Request.Builder getDefRequestBuilder() {
        return new Request.Builder()
                .addHeader("appkey ", ConfigUtil.getConfigValue(Preferences.KEYS))
                .addHeader("Content-Type", "application/json") // application/json         contentType
                .addHeader("apptype", "hrloo_app20")
                .addHeader("versionname", SupportUtility.getAppVersionName(activity))
                .addHeader("versioncode", SupportUtility.getAppVersionCode(activity) + "");
    }

    private String getUrlEnd() {
        // token,devid这二个参数以GET方式放在每次请求的URL后面，登录成功后服务器返回token,未登录时token可为空值,devid设备ID要求唯一由APP产生
        return "&m=mapi2&token=" + encode(ConfigUtil.getConfigValue(Preferences.TOKEN)) + "&devid=" + mDeviceUtil.getAndroidId();
    }

    public String requestUrl(String module, String method) {
        return BASE_URL + "c=" + module + "&a=" + method + getUrlEnd();
    }

    public void get(String url, Callback response) {
        mOkHttpClient.newCall(getDefRequestBuilder()
                .url(url)
                .build())
                .enqueue(response);
    }


    private void testCode() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBuilder() // 多模块
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("", ""),
                        RequestBody.create(JSON, "")
                )
                .addFormDataPart("k", "v")
                .build();
        RequestBody requestBodyJson = RequestBody.create(JSON, "json..."); // json串
        RequestBody requestBodyKV = new FormEncodingBuilder() // 键值对
                .add("k1", "v1")
                .addEncoded("k2", "v2")
                .build();
        final Request request = new Request.Builder()
                .url("")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", contentType)
                .cacheControl(CacheControl.FORCE_CACHE)
                .put(requestBody)
                .post(requestBodyJson)
                .post(requestBodyKV)
                .get()
                .tag("1") // OkHttpClient.cancel(tag)来取消所有带有这个tag的call
                .build();
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File("filename"), cacheSize);

        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setCache(cache);
        okHttpClient.setProxy(new Proxy(Proxy.Type.SOCKS, new SocketAddress() {
            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        }));
        okHttpClient.setRetryOnConnectionFailure(true);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() { // 异步
            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    InputStream inputStream = response.body().byteStream();
                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                }
            }
        });
        Response response = call.execute(); // 阻塞式
        if(response.isSuccessful()) {}
        okHttpClient.cancel("1");
    }



    /**
     * 参数编码
     *
     * @return
     */
    public static String encode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~").replace("#", "%23");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 参数反编码
     *
     * @param s
     * @return
     */
    public static String decode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLDecoder.decode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
