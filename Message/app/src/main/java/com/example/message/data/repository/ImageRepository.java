package com.example.message.data.repository;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.activity.result.ActivityResultLauncher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImageRepository {
    public void saveContactImage(Bitmap bitmap, String name, Context context) throws IOException{
        ContextWrapper wrapper = new ContextWrapper(context);
        File directory = wrapper.getDir("contacts image", Context.MODE_PRIVATE);
        File file = new File(directory, name);
        if(!file.exists()){
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }

    public void getImageFromDevice(ActivityResultLauncher<Intent> launcher){
        String type = "image/*";
        Intent intent = new Intent();
        intent.setType(type);
        intent.setAction(Intent.ACTION_PICK);
        launcher.launch(intent);
    }

    public Single<File> getImageFromApp(Context context, String name){
        return Single.fromCallable(() -> {
            ContextWrapper wrapper = new ContextWrapper(context);
            File directory = wrapper.getDir("contacts image", Context.MODE_PRIVATE);
            return new File(directory, name);
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
