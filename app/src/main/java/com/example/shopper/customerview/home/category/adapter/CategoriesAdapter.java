package com.example.shopper.customerview.home.category.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.category.model.Categories;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>{

    private List<Categories> categoriesList;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onButtonItemClick(int position);
    }

    public void setData(List<Categories> list){
        this.categoriesList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);

        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Categories categories = categoriesList.get(position);
        if (categories == null) return;
        holder.txtNameCategories.setText(categories.getName());

        Picasso.get().load(categories.getImage()).into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick != null){
                    onItemClick.onButtonItemClick(holder.getAdapterPosition());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (categoriesList != null)
        {
            return categoriesList.size();
        }
        return 0;
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNameCategories;
        private ImageView imageView;
        private LinearLayout layout;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCategories = itemView.findViewById(R.id.txt_name_categories);
            imageView = itemView.findViewById(R.id.img_category_list);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}
