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
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterOldOrders extends RecyclerView.Adapter<MyAdapterOldOrders.ViewHolder> {

    private ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    Context context;

    public MyAdapterOldOrders(ArrayList<ModelCart> modelCartArrayList, Context context) {
        this.modelCartArrayList = modelCartArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_old_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelCart modelCart = modelCartArrayList.get(position);
        holder.textViewNameOfItemOldOrders.setText(modelCart.getName());
        holder.textViewNumberOfItemOldOrders.setText(modelCart.getNumItem());
        holder.textViewTotalPriceOldOrders.setText(modelCart.getPrice());
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
        @BindView(R.id.textView_name_of_item_oldOrders)
        TextView textViewNameOfItemOldOrders;
        @BindView(R.id.textView_number_of_item_oldOrders)
        TextView textViewNumberOfItemOldOrders;
        @BindView(R.id.textView_total_price_oldOrders)
        TextView textViewTotalPriceOldOrders;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
