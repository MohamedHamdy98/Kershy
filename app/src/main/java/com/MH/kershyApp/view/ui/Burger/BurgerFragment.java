package com.MH.kershyApp.view.ui.Burger;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MH.kershyApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.MH.kershyApp.Adapter.Categories.MyAdapterBurger;
import com.MH.kershyApp.Model.Categories.ModelBurger;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BurgerFragment extends Fragment {

    ArrayList<ModelBurger> modelBurgerArrayList = new ArrayList<>();
    @BindView(R.id.recyclerView_burger)
    RecyclerView recyclerViewBurger;
    MyAdapterBurger myAdapterBurger;
    Context context;
    @BindView(R.id.textView_rating)
    TextView textViewRating;
    @BindView(R.id.textView_timeDelivery)
    TextView textViewTimeDelivery;
    @BindView(R.id.prgressBar)
    ProgressBar prgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_burger, container, false);
        ButterKnife.bind(this, root);
        showTimeAndRating();
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView();
    }

    private void recyclerView() {
        prgressBar.setVisibility(View.VISIBLE);
        recyclerViewBurger.setHasFixedSize(true);
        recyclerViewBurger.setLayoutManager(new LinearLayoutManager(getActivity()));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Menu").child("Burger");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelBurgerArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelBurger modelBurger = snapshot.getValue(ModelBurger.class);
                    modelBurgerArrayList.add(modelBurger);
                }
                myAdapterBurger = new MyAdapterBurger(modelBurgerArrayList, context);
                recyclerViewBurger.setAdapter(myAdapterBurger);
                prgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showTimeAndRating() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String time = dataSnapshot.child("TimeDelivery").getValue(String.class);
                textViewTimeDelivery.setText(time);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Rating").addValueEventListener(new ValueEventListener() {
            float sum;
            int total = 0;
            float avg;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Float rating = snapshot.getValue(Float.class);
                    sum = (sum + rating);
                    total++;
                }
                if (total != 0) {
                    avg = sum / total;
                    textViewRating.setText(avg + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}