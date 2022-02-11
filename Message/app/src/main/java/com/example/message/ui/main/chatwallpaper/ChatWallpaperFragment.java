package com.example.message.ui.main.chatwallpaper;

import android.os.Bundle;
import android.view.View;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.example.message.R;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentChatWallpaperBinding;
import com.example.message.di.OnItemClick;
import com.example.message.ui.adapter.ColorAdapter;
import com.example.message.ui.adapter.WallpaperAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.utils.Utils;

public class ChatWallpaperFragment extends BaseBindingFragment<FragmentChatWallpaperBinding, ChatWallpaperViewModel> {

    private WallpaperAdapter wallpaperAdapter;
    private ColorAdapter colorAdapter;

    @Override
    protected Class<ChatWallpaperViewModel> getViewModel() {
        return ChatWallpaperViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_wallpaper;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        colorAdapter = new ColorAdapter(requireContext(), new OnItemClick() {
            @Override
            public void onColorButtonClick(int colorId) {
                colorButtonClick(colorId);
            }

            @Override
            public void onContactItemClick(Contact contact) {}

            @Override
            public void onMessageItemClick(Sms sms) {}

            @Override
            public void onWallpaperButtonClick(int wallpaper) {}

            @Override
            public void onFontItemClick(int fontId) { }
        });
        colorAdapter.setColorList(viewModel.getColorsWallpaper(), requireContext());
        binding.colorRecyclerView.setAdapter(colorAdapter);

        wallpaperAdapter = new WallpaperAdapter(requireContext(), new OnItemClick() {
            @Override
            public void onColorButtonClick(int colorId) { }

            @Override
            public void onContactItemClick(Contact contact) { }

            @Override
            public void onMessageItemClick(Sms sms) { }

            @Override
            public void onWallpaperButtonClick(int wallpaper) {
                wallpaperButtonClicked(wallpaper);
            }

            @Override
            public void onFontItemClick(int fontId) { }
        });
        wallpaperAdapter.setWallpaperList(viewModel.getImagesWallpaper());
        if(mainViewModel.background.getValue() != null)
        {
            if(mainViewModel.isBackgroundAColor.getValue()){
                colorAdapter.setColorId(mainViewModel.background.getValue());
            }else {
                wallpaperAdapter.setImageId(mainViewModel.background.getValue());
            }
        }
        binding.imageRecyclerView.setAdapter(wallpaperAdapter);
        ((SimpleItemAnimator) binding.imageRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        binding.wallpaperBackbtn.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
    }

    @Override
    protected void onPermissionGranted() { }

    public void colorButtonClick(int colorId){
        switchBackground(true, colorId);
    }

    public void wallpaperButtonClicked(int wallpaper){
        switchBackground(false, wallpaper);
    }

    private void switchBackground(boolean flag, int backgroundId){
        int position;
        if(!flag){
            position = colorAdapter.getId();
            colorAdapter.setColorId(-1);
            colorAdapter.notifyItemChanged(position);
            mainViewModel.isBackgroundAColor.postValue(false);
        }else {
            position = wallpaperAdapter.getId();
            wallpaperAdapter.setImageId(-1);
            wallpaperAdapter.notifyItemChanged(position);
            mainViewModel.isBackgroundAColor.postValue(true);
        }
        mainViewModel.background.postValue(backgroundId);
    }
}
