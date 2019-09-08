package com.example.gongtia.lifestyle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private DatabaseReference mProfileReference;
    private FirebaseAuth mAuth;
    private String userId;

    private TextView mTvUserName, mTvAge, mTvSex, mTvCity, mTvCountry, mTvHeight, mTvWeight;
    private Button mbtEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mTvUserName = (TextView) view.findViewById(R.id.tv_username_field);
        mTvAge = (TextView) view.findViewById(R.id.tv_age_field);
        mTvSex = (TextView) view.findViewById(R.id.tv_sex_field);
        mTvCity = (TextView) view.findViewById(R.id.tv_city_field);
        mTvCountry = (TextView) view.findViewById(R.id.tv_country_field);
        mTvHeight = (TextView) view.findViewById(R.id.tv_height_field);
        mTvWeight = (TextView) view.findViewById(R.id.tv_weight_field);

        mbtEdit = (Button) view.findViewById(R.id.button_edit_profile);
        mbtEdit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mProfileReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        ValueEventListener mListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        };
        mProfileReference.addValueEventListener(mListener);
        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment profileEditFragment = new ProfileEditFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_container, profileEditFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     *
     * @param dataSnapshot - snapshot of the entire database -> iterate snapshot
     */
    private void showData(@NonNull DataSnapshot dataSnapshot){
        mTvUserName.setText( dataSnapshot.child(userId).getValue(User.class).getUserName());
        mTvAge.setText("" + dataSnapshot.child(userId).getValue(User.class).getAge());
        mTvSex.setText(dataSnapshot.child(userId).getValue(User.class).getSex());
        mTvCity.setText(dataSnapshot.child(userId).getValue(User.class).getCity());
        mTvCountry.setText(dataSnapshot.child(userId).getValue(User.class).getCountry());
        mTvHeight.setText("" + dataSnapshot.child(userId).getValue(User.class).getHeight());
        mTvWeight.setText("" + dataSnapshot.child(userId).getValue(User.class).getWeight());
    }



}
