package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Model.Categories.ModelBurger;
import Model.ModelCart;
import Model.ModelSubCart;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.embersoft.expandabletextview.ExpandableTextView;

public class MyAdapterOrders extends RecyclerView.Adapter<MyAdapterOrders.ViewHolder> {
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    ArrayList<ModelSubCart> modelSubCarts = new ArrayList<>();

    private ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    Context context;

    public MyAdapterOrders(ArrayList<ModelCart> modelArrayList, Context context) {
        this.modelCartArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ModelCart modelCart = modelCartArrayList.get(position);
        holder.textViewNameOfItem.setText(modelCart.getName());
        holder.textViewNumberOfItem.setText(modelCart.getNumItem());
        holder.textViewTotalPriceCart.setText(modelCart.getPrice());
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
        @BindView(R.id.textView_total_price_cart)
        TextView textViewTotalPriceCart;
        @BindView(R.id.textView_name_of_item)
        TextView textViewNameOfItem;
        @BindView(R.id.textView_number_of_item)
        TextView textViewNumberOfItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
