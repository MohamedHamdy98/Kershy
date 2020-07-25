package view.ui.CategoryItem;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
    DatabaseReference databaseReference = database.getReference("Menu").child("Offers");
    @BindView(R.id.prgressBar)
    ProgressBar prgressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        startRecyclerView();
        // To Stop button back...
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                }
                return false;
            }
        });
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
                prgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}