package view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapter.AdapterCart;
import Adapter.CategoriesCart.MyAdapterCart;
import Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, databaseReference2;
    GoogleApiClient googleApiClient;
    private static final int REQUEST_CODE = 101;
    @BindView(R.id.recyclerView_cart)
    RecyclerView recyclerViewCart;
    ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    MyAdapterCart myAdapterCart;
    @BindView(R.id.button_apply_cart)
    Button buttonApplyCart;
    @BindView(R.id.button_turnOn_GPS)
    ImageView buttonTurnOnGPS;
    FirebaseRecyclerOptions<ModelCart> options;
    FirebaseRecyclerAdapter<ModelCart, AdapterCart> adapter;
    @BindView(R.id.textView_date_order)
    TextView textViewDateOrder;
    @BindView(R.id.textView_cart_itemTotal)
    TextView textViewCartItemTotal;
    @BindView(R.id.textView_cart_deliveryFee)
    TextView textViewCartDeliveryFee;
    @BindView(R.id.textView_cart_offer)
    TextView textViewCartOffer;
    @BindView(R.id.textView_cart_totalPrice)
    TextView textViewCartTotalPrice;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @BindView(R.id.button_calculate_totalPrice)
    Button buttonCalculateTotalPrice;
    @BindView(R.id.textView_cart_tax)
    TextView textViewCartTax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        // call all functions
        getTimeOrder();
        onClickApplayCart();
    }

    public void getTimeOrder() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        textViewDateOrder.setText(currentDate);
        databaseReference = database.getReference();
        databaseReference.child("Cart").child(userId).child("timeOrder").setValue(currentDate);
        databaseReference.child("AllOrders").child(userId).child("timeOrder").setValue(currentDate);
    }

    // all information about recyclerView
    public void start_recyclerView() {
        recyclerViewCart.setHasFixedSize(true);
        recyclerViewCart.setNestedScrollingEnabled(true);
        recyclerViewCart.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = database.getReference("Cart").child(userId).child("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelCart modelCart;
                modelCartArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    modelCart = snapshot.getValue(ModelCart.class);
                    modelCartArrayList.add(modelCart);
                }
                myAdapterCart = new MyAdapterCart(modelCartArrayList, getApplicationContext());
                recyclerViewCart.setAdapter(myAdapterCart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        start_recyclerView();
        checkDataFirebase();
        hasGPSDevice(this);
        enableLoc();
    }

    public void getFirebase() {
        databaseReference = database.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String offer = dataSnapshot.child("Offers").getValue(String.class);
                String deliveryFee = dataSnapshot.child("deliveryFee").getValue(String.class);
                String totalPrice = dataSnapshot.child("Cart").child(userId).child("totalPrice").getValue(String.class);
                String tax = dataSnapshot.child("Tax").getValue(String.class);
                textViewCartOffer.setText(offer);
                textViewCartDeliveryFee.setText(deliveryFee);
                textViewCartItemTotal.setText(totalPrice);
                textViewCartTax.setText(tax);
                if (totalPrice == "0") {
                    databaseReference.child("Cart").child(userId).child("TotalPriceToPay").setValue("0");
                } else {
                    int Bill = ( Integer.parseInt(totalPrice) + Integer.parseInt(deliveryFee) +
                            (Integer.parseInt(tax)) ) - Integer.parseInt(offer);
                    databaseReference = database.getReference("Cart").child(userId).child("TotalPriceToPay");
                    databaseReference.setValue(String.valueOf(Bill));
                    String bill = dataSnapshot.child("Cart").child(userId).child("TotalPriceToPay")
                            .getValue(String.class);
                    textViewCartTotalPrice.setText(bill);
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                    dataRef.child("AllOrders").child(userId).child("Bill").setValue(bill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getFirebasr2() {
        databaseReference = database.getReference("Cart").child(userId).child("Order");
        //databaseReference2 = database.getReference("Cart").child(userId).child("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    databaseReference = database.getReference("Cart").child(userId).child("totalPrice");
                    // databaseReference2 = database.getReference("Cart").child(userId).child("TotalPriceToPay");
                    databaseReference.setValue("0");
                    //databaseReference2.setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getTotalPrice() {
        int oneType;
        int overTotal = 0;
        for (int i = 0; i < modelCartArrayList.size(); i++) {
            ModelCart modelCart = modelCartArrayList.get(i);
            oneType = ((Integer.valueOf(modelCart.getPrice()))) * ((Integer.valueOf(modelCart.getNumItem())));
            overTotal = overTotal + oneType;
            databaseReference = database.getReference("Cart").child(userId).child("totalPrice");
            databaseReference.setValue(String.valueOf(overTotal));
        }
    }

    public void checkDataFirebase() {
        databaseReference = database.getReference("Cart").child(userId).child("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    onClickcalculaiteTotalPrice();
                } else {
                    databaseReference = database.getReference("Cart").child(userId).child("TotalPriceToPay");
                    databaseReference2 = database.getReference("Cart").child(userId).child("totalPrice");
                    databaseReference.setValue("0");
                    databaseReference2.setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // all button in activity...
    private void onClickcalculaiteTotalPrice() {
        buttonCalculateTotalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTotalPrice();
                getFirebase();
                getFirebasr2();
            }
        });
    }

    private void onClickApplayCart() {
        buttonApplyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(CartActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(CartActivity.this, REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }

    public void statusCheckGPS() {
        final LocationManager manager = (LocationManager) getSystemService(CartActivity.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            Toast.makeText(this, "GPS is turning on.", Toast.LENGTH_LONG).show();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("GPS.").setMessage("To continue please turn on Gps.")
                .setPositiveButton("Turn on.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        AlertDialog alert = builder.create();
        alert.show();
    }

    // turn on gps
    private void turnOn_GPS() {
        buttonTurnOnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCheckGPS();
            }
        });
    }

    public void ItemTouch() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(CartActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(CartActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                databaseReference.child("Cart").removeValue();
                modelCartArrayList.remove(position);
                ModelCart modelCart = new ModelCart();
                myAdapterCart.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewCart);
    }

}