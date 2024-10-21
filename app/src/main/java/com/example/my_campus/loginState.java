package com.example.my_campus;

import android.content.Context;
import android.content.SharedPreferences;

public class loginState {
    private static final String PREF_NAME = "MyAppPrefs";  // Name of the SharedPreferences file
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // Key for the login state

    // Save the login state (true for logged in, false for logged out)
    public static void setLoginState(Context context, boolean isLoggedIn, String mobileNumber, String email, String name, String branch, String year, String rollNo, String role) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putString("mobileNumber", mobileNumber);
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("branch", branch);
        editor.putString("year", year);
        editor.putString("rollNo", rollNo);
        editor.putString("role", role);
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
        return sharedPref.getString("mobileNumber", null); //Null if Mobile number not available
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("email", null); //Null if Mobile number not available
    }
    public static String getUserName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("name", null); //Null if Mobile number not available
    }
    public static String getUserBranch(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("branch", null); //Null if Mobile number not available
    }
    public static String getUserYear(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("year", null); //Null if Mobile number not available
    }
    public static String getUserRollNo(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("rollNo", null); //Null if Mobile number not available
    }
    public static String getUserRole(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString("role", null); //Null if Mobile number not available
    }

}
