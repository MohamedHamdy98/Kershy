package view.ui.Orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.example.testeverything.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.MyAdapterOldOrdersCardView;
import Adapter.MyAdapterOrders;
import Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OldOrdersFragment extends Fragment {
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    MyAdapterOrders myAdapterOrders;
    MyAdapterOldOrdersCardView myAdapterOldOrdersCardView;

    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @BindView(R.id.recyclerView_group)
    RecyclerView recyclerViewOldOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root =  inflater.inflate(R.layout.fragment_old_orders, container, false);
        ButterKnife.bind(this,root);
        start_recyclerView2();
        return root;
    }
    public void start_recyclerView() {
        recyclerViewOldOrder.setHasFixedSize(true);
        recyclerViewOldOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =  database.getReference("Cart");
        databaseReference.child(userId).child("Order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelCartArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelCart modelCart = snapshot.getValue(ModelCart.class);
                    modelCartArrayList.add(modelCart);
                }
                myAdapterOrders = new MyAdapterOrders(modelCartArrayList, getActivity());
                recyclerViewOldOrder.setAdapter(myAdapterOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void start_recyclerView2() {
        recyclerViewOldOrder.setHasFixedSize(true);
        recyclerViewOldOrder.setNestedScrollingEnabled(true);
        recyclerViewOldOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                       // modelCart = snapshot.getValue(ModelCart.class);
                        modelCartArrayList.add(modelCart);
                    }
                myAdapterOldOrdersCardView = new MyAdapterOldOrdersCardView(modelCartArrayList, getActivity());
                recyclerViewOldOrder.setAdapter(myAdapterOldOrdersCardView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}