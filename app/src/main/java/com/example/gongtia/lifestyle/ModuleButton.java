package com.example.gongtia.lifestyle;

import android.graphics.drawable.Drawable;

public class ModuleButton {
    private Drawable image;
    private String text;

    public ModuleButton(Drawable image, String text){
        this.image = image;
        this.text = text;
    }

    public Drawable getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
