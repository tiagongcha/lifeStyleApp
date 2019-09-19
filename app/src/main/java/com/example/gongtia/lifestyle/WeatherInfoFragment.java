package com.example.gongtia.lifestyle;


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
    String description, snow, windSpeed, winDirection;
    String date;
    String weatherCode;
    TextView dateTV, siteTV, descriptionTV, temperatureTV;
    ImageView im;
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
        //Get city name by Longitude and Latitude
        String lat = Double.toString(location.getLatitude());
        String lon =Double.toString(location.getLongitude());

        // Send city name to weatherbit.io to get Weacthre information
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

        JSONObject current = (JSONObject) arrData.get(0);
        if (current == null) {
            return;
        }
        date = current.optString("valid_date");
        temprature = current.optString("temp");
        maxTemprature = current.optString("max_temp");
        minTemprature = current.optString("min_temp");
        snow = current.optString("snow");
        JSONObject weatherDes = current.getJSONObject("weather");
        description = weatherDes.optString("description");
        weatherCode = weatherDes.optString("code");
        winDirection = current.optString("wind_cdir_full");
        windSpeed = current.optString("wind_spd") + "m/s";

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
        descriptionTV.setText(description);
        temperatureTV.setText(Html.fromHtml(minTemprature + "<sup>o</sup>C" + " ~ " + maxTemprature + "<sup>o</sup>C"));

        im = view.findViewById(R.id.Icon);
        String s = meMap.get(weatherCode);
        int imID = this.getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
        im.setImageResource(imID);
    }
}
