package view.ui.Delivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testeverything.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryFragment extends Fragment {
    @BindView(R.id.image_order)
    ImageView imageOrder;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.button_rating)
    Button openRating;
    @BindView(R.id.rating)
    RatingBar rating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_delivery, container, false);
        ButterKnife.bind(this, root);
        saveSeekBarProgress();
        openRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float ratingNumber = rating.getRating();
                Toast.makeText(getActivity(), ratingNumber + "", Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = database.getReference("Rating")
                        .child(userId).child("Rating");
                databaseReference.setValue(ratingNumber);
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        controlDelivery();
    }

    private void controlDelivery() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Cart")
                .child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Boolean writeOrder = dataSnapshot.child("writeOrder").getValue(Boolean.class);
                    Boolean preparingOrder = dataSnapshot.child("preparingOrder").getValue(Boolean.class);
                    Boolean way = dataSnapshot.child("wayOrder").getValue(Boolean.class);
                    Boolean deliveredOrder = dataSnapshot.child("deliveredOrder").getValue(Boolean.class);
                    Boolean progress = dataSnapshot.child("progress").getValue(Boolean.class);
                    if (progress == true) {
                        Snackbar.make(getView(), "wait", Snackbar.LENGTH_SHORT).show();
                        message.setText("Please! Wait");
                        imageOrder.setImageResource(R.drawable.ic_time_180);
                    }
                    if (progress == writeOrder) {
                        Snackbar.make(getView(), "written", Snackbar.LENGTH_SHORT).show();
                        message.setText("Your order is written");
                        imageOrder.setImageResource(R.drawable.ic_order_180);
                    }
                    if (progress == preparingOrder) {
                        Snackbar.make(getView(), "Preparing", Snackbar.LENGTH_SHORT).show();
                        message.setText("Preparing your order");
                        imageOrder.setImageResource(R.drawable.ic_cook);
                    }
                    if (progress == way) {
                        Snackbar.make(getView(), "way", Snackbar.LENGTH_SHORT).show();
                        message.setText("Your order is on the way");
                        imageOrder.setImageResource(R.drawable.ic_delivery_180);
                    }
                    if (progress == deliveredOrder) {
                        Snackbar.make(getView(), "delivered", Snackbar.LENGTH_SHORT).show();
                        message.setText("The order was delivered");
                        imageOrder.setImageResource(R.drawable.ic_receive_180);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please! Choose Order.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void saveSeekBarProgress() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Cart").child(userId)
                .child("valueSeekBar");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int value = dataSnapshot.getValue(Integer.class);
                    seekBar.setProgress(value);
                } else {
                    Toast.makeText(getActivity(), "Please! Choose Order.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}