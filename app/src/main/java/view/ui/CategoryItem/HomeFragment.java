package view.ui.CategoryItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testeverything.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.MyAdapterItemOffer;
import Model.ModelItemOffer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    ArrayList<ModelItemOffer> modelItemOfferArrayList = new ArrayList<>();
    MyAdapterItemOffer adapterItemOffer;
    @BindView(R.id.recyclerView_item_offer)
    RecyclerView recyclerViewItemOffer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Menu").child("offer");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);

        startRecyclerView();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//        databaseReference = database.getReference("Menu").child("Burger").child("0");
//        databaseReference.child("price_burger").setValue("40");
        return root;
    }


    public void startRecyclerView() {
        recyclerViewItemOffer.setHasFixedSize(true);
        recyclerViewItemOffer.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelItemOfferArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelItemOffer modelItemOffer = snapshot.getValue(ModelItemOffer.class);
                    modelItemOfferArrayList.add(modelItemOffer);
                }
                adapterItemOffer = new MyAdapterItemOffer(modelItemOfferArrayList, getActivity());
                recyclerViewItemOffer.setAdapter(adapterItemOffer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}