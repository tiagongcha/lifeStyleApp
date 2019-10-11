package com.example.gongtia.lifestyle;

import com.example.gongtia.lifestyle.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONProfileUtils {
    public static String storeProfileJSON(User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", user.getUserName());
        jsonObject.put("age", user.getAge()+"");
        jsonObject.put("sex", user.getSex());
        jsonObject.put("city", user.getCity());
        jsonObject.put("country", user.getCountry());
        jsonObject.put("weight", user.getWeight()+"");
        jsonObject.put("height", user.getHeight()+"");
        return jsonObject.toString();
    }

    public static User getProfileData(String data) throws JSONException {
        User user = new User();
        JSONObject jsonObject = new JSONObject(data);

        user.setUserName(jsonObject.getString("username"));
        user.setSex(jsonObject.getString("sex"));
        user.setAge(Integer.parseInt(jsonObject.getString("age")));
        user.setCity(jsonObject.getString("city"));
        user.setCountry(jsonObject.getString("country"));
        user.setWeight(Double.parseDouble(jsonObject.getString("weight")));
        user.setHeight(Double.parseDouble(jsonObject.getString("height")));

        return user;
    }


}
