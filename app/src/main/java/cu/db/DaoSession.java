package cu.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import cu.db.cache.Cache;
import cu.db.cache.CacheDao;
import cu.db.config.Config;
import cu.db.config.ConfigDao;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig configDaoConfig;
    private final DaoConfig cacheDaoConfig;

    private final ConfigDao configDao;
    private final CacheDao cacheDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        configDaoConfig = daoConfigMap.get(ConfigDao.class).clone();
        configDaoConfig.initIdentityScope(type);

        cacheDaoConfig = daoConfigMap.get(CacheDao.class).clone();
        cacheDaoConfig.initIdentityScope(type);

        configDao = new ConfigDao(configDaoConfig, this);
        cacheDao = new CacheDao(cacheDaoConfig, this);

        registerDao(Config.class, configDao);
        registerDao(Cache.class, cacheDao);
    }
    
    public void clear() {
        configDaoConfig.getIdentityScope().clear();
        cacheDaoConfig.getIdentityScope().clear();
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public CacheDao getCacheDao() {
        return cacheDao;
    }

}