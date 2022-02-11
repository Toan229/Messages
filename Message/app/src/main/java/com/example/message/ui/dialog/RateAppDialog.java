package com.example.message.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.example.message.R;
import com.example.message.databinding.FragmentDialogRateBinding;
import com.example.message.ui.base.BaseBindingDialogFragment;

public class RateAppDialog extends BaseBindingDialogFragment<FragmentDialogRateBinding> {

    private boolean flag = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialog_rate;
    }

    @Override
    protected void onCreatedView(View view, Bundle savedInstanceState) {
        binding.exitDialogBtn.setOnClickListener(v -> dismiss());
        binding.rateBtn.setOnClickListener(v -> {
            dismiss();
            if (flag){
                openMarket(requireContext());
            }
        });
        binding.star1.setOnClickListener(v -> starOnclick(binding.star1.getId()));
        binding.star2.setOnClickListener(v -> starOnclick(binding.star2.getId()));
        binding.star3.setOnClickListener(v -> starOnclick(binding.star3.getId()));
        binding.star4.setOnClickListener(v -> starOnclick(binding.star4.getId()));
        binding.star5.setOnClickListener(v -> starOnclick(binding.star5.getId()));

    }

    private void starOnclick(int id){
        flag = true;
        int resId1 = R.drawable.rate_app_ic1;
        int resId = R.drawable.rate_app_ic;
        switch (id){
            case R.id.star1:
                binding.star1.setImageResource(resId1);
                binding.star2.setImageResource(resId);
                binding.star3.setImageResource(resId);
                binding.star4.setImageResource(resId);
                binding.star5.setImageResource(resId);
                break;
            case R.id.star2:
                binding.star1.setImageResource(resId1);
                binding.star2.setImageResource(resId1);
                binding.star3.setImageResource(resId);
                binding.star4.setImageResource(resId);
                binding.star5.setImageResource(resId);
                break;
            case R.id.star3:
                binding.star1.setImageResource(resId1);
                binding.star2.setImageResource(resId1);
                binding.star3.setImageResource(resId1);
                binding.star4.setImageResource(resId);
                binding.star5.setImageResource(resId);
                break;
            case R.id.star4:
                binding.star1.setImageResource(resId1);
                binding.star2.setImageResource(resId1);
                binding.star3.setImageResource(resId1);
                binding.star4.setImageResource(resId1);
                binding.star5.setImageResource(resId);
                break;
            case R.id.star5:
                binding.star1.setImageResource(resId1);
                binding.star2.setImageResource(resId1);
                binding.star3.setImageResource(resId1);
                binding.star4.setImageResource(resId1);
                binding.star5.setImageResource(resId1);
                break;
        }
    }

    private void openMarket(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void sendEmail(Context context, String supportEmail) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{supportEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Report (" + context.getPackageName() + ")");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(Intent.createChooser(emailIntent, "Send mail Report App !"));
    }

}
