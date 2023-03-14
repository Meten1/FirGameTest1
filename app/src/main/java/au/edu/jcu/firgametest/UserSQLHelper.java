package au.edu.jcu.firgametest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSQLHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;

    public UserSQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库表
        String createTableSql = "CREATE TABLE IF NOT EXISTS user " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(10), " +
                "password VARCHAR(15)," +
                "score INT" +
                ")";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库表
        String dropTableSql = "DROP TABLE IF EXISTS user";
        db.execSQL(dropTableSql);
        onCreate(db);
    }
}
