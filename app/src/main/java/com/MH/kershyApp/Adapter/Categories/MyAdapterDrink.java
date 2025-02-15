package com.MH.kershyApp.Adapter.Categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.MH.kershyApp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.MH.kershyApp.Model.Categories.ModelDrink;
import com.MH.kershyApp.Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.embersoft.expandabletextview.ExpandableTextView;

public class MyAdapterDrink extends RecyclerView.Adapter<MyAdapterDrink.ViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Cart");

    private ArrayList<ModelDrink> modelDrinkArrayList = new ArrayList<>();
    private Context context;

    public MyAdapterDrink(ArrayList<ModelDrink> modelDrinkArrayList, Context context) {
        this.modelDrinkArrayList = modelDrinkArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ModelDrink modelDrink = modelDrinkArrayList.get(position);
        // For get image
        holder.textNameRecyclerCategory.setText(modelDrink.getName_drink());
        if (modelDrink.getImage_drink().equals("default")) {
            holder.imageViewRecyclerCategory.setImageResource(R.drawable.ic_burger);
        } else {
            Picasso.get().load(modelDrink.getImage_drink()).into(holder.imageViewRecyclerCategory);
        }
        holder.textPriceRecyclerCategory.setText(modelDrink.getPrice_drink());
        holder.textDescriptionRecyclerCategory.setText(modelDrink.getDescription_drink());
        // For hide and show description
        holder.textDescriptionRecyclerCategory.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                ModelDrink modelDrink = modelDrinkArrayList.get(position);
                modelDrink.setShrink(isShrink);
                modelDrinkArrayList.set(position, modelDrink);
            }
        });
        holder.textDescriptionRecyclerCategory.setText(modelDrink.getDescription_drink());
        holder.textDescriptionRecyclerCategory.resetState(modelDrink.isShrink());
        // For add number of item
        holder.buttonAddRecyclerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.textRemoveAddRecyclerCategory.getText().toString());
                holder.textRemoveAddRecyclerCategory.setText(String.valueOf(num + 1));
            }
        });
        holder.linearAddRecyclerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.textRemoveAddRecyclerCategory.getText().toString());
                holder.textRemoveAddRecyclerCategory.setText(String.valueOf(num + 1));
            }
        });
        // For mines number of item
        holder.buttonRemoveRecyclerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.textRemoveAddRecyclerCategory.getText().toString());
                if (num == 0) {
                } else {
                    holder.textRemoveAddRecyclerCategory.setText(String.valueOf(num - 1));
                }
            }
        });
        holder.linearRemoveRecyclerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.textRemoveAddRecyclerCategory.getText().toString());
                if (num == 0) {
                } else {
                    holder.textRemoveAddRecyclerCategory.setText(String.valueOf(num - 1));
                }
            }
        });
        // For add item in cart
        holder.imageAddCart.setOnClickListener(new View.OnClickListener() {
            ModelCart modelCart;
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                modelCart = new ModelCart(
                        holder.textNameRecyclerCategory.getText().toString(),
                        holder.textRemoveAddRecyclerCategory.getText().toString(),
                        holder.textPriceRecyclerCategory.getText().toString());
                // For add item in firebase
                databaseReference.child(userId).child("Order")
                        .child(holder.textNameRecyclerCategory.getText().toString())
                        .setValue(modelCart);
                Snackbar.make(v,"Added to cart successfully",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelDrinkArrayList.size();
    }

    public void setList(ArrayList<ModelDrink> models) {
        this.modelDrinkArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.linear_add_recycler_category)
        LinearLayout linearAddRecyclerCategory;
        @BindView(R.id.imageView_recycler_category)
        ImageView imageViewRecyclerCategory;
        @BindView(R.id.text_name_recycler_category)
        TextView textNameRecyclerCategory;
        @BindView(R.id.text_description_recycler_category)
        ExpandableTextView textDescriptionRecyclerCategory;
        @BindView(R.id.text_price_recycler_category)
        TextView textPriceRecyclerCategory;
        @BindView(R.id.button_remove_recycler_category)
        Button buttonRemoveRecyclerCategory;
        @BindView(R.id.text_remove_add_recycler_category)
        TextView textRemoveAddRecyclerCategory;
        @BindView(R.id.button_add_recycler_category)
        Button buttonAddRecyclerCategory;
        @BindView(R.id.linear_remove_recycler_category)
        LinearLayout linearRemoveRecyclerCategory;
        @BindView(R.id.image_add_cart)
        ImageView imageAddCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
