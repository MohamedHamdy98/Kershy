package view;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;

import java.util.ArrayList;

import Adapter.MyAdapterCart;
import Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;
import view.ui.CategoryItem.BurgerItemActivity;
import view.ui.CategoryItem.HomeFragment;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView_cart)
    RecyclerView recyclerViewCart;

    ArrayList<ModelCart> modelCartArrayList = new ArrayList<>();
    ModelCart modelCart = new ModelCart();
    MyAdapterCart myAdapterCart;
    @BindView(R.id.button_apply_cart)
    Button buttonApplyCart;
    @BindView(R.id.button_turnOn_GPS)
    ImageView buttonTurnOnGPS;
    @BindView(R.id.button_cart_item_back)
    ImageView buttonCartItemBack;
    @BindView(R.id.card_cart_item_back)
    CardView cardCartItemBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        //dialog for user to turn on GPS...
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn on GPS!").setMessage("If you turn off your GPS you should turn on!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        AlertDialog alert = builder.create();
        alert.show();
        // call all functions
        turnOn_GPS();
        start_recyclerView();
        onClick();
    }
    // all button in activity...
    private void onClick() {
        buttonApplyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        buttonCartItemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, BurgerItemActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        cardCartItemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, BurgerItemActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
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

    public void statusCheckGPS() {
        final LocationManager manager = (LocationManager) getSystemService(CartActivity.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
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

    // all information about recyclerView
    public void start_recyclerView() {
        recyclerViewCart.setHasFixedSize(true);
        myAdapterCart = new MyAdapterCart(modelCartArrayList, getApplicationContext());
        modelCartArrayList.add(new ModelCart("Creamy Nachos", "20 LE", "5 Items"));
        modelCartArrayList.add(new ModelCart("Mahraja Mac", "10 LE", "1 Items"));
        modelCartArrayList.add(new ModelCart("Mc Veggie Mac", "50 LE", "2 Items"));
        modelCartArrayList.add(new ModelCart("Mc Veggie Mac", "50 LE", "2 Items"));
        modelCartArrayList.add(new ModelCart("Mc Veggie Mac", "50 LE", "2 Items"));
        modelCartArrayList.add(new ModelCart("Mc Veggie Mac", "50 LE", "2 Items"));
        modelCartArrayList.add(new ModelCart("Mc Veggie Mac", "50 LE", "2 Items"));
        modelCartArrayList.add(new ModelCart("Burger Mac", "80 LE", "1 Items"));
        modelCartArrayList.add(new ModelCart("Fish Mac", "100 LE", "20 Items"));
        recyclerViewCart.setNestedScrollingEnabled(false);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(myAdapterCart);
    }

//
//    public void statusCheck() {
//        final LocationManager manager = (LocationManager) getSystemService(CartActivity.LOCATION_SERVICE);
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//        }
//    }
//
//    private void buildAlertMessageNoGps() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }
}