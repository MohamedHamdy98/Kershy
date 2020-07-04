package view.ui.Delivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testeverything.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryFragment extends Fragment {
    SeekBar seekBar;
    ImageView orderImage;
    TextView textMessage;
    @BindView(R.id.write)
    Button write;
    @BindView(R.id.prepare)
    Button prepare;
    @BindView(R.id.way)
    Button way;
    @BindView(R.id.delivered)
    Button delivered;
    @BindView(R.id.switch1)

    Switch aSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_delivery, container, false);
        ButterKnife.bind(this, root);
        seekBar = root.findViewById(R.id.seekBar);
        orderImage = root.findViewById(R.id.image_order);
        textMessage = root.findViewById(R.id.message);
        seekBar.setEnabled(true);

        onClickButton();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Cart").child(userId);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    databaseReference.child("writeOrder").setValue(true);

                } else {
                    databaseReference.child("writeOrder").setValue(false);
                }

            }
        });
        // To save value is tru or false...
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean wr = dataSnapshot.child("writeOrder").getValue(Boolean.class);
                if (wr == true) {
                    aSwitch.setChecked(true);
                } else {
                    aSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickWrite() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Cart").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String wa = dataSnapshot.child("waitOrder").getValue(String.class);
                String w = dataSnapshot.child("writeOrder").getValue(String.class);
                String p = dataSnapshot.child("preparingOrder").getValue(String.class);
                String way = dataSnapshot.child("wayOrder").getValue(String.class);
                String d = dataSnapshot.child("deliveredOrder").getValue(String.class);
                String progress = dataSnapshot.child("progress").getValue(String.class);
                if (progress == wa) {
                    Toast.makeText(getActivity(), "wait", Toast.LENGTH_SHORT).show();
                    textMessage.setText("Please! Wait");
                    orderImage.setImageResource(R.drawable.ic_time_180);
                } else if (progress == w) {
                    Toast.makeText(getActivity(), "written", Toast.LENGTH_SHORT).show();
                    textMessage.setText("Your order is written");
                    orderImage.setImageResource(R.drawable.ic_order_180);
                } else if (progress == p) {
                    Toast.makeText(getActivity(), "Preparing", Toast.LENGTH_SHORT).show();
                    textMessage.setText("Preparing your order");
                    orderImage.setImageResource(R.drawable.ic_cook);
                } else if (progress == way) {
                    Toast.makeText(getActivity(), "way", Toast.LENGTH_SHORT).show();
                    textMessage.setText("Your order is on the way");
                    orderImage.setImageResource(R.drawable.ic_delivery_180);
                } else if (progress == d) {
                    Toast.makeText(getActivity(), "delivered", Toast.LENGTH_SHORT).show();
                    textMessage.setText("The order was delivered");
                    orderImage.setImageResource(R.drawable.ic_receive_180);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void getprogress(View view) {
        switch (view.getId()) {
            case R.id.write:
                textMessage.setText("Your order is written");
                orderImage.setImageResource(R.drawable.ic_order_180);
                break;
            case R.id.prepare:
                textMessage.setText("Preparing your order");
                orderImage.setImageResource(R.drawable.ic_cook);
                break;
            case R.id.way:
                textMessage.setText("Your order is on the way");
                orderImage.setImageResource(R.drawable.ic_delivery_180);
                break;
            case R.id.delivered:
                textMessage.setText("The order was delivered");
                orderImage.setImageResource(R.drawable.ic_receive_180);
                break;
        }
    }

    public void onClickButton() {
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference("Cart").child(userId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean writeOrder = dataSnapshot.child("writeOrder").getValue(boolean.class);
                        boolean progress = dataSnapshot.child("progress").getValue(boolean.class);
                        if (writeOrder == progress) {
                            Toast.makeText(getActivity(), "written", Toast.LENGTH_SHORT).show();
                            textMessage.setText("Your order is written");
                            orderImage.setImageResource(R.drawable.ic_order_180);
                            seekBar.setProgress(1);
                        } else if (writeOrder != progress) {
                            textMessage.setText("Please! wait");
                            orderImage.setImageResource(R.drawable.ic_time_180);
                            seekBar.setProgress(0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}