package com.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.meidebi.app.base.config.AppConfigs;
import com.meidebi.app.support.utils.AppLogger;
import com.orm.util.SugarCursorFactory;

import static com.orm.util.SugarConfig.getDatabaseVersion;
import static com.orm.util.SugarConfig.getDebugEnabled;

public class SugarDb extends SQLiteOpenHelper {
     public final static int DB_VERSION = 3;

    private final SchemaGenerator schemaGenerator;
    private SQLiteDatabase sqLiteDatabase;
    
    public SugarDb(Context context) {
        super(context,AppConfigs.DB_NAME,
                new SugarCursorFactory(getDebugEnabled(context)), getDatabaseVersion(context));
        schemaGenerator = new SchemaGenerator(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        schemaGenerator.createDatabase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    	AppLogger.e("oldVersion"+oldVersion+"newVersion"+newVersion);
        sqLiteDatabase.execSQL("drop table if exists AccountBean");
        sqLiteDatabase.execSQL("drop table if exists MsgDetailBean");
        sqLiteDatabase.execSQL("drop table if exists OrderShowBean");
        sqLiteDatabase.execSQL("drop table if exists UserinfoBean");
        sqLiteDatabase.execSQL("drop table if exists draft");

        schemaGenerator.doUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    public synchronized SQLiteDatabase getDB() {
        if (this.sqLiteDatabase == null) {
            this.sqLiteDatabase = getWritableDatabase();
        }

        return this.sqLiteDatabase;
    }

}
