package com.example.shopper.staffview.category.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.category.model.MyCategory;

import java.util.List;

public class MyCategoryListAdapter extends RecyclerView.Adapter<MyCategoryListAdapter.ViewHolder> {

    private List<MyCategory> myCategoryList;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnCategoryCheckedChangeListener onCategoryCheckedChangeListener;

    private String selectedCategoryItem = null;

    public MyCategoryListAdapter(List<MyCategory> myCategoryList) {
        this.myCategoryList = myCategoryList;
    }

    public void setOnCategoryCheckedChangeListener(OnCategoryCheckedChangeListener listener) {
        this.onCategoryCheckedChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_category_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCategory myCategory = myCategoryList.get(position);
        holder.txtName.setText(myCategory.getCategoryName());
        holder.checkBox.setChecked(myCategory.isSelected());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đảm bảo chỉ một danh mục được chọn
                for (MyCategory item : myCategoryList) {
                    item.setSelected(false);
                }

                myCategory.setSelected(true);
                selectedCategoryItem = myCategory.getCategoryId();

                notifyDataSetChanged();

                if (onCategoryCheckedChangeListener != null) {
                    onCategoryCheckedChangeListener.onCategoryCheckedChange(myCategory);
                }
            }
        });
    }

    public String getSelectedCategory() {
        return selectedCategoryItem;
    }

    @Override
    public int getItemCount() {
        return myCategoryList.size();
    }

    public interface OnCategoryCheckedChangeListener {
        void onCategoryCheckedChange(MyCategory myCategory);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_category_name);
            checkBox = itemView.findViewById(R.id.check_category);
        }
    }
}
