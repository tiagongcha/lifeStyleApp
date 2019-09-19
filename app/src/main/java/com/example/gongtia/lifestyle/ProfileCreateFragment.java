package com.example.gongtia.lifestyle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class ProfileCreateFragment extends Fragment implements View.OnClickListener{


    private String mUserName, mAge, mSex, mCity, mCountry, mHeight, mWeight;

    private Uri url;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


    //    define UI componets:
    private EditText  etUserName, etAge, etCity, etHeight, etWeight;
    private RadioGroup rgSex;
    private RadioButton rbSex;
    private Button mbtCreate, mbtCountry;
    private ImageButton mbtCamera;
    private ImageView mProfilePic;

    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private User mUserProfile = new User();

    private double weight, height;

    private DatabaseReference mDatabase;

    public ProfileCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_create, container, false);
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

        mbtCreate = view.findViewById(R.id.button_create_profile);
        mbtCreate.setOnClickListener(this);

        mbtCountry = view.findViewById(R.id.button_country);
        mbtCountry.setOnClickListener(this);

        mbtCamera = view.findViewById(R.id.button_camera);
        mbtCamera.setOnClickListener(this);

        mProfilePic = (ImageView) view.findViewById(R.id.iv_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return view;

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
            case R.id.button_create_profile:{
                if(validateInput()){
                    storeUserProfile();
                    Intent goalIntent = new Intent(getActivity(), GoalCreateActivity.class);
                    startActivity(goalIntent);
                }
                break;
            }
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

//                Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showCountry(){
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                mCountry = name;
                mbtCountry.setBackgroundResource(flagDrawableResID);
                picker.dismiss();
            }
        });
        picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
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
        if(mHeight.matches("")){
            etHeight.setError("Height is Required");
            return false;
        }
        if(mWeight.matches("")){
            etWeight.setError("Weight is Required");
            return false;
        }
        if(mAge.matches("")){
            etAge.setError("Age is Required");
        }
        if(mCity.matches("")){
            etCity.setError("City is Required");
        }
        if(mCountry.matches("")){
            mbtCountry.setError("Country is Required");
        }

        if(TextUtils.isEmpty(etWeight.getText())){
            etWeight.setError("Weight is Required");
            return false;
        }
        if(TextUtils.isEmpty(etHeight.getText())){
            etHeight.setError("Height is Required");
            return false;
        }
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

        return true;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }



    private void storeUserProfile(){
//      name and age
        mUserProfile.setUserName(mUserName);
        int age = Integer.parseInt(mAge);
        mUserProfile.setAge(age);
        mUserProfile.setSex(mSex);
//        location
        mUserProfile.setCity(mCity);
        mUserProfile.setCountry(mCountry);
//        health data
        double height = Double.parseDouble(mHeight);
        mUserProfile.setHeight(height);
        double weight = Double.parseDouble(mWeight);
        mUserProfile.setWeight(weight);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUserProfile.setUid(user.getUid());
        mDatabase.child(user.getUid()).setValue(mUserProfile);

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
