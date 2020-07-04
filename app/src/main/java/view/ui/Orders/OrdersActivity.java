package view.ui.Orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.testeverything.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import view.CartActivity;
import view.ui.CategoryItem.BurgerFragment;

public class OrdersActivity extends AppCompatActivity {
    @BindView(R.id.button_cardView_backTo_BurgerActivity)
    CardView buttonCardViewBackToBurgerActivity;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        buttonCardViewBackToBurgerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrdersActivity.this, BurgerFragment.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_item_cart:
                goto_cartActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goto_cartActivity() {
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
    }
}