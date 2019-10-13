package com.example.gongtia.lifestyle.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.ViewModel.ProfileViewModel;
import com.example.gongtia.lifestyle.model.User;
import com.example.gongtia.lifestyle.activity.ProfileEditActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private DatabaseReference mProfileReference;
    private FirebaseAuth mAuth;
    private String userId;

    private TextView mTvUserName, mTvAge, mTvSex, mTvCity, mTvCountry, mTvHeight, mTvWeight;
    private Button mbtEdit;
    private ImageView mProfilePic;
    //    ADD VIEW MODEL:
    private ProfileViewModel mProfileViewModel;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mTvUserName = (TextView) view.findViewById(R.id.tv_userName_field);
        mTvAge = (TextView) view.findViewById(R.id.tv_age_field);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex_field);
        mTvCity = (TextView) view.findViewById(R.id.tv_city_field);
        mTvCountry = (TextView) view.findViewById(R.id.tv_country_field);
        mTvHeight = (TextView) view.findViewById(R.id.tv_height_field);
        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_field);

        mbtEdit = (Button) view.findViewById(R.id.button_edit_profile);
        mbtEdit.setOnClickListener(this);

        mProfilePic = (ImageView) view.findViewById(R.id.iv_profile);

        mAuth = FirebaseAuth.getInstance();
        mProfileReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        ValueEventListener mListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showData(dataSnapshot);
                loadProfilePic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        };
        mProfileReference.addValueEventListener(mListener);
        //        ADD VIEW MODEL!!!!!!!
        mProfileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
//Set the observer
        (mProfileViewModel.getData()).observe(this,nameObserver);
        return view;
    }

    final Observer<User> nameObserver = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            if(user!=null){
                mTvUserName.setText(user.getUserName());
                mTvAge.setText("" + user.getAge());
                mTvCity.setText(user.getCity());
                mTvHeight.setText("" + user.getHeight());
                mTvWeight.setText("" + user.getWeight());
            }
        }
    };

    @Override
    public void onClick(View view) {
//        Fragment profileEditFragment = new ProfileEditFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.content_container, profileEditFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        Intent profileEditIntent = new Intent(getActivity(), ProfileEditActivity.class);
        startActivity(profileEditIntent);
    }

    /**
     *
     * @param dataSnapshot - snapshot of the entire database -> iterate snapshot
     */
    private void showData(@NonNull DataSnapshot dataSnapshot){
        mTvUserName.setText(dataSnapshot.child(userId).getValue(User.class).getUserName());
        mTvAge.setText("" + dataSnapshot.child(userId).getValue(User.class).getAge());
        mTvSex.setText(dataSnapshot.child(userId).getValue(User.class).getSex());
        mTvCity.setText(dataSnapshot.child(userId).getValue(User.class).getCity());
        mTvCountry.setText(dataSnapshot.child(userId).getValue(User.class).getCountry());
        mTvHeight.setText("" + dataSnapshot.child(userId).getValue(User.class).getHeight());
        mTvWeight.setText("" + dataSnapshot.child(userId).getValue(User.class).getWeight());
    }

    private void loadProfilePic(){

        StorageReference ProfileRef = storageRef.child("pics").child(userId);
        final long ONE_MEGABYTE = 1024 * 1024;

        ProfileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mProfilePic.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

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
