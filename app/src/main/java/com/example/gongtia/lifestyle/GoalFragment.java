package com.example.gongtia.lifestyle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GoalFragment extends Fragment {

    private Spinner spinner;
    private Button submit;
    private Integer age = 27, weight = 123, height = 135, goal = 98, curBMR, curColNed, goalBMR, goalColNed;
    private Boolean isMale = true;
    private TextView tvHeight, tvWeight, tvGoal, tvToGO, tvchGoal, tvCurBMR, tvCurColNed, tvGoalBMR, tvGoalColNed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_goal, container, false);
        //text view
        tvWeight = view.findViewById(R.id.textView_Weight);
        tvWeight.setText(weight.toString());
        tvHeight = view.findViewById(R.id.textView_Height);
        tvHeight.setText(height.toString());
        tvGoal = view.findViewById(R.id.textView_Goal);
        tvGoal.setText(goal.toString());
        tvToGO = view.findViewById(R.id.textView_ToDo);
        Integer toGo = weight-goal;
        tvToGO.setText(toGo.toString());

        //goal submit button
        submit = view.findViewById(R.id.button_submit);
        tvchGoal = view.findViewById(R.id.editText_Change);
        String g = tvchGoal.getText().toString();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TO update goal g to the realtime database;
            }
        });

        //BMR text view
        calRMB();
        tvCurBMR = view.findViewById(R.id.textView_current_BMR);
        tvCurBMR.setText(curBMR.toString());
        tvCurColNed = view.findViewById(R.id.textView_current_caloric);
        tvGoalBMR = view.findViewById(R.id.textView_Goal_BMR);
        tvGoalBMR.setText(goalBMR.toString());
        tvGoalColNed = view.findViewById(R.id.textView_goal_caloric);

        // spinner
        spinner = view.findViewById(R.id.spinner_active);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.active, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calCaloric(position);
                //caloric text view
                tvCurColNed.setText(curColNed.toString());
                tvGoalColNed.setText(goalColNed.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void calRMB(){
        //convert unit
        double w = weight * 0.45359237; // lb to kg
        double h = height * 2.54; // inch to cm
        double gw = goal * 0.45359237; //lb to kg
        if(isMale){
            curBMR = (int)(66.4730 + (13.7516 * w) + (5.0033 * h) - (6.7550 * age));
            goalBMR = (int)(66.4730 + (13.7516 * gw) + (5.0033 * h) - (6.7550 * age));
        }else{
            curBMR = (int)(655.0955 + (9.5634 * w) + (1.8496 * h) - (4.6756 * age));
            goalBMR = (int)(655.0955 + (9.5634 * gw) + (1.8496 * h) - (4.6756 * age));
        }

    }

    private void calCaloric(int actlvl){
        if(actlvl == 0){
            curColNed  = (int)(curBMR * 1.2);
            goalColNed = (int)(goalBMR * 1.2);
        }else if(actlvl == 1){
            curColNed  = (int)(curBMR * 1.375);
            goalColNed = (int)(goalBMR * 1.375);
        }else if(actlvl == 2){
            curColNed  = (int)(curBMR * 1.55);
            goalColNed = (int)(goalBMR * 1.55);
        }else if(actlvl == 3){
            curColNed  = (int)(curBMR * 1.725);
            goalColNed = (int)(goalBMR * 1.725);
        }else if(actlvl == 4){
            curColNed  = (int)(curBMR * 1.9);
            goalColNed = (int)(goalBMR * 1.9);
        }
    }
}

