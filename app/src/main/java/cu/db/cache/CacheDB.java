package cu.db.cache;

import android.content.Context;
import android.text.TextUtils;

import cu.db.DaoUtil;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by qianjin on 2015/9/29.
 */
public class CacheDB {

    private static CacheDao cacheDao;

    public static final long CACHE_DAY = 3600 * 1000 * 24;
    public static final long CACHE_HOUR = 3600 * 1000;
    public static final long CACHE_MIN = 60 * 1000;


    public static void init(Context context) {
        cacheDao = DaoUtil.getDaoSession(context).getCacheDao();
    }






    public static void save(Cache cache) {
        Cache savedCache = getCache(cache.getKey());
        if (savedCache == null) {
            cacheDao.insertOrReplace(cache);
        } else {
            savedCache.setValue(cache.getValue());
            savedCache.setCurrentTime(System.currentTimeMillis());
            savedCache.setTimeout(cache.getTimeout());
            cacheDao.insertOrReplace(savedCache);
        }
    }

    public static Cache getCache(String key) {
        QueryBuilder qb = cacheDao.queryBuilder();
        qb.where(CacheDao.Properties.Key.eq(key));
        Cache cache = (Cache) qb.unique();
        if (cache == null) {
            cache = new Cache(key, "");
        } else {
            //timeout
            if (cache.isTimeout()) {
                delete(key);
                cache = new Cache(key, "");
            }
        }
        return cache;
    }

    public static String getCacheValue(String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }
        QueryBuilder qb = cacheDao.queryBuilder();
        qb.where(CacheDao.Properties.Key.eq(key));
        Cache cache = (Cache) qb.unique();
        if (cache == null) {
            return "";
        } else {
            if (cache.isTimeout()) {
                delete(key);
                return "";
            }
        }
        return cache.getValue();
    }


    public static void deleteAll() {
        cacheDao.deleteAll();
    }

    public static void delete(String key) {
        QueryBuilder qb = cacheDao.queryBuilder();
        DeleteQuery bd = qb.where(CacheDao.Properties.Key.eq(key)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
        //or
        //Config config = getConfig(key);
        //configDao.delete(config);
    }

    public static boolean exist(String key) {
        QueryBuilder qb = cacheDao.queryBuilder();
        qb.where(CacheDao.Properties.Key.eq(key));
        long cnt = qb.count();
        return cnt > 0 ? true : false;
    }

    private static long currentTime() {
        return System.currentTimeMillis();
    }


}
