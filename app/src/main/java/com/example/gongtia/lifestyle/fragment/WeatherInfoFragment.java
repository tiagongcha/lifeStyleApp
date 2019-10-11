package com.example.gongtia.lifestyle.fragment;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.ViewModel.WeatherData;
import com.example.gongtia.lifestyle.ViewModel.WeatherViewModel;

import org.json.JSONException;
import java.util.HashMap;

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
    WeatherViewModel mWeatherViewModel;
    Activity mActivity;

    public WeatherInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWeatherCode_Icon();
        //Create the view model
        mWeatherViewModel = ViewModelProviders.of(getActivity()).get(WeatherViewModel.class);

        //Set the observer
        mWeatherViewModel.getData().observe(this, nameObserver);

        //Pass activity
        mActivity = getActivity();
        mWeatherViewModel.setActivity(mActivity);

    }

    //Create an observer that watches the LiveData
    final Observer<WeatherData> nameObserver = new Observer<WeatherData>() {
        @Override
        public void onChanged(WeatherData weatherData) {
            if(weatherData !=null) {
                try {
                    parseData(weatherData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.e("JRM", "onChanged: "+ "weatherData is null");
            }
            setText();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);

        return view;
    }


    public void parseData(WeatherData wd) throws JSONException {

        cityName= wd.cityName;
        countryName = wd.countryName;
        stateName = wd.stateName;
        date = wd.date;


        temprature = wd.temperature;
        maxTemprature = wd.maxTemperature;
        minTemprature = wd.minTemperature;
        snow = wd.snow;

        description = wd.description;
        weatherCode = wd.weatherCode;
        winDirection = wd.winDirection;
        windSpeed = wd.windSpeed;

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

    public void setText() {
        dateTV = getActivity().findViewById(R.id.Date);
        siteTV = getActivity().findViewById(R.id.Site);
        descriptionTV = getActivity().findViewById(R.id.Description);
        temperatureTV = getActivity().findViewById(R.id.Temperature);
        dateTV.setText(date);
        siteTV.setText(cityName + "/" + stateName);
        descriptionTV.setText(description);
        temperatureTV.setText(Html.fromHtml(minTemprature + "<sup>o</sup>C" + " ~ " + maxTemprature + "<sup>o</sup>C"));

        im = getActivity().findViewById(R.id.Icon);
        String s = meMap.get(weatherCode);
        int imID = this.getResources().getIdentifier(s, "drawable", getActivity().getPackageName());
        im.setImageResource(imID);

//        dateTV1 = view.findViewById(R.id.Date_TV_D2);
//        siteTV1 = view.findViewById(R.id.Site_TV_D2);
//        descriptionTV1 = view.findViewById(R.id.Description_D2);
//        temperatureTV1 = view.findViewById(R.id.Temperature_D2);
//        dateTV1.setText(date1);
//        siteTV1.setText(cityName + "/" + stateName);
//        descriptionTV1.setText(description1);
//        temperatureTV1.setText(Html.fromHtml(minTemprature1 + "<sup>o</sup>C" + " ~ " + maxTemprature1 + "<sup>o</sup>C"));
//
//        im1 = view.findViewById(R.id.Icon_IV_D2);
//        String s1 = meMap.get(weatherCode1);
//        int imID1 = this.getResources().getIdentifier(s1, "drawable", getActivity().getPackageName());
//        im1.setImageResource(imID1);
//
//        dateTV2 = view.findViewById(R.id.Date_TV_D3);
//        siteTV2 = view.findViewById(R.id.Site_TV_D3);
//        descriptionTV2 = view.findViewById(R.id.Description_D3);
//        temperatureTV2 = view.findViewById(R.id.Temperature_D3);
//        dateTV2.setText(date2);
//        siteTV2.setText(cityName + "/" + stateName);
//        descriptionTV2.setText(description2);
//        temperatureTV2.setText(Html.fromHtml(minTemprature2 + "<sup>o</sup>C" + " ~ " + maxTemprature2 + "<sup>o</sup>C"));
//
//        im2 = view.findViewById(R.id.Icon_IV_D3);
//        String s2 = meMap.get(weatherCode2);
//        int imID2 = this.getResources().getIdentifier(s2, "drawable", getActivity().getPackageName());
//        im2.setImageResource(imID2);
    }
}
