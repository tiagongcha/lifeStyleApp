package com.example.gongtia.lifestyle.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gongtia.lifestyle.JSONProfileUtils;
import com.example.gongtia.lifestyle.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class GoalRepository {

    private final MutableLiveData<User> user =
            new MutableLiveData<>();
    private User dataSet;

    public GoalRepository (Application application) {
       loadData();
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    private void loadData(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mProfileReference = FirebaseDatabase.getInstance().getReference();
        String userId = mAuth.getCurrentUser().getUid();

        ValueEventListener mListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSet = dataSnapshot.child(userId).getValue(User.class);
                user.setValue(dataSet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        };
        mProfileReference.addValueEventListener(mListener);
    }


    public static int calcCurrentCalories(User user){
        double BMR = 0;
        int currentCalories = 0;
        if(user.getSex().equals("Female")){
            BMR = 655 + (4.3 * user.getWeight()) + (4.7 * user.getHeight()) - (4.7 * user.getAge());
        }else{
            BMR = 66 + (6.3 * user.getWeight()) + (12.9 * user.getHeight()) - (6.8 * user.getAge());
        }

        if(user.getLifeStyle()!= null && user.getLifeStyle().equals("Active")){
            currentCalories = (int)(BMR * 1.75);
        }else{
            currentCalories = (int)(BMR * 1.2);
        }
        return currentCalories;
    }

    public static int calcNewCalories(User user){
        double goalWeight = 0;
        int newCalories = 0;

        double pounds = Double.parseDouble(user.getLbs());

        if(user.getGoal().equals("Maintain")) {
            return calcCurrentCalories(user);
        } else if(user.getGoal().equals("Lose")){
            goalWeight = user.getWeight() - pounds;
        }else{
            goalWeight = user.getWeight() + pounds;
        }
        double BMR = 0;
        if(user.getSex().equals("Female")){
            BMR = 655 + (4.3 * goalWeight) + (4.7 * user.getHeight()) - (4.7 * user.getAge());
        }else{
            BMR = 66 + (6.3 * goalWeight) + (12.9 * user.getHeight()) - (6.8 * user.getAge());
        }

        if(user.getLifeStyle()!=null && user.getLifeStyle().equals("Active")){
            newCalories = (int) (BMR * 1.75);
        }else{
            newCalories = (int) (BMR * 1.2);
        }
        return newCalories;
    }

    public static int calcBMI(User user){
        return (int) (703 * user.getWeight() / (user.getHeight() * user.getHeight()));
    }
}
