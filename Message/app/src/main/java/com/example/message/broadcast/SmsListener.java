package com.example.message.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.example.message.common.Constant;
import com.example.message.data.local.db.MessageDB;
import com.example.message.data.model.Sms;
import com.example.message.ui.main.MainViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SmsListener extends BroadcastReceiver {

    private Context context;
    private List<Sms> smsList;
    private MainViewModel mainViewModel;

    public SmsListener() { }

    public List<Sms> getSmsList() {
        return smsList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constant.ACTION_RECEIVE_SMS)){
            mainViewModel = new MainViewModel();
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.HOUR_PATTERN, Locale.US);
            for(SmsMessage sms : Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                Sms sms1 = new Sms();
                String phone = sms.getDisplayOriginatingAddress();
                if(phone.length() > 4){
                    if(phone.charAt(0) == '8' && phone.charAt(1) == '4') {
                        phone = phone.substring(2);
                        phone = "0" + phone;
                    }else if(phone.charAt(0) == '+' && phone.charAt(1) == '8' && phone.charAt(2) == '4'){
                        phone = phone.substring(3);
                        phone = "0" + phone;
                    }else if(phone.charAt(0) == '0' && phone.charAt(1) == '0' && phone.charAt(2) == '8' && phone.charAt(3) == '4'){
                        phone = phone.substring(4);
                        phone = "0" + phone;
                    }
                }
                sms1.setAddress(phone);
                sms1.setMessage(sms.getMessageBody());
                sms1.setFolder(Constant.INBOX_SMS);
                sms1.setReadState(Constant.UNREAD_STATE);
                sms1.setName(sms1.getAddress());
                sms1.setTime_milliseconds(sms.getTimestampMillis());
                String time = dateFormat.format(new Date(sms1.getTime_milliseconds()));
                sms1.setTime(time);
                smsList = new ArrayList<>();
                smsList.add(sms1);
                if(MessageDB.MESSAGE_DB == null){
                    MessageDB.getAppDatabase(context);
                }
                mainViewModel.saveSmsList(smsList);
            }
        }
    }
}
