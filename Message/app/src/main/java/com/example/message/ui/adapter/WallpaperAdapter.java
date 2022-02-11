package com.example.message.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.message.databinding.ItemWallpaperBinding;
import com.example.message.di.OnItemClick;
import java.util.ArrayList;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {

    private final Context context;
    private final OnItemClick onItemClick;
    private List<Integer> wallpaperList ;
    private int id = -1;

    public void setImageId(int imageId) {
        for(int i = 0; i < this.wallpaperList.size(); i++){
            if(this.wallpaperList.get(i) == imageId){
                id = i;
                return;
            }
        }
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setWallpaperList(List<Integer> wallpaperList) {
        this.wallpaperList = wallpaperList;
    }

    public WallpaperAdapter(Context context, OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        wallpaperList = new ArrayList<>();
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWallpaperBinding binding = ItemWallpaperBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WallpaperViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        holder.bind(wallpaperList.get(position), context,position);
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder{
        ItemWallpaperBinding binding;
        public WallpaperViewHolder(@NonNull ItemWallpaperBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(int wallpaper, Context context, int position){
            binding.changeWallpaperBtn.setBackground(ContextCompat.getDrawable(context, wallpaper));
            binding.changeWallpaperBtn.setOnClickListener(v -> {
                onItemClick.onWallpaperButtonClick(wallpaper);
                if(id != position){
                    notifyItemChanged(id);
                    id = position;
                    notifyItemChanged(position);
                }
            });

            if(id == position){
                binding.isSelected.setVisibility(View.VISIBLE);
            }else{
                binding.isSelected.setVisibility(View.GONE);
            }
        }
    }
}
