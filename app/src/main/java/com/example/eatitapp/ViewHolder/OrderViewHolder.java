package com.example.eatitapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatitapp.Interface.ItemClickListener;
import com.example.eatitapp.R;

import org.w3c.dom.Text;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        //Initialize textviews
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);

        itemView.setOnClickListener(this);
    }

    public void setTxtOrderId(TextView txtOrderId) {
        this.txtOrderId = txtOrderId;
    }

    public void setTxtOrderStatus(TextView txtOrderStatus) {
        this.txtOrderStatus = txtOrderStatus;
    }

    public void setTxtOrderPhone(TextView txtOrderPhone) {
        this.txtOrderPhone = txtOrderPhone;
    }

    public void setTxtOrderAddress(TextView txtOrderAddress) {
        this.txtOrderAddress = txtOrderAddress;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
