package cu.db.Migration;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by User on 2015/9/30.
 */
public class DBMigrationHelper1 extends AbstractMigratorHelper {

    @Override
    public void onUpgrade(SQLiteDatabase db) {

    /* Create a temporal table where you will copy all the data from the previous table that you need to modify with a non supported sqlite operation */
        db.execSQL("CREATE TABLE \"CONFIG2\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"KEY\" TEXT NOT NULL ," + // 1: key
                "\"VALUE\" TEXT NOT NULL );"); // 2: value

    /* Copy the data from one table to the new one */
        db.execSQL("INSERT INTO CONFIG2 (_id, KEY, VALUE)" +
                "   SELECT _id, KEY, VALUE FROM CONFIG;");

    /* Delete the previous table */
        db.execSQL("DROP TABLE CONFIG");
    /* Rename the just created table to the one that I have just deleted */
        db.execSQL("ALTER TABLE CONFIG2 RENAME TO CONFIG");

    /* Add Index/es if you want them */
//        db.execSQL("CREATE INDEX " + "IDX_post_USER_ID ON CONFIG" +
//                " (KEY);");
        //Example sql statement
        db.execSQL("ALTER TABLE CONFIG ADD COLUMN COLUMNTEST TEXT");
    }
}
