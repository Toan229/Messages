package com.example.message.ui.main.policy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import com.example.message.R;
import com.example.message.databinding.FragmentPolicyBinding;
import com.example.message.ui.base.BaseFragment;
import com.example.message.utils.Utils;

public class PolicyFragment extends BaseFragment {
    private FragmentPolicyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_policy, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        launchWebView();
        binding.exitWebView.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigateUp());
    }

    private void launchWebView(){
        String url = getResources().getString(R.string.web_view_url);
        binding.exitWebView.setOnClickListener(v ->
        {
            Navigation.findNavController(binding.getRoot()).navigateUp();
        });
        loadWebUrl(url);
    }

    private void loadWebUrl(String url){
        binding.urlText.setText(url);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.loadUrl(url);
    }
}
