package shiki.com.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import shiki.com.bean.Weather;
import shiki.com.database.DBManager;
import shiki.com.tool.Util;
import shiki.com.weathertest.R;

import static shiki.com.database.DBManager.getInstance;

public class WeatherInfo extends AppCompatActivity {
    ArrayList<String> cityQueue = new ArrayList<String>();
    private int current = 0;
    private int cityCount = 0;
    private int currentPage = 1;
    private String mainCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        current = pref.getInt("current", 0);
        cityCount = pref.getInt("cityCount", 0);
        if (cityCount == 0) {
            Intent intent = new Intent(WeatherInfo.this, InputActivity.class);
            startActivityForResult(intent, 2);
        } else {
            mainCity = pref.getString("main", null);
            for (int i = 0; i < cityCount; i++)
                cityQueue.add(i, pref.getString("city" + i, null));

            Log.d("tag", "onCreate: " + mainCity);


            Util.sendHttpRequest(cityQueue.get(current), getApplicationContext());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(600);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    WeatherInfo.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            upDateUi(cityQueue.get(current));
                        }
                    });
                }
            }).start();
        }
        Button left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (--current > -1) {
                    upDateUi(cityQueue.get(current));
                } else {
                    Toast.makeText(WeatherInfo.this, "已经是第一个", Toast.LENGTH_SHORT).show();
                    current++;
                }

            }
        });
        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (++current < cityCount) {
                    upDateUi(cityQueue.get(current));
                } else {
                    Toast.makeText(WeatherInfo.this, "已经是最后一个", Toast.LENGTH_SHORT).show();
                    current--;
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        DBManager dbManager = DBManager.getInstance(getApplicationContext());

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.add: {
                Intent intent = new Intent(WeatherInfo.this, InputActivity.class);
                startActivityForResult(intent, 1);
            }
            break;
            case R.id.dec: {

                if (current != 0) {

                    if (dbManager.loadWeather(cityQueue.get(current)).getMain() == 1)
                        dbManager.setMain(cityQueue.get(current), cityQueue.get(current-1));
                    cityQueue.remove(current);
                    cityCount--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                                WeatherInfo.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        upDateUi(cityQueue.get(current));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {

                    if (dbManager.loadWeather(cityQueue.get(current)).getMain() == 1)
                        dbManager.setMain(cityQueue.get(current), cityQueue.get(current+1));
                    cityQueue.remove(current);

                    cityCount--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                                WeatherInfo.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        upDateUi(cityQueue.get(current));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
            break;
            case R.id.setMain: {
                dbManager.setMain(mainCity, cityQueue.get(current));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(600);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        WeatherInfo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                upDateUi(cityQueue.get(current));
                            }
                        });
                    }
                }).start();
            }
            break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putInt("current", current);
        Log.d("tag", "setPreference: " + current);
        editor.putInt("cityCount", cityCount);
        editor.putString("main", mainCity);
        for (int i = 0; i < cityCount; i++) {
            editor.putString("city" + i, cityQueue.get(i));
        }
        editor.apply();
        Log.d("tag", "onDestroy: " + mainCity);
    }

    public void upDateUi(String currentCity) {
        DBManager dbManager = getInstance(getApplicationContext());
        Weather currentWeather = dbManager.loadWeather(currentCity);
        TextView tvInfo = (TextView) findViewById(R.id.info);
        TextView tvLocation = (TextView) findViewById(R.id.location);
        TextView tvDate = (TextView) findViewById(R.id.date);
        TextView tvDirect = (TextView) findViewById(R.id.direct);
        TextView tvHumidity = (TextView) findViewById(R.id.humidity);
        TextView tvImg = (TextView) findViewById(R.id.img);
        TextView tvTime = (TextView) findViewById(R.id.time);
        TextView tvMoon = (TextView) findViewById(R.id.moon);
        TextView tvPower = (TextView) findViewById(R.id.power);
        TextView tvTemperature = (TextView) findViewById(R.id.temperature);
        TextView tvMain = (TextView) findViewById(R.id.main);
        TextView tvPage = (TextView) findViewById(R.id.page);
        ImageView imageView = (ImageView) findViewById(R.id.img_info);
        String imgText = currentWeather.getInfo();
        switch (imgText) {
            case "晴": {
                imageView.setImageResource(R.drawable.sun);
                break;
            }
            case "多云": {
                imageView.setImageResource(R.drawable.cloud);
                break;
            }
            case "阵雨": {
                imageView.setImageResource(R.drawable.rain_tmp);
                break;
            }
            case "雨": {
                imageView.setImageResource(R.drawable.rain_big);
                break;
            }
            case "暴雨": {
                imageView.setImageResource(R.drawable.rain_super);
                break;
            }
            case "阴": {
                imageView.setImageResource(R.drawable.overcast);
                break;
            }
            case "小雪": {
                imageView.setImageResource(R.drawable.snow_min);
                break;
            }
            case "中雪": {
                imageView.setImageResource(R.drawable.snow);
                break;
            }
            case "大雪": {
                imageView.setImageResource(R.drawable.snow_big);
                break;
            }
            case "暴雪": {
                imageView.setImageResource(R.drawable.snow_super);
                break;
            }
            case "阵雪": {
                imageView.setImageResource(R.drawable.snow_tmp);
                break;
            }
            case "雾": {
                imageView.setImageResource(R.drawable.fogg_min);
                break;
            }
            case "霾": {
                imageView.setImageResource(R.drawable.fogg_big);
                break;
            }
            case "冰雹": {
                imageView.setImageResource(R.drawable.ice);
                break;
            }
            case "雷阵雨": {
                imageView.setImageResource(R.drawable.thunder_rain);
                break;
            }
        }


        tvInfo.setText(currentWeather.getInfo());
        if (currentWeather.getMain() == 1) {
            tvLocation.setText(currentWeather.getCity_name());
            tvMain.setVisibility(View.VISIBLE);
        } else {
            tvLocation.setText(currentWeather.getCity_name());
            tvMain.setVisibility(View.INVISIBLE);
        }

        tvDate.setText(currentWeather.getDate());
        tvDirect.setText(currentWeather.getDirect());
        tvHumidity.setText("湿度：" + currentWeather.getHumidity());
        tvImg.setText("可视：" + currentWeather.getImg());
        tvMoon.setText("农历：" + currentWeather.getMoon());
        tvPower.setText("风力：" + currentWeather.getPower());
        tvTemperature.setText(currentWeather.getTemperature() + "°");
        tvTime.setText("时间：" + currentWeather.getTime());
        currentPage = current + 1;
        tvPage.setText(currentPage + "/" + cityCount);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 1:
                Log.d("TAG", "onActivityResult: case1" + requestCode + resultCode);
                if (resultCode == RESULT_OK) {
                    cityCount += 1;
                    current = cityCount - 1;
                    cityQueue.add(intent.getStringExtra("cityName"));
                    Util.sendHttpRequest(cityQueue.get(current), getApplicationContext());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                                WeatherInfo.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        upDateUi(cityQueue.get(current));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();


                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Log.d("tag", "onActivityResult: case2" + requestCode + resultCode);
                    cityCount += 1;
                    mainCity = intent.getStringExtra("cityName");
                    DBManager.getInstance(getApplicationContext()).setMain("null", mainCity);
                    cityQueue.add(mainCity);
                    Util.sendHttpRequest(cityQueue.get(current), getApplicationContext());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                                WeatherInfo.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        upDateUi(cityQueue.get(current));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }
                break;
            default:

        }

    }
}




