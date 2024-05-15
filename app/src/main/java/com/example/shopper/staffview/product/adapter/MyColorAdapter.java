package com.example.shopper.staffview.product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.product.model.MyColor;

import java.util.ArrayList;
import java.util.List;

public class MyColorAdapter extends RecyclerView.Adapter<MyColorAdapter.ColorViewHolder> {
    private List<MyColor> colorList;
    private OnColorCheckedChangeListener onColorCheckedChangeListener;
    public List<String> getSelectedColors() {
        List<String> selectedColors = new ArrayList<>();
        for (MyColor color : colorList) {
            if (color.getIsChecked()) {
                selectedColors.add(color.getColorId());
            }
        }
        return selectedColors;
    }

    public MyColorAdapter(List<MyColor> colorList) {
        this.colorList = colorList;
        this.onColorCheckedChangeListener = onColorCheckedChangeListener;
    }


    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_product, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        MyColor color = colorList.get(position);
        holder.colorNameTextView.setText(color.getColorName());
        holder.checkBox.setChecked(color.getIsChecked());
        setColorBackGround(holder.imageView, color.getColorCode());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            color.setIsChecked(isChecked);
            if (onColorCheckedChangeListener != null) {
                onColorCheckedChangeListener.onColorCheckedChange(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }
    public interface OnColorCheckedChangeListener {
        void onColorCheckedChange(MyColor color);
    }
    public void setOnColorCheckedChangeListener(OnColorCheckedChangeListener listener) {
        this.onColorCheckedChangeListener = listener;
    }
    public void setColorBackGround(ImageView imageView, String colorCode) {
        try {
            int color = android.graphics.Color.parseColor(colorCode);
            imageView.setBackgroundColor(color);
        } catch (IllegalArgumentException e) {
            // Xử lý nếu mã màu không hợp lệ
            e.printStackTrace();
        }
    }


    public class ColorViewHolder extends RecyclerView.ViewHolder {
        private TextView colorNameTextView;
        private CheckBox checkBox;
        private ImageView imageView;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);

            colorNameTextView = itemView.findViewById(R.id.txt_color_name);
            imageView = itemView.findViewById(R.id.img_color);
            checkBox = itemView.findViewById(R.id.check_color);

        }

    }


}
