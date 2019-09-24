package com.example.gongtia.lifestyle;

public interface Profile {
    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void showCountry();

    public void storeUserProfile();

    public void dispatchTakePictureIntent();

    public boolean validateInput();
}
