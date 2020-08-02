package com.MH.kershyApp.view.ui.Sweet;

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

import com.MH.kershyApp.Adapter.Categories.MyAdapterSweet;

import com.MH.kershyApp.Model.Categories.ModelSweet;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SweetFragment extends Fragment {
    ArrayList<ModelSweet> modelSweetArrayList = new ArrayList<>();
    @BindView(R.id.recyclerView_sweet)
    RecyclerView recyclerViewSweet;
    MyAdapterSweet myAdapterSweet;
    @BindView(R.id.textView_rating)
    TextView textViewRating;
    @BindView(R.id.textView_timeDelivery)
    TextView textViewTimeDelivery;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Menu").child("Sweet");
    @BindView(R.id.prgressBar)
    ProgressBar prgressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sweet, container, false);
        ButterKnife.bind(this,root);
        startRecyclerView();
        showTimeAndRating();
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
    public void startRecyclerView() {
        prgressBar.setVisibility(View.VISIBLE);
        recyclerViewSweet.setHasFixedSize(true);
        recyclerViewSweet.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelSweetArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelSweet modelSweet = snapshot.getValue(ModelSweet.class);
                    modelSweetArrayList.add(modelSweet);
                }
                myAdapterSweet = new MyAdapterSweet(modelSweetArrayList, getActivity());
                recyclerViewSweet.setAdapter(myAdapterSweet);
                prgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showTimeAndRating(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
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