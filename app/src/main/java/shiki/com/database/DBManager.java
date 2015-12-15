package shiki.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import shiki.com.bean.Weather;
import shiki.com.bean.WeatherJson;

/**
 * Created by wnh50 on 2015/12/10.
 */
public class DBManager {
    //数据库名
    public static final String DB_NAME = "Weather.db";

    //数据库版本
    public static final int VERSION = 1;
    private static DBManager dbManager;
    private SQLiteDatabase db;

    //构造方法私有化
    private DBManager(Context context) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取DBManager实例
    public synchronized static DBManager getInstance(Context context) {
        if (null == dbManager) {
            dbManager = new DBManager(context);

        }
        return dbManager;
    }

    //将获取的weather保存到数据库
    public void saveCurrent(Weather weather) {
        if (isCityExist(weather.getCity_name())) {
            ContentValues values = new ContentValues();
            values.put("info", weather.getInfo());
            values.put("date", weather.getDate());
            values.put("time", weather.getTime());
            values.put("moon", weather.getMoon());
            values.put("temperature", weather.getTemperature());
            values.put("humidity", weather.getHumidity());
            values.put("img",weather.getImg());
            values.put("direct", weather.getDirect());
            values.put("power", weather.getPower());
            db.update("cWeather",values,"city_name=?",new String[]{weather.getCity_name()});
            Log.d("TAG", "updateCurrent: 4" + weather.getCity_name());

        } else {
            Log.d("TAG", "insertCurrent: 5");
            ContentValues values = new ContentValues();
            Log.d("TAG", "insertCurrent: 6");
            values.put("city_code", weather.getCity_code());
            values.put("city_name", weather.getCity_name());
            values.put("info", weather.getInfo());
            values.put("date", weather.getDate());
            values.put("time", weather.getTime());
            values.put("moon", weather.getMoon());
            values.put("temperature", weather.getTemperature());
            values.put("humidity", weather.getHumidity());
            values.put("img",weather.getImg());
            values.put("direct", weather.getDirect());
            values.put("power", weather.getPower());
            values.put("main",0);
            db.insert("cWeather", null, values);
        }
    }

    //从数据库读取CurrentWeather
    public Weather loadWeather(String cityName) {
        Weather currentWeather = new Weather();
        Log.d("TAG", "loadCurrent: 7");
        String sql = "select * from cWeather where city_name="+"'"+cityName+"'"+";";
        Log.d("TAG", "loadcurrent:8" );
        Cursor cursor = db.rawQuery(sql, null);
        Log.d("TAG", "loadWeather: " + "cursor");
        if (cursor.moveToFirst()) {
            Log.d("TAG", "loadWeather: move to first");
            do {
                currentWeather.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
                Log.d("TAG", "loadcurrent  9" + cursor.getString(cursor.getColumnIndex("city_name")));
                currentWeather.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                currentWeather.setInfo(cursor.getString(cursor.getColumnIndex("info")));
                currentWeather.setDate(cursor.getString(cursor.getColumnIndex("date")));
                currentWeather.setTime(cursor.getString(cursor.getColumnIndex("time")));
                currentWeather.setMoon(cursor.getString(cursor.getColumnIndex("moon")));
                currentWeather.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
                currentWeather.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
                currentWeather.setImg(cursor.getString(cursor.getColumnIndex("img")));
                currentWeather.setDirect(cursor.getString(cursor.getColumnIndex("direct")));
                currentWeather.setPower(cursor.getString(cursor.getColumnIndex("power")));
                currentWeather.setMain(cursor.getInt(cursor.getColumnIndex("main")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("TAG", "loadWeather: cursor.close");
        return currentWeather;

    }

    //返回main为1的city_name
    public String isMainExist() {
        String sql = "select * from cWeather where main=1;";
        String city_name = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("TAG", "isMainExist:  main checked" + city_name);
        return city_name;
    }

    //将传入的city_name设为main
    public void setMain(String old_city_name, String new_city_name) {
        ContentValues values = new ContentValues();
        values.put("main", 1);
        db.update("cWeather", values, "city_name=?", new String[]{new_city_name});
        if (!old_city_name.equals("null")) {
            values.put("main", 0);
            db.update("cWeather", values, "city_name=?", new String[]{old_city_name});
        }
        Log.i("TAG", "setMain: main seted" + old_city_name + new_city_name);
    }

    //查询city是否已存在
    public boolean isCityExist(String city_name) {
        String sql = "select * from cWeather where city_name=" + "'" + city_name + "'" + ";";
        String cityName=null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                cityName = cursor.getString(cursor.getColumnIndex("city_name"));

                Log.i("TAG", "isCityExist: " + city_name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return city_name.equals(cityName);
    }

    //查询city是否为main
    public boolean isMain(String cityName){
        String sql="select main from cWeather where city_name=" + "'" + cityName + "'" + ";";
        Cursor cursor=db.rawQuery(sql,null);
        int main=0;
        if (cursor.moveToFirst()){
            do{
                main=cursor.getInt(cursor.getColumnIndex("main"));

            }while (cursor.moveToNext());
        }
        return main==1;
    }

}
