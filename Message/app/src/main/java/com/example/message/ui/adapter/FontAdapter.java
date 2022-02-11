package com.example.message.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.message.databinding.ItemFontBinding;
import com.example.message.di.OnItemClick;
import java.util.Map;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    private Map<Integer, String> fonts;
    private Object[] keySet;
    private OnItemClick onItemClick;
    private int id = -1;
    private int font = 0;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public FontAdapter(OnItemClick onItemClick, Context context) {
        this.onItemClick = onItemClick;
        this.context = context;
    }

    public void setFonts(Map<Integer, String> fonts) {
        this.fonts = fonts;
        keySet = this.fonts.keySet().toArray();
        for(int i = 0; i < keySet.length; i++){
            if(font == (int)keySet[i]){
                id = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFontBinding binding = ItemFontBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FontViewHolder(binding);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        String name = fonts.get(keySet[position]);
        int fontId = (int)keySet[position];
        holder.bind(fontId, name, position);
    }

    @Override
    public int getItemCount() {
        return fonts.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder{
        private ItemFontBinding binding;
        public FontViewHolder(@NonNull ItemFontBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Integer fontId, String fontName, int position){
            binding.fontBtn.setText(fontName);
            binding.fontBtn.setOnClickListener(v -> {
                onItemClick.onFontItemClick(fontId);
                if(id != position){
                    notifyItemChanged(id);
                    id = position;
                    notifyItemChanged(position);
                }
            });
            Typeface typeface = ResourcesCompat.getFont(context, fontId);
            binding.fontBtn.setTypeface(typeface);
            if(id == position){
                binding.fontSelected.setVisibility(View.VISIBLE);
            }else {
                binding.fontSelected.setVisibility(View.GONE);
            }
        }
    }

}
