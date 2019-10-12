package com.example.gongtia.lifestyle.ViewModel;

import com.example.gongtia.lifestyle.model.User;
import com.example.gongtia.lifestyle.repository.GoalRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoalViewModel extends ViewModel {

    private MutableLiveData<User> mUser;
    private GoalRepository mRepo;

    public GoalViewModel(){
    }

    public void init(){
        if(mUser != null){
            return;
        }
        mRepo = GoalRepository.getInstance();
        mUser = mRepo.getUser();
    }

    public LiveData<User> getUser(){
        return mUser;
    }


    public int calcCurrentCalories(){
        double BMR = 0;
        int currentCalories = 0;
        User user = mUser.getValue();
        if(user.getSex().equals("Female")){
            BMR = 655 + (4.3 * user.getWeight()) + (4.7 * user.getHeight()) - (4.7 * user.getAge());
        }else{
            BMR = 66 + (6.3 * user.getWeight()) + (12.9 * user.getHeight()) - (6.8 * user.getAge());
        }

        if(user.getLifeStyle().equals("Active")){
            currentCalories = (int)(BMR * 1.75);
        }else{
            currentCalories = (int)(BMR * 1.2);
        }
        return currentCalories;
    }

    public int calcNewCalories(){
        double goalWeight = 0;
        int newCalories = 0;

        User user = mUser.getValue();
        double pounds = Double.parseDouble(user.getLbs());

        if(user.getGoal().equals("Maintain")) {
            return calcCurrentCalories();
        } else if(user.getGoal().equals("Lose")){
            goalWeight = user.getWeight() - pounds;
        }else{
            goalWeight = user.getWeight() + pounds;
        }
        double BMR = 0;
        if(user.getSex().equals("Female")){
            BMR = 655 + (4.3 * goalWeight) + (4.7 * user.getHeight()) - (4.7 * user.getAge());
        }else{
            BMR = 66 + (6.3 * goalWeight) + (12.9 * user.getHeight()) - (6.8 * user.getAge());
        }

        if(user.getLifeStyle().equals("Active")){
            newCalories = (int) (BMR * 1.75);
        }else{
            newCalories = (int) (BMR * 1.2);
        }
        return newCalories;
    }

    public int calcBMI(){
        User user = mUser.getValue();
        return (int) (703 * user.getWeight() / (user.getHeight() * user.getHeight()));
    }
}
