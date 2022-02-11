package com.example.message.ui.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.message.data.model.Contact;
import com.example.message.databinding.ItemSearchContactBinding;
import com.example.message.di.OnItemClick;
import java.util.ArrayList;
import java.util.List;

public class SearchContactsAdapter extends ListAdapter<Contact, SearchContactsAdapter.ContactViewHolder> {
    private List<Contact> contactList = new ArrayList<>();
    protected OnItemClick onItemClick;
    private Typeface typeface;

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public SearchContactsAdapter(OnItemClick onItemClick) {
        super(diffCallback);
        this.onItemClick = onItemClick;
        typeface = null;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchContactBinding binding = ItemSearchContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        if(contactList != null){
            return contactList.size();
        }else {
            return 0;
        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        private final ItemSearchContactBinding binding;

        public ContactViewHolder(@NonNull ItemSearchContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Contact contact){
            binding.contactName.setOnClickListener(view -> onItemClick.onContactItemClick(contact));
            binding.contactPhone.setOnClickListener(view -> onItemClick.onContactItemClick(contact));
            binding.contactName.setText(contact.getName());
            binding.contactPhone.setText(contact.getPhoneNumber());
            if(typeface != null){
                binding.contactName.setTypeface(typeface);
                binding.contactPhone.setTypeface(typeface);
            }
        }
    }

    @Override
    public void submitList(@Nullable List<Contact> list) {
        super.submitList(list);
    }

    private final static DiffUtil.ItemCallback<Contact> diffCallback = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getImage().equals(newItem.getImage());
        }
    };

}
