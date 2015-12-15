package shiki.com.tool;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import shiki.com.bean.Weather;
import shiki.com.bean.WeatherJson;
import shiki.com.database.DBManager;


/**
 * Created by wnh50 on 2015/12/10.
 */
public class Util {

    public static void sendHttpRequest(final String cityName, final Context context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    final String url1 = "http://op.juhe.cn/onebox/weather/query?cityname=";
                    final String url2 = "&key=9ea76c9822ae145a4383673886b9ccee";
                    URL url = new URL(url1 + cityName + url2);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("tag", "run: "+response);

                    Gson gson = new Gson();
                    WeatherJson weather = gson.fromJson(response.toString(), WeatherJson.class);
                    final Weather weather1 = weatherConvert(weather);
                    final DBManager dbManager = DBManager.getInstance(context);
                    dbManager.saveCurrent(weather1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }


        }).start();
    }


    private static Weather weatherConvert(WeatherJson weatherJson) {
        Weather weather = new Weather();
        weather.setCity_name(weatherJson.getResult().getData().getRealTime().getCity_name());
        weather.setCity_code(weatherJson.getResult().getData().getRealTime().getCity_code());
        weather.setDate(weatherJson.getResult().getData().getRealTime().getDate());
        weather.setInfo(weatherJson.getResult().getData().getRealTime().getWeather().getInfo());
        weather.setMoon(weatherJson.getResult().getData().getRealTime().getMoon());
        weather.setTime(weatherJson.getResult().getData().getRealTime().getUpdateTime());
        weather.setTemperature(weatherJson.getResult().getData().getRealTime().getWeather().getTemperature());
        weather.setHumidity(weatherJson.getResult().getData().getRealTime().getWeather().getHumidity());
        weather.setImg(weatherJson.getResult().getData().getRealTime().getWeather().getImg());
        weather.setDirect(weatherJson.getResult().getData().getRealTime().getWind().getDirect());
        weather.setPower(weatherJson.getResult().getData().getRealTime().getWind().getPower());
        return weather;
    }
}
