package com.example.message.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.data.repository.ContactsRepository;
import com.example.message.data.repository.ImageRepository;
import com.example.message.data.repository.SettingRepository;
import com.example.message.data.repository.SmsRepository;
import com.example.message.ui.base.BaseViewModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class MainViewModel extends BaseViewModel {
    public LiveData<List<Sms>> smsList;
    public MutableLiveData<List<Sms>> smsListByContact = new MutableLiveData<>();
    public MutableLiveData<Contact> contact = new MutableLiveData<>();
    public MutableLiveData<List<Contact>> contactList1 = new MutableLiveData<>();
    public LiveData<List<Contact>> contactList;
    public MutableLiveData<Boolean> popBackNav = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFinnishLoadData = new MutableLiveData<>();

    public MutableLiveData<Boolean> isBackgroundAColor = new MutableLiveData<>();
    public MutableLiveData<Integer> background = new MutableLiveData<>();
    public MutableLiveData<Integer> font = new MutableLiveData<>();
    public MutableLiveData<Bitmap> userImage = new MutableLiveData<>();

    SmsRepository smsRepository;
    ContactsRepository contactsRepository;
    SettingRepository settingRepository;
    ImageRepository imageRepository;

    @Inject
    public MainViewModel() {
        settingRepository = new SettingRepository();
        smsRepository = new SmsRepository();
        isBackgroundAColor.postValue(true);
        isFinnishLoadData.postValue(false);
        contactsRepository = new ContactsRepository();
        imageRepository = new ImageRepository();
    }

    public List<Sms> addNameToSms(List<Sms> smsList, List<Contact> contacts){
        if(contacts != null && smsList != null){
            Map<String, String> contactsMap = new HashMap<>();
            for(Contact contact : contacts){
                contactsMap.put(contact.getPhoneNumber(), contact.getName() + "-" + contact.getImage());
            }
            for(Sms sms : smsList){
                if(contactsMap.containsKey(sms.getAddress())){
                    String[] data = contactsMap.get(sms.getAddress()).split("-");
                    if(sms.getName() == null || sms.getName().isEmpty() || sms.getName().equals(sms.getAddress())){
                        sms.setName(data[0]);
                    }
                    if(sms.getAddress() == null || sms.getAddress().length() == 0){
                        sms.setAddress("Unknown");
                    }
                    sms.setImage(data[1]);
                }
            }
        }
        return smsList;
    }

    public void fetchSms(@NonNull Context context){
        Boolean isFirstLoadSms = smsRepository.isFirstLoadSms(context);
        if(isFirstLoadSms){
            smsRepository.fetchSms(context).subscribe(new SingleObserver<List<Sms>>() {
                @Override
                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Sms> smsList2) {
                    saveSmsList(smsList2);
                    smsRepository.saveSmsPreference(false, context);
                }

                @Override
                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                    Toast.makeText(context, "Can't read data from device.\n Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
        smsRepository.getAllSmsFromApp().subscribe(new SingleObserver<LiveData<List<Sms>>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull LiveData<List<Sms>> smsList2) {
                smsList = smsList2;
                if(!popBackNav.getValue()){
                    popBackNav.postValue(true);
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Toast.makeText(context, "Can't read data from app\n Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getContactsFromDevice(Context context){
        contactsRepository.fetchContacts(context).subscribe(new SingleObserver<List<Contact>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Contact> contactList2) {
                List<Contact> removeList = contactList1.getValue();
                contactList1.postValue(contactList2);
                if(removeList != null && removeList.size() > 0){
                    contactList2.removeAll(removeList);
                }
                if(contactList2.size() > 0){
                    saveContacts(contactList2);
                }
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Toast.makeText(context, "Can't read contacts from device.\n"+ e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getContactsFromApp(Context context){
        contactsRepository.getContactsFromApp().subscribe(new SingleObserver<LiveData<List<Contact>>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull LiveData<List<Contact>> contacts) {
                contactList = contacts;
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Toast.makeText(context, "Can't read contacts from device.\n"+ e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void saveSmsList(List<Sms> smsList){
        smsRepository.saveAllSmsToDB(smsList).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() { }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) { }
        });
    }

    public void saveContacts(List<Contact> contactList){
        contactsRepository.saveContactsToDB(contactList)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() { }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) { }
                });
    }

    public void sendSms(Sms sms){
        List<Sms> smsList1;
        if(smsListByContact.getValue() == null){
            smsList1 = new ArrayList<>();
        }else {
            smsList1 = smsListByContact.getValue();
        }

        List<Sms> smsList2;
        if(smsList.getValue() == null){
            smsList2 = new ArrayList<>();
        }else {
            smsList2 = smsList.getValue();
        }
        smsList1.add(sms);
        smsList2.add(sms);
        smsListByContact.postValue(smsList1);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms.getAddress(), null, sms.getMessage(), null, null);
        smsRepository.saveAMessage(sms).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {}

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {}
        });
    }

    public List<Sms> messageFilter(String address){
        List<Sms> smsList1;
        if(smsList.getValue() != null){
            smsList1 = new ArrayList<>(smsList.getValue());
        }else {
            smsList1 = new ArrayList<>();
        }
        int size = smsList1.size();
        for (int i = size - 1; i > -1; i--) {
            if (!smsList1.get(i).getAddress().equals(address)) {
                smsList1.remove(i);
            }
        }
//        Collections.sort(smsList1, (sms1, sms2) -> Long.compare(sms1.getTime_milliseconds(), sms2.getTime_milliseconds()));
        return smsList1;
    }

    public void loadSetting(Context context){
        settingRepository.setContext(context);
        Boolean isBackgroundAColor = settingRepository.isBackgroundAColor();
        this.isBackgroundAColor.postValue(isBackgroundAColor);
        int backgroundId;
        if(isBackgroundAColor){
            backgroundId = settingRepository.getBackgroundColor();
        }else {
            backgroundId = settingRepository.getBackgroundImage();
        }
        if(backgroundId != -1){
            background.postValue(backgroundId);
        }

        int fontID = settingRepository.getFontSetting();
        if(fontID != - 1){
            font.postValue(fontID);
        }
    }

    public void saveWallpaperSetting(Boolean isBackgroundAColor, int backgroundId){
        settingRepository.setBackground(isBackgroundAColor);
        if(isBackgroundAColor){
            settingRepository.saveBackgroundColor(backgroundId);
        }else {
            settingRepository.saveBackgroundImage(backgroundId);
        }
    }

    public void saveFontSetting(int fontId){
        settingRepository.saveFontSetting(fontId);
    }

    public void getUserImage(Context context, String image){
        imageRepository.getImageFromApp(context, image).subscribe(new SingleObserver<File>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull File file) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                userImage.postValue(bitmap);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });
    }

    public void updateSmsList(List<Sms> smsList){
        smsRepository.updateSmsList(smsList).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() { }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) { }
        });
    }
}