package Adapter.CategoriesCart;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.ViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Cart");

    private ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    Context context;
    String userId;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ModelCart modelCart = modelCartArrayList.get(position);
        holder.textViewNameCart.setText(modelCart.getName());
        holder.textViewNumberItemCart.setText(modelCart.getNumItem());
        holder.textViewPriceCart.setText(modelCart.getPrice());
        int onePrice = ((Integer.valueOf(modelCart.getPrice()))) * ((Integer.valueOf(modelCart.getNumItem())));
        holder.textViewPriceCart.setText(String.valueOf(onePrice));
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = database.getReference().child("Cart")
                        .child(userId).child("Order").child(modelCart.name);
                databaseReference.removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelCartArrayList.size();
    }

    public void setList(ArrayList<ModelCart> models) {
        this.modelCartArrayList = models;
        notifyDataSetChanged();
    }

    public void grandTotal() {
        int oneType;
        int overTotal =0 ;
        for (int i = 0; i < modelCartArrayList.size(); i++) {
            ModelCart modelCart = modelCartArrayList.get(i);
            oneType = ((Integer.valueOf(modelCart.getPrice()))) * ((Integer.valueOf(modelCart.getNumItem())));
            overTotal = overTotal + oneType;
            databaseReference = database.getReference("totalPrice");
            databaseReference.setValue(String.valueOf(overTotal));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_name_cart)
        TextView textViewNameCart;
        @BindView(R.id.textView_price_cart)
        TextView textViewPriceCart;
        @BindView(R.id.textView_number_item_cart)
        TextView textViewNumberItemCart;
        @BindView(R.id.imageView_delete)
        ImageView imageViewDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
