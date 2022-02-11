package com.example.message.ui.main.mesages;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.common.Constant;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentMessagesBinding;
import com.example.message.di.OnItemClick;
import com.example.message.ui.adapter.MessageAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.ui.main.home.HomeFragmentDirections;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessagesFragment extends BaseBindingFragment<FragmentMessagesBinding, MessagesViewModel> {
    private MessageAdapter adapter;

    @Override
    protected Class<MessagesViewModel> getViewModel() {
        return MessagesViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_messages;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        adapter = new MessageAdapter(new OnItemClick() {
            @Override
            public void onColorButtonClick(int colorId) { }

            @Override
            public void onContactItemClick(Contact contact) { }

            @Override
            public void onMessageItemClick(Sms sms) {
                messageItemClicked(sms);
            }

            @Override
            public void onWallpaperButtonClick(int wallpaper) { }

            @Override
            public void onFontItemClick(int fontId) { }
        }, requireContext());
        binding.newMessageBtn.setOnClickListener(v -> {
            NavDirections navDirections = HomeFragmentDirections.actionHomeFragmentToNewMessageFragment();
            Navigation.findNavController(binding.getRoot()).navigate(navDirections);
        });
        binding.searchMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Sms> smsList = viewModel.filter(charSequence.toString(), mainViewModel.smsList.getValue());
                adapter.setSmsList(smsList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        mainViewModel.smsList.observe(getViewLifecycleOwner(), smsList -> {
            List<Contact> list1 = mainViewModel.contactList.getValue();
            if(list1 == null){
                list1 = mainViewModel.contactList1.getValue();
            }
            List<Sms> smsList1 = mainViewModel.addNameToSms(smsList, list1);
            if(smsList1 != null){
                adapter.setSmsList(smsList1);
            }else {
                adapter.setSmsList(smsList);
            }
            Collections.sort(smsList1);
            binding.recyclerView.setAdapter(adapter);
        });

        mainViewModel.contactList.observe(getViewLifecycleOwner(), contacts -> {
            if(contacts != null){
                if(mainViewModel.smsList.getValue() != null)
                {
                    List<Sms> smsList = mainViewModel.smsList.getValue();;
                    List<Contact> list1 = mainViewModel.contactList.getValue();
                    if(list1 == null){
                        list1 = mainViewModel.contactList1.getValue();
                    }
                    List<Sms> smsList1 = mainViewModel.addNameToSms(smsList, list1);
                    mainViewModel.updateSmsList(smsList1);
                }
            }
        });

        mainViewModel.font.observe(getViewLifecycleOwner(), integer -> {
            if(integer != null){
                Typeface typeface = ResourcesCompat.getFont(requireContext(), integer);
                adapter.setTypeface(typeface);
                binding.recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onPermissionGranted() { }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void messageItemClicked(Sms sms) {
        List<Sms> smsList1 = mainViewModel.messageFilter(sms.getAddress());
        boolean findContact = true;
        if(mainViewModel.contactList1.getValue() != null){
            for(Contact contact: mainViewModel.contactList1.getValue()){
                if(contact.getPhoneNumber().equals(sms.getAddress())){
                    mainViewModel.contact.postValue(contact);
                    findContact = false;
                    break;
                }
            }
        }
        if(findContact){
            mainViewModel.contact.postValue(new Contact(sms.getAddress(), sms.getAddress(), "custom"));
        }
        for(Sms sms1: smsList1){
            sms1.setReadState(Constant.READ_STATE);
        }
        mainViewModel.updateSmsList(smsList1);
        Collections.reverse(smsList1);
        mainViewModel.smsListByContact.postValue(smsList1);
        NavDirections navDirections = HomeFragmentDirections.actionHomeFragmentToChatFragment();
        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
    }
}
