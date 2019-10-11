package com.example.gongtia.lifestyle.ViewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.gongtia.lifestyle.Room.AppDatabase;
import com.example.gongtia.lifestyle.Room.WeatherDataEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


public class WeatherRepository {
    MutableLiveData<WeatherData> jsonData = new MutableLiveData<WeatherData>();
    private Activity mActivity;

    WeatherRepository(Application application) {

    }

    public void setActivity(Activity activity) {
        mActivity = activity;
        if (mActivity!= null) {
            loadData();
        }

    }

    public MutableLiveData<WeatherData> getData() {
        return  jsonData;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData() {
        new AsyncTask<Void, Void, WeatherData>() {


            @Override
            protected WeatherData doInBackground(Void... voids) {
                Location location = getLocation(mActivity);
                WeatherData wd = null;
                try {
                    wd = getWeather(location);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return wd;
            }

            @Override
            protected void onPostExecute(WeatherData weatherData) {
                super.onPostExecute(weatherData);
                jsonData.setValue(weatherData);
            }
        }.execute();
    }

    public Location getLocation (Activity activity) {

        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) activity.getSystemService(serviceString);

        String provider = LocationManager.GPS_PROVIDER;

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "quit", Toast.LENGTH_LONG).show();
            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }

    public WeatherData getWeather(Location location) throws IOException, JSONException {
        List<WeatherData> lw = new LinkedList<>();
        //Get location's Longitude and Latitude
        String lat = Double.toString(location.getLatitude());
        String lon =Double.toString(location.getLongitude());

        // Send Longitude and Latitude to weatherbit.io to get Weather information
        String weatherURL = "https://api.weatherbit.io/v2.0/forecast/daily?lat=" + lat+"&lon="+lon + "&key=cd1c1aa25a414247a70a1450ba94a3d4";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        //Remove strict for  don't run network operation on main thread
        StrictMode.setThreadPolicy(policy);
        URL url = new URL(weatherURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3 * 1000);
        if (conn.getResponseCode() != 200) {
            Toast.makeText(mActivity, "请求url失败", Toast.LENGTH_SHORT).show();
            throw new RuntimeException("请求url失败");
        }

        //Read Data from InputStream to String
        BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }

        String weatherJson = total.toString();
        conn.disconnect();
//        Toast.makeText(this, weatherJson, Toast.LENGTH_SHORT).show();

        //Convert string to JsonObject and parse contents
        JSONObject weatherRoot = new JSONObject(weatherJson);
        JSONArray arrData = weatherRoot.getJSONArray("data");
        WeatherData weatherData = new WeatherData();
        weatherData.cityName = weatherRoot.optString("city_name");
        weatherData.countryName = weatherRoot.optString("country_code");
        weatherData.stateName = weatherRoot.optString("state_code");
        //today information
        JSONObject current = (JSONObject) arrData.get(0);
        if (current == null) {
            return null;
        }
        weatherData.date = "Today";
        weatherData.temperature = current.optString("temp");
        weatherData.maxTemperature = current.optString("max_temp");
        weatherData.minTemperature = current.optString("min_temp");
        weatherData.snow = current.optString("snow");
        JSONObject weatherDes = current.getJSONObject("weather");
        weatherData.description = weatherDes.optString("description");
        weatherData.weatherCode = weatherDes.optString("code");
        weatherData.winDirection = current.optString("wind_cdir_full");
        weatherData.windSpeed = current.optString("wind_spd") + "m/s";
        Log.e("wr", "getWeather: Description"+ weatherData.description);

        //Tomorrow information
        WeatherData weatherData1 = new WeatherData();
        weatherData1.cityName = weatherRoot.optString("city_name");
        weatherData1.countryName = weatherRoot.optString("country_code");
        weatherData1.stateName = weatherRoot.optString("state_code");
        JSONObject current1 = (JSONObject) arrData.get(1);
        if (current == null) {
            return null;
        }
        weatherData1.date = "Tomorrow";
        weatherData1.temperature = current1.optString("temp");
        weatherData1.maxTemperature = current1.optString("max_temp");
        weatherData1.minTemperature = current1.optString("min_temp");
        weatherData1.snow = current1.optString("snow");
        JSONObject weatherDes1 = current1.getJSONObject("weather");
        weatherData1.description = weatherDes1.optString("description");
        weatherData1.weatherCode= weatherDes1.optString("code");
        weatherData1.winDirection = current1.optString("wind_cdir_full");
        weatherData1.windSpeed = current1.optString("wind_spd") + "m/s";

        //Third day information
        JSONObject current2 = (JSONObject) arrData.get(2);
        if (current == null) {
            return null;
        }
        WeatherData weatherData2 = new WeatherData();
        weatherData2.cityName = weatherRoot.optString("city_name");
        weatherData2.countryName = weatherRoot.optString("country_code");
        weatherData2.stateName = weatherRoot.optString("state_code");
        weatherData2.date = current2.optString("valid_date");
        weatherData2.temperature = current2.optString("temp");
        weatherData2.maxTemperature = current2.optString("max_temp");
        weatherData2.minTemperature = current2.optString("min_temp");
        weatherData2.snow = current2.optString("snow");
        JSONObject weatherDes2 = current2.getJSONObject("weather");
        weatherData2.description = weatherDes2.optString("description");
        weatherData2.weatherCode= weatherDes2.optString("code");
        weatherData2.winDirection= current2.optString("wind_cdir_full");
        weatherData2.windSpeed= current2.optString("wind_spd") + "m/s";

//       Toast.makeText(this, weatherInfo, Toast.LENGTH_SHORT).show();
        return weatherData;
    }


    @SuppressLint("StaticFieldLeak")
    public void saveDataToDB(WeatherDataEntity wd) {
        //Database operation must in another thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase db = AppDatabase.getInstance(mActivity.getBaseContext());
                db.weatherDataDao().insertWeatherDataEntity(wd);
                return null;
            }
        }.execute();
    }


}
