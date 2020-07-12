package view.ui.Drink;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import Adapter.Categories.MyAdapterDrink;
import Adapter.Categories.MyAdapterSweet;
import Model.Categories.ModelDrink;
import Model.Categories.ModelSweet;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrinkFragment extends Fragment {
    ArrayList<ModelDrink> modelDrinkArrayList = new ArrayList<>();
    @BindView(R.id.recyclerView_drink)
    RecyclerView recyclerViewDrink;
    MyAdapterDrink myAdapterDrink;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Menu").child("Drink");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink, container, false);
        ButterKnife.bind(this,root);
        startRecyclerView();
        return root;
    }
    public void startRecyclerView() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerViewDrink.setHasFixedSize(true);
        recyclerViewDrink.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelDrinkArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelDrink modelDrink = snapshot.getValue(ModelDrink.class);
                    modelDrinkArrayList.add(modelDrink);
                }
                myAdapterDrink = new MyAdapterDrink(modelDrinkArrayList, getActivity());
                recyclerViewDrink.setAdapter(myAdapterDrink);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}