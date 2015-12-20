package cu.db;

import android.content.Context;

import cu.db.cache.CacheDB;
import cu.db.config.ConfigUtil;


/**
 * Created by qianjin on 2015/9/29.
 */
public class DaoUtil {


    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static final String DB_NAME = "m_android_db";

    /**
     * 使用Db前需init
     * @param context
     */
    public static void init(Context context) {
        daoSession = getDaoSession(context);
        ConfigUtil.init(context);
        CacheDB.init(context);
    }

    private static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
