package com.example.shopper.staffview.category.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.staffview.category.model.MyCategory;

import java.util.ArrayList;
import java.util.List;

public class MyCategoryAdapter extends RecyclerView.Adapter<MyCategoryAdapter.ViewHolder> implements Filterable {

    private static List<MyCategory> myCategoryList;
    private List<MyCategory> oldcategoryList;
    private static OnItemClickListener onItemClickListener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.isEmpty()){
                    myCategoryList = oldcategoryList;
                }
                else{
                    List<MyCategory> list = new ArrayList<>();
                    for(MyCategory object : oldcategoryList){
                        if(object.getCategoryName().toLowerCase().contains(search.toLowerCase())){
                            list.add(object);
                        }
                    }
                    myCategoryList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = myCategoryList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                myCategoryList = (ArrayList<MyCategory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(MyCategory myCategory);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MyCategoryAdapter(List<MyCategory> myCategoryList) {
        this.myCategoryList = myCategoryList;
        this.oldcategoryList = myCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCategory myCategory = myCategoryList.get(position);
        holder.txtName.setText(myCategory.getCategoryName());
        holder.txtQuantity.setText(String.valueOf(myCategory.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(myCategory.getCategoryImage())
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return myCategoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItem;
        public TextView txtName;
        public TextView txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item_img);
            txtName = itemView.findViewById(R.id.txt_item_name);
            txtQuantity = itemView.findViewById(R.id.txt_item_quanity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            MyCategory clickedItem = myCategoryList.get(position);
                            onItemClickListener.onItemClick(clickedItem);
                        }
                    }
                }
            });
        }
    }
}