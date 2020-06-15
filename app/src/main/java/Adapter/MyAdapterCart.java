package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testeverything.R;

import java.util.ArrayList;

import Model.ModelCart;

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.ViewHolder> {
    private ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    Context context;

    public MyAdapterCart(ArrayList<ModelCart> modelCartArrayList, Context context) {
        this.modelCartArrayList = modelCartArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCart modelCart = modelCartArrayList.get(position);
        holder.textView_nameCart.setText(modelCart.getName_cart());
        holder.textView_priceCart.setText(modelCart.getPrice());
        holder.textView_numberItem.setText(modelCart.getNumber_item());
    }

    @Override
    public int getItemCount() {
        return modelCartArrayList.size();
    }

    public void setList(ArrayList<ModelCart> models) {
        this.modelCartArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_nameCart,textView_priceCart,textView_numberItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_nameCart = itemView.findViewById(R.id.textView_name_cart);
            textView_priceCart = itemView.findViewById(R.id.textView_price_cart);
            textView_numberItem = itemView.findViewById(R.id.textView_number_item_cart);
        }
    }
}
