package com.example.shopper.staffview.product.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<String> removedImageUrls = new ArrayList<>();

    // Thêm phương thức này để trả về danh sách các ảnh đã bị xóa
    public List<String> getRemovedImageUrls() {
        return removedImageUrls;
    }

    private List<String> imageUrls;


    public List<String> getImageUrls() {
        return imageUrls;
    }
    private OnImageClickListener onImageClickListener;

    public ImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R
                .layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);
        // Thiết lập OnClickListener cho ImageButton
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức onImageClick trong onImageClickListener để xóa ảnh tại vị trí đã cho
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(position);
                }
            }
        });
    }
    public void addImage(String imageUrl) {
        imageUrls.add(imageUrl);
        notifyDataSetChanged();
    }

    public void onImageClick(int clickedPosition) {
        // Xóa ảnh tại vị trí đã chọn khi nhấp vào ImageButton
        if (clickedPosition >= 0 && clickedPosition < imageUrls.size()) {
            // Lấy URL của ảnh đã bị xóa
            String removedImageUrl = imageUrls.get(clickedPosition);

            // Thêm URL của ảnh đã bị xóa vào danh sách removedImageUrls
            removedImageUrls.add(removedImageUrl);

            // Xóa ảnh khỏi danh sách imageUrls
            imageUrls.remove(clickedPosition);

            // Cập nhật lại RecyclerView
            notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgViewAdd);
            deleteButton = itemView.findViewById(R.id.imgViewDelete);
        }
    }

    // Interface để xử lý sự kiện click vào ImageButton
    public interface OnImageClickListener {
        void onImageClick(int position);
    }
}
