package com.example.message.ui.main.contacts;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentContactsBinding;
import com.example.message.di.OnItemClick;
import com.example.message.ui.adapter.ContactsAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.ui.main.home.HomeFragmentDirections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsFragment extends BaseBindingFragment<FragmentContactsBinding, ContactsViewModel> {
    @Override
    protected Class<ContactsViewModel> getViewModel() {
        return ContactsViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        mainViewModel.getContactsFromApp(requireContext());
        binding.setLifecycleOwner(getViewLifecycleOwner());
        ContactsAdapter adapter = new ContactsAdapter(new OnItemClick() {
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
        binding.contactView.setAdapter(adapter);
        mainViewModel.font.observe(getViewLifecycleOwner(), integer -> {
            if(integer != null){
                Typeface typeface = ResourcesCompat.getFont(requireContext(), integer);
                adapter.setTypeface(typeface);
                adapter.notifyDataSetChanged();
            }
        });
        mainViewModel.contactList.observe(getViewLifecycleOwner(), contactList -> {
            mainViewModel.contactList1.postValue(contactList);
            adapter.setContactList(contactList);
            adapter.notifyDataSetChanged();
        });

        binding.searchContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Contact> contactList = viewModel.filter(charSequence.toString(), mainViewModel.contactList1.getValue());
                adapter.setContactList(contactList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    @Override
    protected void onPermissionGranted() {

    }

    private void contactItemClick(Contact contact){
        mainViewModel.contact.postValue(contact);
        List<Sms> smsList = new ArrayList<>();
        if(mainViewModel.smsList.getValue() != null){
            smsList.addAll(mainViewModel.smsList.getValue());
            int size = smsList.size();
            for (int i = size - 1; i > -1; i--) {
                if (!smsList.get(i).getAddress().equals(contact.getPhoneNumber())) {
                    smsList.remove(i);
                }
            }
        }
        mainViewModel.smsListByContact.postValue(smsList);
        NavDirections directions = HomeFragmentDirections.actionHomeFragmentToDetailContactFragment2();
        Navigation.findNavController(binding.getRoot()).navigate(directions);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
