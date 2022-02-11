package com.example.message.ui.main.home;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.viewpager2.widget.ViewPager2;
import com.example.message.R;
import com.example.message.databinding.FragmentHomeBinding;
import com.example.message.ui.adapter.ViewPageAdapter;
import com.example.message.ui.base.BaseBindingFragment;
import com.example.message.utils.Utils;

public class HomeFragment extends BaseBindingFragment<FragmentHomeBinding, HomeViewModel> {
    @Override
    protected Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        Utils.setSystemBarTranslucentStatus(requireActivity(), R.color.white, true);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(requireParentFragment());
        binding.viewPager.setAdapter(viewPageAdapter);
        binding.bottomNavigation.setOnItemSelectedListener( item -> {
            onBottomNavigationItemSelected(item.getItemId());
            return true;
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < binding.bottomNavigation.getMenu().size(); i++){
                    if(i == position){
                        binding.bottomNavigation.getMenu().getItem(i).setChecked(true);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onResume();
    }

    @Override
    protected void onPermissionGranted() {

    }

    private void onBottomNavigationItemSelected(int id){
        switch (id){
            case R.id.message:
                binding.viewPager.setCurrentItem(0);
                break;
            case R.id.contact:
                binding.viewPager.setCurrentItem(1);
                break;
            case R.id.setting:
                binding.viewPager.setCurrentItem(2);
                break;
        }
    }

}
