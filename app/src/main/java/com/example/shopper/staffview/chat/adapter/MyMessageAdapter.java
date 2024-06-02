package com.example.shopper.staffview.chat.adapter;

import static com.example.shopper.R.drawable.background_message_blue;
import static com.example.shopper.R.drawable.background_message_white;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.chat.model.MyMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyViewHolder> {
    private Context context;
    private List<MyMessage> messageList;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public MyMessageAdapter(Context context, ArrayList<MyMessage> messagesArrayList)
    {
        this.context = context;
        this.messageList = messagesArrayList;
    }

    public void add(MyMessage messageObject){
        messageList.add(messageObject);
        notifyDataSetChanged();
    }
    public void clear(){
        messageList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyMessage messageObject = messageList.get(position);
        if(messageObject == null)
        {
            return;
        }
        holder.txtContentMessage.setText(messageObject.getMessage());
        holder.txtTimeMessage.setText(messageObject.getTime());
        if (messageObject.getSenderId().equals(FirebaseAuth.getInstance().getUid()))
        {
            holder.layout_message_item.setGravity(Gravity.RIGHT);
            holder.layout_rowMessage.setBackgroundResource(background_message_blue);
            holder.txtContentMessage.setTextColor((ContextCompat.getColor(context,R.color.white)));
            holder.txtTimeMessage.setTextColor((ContextCompat.getColor(context,R.color.white)));
        }
        else if(messageObject.getReceiveId().equals(FirebaseAuth.getInstance().getUid()))
        {
            holder.layout_message_item.setGravity(Gravity.LEFT);
            holder.layout_rowMessage.setBackgroundResource(background_message_white);
            holder.txtContentMessage.setTextColor((ContextCompat.getColor(context,R.color.black)));
            holder.txtTimeMessage.setTextColor((ContextCompat.getColor(context,R.color.black)));
        }
        else if(messageObject.getReceiveId().equals("Staff"))
        {
            holder.layout_message_item.setGravity(Gravity.LEFT);
            holder.layout_rowMessage.setBackgroundResource(background_message_white);
            holder.txtContentMessage.setTextColor((ContextCompat.getColor(context,R.color.black)));
            holder.txtTimeMessage.setTextColor((ContextCompat.getColor(context,R.color.black)));
        }
        else if(messageObject.getSenderId().equals("Staff")){
            holder.layout_message_item.setGravity(Gravity.RIGHT);
            holder.layout_rowMessage.setBackgroundResource(background_message_blue);
            holder.txtContentMessage.setTextColor((ContextCompat.getColor(context,R.color.white)));
            holder.txtTimeMessage.setTextColor((ContextCompat.getColor(context,R.color.white)));
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtContentMessage, txtTimeMessage;
        private LinearLayout layout_message_item;
        private LinearLayout layout_rowMessage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_message_item = itemView.findViewById(R.id.layout_message_item);
            layout_rowMessage = itemView.findViewById(R.id.layout_rowMessage);
            txtContentMessage = itemView.findViewById(R.id.txtContentMessage);
            txtTimeMessage = itemView.findViewById(R.id.txtTimeMessage);
        }
    }
}
