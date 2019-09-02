package com.example.gongtia.lifestyle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;


import java.text.DecimalFormat;

import static android.app.Activity.RESULT_OK;


public class ProfileCreateFragment extends Fragment implements View.OnClickListener{

    //    parsed data:
    private String mUserName, mAge, mCity, mCountry, mHeight, mWeight, profilePhotoPath;

//    define UI componets:
    private EditText  etUserName, etAge, etCity, etHeight, etWeight;
    private RadioGroup rgSex;
    private RadioButton rbSex;
    private Button mbtCreate, mbtCountry, mbtShowBMI;
    private ImageButton mbtCamera;
    private ImageView mProfilePic;
    private TextView tv_showBMI;

    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //Define a bitmap
    Bitmap mThumbnailImage;

    private double weight, height;

    public ProfileCreateFragment() {
        // Required empty public constructor
    }

    public interface OnDataPass{
        public void onDataPass(String[] data);
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

        mbtShowBMI = view.findViewById(R.id.button_showBMI);
        mbtShowBMI.setOnClickListener(this);

        tv_showBMI = (TextView) view.findViewById(R.id.tv_showBMI);

        return view;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_camera:{
//                TODO: Take Pic intent:
                //The button press should open a camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }

            case R.id.button_country:{
                showCountry();
                break;
            }

            case R.id.button_showBMI:{
                if(validateBMIInput()){
                    showBMI();
                }
                break;
            }
            case R.id.button_create_profile:{
                validateInput();
                break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap thumbnailImage = (Bitmap) extras.get("data");
            mProfilePic.setImageBitmap(thumbnailImage);
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


    private boolean validateBMIInput(){
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

    private void showBMI(){
        double bmi = 703 * weight/(height * height);
        tv_showBMI.setText("" + new DecimalFormat("#.##").format(bmi));
    }

    private boolean validateInput(){
        mUserName = etUserName.getText().toString();
        mHeight = etHeight.getText().toString();
        mWeight = etWeight.getText().toString();
        mAge = etAge.getText().toString();
        mCity = etCity.getText().toString();

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

        return true;
    }

}
