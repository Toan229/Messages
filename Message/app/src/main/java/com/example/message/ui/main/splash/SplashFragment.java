package com.example.message.ui.main.splash;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.message.R;
import com.example.message.databinding.FragmentSplashBinding;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.ui.main.MainActivity;
import com.example.message.ui.main.MainViewModel;
import com.example.message.utils.Utils;

public class SplashFragment extends BaseBindingFragment<FragmentSplashBinding, MainViewModel> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        mainViewModel.popBackNav.observe(getViewLifecycleOwner(), isGranted -> {
//            ((MainActivity)requireActivity()).changeStartDestination(R.id.homeFragment);
            Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.startColor, false);
            Handler handler = new Handler();
            handler.postDelayed(() -> ((MainActivity)requireActivity()).changeStartDestination(R.id.homeFragment), 1000);
        });
    }

    @Override
    protected void onPermissionGranted() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
}
