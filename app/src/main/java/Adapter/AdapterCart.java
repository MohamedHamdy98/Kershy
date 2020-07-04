package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testeverything.R;

import java.util.ArrayList;

import Model.ModelCart;

public class AdapterCart extends RecyclerView.ViewHolder {
    public TextView name,price,numItem;
    public AdapterCart(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textView_name_cart);
        price = itemView.findViewById(R.id.textView_price_cart);
        numItem = itemView.findViewById(R.id.textView_number_item_cart);
    }

}
