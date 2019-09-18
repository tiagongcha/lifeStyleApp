package com.example.gongtia.lifestyle;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GoalCreateFragment extends Fragment implements View.OnClickListener{

    private String mGoal, mLbs, mLifestyle;
    private String userId;
    private FirebaseAuth mAuth;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private RadioGroup rgGoal, rgLifestyle;
    private RadioButton rbGoal, rbLifestyle;
    private Button mbtSubmit;
    private EditText etLbs;

    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_goal_create, container, false);
        etLbs = view.findViewById(R.id.et_lbs);

        rgGoal = (RadioGroup)  view.findViewById(R.id.rg_goal);
        rbGoal = (RadioButton) view.findViewById(rgGoal.getCheckedRadioButtonId());
        rgGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbGoal = (RadioButton) view.findViewById(checkedId);
            }
        });

        rgLifestyle = (RadioGroup) view.findViewById(R.id.rg_active);
        rbLifestyle = (RadioButton) view.findViewById(rgLifestyle.getCheckedRadioButtonId());
        rgLifestyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbLifestyle = (RadioButton) view.findViewById(checkedId);
            }
        });


        mbtSubmit = view.findViewById(R.id.button_create_goal);
        mbtSubmit.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        return view;
    }

    @Override
    public void onClick(View v) {
        if(validateLbs()){
            storeUserGoal();
//           jump to home activity
            Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
            startActivity(homeIntent);
        }
    }

    private boolean validateLbs() {
        mGoal = rbGoal.getText().toString();
        mLifestyle = rbLifestyle.getText().toString();
        mLbs = etLbs.getText().toString();

        if(!mGoal.equals("Maintain")){
            if(TextUtils.isEmpty(etLbs.getText())){
                etLbs.setError("Enter pounds");
                return false;
            }
            double lbs = Double.parseDouble(mLbs);

            if(lbs > 2){
                etLbs.setError("Getting Overzealous! Try <= 2 lbs");
                return false;
            }
        }
        return true;
    }

    private void storeUserGoal(){
        mDatabase.child(userId).child("goal").setValue(mGoal);
        mDatabase.child(userId).child("lifestyle").setValue(mLifestyle);
        mDatabase.child(userId).child("lbs").setValue(mLbs);
    }

}

