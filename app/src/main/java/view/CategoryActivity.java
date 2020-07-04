package view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testeverything.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import Model.ModelCart;
import Model.User;

public class CategoryActivity extends AppCompatActivity  {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    private StorageReference mStorageRef;
    Uri imageUri;
    ImageView imageView_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);

//        SharedPreferences sharedPreferences = getSharedPreferences("saveinfo",MODE_PRIVATE);
//        String name = sharedPreferences.getString("name","User Name");

        databaseReference = database.getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String name = dataSnapshot.child("userName").getValue(String.class);
                TextView userName = navigationView.getHeaderView(0).findViewById(R.id.textView_userName);
                userName.setText(name);
                imageView_user = navigationView.getHeaderView(0).findViewById(R.id.imageView_user);
                if (imageView_user == null){
                    Toast.makeText(CategoryActivity.this, "Wait!", Toast.LENGTH_SHORT).show();
                } else {
                    Glide.with(CategoryActivity.this)
                            .load(user.getImageURL())
                            .into(imageView_user);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_burger, R.id.nav_sweet, R.id.nav_drink, R.id.nav_orders, R.id.nav_delivery
        ,R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.button_item_cart:
                goto_cartActivity();
                return true;
            case R.id.nav_logout:
                logOut();
                return true;
            case R.id.nav_share_app:
                shareApplication();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goto_cartActivity() {
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void logOut(){
        Toast.makeText(CategoryActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = getSharedPreferences("keep",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember","false");
            editor.apply();
        Intent intent = new Intent(CategoryActivity.this, LogInActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
           // System.exit(0);
    }

    private void shareApplication(){
        Toast.makeText(this, "ShareApplication", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String body = "https://drive.google.com/file/d/1a67pLUHyn4HYJh7b_IWKx8HNdn2xuyJP/view?usp=sharing";
        String subject = "Kershy App";
        //http://play.google.com/store/apps/details?id=com.example.testeverything
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

}