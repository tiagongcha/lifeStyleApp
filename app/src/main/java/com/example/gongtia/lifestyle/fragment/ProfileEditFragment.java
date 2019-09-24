package com.example.gongtia.lifestyle.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.model.User;
import com.example.gongtia.lifestyle.activity.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ProfileEditFragment extends Fragment implements View.OnClickListener {

    private String mUserName, mAge, mSex, mCity, mCountry, mHeight, mWeight, profilePhotoPath;

    //    define UI componets:
    private EditText etUserName, etAge, etCity, etHeight, etWeight;
    private RadioGroup rgSex;
    private RadioButton rbSex;
    private Button mbtUpdate, mbtCountry;
    private ImageButton mbtCamera;
    private ImageView mProfilePic;

    private double weight, height;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private DatabaseReference mProfileReference;
    private FirebaseAuth mAuth;
    private String userId;
    private Uri url;
    private boolean setCountry = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        etUserName = view.findViewById(R.id.et_userName);
        etAge = view.findViewById(R.id.et_age);
        etCity = view.findViewById(R.id.et_city);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);

        rgSex = (RadioGroup) view.findViewById(R.id.rg_sex);
        rbSex = (RadioButton) view.findViewById(rgSex.getCheckedRadioButtonId());
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSex = (RadioButton) view.findViewById(checkedId);
            }
        });

        mbtUpdate = view.findViewById(R.id.button_update_profile);
        mbtUpdate.setOnClickListener(this);

        mbtCountry = view.findViewById(R.id.button_country);
        mbtCountry.setOnClickListener(this);

        mbtCamera = view.findViewById(R.id.button_camera);
        mbtCamera.setOnClickListener(this);

        mProfilePic = (ImageView) view.findViewById(R.id.iv_profile);

        mAuth = FirebaseAuth.getInstance();
        mProfileReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();

        ValueEventListener mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                populateDate(dataSnapshot);
                loadProfilePic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mProfileReference.addValueEventListener(mListener);

        if(savedInstanceState != null){
            etUserName.setText("" + savedInstanceState.getString("username_text"));
            etAge.setText("" + savedInstanceState.getString("age_text"));
            etCity.setText("" + savedInstanceState.getString("city_text"));
            etHeight.setText("" + savedInstanceState.getString("height_text"));
            etWeight.setText("" + savedInstanceState.get("weight_text"));
        }

        return view;
    }

    private void populateDate(@NonNull DataSnapshot dataSnapshot) {
        etUserName.setText(dataSnapshot.child(userId).getValue(User.class).getUserName());
        etAge.setText("" + dataSnapshot.child(userId).getValue(User.class).getAge());
        etCity.setText(dataSnapshot.child(userId).getValue(User.class).getCity());
        etHeight.setText("" + dataSnapshot.child(userId).getValue(User.class).getHeight());
        etWeight.setText("" + dataSnapshot.child(userId).getValue(User.class).getWeight());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_camera:{
                dispatchTakePictureIntent();
                break;
            }

            case R.id.button_country:{
                showCountry();
                break;
            }
            case R.id.button_update_profile:{
                if(validateInput()){
                    updateUserProfile();
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                }
                break;
            }
        }

    }

    private void updateUserProfile() {
        DatabaseReference curRef = mProfileReference.child(userId);
        curRef.child("userName").setValue(mUserName);
        if(!mAge.matches("")){
            int age = Integer.parseInt(mAge);
            curRef.child("age").setValue(age);
        }
        if(!mCity.matches("")){
            curRef.child("city").setValue(mCity);
        }

        if(setCountry){
            curRef.child("country").setValue(mCountry);
        }
        curRef.child("sex").setValue(mSex);

        if(!mHeight.matches("") && !mWeight.matches("")){
            double height = Double.parseDouble(mHeight);
            double weight = Double.parseDouble(mWeight);
            curRef.child("height").setValue(height);
            curRef.child("weight").setValue(weight);
        }
    }

    private void showCountry() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                mCountry = name;
                mbtCountry.setBackgroundResource(flagDrawableResID);
                picker.dismiss();
                setCountry = true;
            }
        });
        picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    private void loadProfilePic() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference ProfileRef = storageRef.child("pics").child(uid);
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

    private boolean validateInput(){
        mUserName = etUserName.getText().toString();
        mHeight = etHeight.getText().toString();
        mWeight = etWeight.getText().toString();
        mAge = etAge.getText().toString();
        mCity = etCity.getText().toString();
        mSex = rbSex.getText().toString();

        if(mUserName.matches("")){
            etUserName.setError("User Name is Required");
            return false;
        }

        if(!mWeight.matches("") && !mHeight.matches("")){
            mWeight = etWeight.getText().toString();
            mHeight = etHeight.getText().toString();
            weight = Double.parseDouble(mWeight);
            height = Double.parseDouble(mHeight);
            if(weight <= 0 || weight >= 2000){
                etWeight.setError("Weight not Valid");
                return false;
            }
            if(height <=0 || height >= 200){
                etHeight.setError("Height not Valid");
                return false;
            }

        }
        return true;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap thumbnailImage = (Bitmap) extras.get("data");
            uploadImageAndSaveUri(thumbnailImage);
        }
    }

    private void uploadImageAndSaveUri(Bitmap thumbnailImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference photoRef = storageRef.child("pics/" + uid);
        thumbnailImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                url = uri.getResult();

                mProfilePic.setImageBitmap(thumbnailImage);

                Toast.makeText(getActivity(), "Upload Success, download URL " +
                        url.toString(), Toast.LENGTH_LONG).show();
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

<<<<<<< HEAD:app/src/main/java/com/example/gongtia/lifestyle/fragment/ProfileEditFragment.java
=======
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("username_text", mUserName);
        outState.putString("age_text", mAge);
        outState.putString("city_text", mCity);
        outState.putString("height_text", mHeight);
        outState.putString("weight_text", mWeight);
    }

>>>>>>> upstream/master:app/src/main/java/com/example/gongtia/lifestyle/fragment/ProfileEditFragment.java
}
