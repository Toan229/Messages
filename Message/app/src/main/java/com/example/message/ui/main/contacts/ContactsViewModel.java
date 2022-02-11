package com.example.message.ui.main.contacts;

import com.example.message.data.model.Contact;
import com.example.message.ui.base.BaseViewModel;
import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends BaseViewModel {

    public ContactsViewModel(){}

    public List<Contact> filter(String text, List<Contact> contacts){
        if(text.toLowerCase().length() > 0){
            List<Contact> contactList = new ArrayList<>();
            text = text.toLowerCase();
            for(Contact item: contacts){
                if (item.getName().toLowerCase().contains(text) || item.getPhoneNumber().contains(text)) {
                    contactList.add(item);
                }
            }
            return contactList;
        }else {
            return contacts;
        }
    }
}
