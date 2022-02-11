package com.example.message.ui.main.mesages;

import com.example.message.data.model.Sms;
import com.example.message.ui.base.BaseViewModel;
import java.util.ArrayList;
import java.util.List;

public class MessagesViewModel extends BaseViewModel {

    public MessagesViewModel(){ }

    public List<Sms> filter(String text, List<Sms> smsList){
        if(text.toLowerCase().length() > 0){
            List<Sms> result = new ArrayList<>();
            text = text.toLowerCase();
            for(Sms item: smsList){
                if (item.getName().toLowerCase().contains(text) || item.getAddress().toLowerCase().contains(text)) {
                    result.add(item);
                }
            }
            return result;
        }else {
            return smsList;
        }
    }

}
