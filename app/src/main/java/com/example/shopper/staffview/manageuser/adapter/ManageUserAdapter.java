package com.example.shopper.staffview.manageuser.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.authentication.model.User;
import com.example.shopper.staffview.utils.itfRCVListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageUserAdapter extends RecyclerView.Adapter<ManageUserAdapter.ListUsersHolder> implements Filterable {
    private itfRCVListItem itfRCVListItem;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Context context;
    ArrayList<User> arrayList;
    ArrayList<User> arrayListOld;
    public ManageUserAdapter(Context context, ArrayList<User> objects, itfRCVListItem itfRCVListItem) {
        this.arrayList = objects;
        this.context = context;
        this.arrayListOld = objects;
        this.itfRCVListItem = itfRCVListItem;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ListUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_manage_user,
                parent,false);
        return new ListUsersHolder(view, itfRCVListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUsersHolder holder, int position) {
        User object = arrayList.get(position);
        if(object == null)
        {
            return;
        }
        String uri=object.getAvatar();
        try{
            if(uri.isEmpty())
            {
            }
            else Picasso.get().load(uri).into(holder.ava);
        }
        catch (Exception e)
        {

        }
        if(object.getStatus().equals("Online"))
        {
            holder.status.setText(object.getStatus());
            holder.status.setTextColor(Color.GREEN);
        }
        else
        {
            holder.status.setText(object.getStatus());
        }
        holder.name.setText(object.getFullName());
        holder.btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Resetting password...");
                progressDialog.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Sending request resetting password for this Staff")
                        .setMessage("Are you sure want to send ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.sendPasswordResetEmail(object.getEmail())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context,
                                                            "Reset password email sent",
                                                            Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                } else {
                                                    Toast.makeText(context,
                                                            "Error sending reset password email",
                                                            Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.dismiss();
                            }
                        });
                builder.show();
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


    public static class ListUsersHolder extends RecyclerView.ViewHolder{
        private ImageView ava;
        private TextView name;
        private TextView status;
        private Button btn_reset;
        public ListUsersHolder(@NonNull View itemView, itfRCVListItem itf_rcv_listItem) {
            super(itemView);
            ava = itemView.findViewById(R.id.img_staff);
            name = itemView.findViewById(R.id.txt_admin_name);
            status = itemView.findViewById(R.id.txt_status);
            btn_reset = itemView.findViewById(R.id.btn_reset);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itf_rcv_listItem != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            itf_rcv_listItem.onClick(pos);
                        }
                    }
                }
            });
        }
    }
    public void setData(ArrayList<User> arrayList)
    {
        this.arrayList=arrayList;
        notifyDataSetChanged();
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

}
