package com.example.message.ui.main.newmessage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.common.Constant;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentNewMessageBinding;
import com.example.message.di.OnItemClick;
import com.example.message.ui.adapter.SearchContactsAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewMessageFragment extends BaseBindingFragment<FragmentNewMessageBinding, NewMessageViewModel> {
    @Override
    protected Class<NewMessageViewModel> getViewModel() {
        return NewMessageViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_message;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.cancel.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
        binding.sendMessageButton.setOnClickListener(v -> {
            String address = binding.addressEditText.getText() + "";
            String message = binding.messageBody.getText() + "";
            if(!address.matches(Constant.REGEX_IS_NOT_NUMBER) && !address.matches(Constant.REGEX_EXCLUSIVE_SPECIAL_CHARACTER)){
                if(!address.isEmpty()&& !message.isEmpty()){
                    if(address.matches(Constant.REGEX_EXCLUSIVE_SPECIAL_CHARACTER)){
                        Toast.makeText(requireContext(), R.string.invalidAddress,Toast.LENGTH_SHORT).show();
                    }
                    else {
                        List<Contact> listContact = mainViewModel.contactList1.getValue();
                        boolean findContact = true;
                        Contact contact1 = null;
                        if(listContact != null){
                            for(Contact contact: listContact){
                                if(contact.getPhoneNumber().equals(address)){
                                    contact1 = contact;
                                    mainViewModel.contact.postValue(contact);
                                    findContact = false;
                                    break;
                                }
                            }
                        }
                        if(findContact){
                            contact1 = new Contact(address, address, address);
                            mainViewModel.contact.postValue(new Contact(address, address, Constant.DEFAULT_CONTACT_IMAGE));
                        }
                        sendMessage(contact1, message);

                        NavDirections navDirections = NewMessageFragmentDirections.actionNewMessageFragmentToChatFragment();
                        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
                    }
                }else if(message.isEmpty()){
                    mainViewModel.contact.postValue(new Contact(address, address, Constant.DEFAULT_CONTACT_IMAGE));
                    NavDirections navDirections = NewMessageFragmentDirections.actionNewMessageFragmentToChatFragment();
                    Navigation.findNavController(binding.getRoot()).navigate(navDirections);
                }
            }
        });
        SearchContactsAdapter searchContactsAdapter = new SearchContactsAdapter(new OnItemClick() {
            @Override
            public void onColorButtonClick(int colorId) { }

            @Override
            public void onContactItemClick(Contact contact) {
                contactItemClick(contact);
            }

            @Override
            public void onMessageItemClick(Sms sms) { }

            @Override
            public void onWallpaperButtonClick(int wallpaper) { }

            @Override
            public void onFontItemClick(int fontId) { }
        });
        binding.recyclerView.setAdapter(searchContactsAdapter);
        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchContactsAdapter.setContactList(viewModel.filter(charSequence.toString(), mainViewModel.contactList1.getValue()));
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });


    }

    @Override
    public void onResume() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onResume();
    }

    private void sendMessage(Contact contact, String message){
        String readState = Constant.READ_STATE;
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.HOUR_PATTERN, Locale.US);
        long time_milliseconds = now.getTime();
        String time = dateFormat.format(now);
        String folder = Constant.SENT_SMS;
        Sms objSms = new Sms(contact.getName(), contact.getPhoneNumber(), message, readState, time, time_milliseconds, folder, Constant.DEFAULT_CONTACT_IMAGE);
        mainViewModel.sendSms(objSms);
    }

    private void contactItemClick(Contact contact){
        mainViewModel.contact.postValue(contact);
        NavDirections navDirections = NewMessageFragmentDirections.actionNewMessageFragmentToChatFragment();
        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
    }

    @Override
    protected void onPermissionGranted() { }
}
