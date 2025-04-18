package com.example.my_campus;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginState {
    private static final String PREF_NAME = "MyAppPrefs";  // Name of the SharedPreferences file
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // Key for the login state
    static String [] Role = new String[1];

    // Save the login state (true for logged in, false for logged out)
    public static void setLoginState(Context context, boolean isLoggedIn, String mobileNumber, String email, String name, String branch, String year, String rollNo) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putString("mobileNumber", mobileNumber);
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("branch", branch);
        editor.putString("year", year);
        editor.putString("rollNo", rollNo);
        editor.apply(); // Save the changes asynchronously
    }
    public static void setLoginState(Context context, boolean isLoggedIn) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply(); // Save the changes asynchronously
    }



    // Retrieve the login state (default is false if not found)
    public static boolean getLoginState(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false); // Default value is false (logged out)
    }
    public static String getUserMobileNumber(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("mobileNumber", "No such data");
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("email", "No such data"); //Null if Mobile number not available
    }
    public static String getUserName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("name", "No such data"); //Null if Mobile number not available
    }
    public static String getUserBranch(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("branch", "No such data"); //Null if Mobile number not available
    }
    public static String getUserYear(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("year", "No such data"); //Null if Mobile number not available
    }
    public static String getUserRollNo(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("rollNo", "No such data"); //Null if Mobile number not available
    }

    public static void setProfileImageUrl(Context context, String url){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("profileImageUrl", url);
        editor.apply();
    }

    public static String getProfileImageUrl(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("profileImageUrl", "No such data");
    }

    public static void setUserRole(Context context, String role){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userRole", role);
        editor.apply();
    }

    public static String getUserRole(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("userRole", "No such data");
    }

    public static void setDeviceToken(Context context, String token){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("deviceToken", token);
        editor.apply();
    }

    public static String getDeviceToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("deviceToken", "No such data");
    }

}
