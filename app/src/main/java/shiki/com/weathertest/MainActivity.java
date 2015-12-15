package shiki.com.weathertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import shiki.com.activity.WeatherInfo;
import shiki.com.database.DBManager;
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (Exception e){e.printStackTrace();}
                Intent intent=new Intent(MainActivity.this,WeatherInfo.class);
                startActivity(intent);
            }
        }).start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "onPause: onpause");
        finish();
    }
}
