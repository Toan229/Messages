package com.example.message.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.message.R;
import com.example.message.common.Constant;
import com.example.message.data.model.Sms;
import com.example.message.databinding.ItemMessageBinding;
import com.example.message.di.OnItemClick;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ListAdapter<Sms, MessageAdapter.MessageViewHolder> {

    private List<Sms> smsList;
    private List<String> checkList;
    private OnItemClick onItemClick;
    private Typeface typeface;
    private final String readStateColor = "#8E8E93";
    private Context context;

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setSmsList(List<Sms> smsList) {
        if(smsList != null){
            this.smsList = new ArrayList<>();
            checkList = new ArrayList<>();
            for(Sms sms: smsList){
                if(!checkList.contains(sms.getAddress())){
                    checkList.add(sms.getAddress());
                    this.smsList.add(sms);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void submitList(@Nullable List<Sms> list) {
        setSmsList(list);
    }

    public MessageAdapter(OnItemClick onItemClick, Context context) {
        super(diffCallback);
        this.onItemClick = onItemClick;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Sms sms = smsList.get(position);
        holder.bind(sms, typeface);
    }

    @Override
    public int getItemCount() {
        if(smsList != null){
            return smsList.size();
        }else {
            return 0;
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemMessageBinding binding;

        public MessageViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Sms sms, @Nullable Typeface typeface){
            Log.d("toan", "typeface " + typeface);
            if(sms.getName() != null && sms.getName().length() > 0){
                binding.contactName.setText(sms.getName());
            }else {
                binding.contactName.setText(sms.getAddress());
            }

            String message = sms.getMessage();
            if(message.length() > 20){
                message = message.substring(0, 20) + "...";
            }
            SimpleDateFormat target = new SimpleDateFormat(Constant.HOUR_PATTERN_1);
            SimpleDateFormat source = new SimpleDateFormat(Constant.HOUR_PATTERN);
            try{
                String time = target.format(source.parse(sms.getTime()));
                binding.messageTime.setText(time);
            }catch (ParseException ex){
                binding.messageTime.setText(sms.getTime());
            }
            binding.getRoot().setOnClickListener(v -> onItemClick.onMessageItemClick(sms));
            binding.messageContent.setText(message);
            Picasso.get().load(sms.getImage()).placeholder(R.drawable.user_icon).into(binding.messageIcon);
            if(typeface == null){
                Typeface typeface1 = ResourcesCompat.getFont(context, R.font.sf_pro_text_semibold);
                Typeface typeface2 = ResourcesCompat.getFont(context, R.font.sf_pro_text_regular);
                Typeface typeface3 = ResourcesCompat.getFont(context, R.font.sf_pro_text_medium);
                if(sms.getReadState().equals(Constant.UNREAD_STATE)){
                    Log.d("toan", sms + " not read");
                    binding.contactName.setTypeface(typeface1);
                    binding.messageContent.setTextColor(Color.BLACK);
                    binding.messageContent.setTypeface(typeface1);
                }else {
                    Log.d("toan", sms + " read");
                    binding.messageContent.setTextColor(Color.parseColor(readStateColor));
                    binding.messageContent.setTypeface(typeface2);
                    binding.contactName.setTypeface(typeface3);
                }
                binding.messageTime.setTypeface(typeface2);
            } else {
                binding.messageContent.setTypeface(typeface);
                binding.messageTime.setTypeface(typeface);
            }
        }
    }

    private final static DiffUtil.ItemCallback<Sms> diffCallback = new DiffUtil.ItemCallback<Sms>(

    ) {
        @Override
        public boolean areItemsTheSame(@NonNull Sms oldItem, @NonNull Sms newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Sms oldItem, @NonNull Sms newItem) {
            return oldItem.equals(newItem);
        }
    };

}
