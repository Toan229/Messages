package com.example.message.di;

import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;

public interface OnItemClick {
    void onColorButtonClick(int colorId);
    void onContactItemClick(Contact contact);
    void onMessageItemClick(Sms sms);
    void onWallpaperButtonClick(int wallpaper);
    void onFontItemClick(int fontId);
}
