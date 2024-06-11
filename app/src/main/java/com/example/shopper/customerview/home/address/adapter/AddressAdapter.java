package com.example.shopper.customerview.home.address.adapter;

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
import com.example.shopper.customerview.home.address.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addressList;
    private Context context;
    private OnCheckedChangeListener checkClick;
    private DeleteClick deleteClick;

    public AddressAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Address> list) {
        this.addressList = list;
        notifyDataSetChanged();
    }

    public void setCheckClick(OnCheckedChangeListener checkClick) {
        this.checkClick = checkClick;
    }

    public void setDeleteClick(DeleteClick deleteClick) {
        this.deleteClick = deleteClick;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        if (addressList == null) return;

        holder.check.setChecked(address.getCheck());
        holder.Name.setText(address.getName());
        holder.SDT.setText(address.getSdt());
        holder.DiaChi.setText(address.getDiaChi());
        holder.PhuongXa.setText(address.getPhuongXa());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClick != null) {
                    deleteClick.deleteClickOn(holder.getAdapterPosition());
                }
            }
        });

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkClick != null) {
                    checkClick.onCheckedChange(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (addressList.size() != 0) {
            return addressList.size();
        }
        return 0;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int position);
    }

    public interface DeleteClick {
        void deleteClickOn(int position);
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        RadioButton check;
        TextView Name, SDT, DiaChi, PhuongXa;
        ImageView delete;

        public AddressViewHolder(@NonNull View view) {
            super(view);
            check = view.findViewById(R.id.Check);
            Name = view.findViewById(R.id.txt_Ten);
            SDT = view.findViewById(R.id.txt_SDT);
            DiaChi = view.findViewById(R.id.txt_DC);
            PhuongXa = view.findViewById(R.id.txt_PX);
            delete = view.findViewById(R.id.btn_delete);
        }
    }

}