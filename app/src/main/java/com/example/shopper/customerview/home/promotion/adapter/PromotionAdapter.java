package com.example.shopper.customerview.home.promotion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.promotion.model.Promotion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    private List<Promotion> promotionsList;
    private Context context;
    private OnCheckedChangeListener checkClick;

    public void setData(List<Promotion> list){
        this.promotionsList = list;
        notifyDataSetChanged();
    }
    public void setCheckClick(OnCheckedChangeListener checkClick) {
        this.checkClick = checkClick;
    }
    public interface OnCheckedChangeListener {
        void onCheckedChange(int position);
    }
    public PromotionAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion_apply, parent, false);
        return new PromotionAdapter.PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionAdapter.PromotionViewHolder holder, int position) {
        Promotion promotion = promotionsList.get(position);
        if(promotion == null) return;

        Picasso.get().load(promotion.getHinhAnhKM()).into(holder.imageView);
        holder.TenKM.setText(promotion.getTenKM());
        holder.DonToiThieu.setText(String.valueOf(promotion.getDonToiThieu()));
        holder.NgayBD.setText(promotion.getNgayBatDau());
        holder.NgayKT.setText(promotion.getNgayKetThuc());
        holder.check.setChecked(promotion.isCheck());

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkClick != null){
                    checkClick.onCheckedChange(holder.getLayoutPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(promotionsList.size() != 0)
        {
            return promotionsList.size();
        }
        return 0;
    }
    public class PromotionViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView DonToiThieu;
        TextView TenKM;
        TextView NgayBD;
        TextView NgayKT;
        RadioButton check;
        public PromotionViewHolder(@NonNull View view){
            super(view);
            imageView = view.findViewById(R.id.imagePromotion);
            DonToiThieu = view.findViewById(R.id.txtDetailsPromotion);
            TenKM = view.findViewById(R.id.txtNamePromotion);
            NgayBD = view.findViewById(R.id.txtStartPromotion);
            NgayKT = view.findViewById(R.id.txtEndPromotion);
            check = view.findViewById(R.id.Check);
        }
    }

}