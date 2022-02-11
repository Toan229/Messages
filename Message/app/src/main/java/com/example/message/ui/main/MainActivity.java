package com.example.message.ui.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.WindowManager;

import com.example.message.R;
import com.example.message.data.local.db.MessageDB;
import com.example.message.databinding.ActivityMainBinding;
import com.example.message.ui.base.BaseBindingActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding, MainViewModel> {

    private NavHostFragment navHostFragment;
    public NavController navController;
    private NavGraph navGraph;
    private List<String> permissionsDenied;
    boolean mBound;
    private final ActivityResultLauncher<Intent> activityResultLauncher
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                for (int i = permissionsDenied.size() - 1; i > -1; i --) {
                    if(ContextCompat.checkSelfPermission(this, permissionsDenied.get(i)) == PackageManager.PERMISSION_GRANTED){
                        permissionsDenied.remove(i);
                    }
                }
                if(permissionsDenied.contains(Manifest.permission.READ_SMS)){
                    viewModel.popBackNav.postValue(true);
                }
            });

    public final static String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int REQUEST_CODE = 100;
    private final ContentObserver mObserver = new ContentObserver(new Handler()){
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MainViewModel mainViewModel = new MainViewModel();
            mainViewModel.getContactsFromDevice(getApplicationContext());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    public void setupView(Bundle savedInstanceState) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }

    @Override
    public void setupData() { }

    private void checkAndRequestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            List<String> unGrantedPermissions = new ArrayList<>();
            for (String permission: permissions) {
                if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                    unGrantedPermissions.add(permission);
                }
            }
            if(unGrantedPermissions.size() != 0){
                Object[] unGranted1 = unGrantedPermissions.toArray();
                String[] unGranted2 = Arrays.copyOf(unGranted1, unGrantedPermissions.size(), String[].class);
                requestPermissions(unGranted2, REQUEST_CODE);
            }else {
                permissionsDenied = unGrantedPermissions;
                viewModel.popBackNav.postValue(true);
            }
        }else {
            viewModel.popBackNav.postValue(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> unGrantedPermissions = new ArrayList<>();
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            for(int i = 0; i < permissions.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    unGrantedPermissions.add(permissions[i]);
                }
            }
            if(unGrantedPermissions.size() == 0){
                viewModel.popBackNav.postValue(true);
            }else {
                permissionsDenied = unGrantedPermissions;
                showDialogPermission();
            }
        }
    }

    public void showDialogPermission() {
        new AlertDialog.Builder(this).setMessage(
                R.string.requirePermission).setPositiveButton(
                R.string.goSetting, (dialog, which) -> {
                    // navigate to settings
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    activityResultLauncher.launch(intent);
                }).setNegativeButton(R.string.goBack, (dialog, which) -> {
                    // leave?
                    dialog.dismiss();
                    viewModel.popBackNav.postValue(true);
                }).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1000);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        checkAndRequestPermission();
        MessageDB.getAppDatabase(this);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        permissionsDenied = new ArrayList<>();
        MessageDB.getAppDatabase(this);
        viewModel.popBackNav.observe(this, aBoolean -> {
            viewModel.loadSetting(this);
            viewModel.getContactsFromDevice(this);
            viewModel.fetchSms(this);
            viewModel.getContactsFromApp(this);
            if(!permissionsDenied.contains(Manifest.permission.READ_CONTACTS)){
                this.getContentResolver().registerContentObserver(
                        ContactsContract.Contacts.CONTENT_URI, true, mObserver);
            }
//            if(!permissionsDenied.contains(Manifest.permission.READ_SMS) && !isSmsServiceRunning(SmsService.class)){
//                Intent startSmsService = new Intent(this, SmsService.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startService(startSmsService);
//                }
//            }
        });
    }

    private boolean isSmsServiceRunning(Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(serviceInfo.getClass().getName())){
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() <= 1){
            super.onBackPressed();
        }else {
            navController.navigateUp();
        }
    }

    @Override
    protected void onStop() {
        mBound = false;
        if(viewModel.background.getValue() != null){
            viewModel.saveWallpaperSetting(viewModel.isBackgroundAColor.getValue(), viewModel.background.getValue());
        }
        if(viewModel.font.getValue() != null){
            viewModel.saveFontSetting(viewModel.font.getValue());
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.getContentResolver().unregisterContentObserver(mObserver);
        if(MessageDB.MESSAGE_DB.isOpen()){
            MessageDB.MESSAGE_DB.close();
        }
        super.onDestroy();
    }

    public void changeStartDestination(int screenId) {
        if (navGraph == null) {
            navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        }
        navGraph.setStartDestination(screenId);
        navController.setGraph(navGraph);
    }

}