package com.example.gongtia.lifestyle.ViewModel;

import android.app.Application;

import com.example.gongtia.lifestyle.model.User;
import com.example.gongtia.lifestyle.repository.GoalRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GoalViewModel extends AndroidViewModel {

    private MutableLiveData<User> mUser;
    private GoalRepository mRepo;

    public GoalViewModel(@NonNull Application application){
        super(application);
        mRepo = new GoalRepository(application);
        mUser = mRepo.getUser();
    }

    public LiveData<User> getUser(){
        return mUser;
    }

}
