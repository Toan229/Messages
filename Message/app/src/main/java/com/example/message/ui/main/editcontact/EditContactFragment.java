package com.example.message.ui.main.editcontact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.data.model.Contact;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentEditContactBinding;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.utils.Utils;
import com.squareup.picasso.Picasso;
import java.util.List;

public class EditContactFragment extends BaseBindingFragment<FragmentEditContactBinding, EditContactViewModel> {

    private boolean isNew = false;
    private String uriImage;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getData() != null){
            Uri uri = result.getData().getData();
            binding.userIc.setImageURI(uri);
            enableDoneButton();
//            Bitmap bitmap = ((BitmapDrawable)binding.userIc.getDrawable()).getBitmap();
            isNew = mainViewModel.contact.getValue().getImage().equals("custom");
            uriImage = uri.toString();
//            viewModel.saveContactImage(bitmap, mainViewModel.contact.getValue().getImage(), requireActivity().getApplicationContext());
        }
    });

    @Override
    protected Class<EditContactViewModel> getViewModel() {
        return EditContactViewModel.class;
    }

    @Override
    public
    int getLayoutId() {
        return R.layout.fragment_edit_contact;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        Contact contact = mainViewModel.contact.getValue();
        if(!contact.getImage().equals("custom")){
            binding.addImageBtn.setText("Edit");
        }
        binding.contactNameTxt.setText(contact.getName());
        binding.contactNumberTxt.setText(contact.getPhoneNumber());
        Picasso.get().load(contact.getImage()).placeholder(R.drawable.user_icon).into(binding.userIc);
        binding.done.setOnClickListener(v -> {
            String name = binding.contactNameTxt.getText().toString();
            String phoneNum = binding.contactNumberTxt.getText().toString();
            if (name.isEmpty()) {
                name = contact.getPhoneNumber();
            }
            if (phoneNum.isEmpty()) {
                phoneNum = contact.getPhoneNumber();
            }
            if(uriImage == null){
                uriImage = mainViewModel.contact.getValue().getImage();
            }
            Contact contact1 = new Contact(mainViewModel.contact.getValue().getId(), name, phoneNum, uriImage);
            mainViewModel.contact.postValue(contact1);

            viewModel.updateContact(contact1, requireContext(), isNew);
            Log.d("toan", contact1.toString());
            List<Sms> list = mainViewModel.smsListByContact.getValue();
            if(list != null){
                for(Sms sms: list){
                    if(!sms.getAddress().equals(contact1.getPhoneNumber())){
                        sms.setName(sms.getAddress());
                    }else if(!sms.getName().equals(contact1.getName())){
                        sms.setName(contact1.getName());
                    }
                }
            }
            mainViewModel.updateSmsList(list);
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.contactNameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.cancelEditName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                enableDoneButton();
            }
        });

        binding.contactNumberTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.cancelEditNumber.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                enableDoneButton();
            }
        });

        binding.exitFragment.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());

        binding.cancelEditName.setOnClickListener(v -> binding.contactNameTxt.setText(""));

        binding.cancelEditNumber.setOnClickListener(v -> binding.contactNumberTxt.setText(""));

        binding.addImageBtn.setOnClickListener(v -> viewModel.getImageFromLibrary(launcher));

        mainViewModel.font.observe(getViewLifecycleOwner(), integer -> {
            Typeface typeface = ResourcesCompat.getFont(requireContext(), integer);
            binding.contactNameTxt.setTypeface(typeface);
            binding.contactNumberTxt.setTypeface(typeface);
            binding.exitFragment.setTypeface(typeface);
            binding.addImageBtn.setTypeface(typeface);
            binding.done.setTypeface(typeface);
        });
    }


    private void enableDoneButton(){
        binding.done.setEnabled(true);
        binding.done.setTextColor(getResources().getColor(R.color.blue));
    }

    @Override
    protected void onPermissionGranted() {

    }

}
