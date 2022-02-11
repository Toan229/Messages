package com.example.message.data.local.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.message.common.Constant;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;

@Database(entities = {Contact.class, Sms.class}, version = Constant.DB_VERSION, exportSchema = false)
public abstract class MessageDB extends RoomDatabase {

    public abstract ContactDao contactDao();

    public abstract SmsDao smsDao();

    public static MessageDB MESSAGE_DB = null;

    public static Migration MIGRATION_1_2 = new Migration(1, 2){

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static MessageDB getAppDatabase(Context context){
        if(MESSAGE_DB == null){
            synchronized (MessageDB.class){
                MessageDB instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        MessageDB.class,
                        Constant.DB_NAME
                ).addMigrations(MIGRATION_1_2)
                        .build();
                MESSAGE_DB = instance;
            }
        }
        return MESSAGE_DB;
    }

}


