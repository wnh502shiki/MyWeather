package shiki.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wnh50 on 2015/12/10.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_WEATHER = "create table cWeather ("
            + "id integer primary key autoincrement, "
            + "city_code text, "
            + "city_name text, "
            + "date text, "
            + "time text, "
            + "moon text, "
            + "temperature text, "
            + "humidity text, "
            + "info text, "
            + "img text, "
            + "direct text, "
            + "power text, "
            + "main int)";
    public Context mContext;

    public MyDatabaseHelper(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER);
        Log.i("TAG", "onCreate: database created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
