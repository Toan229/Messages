package com.example.message.ui.main.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.databinding.FragmentSettingsBinding;
import com.example.message.ui.adapter.SettingAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.ui.dialog.RateAppDialog;

import java.util.Arrays;

import company.librate.RateDialog;

public class SettingsFragment extends BaseBindingFragment<FragmentSettingsBinding, SettingsViewModel> {
    @Override
    protected Class<SettingsViewModel> getViewModel() {
        return SettingsViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        SettingAdapter settingAdapter = new SettingAdapter(item -> itemClick(item));
        settingAdapter.setSetting(Arrays.asList("Chat Wallpaper", "Font", "Privacy Policy", "Rate App", "Feedback"));
        binding.settingRecyclerView.setAdapter(settingAdapter);
    }

    @Override
    protected void onPermissionGranted() {

    }

    private void itemClick(String settingItem){
        switch (settingItem){
            case "Chat Wallpaper":
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_chatWallpaperFragment);
                break;
            case "Font":
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_editFontFragment);
                break;
            case "Privacy Policy":
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_policyFragment);
                break;
            case "Rate App":
                showRateApp(requireContext(), null);
                break;
            case "Feedback":

                break;
        }
    }

    private void showRateApp(Context context, String supportEmail) {
        RateDialog rateDialog = new RateDialog(context, supportEmail, true);
        Window window = rateDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        rateDialog.show();
    }

}
