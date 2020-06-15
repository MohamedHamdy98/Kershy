package view.ui.CategoryItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;

import java.util.ArrayList;

import Adapter.MyAdapterCategoryBurger;
import Model.ModelCategory;
import butterknife.BindView;
import butterknife.ButterKnife;
import view.CartActivity;
import view.CategoryActivity;

public class BurgerItemActivity extends AppCompatActivity  {
    ArrayList<ModelCategory> modelCategoryArrayList = new ArrayList<>();
    @BindView(R.id.recyclerView_burger)
    RecyclerView recyclerViewBurger;
    MyAdapterCategoryBurger myAdapterCategoryBurger;
    Context context;
    @BindView(R.id.button_burger_item_back)
    ImageView buttonBurgerItemBack;
    @BindView(R.id.button_item_cart)
    ImageView buttonBurgerItemCart;
    @BindView(R.id.card_burger_item_back)
    CardView cardBurgerItemBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger_item);
        ButterKnife.bind(this);
        startRecyclerView();
        onClick();
        buttonBurgerItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void startRecyclerView() {
        modelCategoryArrayList.add(new ModelCategory("Burger",
                "At first glance, this is not a suitable spice for coffee, which can spoil the drink, but it is only at first glance. In fact, pepper gives a highlight to this aromatic and tasty drink and only allows you to open new impressions",
                "50 LE", R.drawable.burgeritemback));
        modelCategoryArrayList.add(new ModelCategory("Creamy nachos",
                "Many coffee lovers in the fall enjoy spicy pumpkin latte, but if you want to save or make this drink out of season, you can easily make it at home. Drink can be made on the stove or in the microwave.",
                "20 LE", R.drawable.burger));
        modelCategoryArrayList.add(new ModelCategory("Maharaja mac",
                "Indian cuisine is famous for its abundance of spices and spices. Coffee drink, popular in this country, is no exception.",
                "80 LE", R.drawable.b));
        modelCategoryArrayList.add(new ModelCategory("Mc Veggie mac",
                "In Brazil, they love coffee, they don\\’t just adore it. The average Brazilian drinks up to 12 cups of coffee a day and therefore the technology of making coffee in Brazil is perfect. In Brazil, just a cup of hot coffee is a symbol of respect and hospitality.",
                "40 LE", R.drawable.burgeritemback));
        recyclerViewBurger.setHasFixedSize(true);
        myAdapterCategoryBurger = new MyAdapterCategoryBurger(modelCategoryArrayList, context);
        recyclerViewBurger.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBurger.setAdapter(myAdapterCategoryBurger);
    }


    public void onClick() {
        buttonBurgerItemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurgerItemActivity.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        cardBurgerItemBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurgerItemActivity.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });


    }
}