package com.example.gongtia.lifestyle;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherButtonFragment extends Fragment  {

    private Button weatherButton;
    private View view;
    public WeatherButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_weather_button, container, false);
        weatherButton = (Button) view.findViewById(R.id.weatherrButton);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "success2", Toast.LENGTH_SHORT).show();
                Intent weatherIntent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(weatherIntent);
            }
        });
    }

}
