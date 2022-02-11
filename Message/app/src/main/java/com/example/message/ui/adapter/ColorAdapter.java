package com.example.message.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.message.R;
import com.example.message.databinding.ItemBackgroundColorBinding;
import com.example.message.di.OnItemClick;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    OnItemClick onItemClick;
    private int id = -1;
    private List<Integer> colorList;
    private Context context;

    public void setColorId(int colorId){
        for(int i = 0; i < this.colorList.size(); i++){
            if(this.colorList.get(i) == colorId){
                id = i;
                return;
            }
        }
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setColorList(List<Integer> colorList, Context context) {
        this.colorList = colorList;
        this.context = context;
    }

    public ColorAdapter(Context context, OnItemClick onItemClick){
        this.onItemClick = onItemClick;
        this.context = context;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBackgroundColorBinding binding = ItemBackgroundColorBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ColorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.bind(colorList.get(position), position, context);
    }

    @Override
    public int getItemCount() {
        if(colorList != null){
            return colorList.size();
        }else {
            return 0;
        }
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder{

        ItemBackgroundColorBinding binding;

        public ColorViewHolder(@NonNull ItemBackgroundColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int colorID, int position, Context context){
            binding.buttonColor.setBackgroundTintList(ContextCompat.getColorStateList(context, colorID));
            binding.cardView.setStrokeColor(ContextCompat.getColorStateList(context, colorID));
            binding.buttonColor.setOnClickListener(v -> {
                onItemClick.onColorButtonClick(colorID);
                binding.cardView.invalidate();
                if(id != position){
                    notifyItemChanged(id);
                    id = position;
                    notifyItemChanged(position);
                }
            });
            if(id == position){
                binding.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
                binding.cardView.setStrokeColor(ContextCompat.getColorStateList(context, R.color.blue));
                if(colorID == R.color.white){
                    binding.buttonColor.setBackgroundTintList(null);
                }
            }else{
                binding.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, colorID));
            }
        }
    }
}
