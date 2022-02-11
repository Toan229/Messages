package com.example.message.ui.main.chat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.common.Constant;
import com.example.message.data.model.Sms;
import com.example.message.databinding.FragmentChatBinding;
import com.example.message.ui.adapter.ChatArrayAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.ui.main.MainActivity;
import com.example.message.utils.Utils;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends BaseBindingFragment<FragmentChatBinding, ChatViewModel> {
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result){
            sendMessage();
        }else {
            ((MainActivity)requireActivity()).showDialogPermission();
        }
    });
    private ChatArrayAdapter adapter;
    @Override
    protected Class<ChatViewModel> getViewModel() {
        return ChatViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        String name = mainViewModel.contact.getValue().getName();
        String address = mainViewModel.contact.getValue().getPhoneNumber();
        Picasso.get().load(mainViewModel.contact.getValue().getImage()).placeholder(R.drawable.user_icon).into(binding.circleImage);
        binding.contactName.setText(name);
        binding.backButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        binding.sendMessageButton.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                sendMessage();
            }else {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });

        if(mainViewModel.background.getValue() != null){
            if(mainViewModel.isBackgroundAColor.getValue()){
                binding.listView.setBackgroundColor(ContextCompat.getColor(requireContext(), mainViewModel.background.getValue()));
            }else {
                binding.listView.setBackground(ContextCompat.getDrawable(requireContext(), mainViewModel.background.getValue()));
            }
        }

        adapter = new ChatArrayAdapter(requireContext(), R.layout.item_inbox_message);
        if(mainViewModel.smsListByContact.getValue() == null){
            adapter.setSmsList(new ArrayList<>());
        }else {
            adapter.setSmsList(mainViewModel.smsListByContact.getValue());
        }
        binding.listView.setAdapter(adapter);

        mainViewModel.smsList.observe(getViewLifecycleOwner(), smsList1 -> {
            List<Sms> smsList2 = mainViewModel.messageFilter(address);
            mainViewModel.smsListByContact.postValue(smsList2);
            adapter.setSmsList(smsList2);
            binding.listView.setAdapter(adapter);
        });
        mainViewModel.font.observe(getViewLifecycleOwner(), integer -> {
            if(integer != null){
                Typeface typeface = ResourcesCompat.getFont(requireContext(), integer);
                adapter.setTypeface(typeface);
            }
        });
    }

    @Override
    public void onResume() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onResume();
    }

    private void sendMessage(){
        String address = mainViewModel.contact.getValue().getPhoneNumber();
        String message = binding.messageBody.getText().toString();
        String readState = Constant.READ_STATE;
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.HOUR_PATTERN, Locale.US);
        long time_milliseconds = now.getTime();
        String time = dateFormat.format(now);
        String folder = Constant.SENT_SMS;
        Sms objSms = new Sms(address, address, message, readState, time, time_milliseconds, folder, mainViewModel.contact.getValue().getImage());
        mainViewModel.sendSms(objSms);
        adapter.add(objSms);
        binding.messageBody.setText("");
    }

    @Override
    protected void onPermissionGranted() {

    }


}
