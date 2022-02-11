package com.example.message.ui.main.detailscontact;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.data.model.Contact;
import com.example.message.databinding.FragmentDetailsContactBinding;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.utils.Utils;
import com.squareup.picasso.Picasso;

public class DetailContactFragment extends BaseBindingFragment<FragmentDetailsContactBinding, DetailContactViewModel> {

    @Override
    protected Class getViewModel() {
        return DetailContactViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_details_contact;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        binding.callBtn.setOnClickListener(v -> makeTheCall(binding.phoneNum.getText().toString()));
        binding.detailBackbtn.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
        binding.editBtn.setOnClickListener(v -> openEditContact());
        mainViewModel.contact.observe(getViewLifecycleOwner(), contact -> {
            binding.userName.setText(contact.getName());
            binding.phoneNum.setText(contact.getPhoneNumber());
            Picasso.get().load(contact.getImage()).placeholder(R.drawable.user_icon).into(binding.userIc);
        });
        mainViewModel.contact.observe(getViewLifecycleOwner(), contact -> {
            if (contact != null){
                binding.userName.setText(contact.getName());
                binding.phoneNum.setText(contact.getPhoneNumber());
                Picasso.get().load(contact.getImage()).placeholder(R.drawable.user_icon).into(binding.userIc);
            }
        });
        binding.messageBtn.setOnClickListener(v -> {
            NavDirections navDirections = DetailContactFragmentDirections.actionDetailContactFragmentToChatFragment();
            Navigation.findNavController(binding.getRoot()).navigate(navDirections);
        });
        mainViewModel.font.observe(getViewLifecycleOwner(), integer -> {
            Typeface typeface = ResourcesCompat.getFont(requireContext(), integer);
            binding.messageBtn.setTypeface(typeface);
            binding.phoneNum.setTypeface(typeface);
            binding.userName.setTypeface(typeface);
            binding.callBtn.setTypeface(typeface);
        });
    }

    @Override
    protected void onPermissionGranted() { }

    private void makeTheCall(String phoneNumber){
        if(phoneNumber != null && phoneNumber.length() > 0){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            Uri phoneUri = Uri.parse("tel:" + phoneNumber);
            callIntent.setData(phoneUri);
            startActivity(callIntent);
        }
    }

    private void openEditContact(){
        NavDirections navDirections = DetailContactFragmentDirections.actionDetailContactFragmentToEditContactFragment();
        Navigation.findNavController(binding.getRoot()).navigate(navDirections);
    }
}
