package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterOldOrdersCardView extends RecyclerView.Adapter<MyAdapterOldOrdersCardView.ViewHolder> {

    private ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    Context context;

    public MyAdapterOldOrdersCardView(ArrayList<ModelCart> modelCartArrayList, Context context) {
        this.modelCartArrayList = modelCartArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_view_old_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.recyclerViewOldOrdersCard.setNestedScrollingEnabled(false);
        holder.recyclerViewOldOrdersCard.setHasFixedSize(true);
        holder.recyclerViewOldOrdersCard.setLayoutManager(new LinearLayoutManager(context));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference =  database.getReference("Cart");
        databaseReference.child(userId).child("OldOrders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelCart modelCart = new ModelCart();
                modelCartArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String numItem = snapshot.child("numItem").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);
                    modelCart.setName(name);
                    modelCart.setNumItem(numItem);
                    modelCart.setPrice(price);
                    //modelCart = snapshot.getValue(ModelCart.class);
                    modelCartArrayList.add(modelCart);
                }
                MyAdapterOrders myAdapterOrders;
                myAdapterOrders = new MyAdapterOrders(modelCartArrayList, context);
                holder.recyclerViewOldOrdersCard.setAdapter(myAdapterOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView_OldOrdersCard)
        RecyclerView recyclerViewOldOrdersCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
