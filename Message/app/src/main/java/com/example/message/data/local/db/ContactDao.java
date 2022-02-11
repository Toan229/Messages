package com.example.message.data.local.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.message.data.model.Contact;
import java.util.List;
import kotlin.jvm.JvmSuppressWildcards;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact ORDER BY name ASC")
    LiveData<List<Contact>> getAllContact();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    void insertAllContacts(List<Contact> contacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact contact);

    @Update
    void updateContact(Contact contact);

    @Query("DELETE FROM contact")
    void deleteAllContact();
}
