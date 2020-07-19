package view.ui.CategoryItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.Categories.MyAdapterBurger;
import Model.Categories.ModelBurger;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_burger, container, false);
        ButterKnife.bind(this, root);
        showTimeAndRating();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView();
    }

    private void recyclerView() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerViewBurger.setHasFixedSize(true);
        recyclerViewBurger.setLayoutManager(new LinearLayoutManager(getActivity()));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Menu").child("Burger");
        // databaseReference.child("M").child("Burger").push().getKey();
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
                progressDialog.dismiss();
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
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        Float numberRating = data.getValue(Float.class);

                        textViewRating.setText(numberRating + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}