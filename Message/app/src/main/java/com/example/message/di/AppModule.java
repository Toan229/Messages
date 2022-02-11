package com.example.message.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.example.message.common.Constant;
import com.example.message.data.local.db.ContactDao;
import com.example.message.data.local.db.MessageDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference(Application context) {
        return PreferenceManager.getDefaultSharedPreferences(context);

    }

    @Provides
    @Singleton
    public MessageDB provideRoomDb(Application context) {
        return Room.databaseBuilder(context, MessageDB.class, Constant.DB_NAME).fallbackToDestructiveMigration().addMigrations(MessageDB.MIGRATION_1_2).build();
    }

    @Provides
    @Singleton
    public ContactDao provideMessageThreadDao(MessageDB db) {
        return db.contactDao();
    }
}
