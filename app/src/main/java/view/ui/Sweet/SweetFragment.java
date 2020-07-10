package view.ui.Sweet;

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

import Adapter.Categories.MyAdapterSweet;
import Adapter.MyAdapterItemOffer;
import Model.Categories.ModelSweet;
import Model.ModelItemOffer;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SweetFragment extends Fragment {
    ArrayList<ModelSweet> modelSweetArrayList = new ArrayList<>();
    @BindView(R.id.recyclerView_sweet)
    RecyclerView recyclerViewSweet;
    MyAdapterSweet myAdapterSweet;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("M").child("Sweet");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sweet, container, false);
        ButterKnife.bind(this,root);
        startRecyclerView();
        return root;
    }
    public void startRecyclerView() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
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
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}