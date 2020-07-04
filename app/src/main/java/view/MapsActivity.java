package view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testeverything.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import view.ui.Orders.OrdersFragment;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Cart");

    @BindView(R.id.cardView_gone)
    CardView cardViewGone;
    @BindView(R.id.button_reset_myLocation_GPS)
    ImageView buttonResetMyLocationGPS;
    @BindView(R.id.button_apply_toPay_money)
    Button buttonApplyToPayMoney;
    @BindView(R.id.editText_phone_map)
    EditText editTextPhoneMap;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        fusedLocationProviderClient = LocationServices.
                getFusedLocationProviderClient(this);
        reset_myLocation();
        swipe_activity();
        onClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchLastLocation();
    }

    private void swipe_activity() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (mMap == null) {
                    finish();
                    startActivity(getIntent());
                } else {
                    fetchLastLocation();
                    mMap.clear();
                }
            }
        });
    }

    private void onClick() {
        buttonApplyToPayMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cardViewGone.setVisibility(View.GONE);
                if(editTextPhoneMap.getText().toString().isEmpty()){
                    Snackbar.make(v,"Write your phone",Snackbar.LENGTH_LONG).show();
                }
                else{
                    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseReference.child(userId).child("Phone").setValue(editTextPhoneMap.getText().toString());
                    SharedPreferences sharedPreferences = getSharedPreferences("saveinfo",MODE_PRIVATE);
                    String name = sharedPreferences.getString("name","User Name");
                    databaseReference.child(userId).child("UserName").setValue(name);
                    // To check progress bar in delivery fragment
                    databaseReference.child(userId).child("writeOrder").setValue(false);
                    databaseReference.child(userId).child("preparingOrder").setValue(false);
                    databaseReference.child(userId).child("wayOrder").setValue(false);
                    databaseReference.child(userId).child("deliveredOrder").setValue(false);
                    databaseReference.child(userId).child("waitOrder").setValue(false);
                    databaseReference.child(userId).child("progress").setValue(true);
                    Toast.makeText(MapsActivity.this, "Please wait!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MapsActivity.this,CategoryActivity.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                }
//                Fragment fragment = new OrdersFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.m, fragment);
//                fragmentTransaction.commit();
            }
        });
    }

    private void reset_myLocation() {
        buttonResetMyLocationGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap == null) {
                    finish();
                    startActivity(getIntent());
                } else {
                    mMap.clear();
                    fetchLastLocation();
                }
            }
        });
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                            + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseReference.child(userId).child("Address").setValue(currentLocation.getLatitude() + " " +
                            currentLocation.getLongitude());
                    mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                } else {
                    Toast.makeText(MapsActivity.this, "Please! Close App and open it again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I am here");
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("I am here")).showInfoWindow();
            }
        });
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
}
