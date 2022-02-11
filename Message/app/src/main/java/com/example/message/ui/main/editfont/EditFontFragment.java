package com.example.message.ui.main.editfont;

import android.os.Bundle;
import android.view.View;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentEditFontBinding;
import com.example.message.di.OnItemClick;
import com.example.message.ui.adapter.FontAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.utils.Utils;

public class EditFontFragment extends BaseBindingFragment<FragmentEditFontBinding, EditFontViewModel> {
    @Override
    protected Class<EditFontViewModel> getViewModel() {
        return EditFontViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_font;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        binding.fontBackbtn.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
        FontAdapter fontAdapter = new FontAdapter(new OnItemClick() {
            @Override
            public void onColorButtonClick(int colorId) { }

            @Override
            public void onContactItemClick(Contact contact) { }

            @Override
            public void onMessageItemClick(Sms sms) { }

            @Override
            public void onWallpaperButtonClick(int wallpaper) { }

            @Override
            public void onFontItemClick(int fontId) {
                onEditFontClick(fontId);
            }
        }, requireContext());
        if(mainViewModel.font.getValue() != null){
            fontAdapter.setFont(mainViewModel.font.getValue());
        }
        fontAdapter.setFonts(viewModel.getFonts());
        binding.fontRecyclerView.setAdapter(fontAdapter);
    }

    @Override
    protected void onPermissionGranted() {

    }

    public void onEditFontClick(int fontId){
        mainViewModel.font.postValue(fontId);
    }
}
