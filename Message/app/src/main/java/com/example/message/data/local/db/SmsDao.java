package com.example.message.data.local.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.message.data.model.Sms;
import java.util.List;
import kotlin.jvm.JvmSuppressWildcards;

@Dao
public interface SmsDao {

    @Query("SELECT * FROM sms ORDER BY timeMilliseconds DESC")
    LiveData<List<Sms>> getAllMessage();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    void insertAllMessage(List<Sms> smsList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSms(Sms sms);

    @Update
    void updateAllSms(List<Sms> smsList);
}
