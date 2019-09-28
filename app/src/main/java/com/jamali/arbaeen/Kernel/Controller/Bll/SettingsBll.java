package com.jamali.arbaeen.Kernel.Controller.Bll;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsBll {

    private final Context context;
    private final SharedPreferences preferences;
    private final String DEFAULT_URL = "http://172.30.1.43/cp";
    //private final String DEFAULT_URL = "https://bina.epedc.ir/cp";
    private final String DEFAULT_PORT = "8084";
    private final String DEFAULT_PORT2 = "8081";

    public SettingsBll(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    public String getUrlAddress() {
        return preferences.getString("Api", DEFAULT_URL);
    }

    public void setUrlAddress(String address) {
        preferences.edit().putString("Api", address).apply();
    }

    public String getPort() {
        return preferences.getString("Port", DEFAULT_PORT);
    }

    public void setPort(String port) {
        preferences.edit().putString("Port", port).apply();
    }

    public String getPort2() {
        return preferences.getString("Port2", DEFAULT_PORT2);
    }

    public void setPort2(String port) {
        preferences.edit().putString("Port2", port).apply();
    }

    public void setMode(boolean isOnline) {
        if (isOnline) {
            preferences.edit().putBoolean("Mode", true).apply();
        } else preferences.edit().putBoolean("Mode", false).apply();
    }

    public boolean isOnline() {
        return preferences.getBoolean("Mode", true);
    }

    public String getTicket() {
        return preferences.getString("Token", null);
    }

    public void setTicket(String token) {
        preferences.edit().putString("Token", token).apply();
    }

    public void logout() {
        preferences.edit().putString("Token", null).apply();
    }

    public String getUserPostId() {
        return preferences.getString("UserPostId", null);
    }

    public void setUserPostId(String token) {
        preferences.edit().putString("UserPostId", token).apply();
    }
}
