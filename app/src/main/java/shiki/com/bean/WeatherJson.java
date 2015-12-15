package shiki.com.bean;

import java.util.List;

/**
 * Created by wnh50 on 2015/12/9.
 */
public class WeatherJson {
    private String reason;
    private WeatherResult result;

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public WeatherResult getResult() {
        return result;
    }
    public void setResult(WeatherResult result) {
        this.result = result;
    }


    public class WeatherResult{
        private DataWeather data;

        public DataWeather getData() {
            return data;
        }
        public void setData(DataWeather data) {
            this.data = data;
        }

        public class DataWeather{
            private RealTime realtime;
            private List<Daylist> weather;

            public List<Daylist> getWeather() {
                return weather;
            }
            public void setWeather(List<Daylist> weather) {
                this.weather = weather;
            }

            public RealTime getRealTime() {
                return realtime;
            }
            public void setRealTime(RealTime realtime) {
                this.realtime = realtime;
            }

            public class RealTime{
                private String city_code;
                private String city_name;
                private String date;
                private String time;
                private String moon;
                private Weather weather;
                private Wind wind;

                public Wind getWind() {
                    return wind;
                }
                public void setWind(Wind wind) {
                    this.wind = wind;
                }
                public String getUpdateTime() {
                    return time;
                }
                public void setUpdateTime(String time) {
                    this.time = time;
                }
                public String getMoon() {
                    return moon;
                }
                public void setMoon(String moon) {
                    this.moon = moon;
                }
                public Weather getWeather() {
                    return weather;
                }
                public void setWeather(Weather weather) {
                    this.weather = weather;
                }
                public String getCity_code() {
                    return city_code;
                }
                public void setCity_code(String city_code) {
                    this.city_code = city_code;
                }
                public String getCity_name() {
                    return city_name;
                }
                public void setCity_name(String city_name) {
                    this.city_name = city_name;
                }
                public String getDate() {
                    return date;
                }
                public void setDate(String date) {
                    this.date = date;
                }

                public class Weather{
                    private String temperature;
                    private String humidity;
                    private String info;
                    private String img;

                    public String getTemperature() {
                        return temperature;
                    }
                    public void setTemperature(String temperature) {
                        this.temperature = temperature;
                    }
                    public String getHumidity() {
                        return humidity;
                    }
                    public void setHumidity(String humidity) {
                        this.humidity = humidity;
                    }
                    public String getInfo() {
                        return info;
                    }
                    public void setInfo(String info) {
                        this.info = info;
                    }
                    public String getImg() {
                        return img;
                    }
                    public void setImg(String img) {
                        this.img = img;
                    }
                }

                public class Wind{
                    private String direct;
                    private String power;

                    public String getDirect() {
                        return direct;
                    }
                    public void setDirect(String direct) {
                        this.direct = direct;
                    }
                    public String getPower() {
                        return power;
                    }
                    public void setPower(String power) {
                        this.power = power;
                    }

                }
            }
            public class Daylist{
                private String date;
                private Info info;
                private String nongli;

                public String getDate() {
                    return date;
                }
                public void setDate(String date) {
                    this.date = date;
                }
                public Info getInfo() {
                    return info;
                }
                public void setInfo(Info info) {
                    this.info = info;
                }
                public String getNongli() {
                    return nongli;
                }
                public void setNongli(String nongli) {
                    this.nongli = nongli;
                }


                public class Info{
                    private List<String> day;
                    private List<String> night;

                    public List<String> getDay() {
                        return day;
                    }
                    public void setDay(List<String> day) {
                        this.day = day;
                    }
                    public List<String> getNight() {
                        return night;
                    }
                    public void setNight(List<String> night) {
                        this.night = night;
                    }



                }
                }

            }
        }
}



