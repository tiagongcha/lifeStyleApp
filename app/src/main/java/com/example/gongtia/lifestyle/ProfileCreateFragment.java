package com.example.gongtia.lifestyle;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class ProfileCreateFragment extends Fragment implements View.OnClickListener{

    //    parsed data:
    private String mUserName, mAge, mSex, mCity, mCountry, mHeight, mWeight, profilePhotoPath;

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

//        mbtShowBMI = view.findViewById(R.id.button_showBMI);
//        mbtShowBMI.setOnClickListener(this);
//
//        tv_showBMI = (TextView) view.findViewById(R.id.tv_showBMI);

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
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                }
                break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            mProfilePic = getActivity().findViewById(R.id.iv_profile);
            mProfilePic.setImageBitmap(BitmapFactory.decodeFile(profilePhotoPath));
        }
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


//    private boolean validateBMIInput(){
//        if(TextUtils.isEmpty(etWeight.getText())){
//            etWeight.setError("Weight is Required");
//            return false;
//        }
//        if(TextUtils.isEmpty(etHeight.getText())){
//            etHeight.setError("Height is Required");
//            return false;
//        }
//        mWeight = etWeight.getText().toString();
//        mHeight = etHeight.getText().toString();
//        weight = Double.parseDouble(mWeight);
//        height = Double.parseDouble(mHeight);
//        if(weight <= 0 || weight >= 2000){
//            etWeight.setError("Weight not Valid");
//            return false;
//        }
//        if(height <=0 || height >= 200){
//            etHeight.setError("Height not Valid");
//            return false;
//        }
//        return true;
//    }

//    private void showBMI(){
//        double bmi = 703 * weight/(height * height);
//        tv_showBMI.setText("" + new DecimalFormat("#.##").format(bmi));
//    }

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

    private File createImageFile() throws IOException {
        // Create the profile image file name.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);
        profilePhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.example.gongtia.lifestyle", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
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


}
