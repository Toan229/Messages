package com.example.message.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.message.R;
import com.example.message.common.Constant;
import com.example.message.data.model.Sms;
import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<Sms> {

    private List<Sms> smsList;
    private TextView smsContent;
    private Typeface typeface;

    public List<Sms> getSmsList() {
        return smsList;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setSmsList(List<Sms> smsList) {
        this.smsList = smsList;
    }

    public ChatArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        smsList = new ArrayList<>();
    }

    @Override
    public void add(@Nullable Sms object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        if(smsList != null){
            return smsList.size();
        }else {
            return 0;
        }
    }

    @Nullable
    @Override
    public Sms getItem(int position) {
        return smsList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Sms sms = smsList.get(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(sms.getFolder().equals(Constant.SENT_SMS)){
            view = inflater.inflate(R.layout.item_sent_message, parent, false);
        }else {
            view = inflater.inflate(R.layout.item_inbox_message, parent, false);
        }
        smsContent = view.findViewById(R.id.message);
        smsContent.setText(sms.getMessage());
        if(typeface != null){
            smsContent.setTypeface(typeface);
        }
        return view;
    }
}
