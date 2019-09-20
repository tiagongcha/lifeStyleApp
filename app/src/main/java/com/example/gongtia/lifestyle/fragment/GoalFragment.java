package com.example.gongtia.lifestyle.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.model.User;
import com.example.gongtia.lifestyle.activity.GoalEditActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GoalFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mProfileReference;
    private FirebaseAuth mAuth;
    private String userId;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private TextView mTvCurrentBMI, mTvCurrentCalories, mTvNewCalories, mTvBMIDes, mTvBMRDes;
    private Button mbtEdit;

    private double  mHeight, mWeight;
    private int mAge;
    private String mSex, mLifestyle, mGoal, mLbs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_goal, container, false);

        mTvCurrentBMI = (TextView) view.findViewById(R.id.tv_currentBMI);
        mTvCurrentCalories = (TextView) view.findViewById(R.id.tv_currentCalories);
        mTvNewCalories = (TextView) view.findViewById(R.id.tv_newCalories);

        mbtEdit = (Button) view.findViewById(R.id.button_edit_goal);
        mbtEdit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mProfileReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        ValueEventListener mListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        };
        mProfileReference.addValueEventListener(mListener);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent editGoalIntent = new Intent(getActivity(), GoalEditActivity.class);
        startActivity(editGoalIntent);
    }

    /**
     *
     * @param dataSnapshot - snapshot of the entire database -> iterate snapshot
     */
    private void getData(@NonNull DataSnapshot dataSnapshot){
       mHeight = Double.parseDouble("" + dataSnapshot.child(userId).getValue(User.class).getHeight());
       mWeight = dataSnapshot.child(userId).getValue(User.class).getWeight();
       mAge = dataSnapshot.child(userId).getValue(User.class).getAge();
       mSex = dataSnapshot.child(userId).getValue(User.class).getSex();
       mGoal = dataSnapshot.child(userId).getValue(User.class).getGoal();
       mLbs = dataSnapshot.child(userId).getValue(User.class).getLbs();
       mLifestyle = dataSnapshot.child(userId).getValue(User.class).getLbs();
    }


    private double calcCurrentCalories(){
        double currentCalories;
        double BMR = 0;
        if(mSex.equals("Female")){
            BMR = 655 + (4.3 * mWeight) + (4.7 * mHeight) - (4.7 * mAge);
        }else{
            BMR = 66 + (6.3 * mWeight) + (12.9 * mHeight) - (6.8 * mAge);
        }

        if(mLifestyle.equals("Active")){
            currentCalories = BMR * 1.75;
        }else{
            currentCalories = BMR * 1.2;
        }
        return currentCalories;
    }

    private double calcNewCalories(){
        double goalWeight = 0;
        double newCalories = 0;
        double pounds = Double.parseDouble(mLbs);

        if(mGoal.equals("Maintain")){
            return calcCurrentCalories();
        }

        else if(mGoal.equals("Lose")){
            goalWeight -= pounds;
        }else{
            goalWeight += pounds;
        }
        double BMR = 0;
        if(mSex.equals("Female")){
            BMR = 655 + (4.3 * goalWeight) + (4.7 * mHeight) - (4.7 * mAge);
        }else{
            BMR = 66 + (6.3 * goalWeight) + (12.9 * mHeight) - (6.8 * mAge);
        }

        if(mLifestyle.equals("Active")){
            newCalories = BMR * 1.75;
        }else{
            newCalories = BMR * 1.2;
        }
        return newCalories;
    }


    private double calcBMI(){
        return 703 * mWeight / (mHeight * mHeight);
    }

    private void setData(){
        double BMI = calcBMI();
        double currentCalories = calcCurrentCalories();
        double newCalories = calcNewCalories();

        if((mSex.equals("Female") && currentCalories < 1000) || (mSex.equals("Female") && newCalories < 1000)){
            Toast.makeText(getActivity(), "Your calories intake is less than 1000 which is unhealthy", Toast.LENGTH_SHORT).show();
        }

        if((mSex.equals("Male") && currentCalories < 1200) || (mSex.equals("Male") && newCalories < 1200)){
            Toast.makeText(getActivity(), "Your calories intake is less than 1200 which is unhealthy", Toast.LENGTH_SHORT).show();
        }

        mTvCurrentCalories.setText("" + new DecimalFormat("#.##").format(currentCalories));
        mTvCurrentBMI.setText("" + new DecimalFormat("#.##").format(BMI));
        mTvNewCalories.setText("" + new DecimalFormat("#.##").format(newCalories));
    }

    @Override public void onResume() {
        super.onResume();
        //lock screen to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override public void onPause() {
        super.onPause();
        //set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

}
