package view.ui.CategoryItem;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_burger, container, false);
        ButterKnife.bind(this, root);
       // startRecyclerView();
       recyclerView();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView();
    }

    public void recyclerView(){
        recyclerViewBurger.setHasFixedSize(true);
        recyclerViewBurger.setLayoutManager(new LinearLayoutManager(getActivity()));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Menu");
        databaseReference.child("Burger").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelBurgerArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelBurger modelBurger = snapshot.getValue(ModelBurger.class);
                    modelBurgerArrayList.add(modelBurger);
                }
                myAdapterBurger = new MyAdapterBurger(modelBurgerArrayList, context);
                recyclerViewBurger.setAdapter(myAdapterBurger);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void startRecyclerView() {
        modelBurgerArrayList.add(new ModelBurger("Meat",
                "At first glance, this is not a suitable spice for coffee, which can spoil the drink, but it is only at first glance. In fact, pepper gives a highlight to this aromatic and tasty drink and only allows you to open new impressions",
                "50", R.drawable.burgeritemback));
        modelBurgerArrayList.add(new ModelBurger("Creamy nachos",
                "Many coffee lovers in the fall enjoy spicy pumpkin latte, but if you want to save or make this drink out of season, you can easily make it at home. Drink can be made on the stove or in the microwave.",
                "20", R.drawable.burger));
        modelBurgerArrayList.add(new ModelBurger("Maharaja mac",
                "Indian cuisine is famous for its abundance of spices and spices. Coffee drink, popular in this country, is no exception.",
                "80", R.drawable.b));
        modelBurgerArrayList.add(new ModelBurger("Mc Veggie mac",
                "In Brazil, they love coffee, they don\\’t just adore it. The average Brazilian drinks up to 12 cups of coffee a day and therefore the technology of making coffee in Brazil is perfect. In Brazil, just a cup of hot coffee is a symbol of respect and hospitality.",
                "40", R.drawable.burgeritemback));
        recyclerViewBurger.setHasFixedSize(true);
        myAdapterBurger = new MyAdapterBurger(modelBurgerArrayList, context);
        recyclerViewBurger.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewBurger.setAdapter(myAdapterBurger);
    }


//    public void onClick() {
//        buttonBurgerItemBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
//        cardBurgerItemBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
//
//
//    }
}