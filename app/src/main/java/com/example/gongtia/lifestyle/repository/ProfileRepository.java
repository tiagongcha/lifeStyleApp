package com.example.gongtia.lifestyle.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.gongtia.lifestyle.JSONProfileUtils;
import com.example.gongtia.lifestyle.Profile;
import com.example.gongtia.lifestyle.Room.ProfileDao;
import com.example.gongtia.lifestyle.Room.ProfileTable;
import com.example.gongtia.lifestyle.Room.WeatherDataEntity;
import com.example.gongtia.lifestyle.activity.LoginActivity;
import com.example.gongtia.lifestyle.model.User;

import org.json.JSONException;

import androidx.lifecycle.MutableLiveData;

public class ProfileRepository {
    private final MutableLiveData<User> jsonData =
            new MutableLiveData<User>();
//    primary key:
    private static String mUserName;
    private static String mJsonString;
    private ProfileDao mProfileDao;
    private String userJson;

    public ProfileRepository(Application application){
//        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
//        mProfileDao = db.profileDao();
        loadData();
    }

    public void setUser(String user, String userName){
        userJson = user;
        mUserName = userName;
        loadData();
    }

    public MutableLiveData<User> getData() {
        return jsonData;
    }

    private void loadData(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
//                TODO: FILL IN
                String user = strings[0];
                return user;
            }

            @Override
            protected void onPostExecute(String s) {
                if(s!=null) {
                    mJsonString = s;
                    insert();
                    try {
                        jsonData.setValue(JSONProfileUtils.getProfileData(s));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute(userJson);

    }

    private void insert(){
        ProfileTable profileTable = new ProfileTable(mUserName,mJsonString);
        new insertAsyncTask(mProfileDao).execute(profileTable);
    }

    private static class insertAsyncTask extends AsyncTask<ProfileTable,Void,Void>{
        private ProfileDao mAsyncTaskDao;

        insertAsyncTask(ProfileDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ProfileTable... profileTables) {
            mAsyncTaskDao.insert(profileTables[0]);
            return null;
        }
    }

    public static void saveDataToDB(User user){
        String userJson = null;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... Voids) {
                try {
                    String userJson = JSONProfileUtils.storeProfileJSON(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ProfileTable wde = new ProfileTable(mUserName, userJson);

                LoginActivity.db.profileDao().insert(wde);
                return null;
            }
        }.execute();
    }

}
