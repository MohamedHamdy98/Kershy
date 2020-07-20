package view.ui.Drink;

import android.app.ProgressDialog;
import android.content.Context;
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
    @BindView(R.id.textView_rating)
    TextView textViewRating;
    @BindView(R.id.textView_timeDelivery)
    TextView textViewTimeDelivery;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Menu").child("Drink");
    @BindView(R.id.prgressBar)
    ProgressBar prgressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink, container, false);
        ButterKnife.bind(this,root);
        startRecyclerView();
        showTimeAndRating();
        return root;
    }
    public void startRecyclerView() {
        prgressBar.setVisibility(View.VISIBLE);
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