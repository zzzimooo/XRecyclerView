package cu.db.Migration;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by User on 2015/9/30.
 */
public abstract class AbstractMigratorHelper {

    public abstract void onUpgrade(SQLiteDatabase db);

}
