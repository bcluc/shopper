package com.example.shopper.staffview.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.authentication.model.User;
import com.example.shopper.staffview.chat.activity.SpecificChat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatFragmentAdapter extends RecyclerView.Adapter<ChatFragmentAdapter.NoteViewHolder> implements Filterable {
    Context context;
    ArrayList<User> arrayList;
    ArrayList<User> arrayListOld;

    public ChatFragmentAdapter(Context context, ArrayList<User> objects) {
        this.arrayList = objects;
        this.context = context;
        this.arrayListOld = objects;
    }

    @NonNull
    @Override
    public ChatFragmentAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_row,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        User firebasemodel = arrayList.get(position);
        if(firebasemodel == null)
        {
            return;
        }
        noteViewHolder.particularusername.setText(firebasemodel.getFullName());
        String uri=firebasemodel.getAvatar();

        Picasso.get().load(uri).into(noteViewHolder.mimageviewofuser);
        if(firebasemodel.getStatus().equals("Online"))
        {
            noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
            noteViewHolder.statusofuser.setTextColor(Color.GREEN);
        }
        else
        {
            noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
        }

        noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), SpecificChat.class);
                intent.putExtra("name",firebasemodel.getFullName());
                intent.putExtra("receiveruid",firebasemodel.getUserId());
                intent.putExtra("imageuri",firebasemodel.getAvatar());
                intent.putExtra("type",firebasemodel.getUserType());
                view.getContext().startActivity(intent);
            }
        });
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        if(arrayList!=null)
            return arrayList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.isEmpty()){
                    arrayList = arrayListOld;
                }
                else{
                    ArrayList<User> list = new ArrayList<>();
                    for(User object : arrayListOld){
                        if(object.getFullName().toLowerCase().contains(search.toLowerCase())){
                            list.add(object);
                        }
                    }
                    arrayList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setData(ArrayList<User> arrayList)
    {
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView particularusername;
        private TextView statusofuser;
        private ImageView mimageviewofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            mimageviewofuser=itemView.findViewById(R.id.imageviewofuser);
        }
    }
}
