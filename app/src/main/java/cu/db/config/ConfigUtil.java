package cu.db.config;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import cu.db.DaoUtil;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by qianjin on 2015/9/7.
 */
public class ConfigUtil {

    private static ConfigDao configDao;

    /**
     * 使用ConfigUtil前需init
     *
     * @param context
     */
    public static void init(Context context) {
        configDao = DaoUtil.getDaoSession(context).getConfigDao();
    }


    public static void save(Config config) {
        Config savedConfig = getConfig(config.getKey());
        if (savedConfig == null) { // 不会出现为空的情况
            configDao.insertOrReplace(config);
        } else {
            savedConfig.setValue(config.getValue());
            configDao.insertOrReplace(savedConfig);
        }
    }

    public static void save(String key, String value) {
        Config config = new Config(key, value);
        save(config);
    }

    public static void save(String key, int value) {
        save(key, String.valueOf(value));
    }

    public static void save(String key, boolean value) {
        save(key, String.valueOf(value));
    }

    public static Config getConfig(String key) {
        QueryBuilder qb = configDao.queryBuilder();
        qb.where(ConfigDao.Properties.Key.eq(key));
        Config config = (Config) qb.unique();
        if (config == null) {
            config = new Config(key, "no data");
        }
        return config;
    }

    public static String getConfigValue(String key) {
        QueryBuilder qb = configDao.queryBuilder();
        qb.where(ConfigDao.Properties.Key.eq(key));
        Config config = (Config) qb.unique();
        if (config == null) {
            return "";
        }
        return config.getValue();
    }


    public static int getIntConfigValue(String key) {
        String value = getConfigValue(key);
        if (!TextUtils.isEmpty(value)) {
            return Integer.valueOf(value);
        }
        return 0;
    }

    public static boolean getBooleanConfigValue(String key) {
        String value = getConfigValue(key);
        if (!TextUtils.isEmpty(value)) {
            return Boolean.valueOf(value);
        }
        return false;
    }

    public static void deleteAll() {
        configDao.deleteAll();
    }

    public static void delete(String key) {
        QueryBuilder qb = configDao.queryBuilder();
        DeleteQuery bd = qb.where(ConfigDao.Properties.Key.eq(key)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
        //or
        //Config config = getConfig(key);
        //configDao.delete(config);
    }

    public static boolean exist(String key) {
        QueryBuilder qb = configDao.queryBuilder();
        qb.where(ConfigDao.Properties.Key.eq(key));
        long cnt = qb.count();
        return cnt > 0 ? true : false;
    }

    public static void savaObjConfigData(String key, Object obj) {
        ConfigUtil.save(key, JSON.toJSONString(obj));
    }

    public static @Nullable
    <T> T getObjConfigData(String key, Class<T> clazz) {
        String value = ConfigUtil.getConfigValue(key);
        if (!TextUtils.isEmpty(value)) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }


}
