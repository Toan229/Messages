package com.example.message.ui.main.editfont;

import com.example.message.data.repository.FontRepository;
import com.example.message.ui.base.BaseViewModel;
import java.util.Map;

public class EditFontViewModel extends BaseViewModel {
    FontRepository fontRepository;
    public EditFontViewModel(){
        fontRepository = new FontRepository();
    }

    public Map<Integer, String> getFonts(){
        return fontRepository.getFonts();
    }
}
