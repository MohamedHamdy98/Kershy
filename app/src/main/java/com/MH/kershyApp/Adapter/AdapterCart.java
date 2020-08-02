package com.MH.kershyApp.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.MH.kershyApp.R;

public class AdapterCart extends RecyclerView.ViewHolder {
    public TextView name,price,numItem;
    public AdapterCart(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textView_name_cart);
        price = itemView.findViewById(R.id.textView_price_cart);
        numItem = itemView.findViewById(R.id.textView_number_item_cart);
    }

}
