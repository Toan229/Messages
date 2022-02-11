package com.example.message.data.repository;

import com.example.message.R;
import java.util.LinkedHashMap;
import java.util.Map;

public class FontRepository {
    public Map<Integer, String> getFonts(){
        Map<Integer, String> fonts = new LinkedHashMap<>();
        fonts.put(R.font.sf_pro_text_semibold, "SF Pro Text (Default)");
        fonts.put(R.font.rubik_regular, "Rubik");
        fonts.put(R.font.lato_regular, "Lato");
        fonts.put(R.font.raleway_regular, "Raleway");
        fonts.put(R.font.noto_sans_regular, "Noto sans");
        fonts.put(R.font.sf_pro_text_regular, "SF Pro Text");
        return fonts;
    }
}
