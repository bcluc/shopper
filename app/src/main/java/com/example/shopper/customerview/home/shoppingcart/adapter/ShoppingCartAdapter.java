package com.example.shopper.customerview.home.shoppingcart.adapter;

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
import com.example.shopper.customerview.home.shoppingcart.model.Cart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>{
    private List<Cart> data;
    private Context context;
    private OnButtonAddClickListener onButtonAddClickListener;
    private OnButtonMinusClickListener onButtonMinusClickListener;
    private OnButtonCheck onButtonCheck;
    private OnItemClick onitemClick;
    private OnButtonDeleteClick onButtonDeleteClick;
    private OnCheckedChangeListener onCheckedChangeListener;


    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }
    public void setOnButtonAddClickListener(OnButtonAddClickListener onButtonAddClickListener) {
        this.onButtonAddClickListener = onButtonAddClickListener;
    }

    public void setOnButtonMinusClickListener(OnButtonMinusClickListener onButtonMinusClickListener) {
        this.onButtonMinusClickListener = onButtonMinusClickListener;
    }

    public void setOnButtonCheck(OnButtonCheck onButtonCheck) {
        this.onButtonCheck = onButtonCheck;
    }

    public void setOnitemClick(OnItemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    public void setOnButtonDeleteClick(OnButtonDeleteClick onButtonDeleteClick) {
        this.onButtonDeleteClick = onButtonDeleteClick;
    }
    public interface OnCheckedChangeListener {
        void onCheckedChange(int position);
    }
    public interface OnButtonAddClickListener {
        void onButtonAddClick(int position);
    }
    public interface OnButtonCheck {
        void onButtonCheckClick(int position);
    }
    public interface OnButtonMinusClickListener {
        void onButtonMinusClick(int position);
    }
    public interface OnItemClick {
        void onButtonItemClick(int position);
    }
    public interface OnButtonDeleteClick {
        void onButtonDeleteClick(int position);
    }
    public void setData(List<Cart> list)
    {
        this.data = list;
        notifyDataSetChanged();
    }
    public ShoppingCartAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        Cart cart = data.get(position);
        if(cart == null) return;

        holder.check.setChecked(cart.isCheck());
        Picasso.get().load(cart.getDataImage()).into(holder.imagesp);
        holder.tensp.setText(cart.getTenSanPham());
        holder.soluong.setText(String.valueOf(cart.getSoLuong()));
        holder.kichthuoc.setText(cart.getSize());
        holder.mausac.setText(cart.getMauSac());
        holder.giaban.setText(String.valueOf(cart.getGiaTien()));
        holder.check.setChecked(cart.isCheck());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onButtonDeleteClick != null){
                    onButtonDeleteClick.onButtonDeleteClick(holder.getAdapterPosition());
                }
            }
        });
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onButtonMinusClickListener != null){
                    onButtonMinusClickListener.onButtonMinusClick(holder.getAdapterPosition());
                }
            }
        });
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onButtonAddClickListener != null){
                    onButtonAddClickListener.onButtonAddClick(holder.getAdapterPosition());
                }
            }
        });
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCheckedChangeListener != null){
                    onCheckedChangeListener.onCheckedChange(holder.getAdapterPosition());
                    //holder.check.setChecked(!cart.isCheck());
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        if(data != null){
            return data.size();
        }
        return 0;
    }

    public class ShoppingCartViewHolder extends RecyclerView.ViewHolder{
        private ImageView imagesp, btn_minus, btn_add, btn_delete;
        private RadioButton check;
        private TextView tensp, kichthuoc, mausac, giaban, soluong;

        public ShoppingCartViewHolder(@NonNull View itemView){
            super(itemView);
            imagesp = itemView.findViewById(R.id.image_product);
            check = itemView.findViewById(R.id.check_button);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_add = itemView.findViewById(R.id.btn_add);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            tensp = itemView.findViewById(R.id.txt_tensanpham);
            kichthuoc = itemView.findViewById(R.id.txt_KichThuoc);
            mausac = itemView.findViewById(R.id.txt_MauSac);
            giaban = itemView.findViewById(R.id.txt_giaban);
            soluong = itemView.findViewById(R.id.txt_soluong);
        }
    }
}
