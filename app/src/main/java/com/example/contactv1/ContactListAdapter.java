package com.example.contactv1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactHolder> implements Filterable {
    private Context context;
    private List<Contact> listFull;
    private List<Contact> listFilter;



    static Intent intentSendEditContact;


    public class ContactHolder extends RecyclerView.ViewHolder {
        public LinearLayout llItem;
        public TextView tvName;
        public ImageView civAvatar;
        public Button btnCall;

        ContactHolder(View itemView) {
            super(itemView);

            llItem = itemView.findViewById(R.id.ll_item);
            tvName = itemView.findViewById(R.id.tv_item_name);
            civAvatar = itemView.findViewById(R.id.civ_avatar);

            intentSendEditContact = new Intent(context, EditContact.class);
            btnCall = itemView.findViewById(R.id.btn_call);
        }
    }

    public ContactListAdapter(Context context, List<Contact> exampleList) {
        System.out.println("Constructor Adapter");
        this.context = context;
        listFilter = exampleList;
        System.out.println("Adapter get list size: " + exampleList.size());
        listFull = listFilter;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ContactHolder holder, final int position) {
        System.out.println("Holder: " + listFilter.get(position));
        final Contact contact = listFilter.get(position);
        holder.tvName.setText(contact.getmName());

        byte[] byteArr = contact.getmAvatar();
        if (byteArr != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
            holder.civAvatar.setImageBitmap(bitmap);
        } else
            holder.civAvatar.setImageResource(R.drawable.ic_contact);



        // Xu ly Call button
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.format("tel:%s", contact.getmMobile());
                Uri uri = Uri.parse(number);
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(uri);
                context.startActivity(intentCall);
            }
        });

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = listFilter.get(position);
                intentSendEditContact.putExtra("contact", contact);
                intentSendEditContact.putExtra("position", position);
                context.startActivity(intentSendEditContact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();
            System.out.println("Ban nhap: " + constraint);
            if (constraint == null || constraint.length() == 0) {
                filteredList = listFull;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contact item : listFull) {
                    if (item.getmName().toLowerCase().contains(filterPattern)) {
                        System.out.println(item);
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            System.out.println("Toi co ket qua: " + filteredList.size());
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listFilter = (ArrayList<Contact>) results.values;
//            System.out.println("Toi publish: "+listFilter.get(0));
            notifyDataSetChanged();
        }
    };
}
