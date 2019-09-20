package com.example.gongtia.lifestyle.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongtia.lifestyle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherInfoFragment extends Fragment {
    String cityName, stateName, countryName;
    String temprature, minTemprature, maxTemprature;
    String temprature1, minTemprature1, maxTemprature1;
    String temprature2, minTemprature2, maxTemprature2;
    String description, snow, windSpeed, winDirection;
    String description1, snow1, windSpeed1, winDirection1;
    String description2, snow2, windSpeed2, winDirection2;
    String date, date1,date2;
    String weatherCode, weatherCode1,weatherCode2;
    TextView dateTV, siteTV, descriptionTV, temperatureTV;
    TextView dateTV1, siteTV1, descriptionTV1, temperatureTV1;
    TextView dateTV2, siteTV2, descriptionTV2, temperatureTV2;

    ImageView im, im1,im2;
    HashMap<String, String> meMap = new HashMap<String, String>();

    public WeatherInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(serviceString);

        String provider = LocationManager.GPS_PROVIDER;

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "quit", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        try {
            getWeather(location);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setWeatherCode_Icon();
//        Toast.makeText(getActivity(),weatherInfo,Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setText(view);
    }

    void getWeather(Location location) throws IOException, JSONException {
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
        conn.setConnectTimeout(6 * 1000);
        if (conn.getResponseCode() != 200) {
            Toast.makeText(getActivity(), "请求url失败", Toast.LENGTH_SHORT).show();
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
        cityName = weatherRoot.optString("city_name");
        countryName = weatherRoot.optString("country_code");
        stateName = weatherRoot.optString("state_code");

        //today information
        JSONObject current = (JSONObject) arrData.get(0);
        if (current == null) {
            return;
        }
        date = "Today";
        temprature = current.optString("temp");
        maxTemprature = current.optString("max_temp");
        minTemprature = current.optString("min_temp");
        snow = current.optString("snow");
        JSONObject weatherDes = current.getJSONObject("weather");
        description = weatherDes.optString("description");
        weatherCode = weatherDes.optString("code");
        winDirection = current.optString("wind_cdir_full");
        windSpeed = current.optString("wind_spd") + "m/s";


        //Tomorrow information
        JSONObject current1 = (JSONObject) arrData.get(1);
        if (current == null) {
            return;
        }
        date1 = "Tomorrow";
        temprature1 = current1.optString("temp");
        maxTemprature1 = current1.optString("max_temp");
        minTemprature1 = current1.optString("min_temp");
        snow1 = current1.optString("snow");
        JSONObject weatherDes1 = current1.getJSONObject("weather");
        description1 = weatherDes1.optString("description");
        weatherCode1= weatherDes1.optString("code");
        winDirection1 = current1.optString("wind_cdir_full");
        windSpeed1 = current1.optString("wind_spd") + "m/s";

        //Third day information
        JSONObject current2 = (JSONObject) arrData.get(2);
        if (current == null) {
            return;
        }
        date2 = current2.optString("valid_date");
        temprature2 = current2.optString("temp");
        maxTemprature2 = current2.optString("max_temp");
        minTemprature2 = current2.optString("min_temp");
        snow2 = current2.optString("snow");
        JSONObject weatherDes2 = current2.getJSONObject("weather");
        description2 = weatherDes2.optString("description");
        weatherCode2= weatherDes2.optString("code");
        winDirection2= current2.optString("wind_cdir_full");
        windSpeed2= current2.optString("wind_spd") + "m/s";

//        weatherInfo ="Date:" + date + "\n" +"City:" + cityName + "\n"  +
//                "Description:"+ description + "\n" +"Temprature:" + temprature +"\n" +
//                "MaxTemp:" + maxTemprature +  "\n" + "MinTemp:" +minTemprature +  "\n"
//                +"WindSpeed:" + windSpeed  + "\n" + "WindDiretion:"+ winDirection;

        //        Toast.makeText(this, weatherInfo, Toast.LENGTH_SHORT).show();
        return;
    }

    public void setWeatherCode_Icon() {
        meMap.put("200", "t01d");
        meMap.put("201", "t01d");
        meMap.put("202", "t01d");
        meMap.put("230", "t04d");
        meMap.put("231", "t04d");
        meMap.put("232", "t04d");
        meMap.put("233", "t04d");
        meMap.put("300", "d01d");
        meMap.put("301", "d01d");
        meMap.put("302", "d01d");
        meMap.put("500", "r01d");
        meMap.put("501", "r01d");
        meMap.put("502", "r03d");
        meMap.put("511", "r04d");
        meMap.put("520", "r04d");
        meMap.put("521", "r05d");
        meMap.put("522", "r04d");
        meMap.put("600", "s01d");
        meMap.put("601", "s02d");
        meMap.put("602", "s02d");
        meMap.put("610", "s01d");
        meMap.put("611", "s05d");
        meMap.put("612", "s05d");
        meMap.put("621", "s01d");
        meMap.put("622", "s02d");
        meMap.put("623", "s02d");
        meMap.put("700", "a01d");
        meMap.put("711", "a01d");
        meMap.put("721", "a01d");
        meMap.put("731", "a01d");
        meMap.put("741", "a01d");
        meMap.put("751", "a01d");
        meMap.put("800", "c01d");
        meMap.put("801", "c02d");
        meMap.put("802", "c02d");
        meMap.put("803", "c03d");
        meMap.put("804", "c04d");
        meMap.put("900", "u00d");
    }

    public void setText(View view) {
        dateTV = view.findViewById(R.id.Date);
        siteTV = view.findViewById(R.id.Site);
        descriptionTV = view.findViewById(R.id.Description);
        temperatureTV = view.findViewById(R.id.Temperature);
        dateTV.setText(date);
        siteTV.setText(cityName + "/" + stateName);
        descriptionTV.setText(description1);
        temperatureTV.setText(Html.fromHtml(minTemprature + "<sup>o</sup>C" + " ~ " + maxTemprature + "<sup>o</sup>C"));

        im = view.findViewById(R.id.Icon);
        String s = meMap.get(weatherCode);
        int imID = this.getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
        im.setImageResource(imID);



        dateTV1 = view.findViewById(R.id.Date_TV_D2);
        siteTV1 = view.findViewById(R.id.Site_TV_D2);
        descriptionTV1 = view.findViewById(R.id.Description_D2);
        temperatureTV1 = view.findViewById(R.id.Temperature_D2);
        dateTV1.setText(date1);
        siteTV1.setText(cityName + "/" + stateName);
        descriptionTV1.setText(description1);
        temperatureTV1.setText(Html.fromHtml(minTemprature1 + "<sup>o</sup>C" + " ~ " + maxTemprature1 + "<sup>o</sup>C"));

        im1 = view.findViewById(R.id.Icon_IV_D2);
        String s1 = meMap.get(weatherCode1);
        int imID1 = this.getResources().getIdentifier(s1, "drawable", getActivity().getPackageName());
        im1.setImageResource(imID1);

        dateTV2 = view.findViewById(R.id.Date_TV_D3);
        siteTV2 = view.findViewById(R.id.Site_TV_D3);
        descriptionTV2 = view.findViewById(R.id.Description_D3);
        temperatureTV2 = view.findViewById(R.id.Temperature_D3);
        dateTV2.setText(date2);
        siteTV2.setText(cityName + "/" + stateName);
        descriptionTV2.setText(description2);
        temperatureTV2.setText(Html.fromHtml(minTemprature2 + "<sup>o</sup>C" + " ~ " + maxTemprature2 + "<sup>o</sup>C"));

        im2 = view.findViewById(R.id.Icon_IV_D3);
        String s2 = meMap.get(weatherCode2);
        int imID2 = this.getResources().getIdentifier(s2, "drawable", getActivity().getPackageName());
        im2.setImageResource(imID2);
    }
}
