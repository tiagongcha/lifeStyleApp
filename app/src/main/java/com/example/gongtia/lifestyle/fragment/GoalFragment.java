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
import com.example.gongtia.lifestyle.ViewModel.GoalViewModel;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class GoalFragment extends Fragment implements View.OnClickListener {

//    private DatabaseReference mProfileReference;
//    private FirebaseAuth mAuth;
//    private String userId;
//    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private TextView mTvCurrentBMI, mTvCurrentCalories, mTvNewCalories, mTvBMIDes, mTvBMRDes;
    private Button mbtEdit;
    private GoalViewModel mGoalViewModel;
    private User mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_goal, container, false);

        mTvCurrentBMI = (TextView) view.findViewById(R.id.tv_currentBMI);
        mTvCurrentCalories = (TextView) view.findViewById(R.id.tv_currentCalories);
        mTvNewCalories = (TextView) view.findViewById(R.id.tv_newCalories);

        mbtEdit = (Button) view.findViewById(R.id.button_edit_goal);
        mbtEdit.setOnClickListener(this);


        mGoalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);

        mGoalViewModel.init();

        mGoalViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setData();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent editGoalIntent = new Intent(getActivity(), GoalEditActivity.class);
        startActivity(editGoalIntent);
    }

//    /**
//     *
//     * @param dataSnapshot - snapshot of the entire database -> iterate snapshot
//     */
//    private void getData(@NonNull DataSnapshot dataSnapshot){
//       mHeight = Double.parseDouble("" + dataSnapshot.child(userId).getValue(User.class).getHeight());
//       mWeight = dataSnapshot.child(userId).getValue(User.class).getWeight();
//       mAge = dataSnapshot.child(userId).getValue(User.class).getAge();
//       mSex = dataSnapshot.child(userId).getValue(User.class).getSex();
//       mGoal = dataSnapshot.child(userId).getValue(User.class).getGoal();
//       mLbs = dataSnapshot.child(userId).getValue(User.class).getLbs();
//       mLifestyle = dataSnapshot.child(userId).getValue(User.class).getLbs();
//    }


    private void setData(){
        mUser = mGoalViewModel.getUser().getValue();
        int currentCalories = mGoalViewModel.calcCurrentCalories();
        int newCalories = mGoalViewModel.calcNewCalories();
        int BMI = mGoalViewModel.calcBMI();
        if((mUser.getSex().equals("Female") && currentCalories < 1000)
                || (mUser.getSex().equals("Female") && newCalories < 1000)){
            Toast.makeText(getActivity(), "Your calories intake is less than 1000 which is unhealthy", Toast.LENGTH_SHORT).show();
        }

        if((mUser.getSex().equals("Male") && currentCalories < 1200)
                || (mUser.getSex().equals("Male") && newCalories < 1200)){
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
