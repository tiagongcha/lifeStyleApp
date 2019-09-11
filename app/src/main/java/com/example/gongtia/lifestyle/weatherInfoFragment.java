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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class weatherInfoFragment extends Fragment {
    String cityName, stateName, countryName;
    String temprature, minTemprature, maxTemprature;
    String description, snow, windSpeed, winDirection;
    String date;
    String weatherInfo;
    ;
    private TextView weatherInfoTV;

    public weatherInfoFragment() {
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
        TextView text = view.findViewById(R.id.wiText);
        text.setText(weatherInfo);
    }

    void getWeather(Location location) throws IOException, JSONException {
        //Get city name by Longitude and Latitude
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Address address = result.get(0);
        if (address == null)
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();

        String locality = address.getLocality();//得到城市

        // Send city name to weatherbit.io to get Weacthre information
        String weatherURL = "https://api.weatherbit.io/v2.0/forecast/daily?city=" + locality + "&key=cd1c1aa25a414247a70a1450ba94a3d4";
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
        if(current ==null) {
            return;
        }
        date = current.optString("valid_date");
        temprature = current.optString("temp");
        maxTemprature = current.optString("max_temp");
        minTemprature = current.optString("min_temp");
        snow = current.optString("snow");
        JSONObject weatherDes= current.getJSONObject("weather");
        description = weatherDes.optString("description");
        winDirection = current.optString("wind_cdir_full");
        windSpeed = current.optString("wind_spd") + "m/s";

        weatherInfo ="Date:" + date + "\n" +"City:" + cityName + "\n"  +
                "Description:"+ description + "\n" +"Temprature:" + temprature +"\n" +
                "MaxTemp:" + maxTemprature +  "\n" + "MinTemp:" +minTemprature +  "\n"
                +"WindSpeed:" + windSpeed  + "\n" + "WindDiretion:"+ winDirection;

        //        Toast.makeText(this, weatherInfo, Toast.LENGTH_SHORT).show();
        return;
    }
}
