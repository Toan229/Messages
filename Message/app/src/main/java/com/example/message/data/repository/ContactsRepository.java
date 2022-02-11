package com.example.message.data.repository;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.example.message.App;
import com.example.message.common.Constant;
import com.example.message.data.local.db.ContactDao;
import com.example.message.data.local.db.MessageDB;
import com.example.message.data.model.Contact;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactsRepository {

    public ContactsRepository() { }

    public Single<List<Contact>> fetchContacts(Context context) {
        return Single.fromCallable(() -> getContactsFromDevice(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<Contact> getContactsFromDevice(Context context){
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor != null){
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    int index1 = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
                    String id = cursor.getString(index1);
                    int index2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String name = cursor.getString(index2);
                    int index3 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phone = cursor.getString(index3);
                    phone = phone.replaceAll(Constant.REGEX_IS_NOT_NUMBER,"");
                    Uri imageUri = getPhotoUri(context, id);
                    String image;
                    if(imageUri != null){
                        image = imageUri.toString();
                    }else image = Constant.DEFAULT_CONTACT_IMAGE;
                    contacts.add(new Contact(id, name, phone, image));
                }
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return contacts;
    }

    public Uri getPhotoUri(Context context, String id) {
        try {
            @SuppressLint("Recycle") Cursor cur = context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.RAW_CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                .parseLong(id));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

    public Completable saveContactsToDB(List<Contact> contactList){
        ContactDao contactDao = MessageDB.MESSAGE_DB.contactDao();
        return Completable.create(emitter -> {
            if(!emitter.isDisposed()){
                contactDao.deleteAllContact();
                contactDao.insertAllContacts(contactList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateContact(Contact contact){
        ContactDao contactDao = MessageDB.MESSAGE_DB.contactDao();
        return Completable.create(emitter -> {
            if(!emitter.isDisposed()){
                contactDao.updateContact(contact);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable modifyContactsInDevice(Contact contact, Context context, boolean isNew){
        return Completable.create(emitter -> {
            if(!emitter.isDisposed()){
                updateName(contact.getId(), contact.getName());
                updatePhone(contact.getId(), contact.getPhoneNumber());
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(contact.getImage()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateContactPhoto(bitmap, contact, context, isNew);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public void updateName(String id, String name) {
        Log.d("toan", "name" + name);
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(id),
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "")
                .build());
        try {
            App.getInstance().getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePhone(String id, String phone){
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(id),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .build());

        try {
            App.getInstance().getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateContactPhoto(Bitmap mBitmap, Contact contact, Context context, boolean isNew){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        if(mBitmap!=null){
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ContentValues contentValues = new ContentValues();
            ContentResolver contentResolver = context.getContentResolver();
            int photoRow = -1;
            String where = ContactsContract.Data.RAW_CONTACT_ID + "=" + contact.getId() + " AND " +
                    ContactsContract.Data.MIMETYPE + "=='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
        }
    }

    public Single<LiveData<List<Contact>>> getContactsFromApp(){
        return Single.fromCallable(() -> MessageDB.MESSAGE_DB.contactDao().getAllContact()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void updateContactPhoto(Bitmap bitmap, String id,Context context){
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        if (bitmap != null) {
            try {
                ByteArrayOutputStream image = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);

                Uri rawContactUri = null;
                Cursor rawContactCursor = context.getContentResolver().query(
                        ContactsContract.RawContacts.CONTENT_URI,
                        new String[]{ContactsContract.RawContacts._ID},
                        ContactsContract.RawContacts._ID + "=" + id,
                        null,
                        null
                );
                if (!rawContactCursor.isAfterLast()) {
                    rawContactCursor.moveToFirst();
                    rawContactUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendPath("" + rawContactCursor.getLong(0)).build();
                }
                rawContactCursor.close();

                ContentValues values = new ContentValues();
                int photoRow = -1;
                String where111 = ContactsContract.Data.RAW_CONTACT_ID + " == " +
                        ContentUris.parseId(rawContactUri) + " AND " + ContactsContract.Data.MIMETYPE + "=='" +
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
                Cursor cursor = context.getContentResolver().query(
                        ContactsContract.RawContacts.CONTENT_URI,
                        null,
                        where111,
                        null,
                        null
                );
                int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
                if (cursor.moveToFirst()) {
                    photoRow = cursor.getInt(idIdx);
                }
                cursor.close();

                values.put(ContactsContract.Data.RAW_CONTACT_ID,
                        ContentUris.parseId(rawContactUri));
                values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
                values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, image.toByteArray());
                values.put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                if (photoRow >= 0) {
                    context.getContentResolver().update(
                            ContactsContract.Data.CONTENT_URI,
                            values,
                            ContactsContract.Data._ID + " = " + photoRow, null);
                } else {
                    context.getContentResolver().insert(
                            ContactsContract.Data.CONTENT_URI,
                            values);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveContactPreference(Boolean isFirstLoad, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.IS_FIRST_LOAD_CONTACT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.IS_FIRST_LOAD_CONTACT, isFirstLoad);
        editor.apply();
    }

    public Boolean isFirstLoadContact(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.IS_FIRST_LOAD_CONTACT, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constant.IS_FIRST_LOAD_CONTACT, true);
    }
}
