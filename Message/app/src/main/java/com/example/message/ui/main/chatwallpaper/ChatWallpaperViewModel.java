package com.example.message.ui.main.chatwallpaper;

import com.example.message.data.repository.WallpaperRepository;
import com.example.message.ui.base.BaseViewModel;
import java.util.List;

public class ChatWallpaperViewModel extends BaseViewModel {
    private WallpaperRepository wallpaperRepository;

    public ChatWallpaperViewModel(){
        wallpaperRepository = new WallpaperRepository();
    }

    public List<Integer> getImagesWallpaper(){
        return wallpaperRepository.getImagesWallpaper();
    }

    public List<Integer> getColorsWallpaper(){
        return wallpaperRepository.getColorsWallpaper();
    }
}
