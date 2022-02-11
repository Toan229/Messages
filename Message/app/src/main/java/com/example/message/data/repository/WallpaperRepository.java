package com.example.message.data.repository;

import com.example.message.R;
import java.util.Arrays;
import java.util.List;

public class WallpaperRepository {
    public List<Integer> getImagesWallpaper(){
        return Arrays.asList(
                R.drawable.background1,
                R.drawable.background2,
                R.drawable.background3,
                R.drawable.background4,
                R.drawable.background5,
                R.drawable.background6
        );
    }

    public List<Integer> getColorsWallpaper(){
        return Arrays.asList(
                R.color.white,
                R.color.black,
                R.color.C_EB5757,
                R.color.C_F2994A,
                R.color.C_F2C94C,
                R.color.C_219653,
                R.color.C_27AE60,
                R.color.C_6FCF97,
                R.color.C_007AFF,
                R.color.C_56CCF2,
                R.color.C_9B51E0,
                R.color.C_2D9CDB
        );
    }
}
