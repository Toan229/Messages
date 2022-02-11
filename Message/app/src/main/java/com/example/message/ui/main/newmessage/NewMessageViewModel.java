package com.example.message.ui.main.newmessage;

import com.example.message.common.Constant;
import com.example.message.data.model.Contact;
import com.example.message.ui.base.BaseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.text.Regex;

@HiltViewModel
public class NewMessageViewModel extends BaseViewModel {
    @Inject
    public NewMessageViewModel() { }


    public List<Contact> filter(String text, List<Contact> contacts){
        if(text.toLowerCase().length() > 0){
            List<Contact> contactList = new ArrayList<>();
            text = text.toLowerCase();
            for(Contact item: contacts){
                if (item.getName().toLowerCase().contains(text) || item.getPhoneNumber().contains(text)) {
                    contactList.add(item);
                }
            }
            if(contactList.size() == 0 && !text.matches(Constant.REGEX_IS_NOT_NUMBER)){
                contactList.add(new Contact(text, text, text, Constant.DEFAULT_CONTACT_IMAGE));
            }
            return contactList;
        }
        else{
            return new ArrayList<>();
        }
    }
}
