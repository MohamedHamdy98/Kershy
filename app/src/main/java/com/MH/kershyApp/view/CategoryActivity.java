package com.MH.kershyApp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.MH.kershyApp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.MH.kershyApp.Model.User;

public class CategoryActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    ImageView imageView_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // To get userName and image to set them in header in navigation drawer..
        databaseReference = database.getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String name = dataSnapshot.child("userName").getValue(String.class);
                TextView userName = navigationView.getHeaderView(0).findViewById(R.id.textView_userName);
                userName.setText(name);
                imageView_user = navigationView.getHeaderView(0).findViewById(R.id.imageView_user);
                if (user.getImageURL().equals("default")) {
                    imageView_user.setImageResource(R.drawable.ic_man);
                } else {
                    Glide.with(CategoryActivity.this).load(user.getImageURL()).into(imageView_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_burger, R.id.nav_sweet, R.id.nav_drink, R.id.nav_orders, R.id.nav_delivery
                , R.id.nav_profile,R.id.nav_language)
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
        switch (item.getItemId()) {
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

    private void logOut() {
        SharedPreferences preferences = getSharedPreferences("keep", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        Intent intent = new Intent(CategoryActivity.this, LogInActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //FirebaseAuth.getInstance().signOut();
        // System.exit(0);
    }

    private void shareApplication() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String body = "https://drive.google.com/file/d/1K69JAxw0aS7eKRt5QKJR_hHBI8VNUWqD/view?usp=sharing";
        String subject = "Kershy App";
        //http://play.google.com/store/apps/details?id=com.example.testeverything
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

}