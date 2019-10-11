package com.example.gongtia.lifestyle;

import org.json.JSONException;

public interface Profile {
    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void showCountry();

    public void storeUserProfile() throws JSONException;

    public void dispatchTakePictureIntent();

    public boolean validateInput();
}
