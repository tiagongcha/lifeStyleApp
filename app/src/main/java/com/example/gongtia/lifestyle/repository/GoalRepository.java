package com.example.gongtia.lifestyle.repository;

import com.example.gongtia.lifestyle.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class GoalRepository {

    private static GoalRepository instance;
    private User dataSet = new User();

    public static GoalRepository getInstance() {
        if(instance == null){
            instance = new GoalRepository();
        }
        return instance;
    }

    public MutableLiveData<User> getUser() {
        setUser();

        MutableLiveData<User> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mProfileReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        ValueEventListener mListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSet = dataSnapshot.child(userId).getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        };
        mProfileReference.addValueEventListener(mListener);
    }

}
