package com.example.message.data.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.message.App;
import com.example.message.common.Constant;
import com.example.message.data.local.db.MessageDB;
import com.example.message.data.model.Sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SmsRepository {
    public SmsRepository(){}

    public List<Sms> getSmsFromDevice(Context mContext){
        List<Sms> smsList = new ArrayList<>();
        Uri uriMessage = Uri.parse(Constant.MESSAGE_CONTENT);
        ContentResolver contentResolver = App.getInstance().getContentResolver();
        Cursor cursor = contentResolver.query(uriMessage, null, null, null, null);
        if(cursor.moveToFirst()){
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.HOUR_PATTERN, Locale.US);
            if(cursor.getCount() > 0){
                int idColumnIndex = cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.THREAD_ID);
                int addressColumnIndex = cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.ADDRESS);
                int bodyColumnIndex = cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY);
                int readColumnIndex = cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.READ);
                int dateColumnIndex = cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.DATE);
                for(int i = 0; i < cursor.getCount(); i++){
                    Sms objSms = new Sms();
                    objSms.setName("");
                    objSms.setId(Integer.parseInt(cursor.getString(idColumnIndex)));
                    String phone = cursor.getString(addressColumnIndex);
                    if(phone.length() > 4){
                        if(phone.charAt(0) == '8' && phone.charAt(1) == '4') {
                            phone = phone.substring(2);
                            phone = "0" + phone;
                        }else if(phone.charAt(0) == '+' && phone.charAt(1) == '8' && phone.charAt(2) == '4'){
                            phone = phone.substring(3);
                            phone = "0" + phone;
                        }else if(phone.charAt(0) == '0' && phone.charAt(1) == '0' && phone.charAt(2) == '8' && phone.charAt(3) == '4'){
                            phone = phone.substring(4);
                            phone = "0" + phone;
                        }
                    }
                    objSms.setAddress(phone);
                    objSms.setMessage(cursor.getString(bodyColumnIndex));
                    objSms.setReadState(cursor.getString(readColumnIndex));
                    String millisecond = cursor.getString(dateColumnIndex);
                    objSms.setTime_milliseconds(Long.parseLong(millisecond));
                    String dateTime = dateFormat.format(new Date(Long.parseLong(millisecond)));
                    objSms.setTime(dateTime);
                    if(cursor.getString(cursor.getColumnIndexOrThrow(Constant.DEFAULT_MESSAGE_TYPE_COLUMN)).contains("1")){
                        objSms.setFolder("inbox");
                    }else {
                        objSms.setFolder("sent");
                    }
                    smsList.add(objSms);
                    cursor.moveToNext();
                }
            }
        }
        Log.d("toan", smsList.toString());
        cursor.close();
        return smsList;
    }

    public Single<List<Sms>> fetchSms(Context context){
        return  Single.fromCallable(() -> getSmsFromDevice(context)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LiveData<List<Sms>>> getAllSmsFromApp(){
        return Single.fromCallable(() -> MessageDB.MESSAGE_DB.smsDao().getAllMessage()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable saveAMessage(Sms sms){
        return Completable.create(emitter -> MessageDB.MESSAGE_DB.smsDao().insertSms(sms))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable saveAllSmsToDB(List<Sms> smsList){
        return Completable.create(emitter -> MessageDB.MESSAGE_DB.smsDao().insertAllMessage(smsList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Boolean isFirstLoadSms(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.IS_FIRST_LOAD_SMS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constant.IS_FIRST_LOAD_SMS, true);
    }

    public void saveSmsPreference(Boolean isFirstLoad, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.IS_FIRST_LOAD_SMS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.IS_FIRST_LOAD_SMS, isFirstLoad);
        editor.apply();
    }

    public Completable updateSmsList(List<Sms> smsList){
        return Completable.create(emitter -> {
            if(!emitter.isDisposed()){
                MessageDB.MESSAGE_DB.smsDao().updateAllSms(smsList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
