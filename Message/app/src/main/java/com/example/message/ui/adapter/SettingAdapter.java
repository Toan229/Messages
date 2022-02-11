package com.example.message.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.message.R;
import com.example.message.databinding.ItemSettingBinding;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {

    private List<String> setting;
    private ItemClick itemClick;

    public void setSetting(List<String> setting) {
        this.setting = setting;
    }

    public SettingAdapter(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSettingBinding binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SettingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        holder.bind(setting.get(position), position);
    }

    @Override
    public int getItemCount() {
        if(setting == null){
            return 0;
        }else {
            return setting.size();
        }
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder{
        private ItemSettingBinding binding;
        public SettingViewHolder(@NonNull ItemSettingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String settingItem, int position){
            int iconId = -1;
            switch (settingItem){
                case "Chat Wallpaper":
                    iconId = R.drawable.chat_wallpaper_ic;
                    break;
                case "Font":
                    iconId = R.drawable.font_ic;
                    break;
                case "Privacy Policy":
                    iconId = R.drawable.policy_ic;
                    break;
                case "Rate App":
                    iconId = R.drawable.rate_ic;
                    break;
                case "Feedback":
                    iconId = R.drawable.feedback_ic;
                    break;
            }
            if(iconId != -1){
                binding.settingIcon.setImageResource(iconId);
            }
            binding.settingTitle.setText(settingItem);
            binding.getRoot().setOnClickListener(v -> itemClick.onItemClick(settingItem));
        }
    }

    public interface ItemClick{
        void onItemClick(String item);
    }
}
