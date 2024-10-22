package com.MH.kershyApp.view.ui.Orders;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MH.kershyApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.MH.kershyApp.Adapter.MyAdapterOrders;
import com.MH.kershyApp.Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersFragment extends Fragment {
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    MyAdapterOrders myAdapterOrders;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @BindView(R.id.recyclerView_order)
    RecyclerView recyclerViewOrder;
    @BindView(R.id.textView_date_order)
    TextView textViewDateOrder;
    @BindView(R.id.textView_cart_itemTotal)
    TextView textViewCartItemTotal;
    @BindView(R.id.textView_cart_deliveryFee)
    TextView textViewCartDeliveryFee;
    @BindView(R.id.textView_cart_offer)
    TextView textViewCartOffer;
    @BindView(R.id.textView_cart_totalPrice)
    TextView textViewCartTotalPrice;
    Context context;
    @BindView(R.id.textView_cart_tax)
    TextView textViewCartTax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, root);
        getFirebase();
        getTimeOrder();
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    @Override
    public void onStart() {
        super.onStart();
        start_recyclerView();
    }
    // For get Time order
    public void getTimeOrder() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        textViewDateOrder.setText(currentDate);
    }
    // For get offers,deliverFee,tax,totalPrice to calculate bill and set them in database..
    public void getFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String totalPrice = dataSnapshot.child("Cart").child(userId).child("totalPrice").getValue(String.class);
                    String offer = dataSnapshot.child("Offers").getValue(String.class);
                    String deliveryFee = dataSnapshot.child("deliveryFee").getValue(String.class);
                    String tax = dataSnapshot.child("Tax").getValue(String.class);
                    if (!TextUtils.isEmpty(totalPrice) && !TextUtils.isEmpty(offer)
                            && !TextUtils.isEmpty(deliveryFee) && !TextUtils.isEmpty(tax)) {
                        textViewCartOffer.setText(offer);
                        textViewCartDeliveryFee.setText(deliveryFee);
                        textViewCartItemTotal.setText(totalPrice);
                        textViewCartTax.setText(tax);
                        if (totalPrice == "0") {
                            databaseReference.child("Cart").child(userId).child("TotalPriceToPay").setValue("0");
                        } else {
                            int Bill = (Integer.parseInt(totalPrice) + Integer.parseInt(deliveryFee)
                                    + (Integer.parseInt(tax))
                                    - Integer.parseInt(offer));
                            databaseReference = database.getReference("Cart").child(userId).child("TotalPriceToPay");
                            databaseReference.setValue(String.valueOf(Bill));
                            String bill = dataSnapshot.child("Cart").child(userId).child("TotalPriceToPay")
                                    .getValue(String.class);
                            textViewCartTotalPrice.setText(bill);
                        }
                    }
                } else {
                    Toast.makeText(context, "Please! Choose Order.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void start_recyclerView() {
        recyclerViewOrder.setHasFixedSize(true);
        recyclerViewOrder.setNestedScrollingEnabled(true);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference = database.getReference("Cart");
        databaseReference.child(userId).child("Order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelCartArrayList.clear();
                ModelCart modelCart;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelCart = snapshot.getValue(ModelCart.class);
                    modelCartArrayList.add(modelCart);
                }
                myAdapterOrders = new MyAdapterOrders(modelCartArrayList, context);
                recyclerViewOrder.setAdapter(myAdapterOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}