package com.example.message.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.message.common.Constant;

public class SettingRepository {
    private SharedPreferences sharedPreferences;

    public void setContext(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SettingRepository() {
    }

    public void setBackground(boolean isBackgroundAColor){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.IS_BACKGROUND_A_COLOR, isBackgroundAColor);
        editor.apply();
    }

   public Boolean isBackgroundAColor(){
        return sharedPreferences.getBoolean(Constant.IS_BACKGROUND_A_COLOR, false);
   }

   public int getBackgroundColor(){
        return sharedPreferences.getInt(Constant.BACKGROUND_COLOR, -1);
   }

   public int getBackgroundImage(){
       return sharedPreferences.getInt(Constant.BACKGROUND_IMAGE, -1);
   }

   public int getFontSetting(){
        return sharedPreferences.getInt(Constant.FONT, -1);
   }

   public void saveBackgroundImage(int backgroundId){
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt(Constant.BACKGROUND_IMAGE, backgroundId);
       editor.apply();
   }

   public void saveBackgroundColor(int colorId){
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt(Constant.BACKGROUND_COLOR, colorId);
       editor.apply();
   }

   public void saveFontSetting(int fontId){
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putInt(Constant.FONT, fontId);
       editor.apply();
   }
}
