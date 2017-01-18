package cn.ucai.fulicenter.model.net;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/1/18.
 */

public class SharedPreferenceUtils {
    private static final String SHARED_PREFERENCE_USER = "cn.ucai.fulicenter.user";
    private static final String SHARED_PREFERENCE_USER_USERNAME = "cn.ucai.fulicenter.user_username";

    private static SharedPreferences preferences;
    private static SharedPreferenceUtils instance;

    public SharedPreferenceUtils(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCE_USER, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtils(context);
        }
        return instance;
    }

    //保存用户名
    public void saveUser(String username) {
        preferences.edit().putString(SHARED_PREFERENCE_USER_USERNAME, username).commit();
    }

    public String getUser() {
        return preferences.getString(SHARED_PREFERENCE_USER_USERNAME, null);
    }
}
