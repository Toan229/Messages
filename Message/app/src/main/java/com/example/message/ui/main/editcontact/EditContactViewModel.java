package com.example.message.ui.main.editcontact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import com.example.message.R;
import com.example.message.data.model.Contact;
import com.example.message.data.repository.ContactsRepository;
import com.example.message.data.repository.ImageRepository;
import com.example.message.ui.base.BaseViewModel;
import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class EditContactViewModel extends BaseViewModel {

    private final ContactsRepository contactsRepository;
    private final ImageRepository imageRepository;

    public EditContactViewModel() {
        contactsRepository = new ContactsRepository();
        imageRepository = new ImageRepository();
    }

    public void updateContact(Contact contact, Context context, boolean isNew){

        contactsRepository.modifyContactsInDevice(contact, context, isNew).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() { }

            @Override
            public void onError(@NonNull Throwable e) { }
        });
//        updateContactInDB(contact);
    }

    public void updateContactInDB(Contact contact){
        contactsRepository.updateContact(contact).subscribe(new CompletableObserver() {
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

    public void getImageFromLibrary(ActivityResultLauncher<Intent> launcher){
        imageRepository.getImageFromDevice(launcher);
    }

    public void saveContactImage(Bitmap bitmap, String name, Context context){
        try{
            imageRepository.saveContactImage(bitmap, name, context);
        }catch (IOException ex){
            Toast.makeText(context, R.string.saveError, Toast.LENGTH_SHORT).show();
        }
    }

}
