package com.example.message.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.message.ui.main.contacts.ContactsFragment;
import com.example.message.ui.main.mesages.MessagesFragment;
import com.example.message.ui.main.settings.SettingsFragment;

public class ViewPageAdapter extends  FragmentStateAdapter{

    private final int NUM_PAGER = 3;

    public ViewPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ContactsFragment();
            case 2:
                return new SettingsFragment();
            default:
                return new MessagesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGER;
    }
}
