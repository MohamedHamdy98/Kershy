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

import com.MH.kershyApp.Model.Categories.ModelBurger;
import com.MH.kershyApp.Model.ModelCart;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.embersoft.expandabletextview.ExpandableTextView;

public class MyAdapterBurger extends RecyclerView.Adapter<MyAdapterBurger.ViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Cart");


    private ArrayList<ModelBurger> categoryArrayList = new ArrayList<>();
    Context context;

    public MyAdapterBurger(ArrayList<ModelBurger> categoryArrayList, Context context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ModelBurger modelBurger = categoryArrayList.get(position);
        holder.textNameRecyclerCategory.setText(modelBurger.getName_burger());
        // For get image
        if (modelBurger.getImage_burger().equals("default")) {
            holder.imageViewRecyclerCategory.setImageResource(R.drawable.ic_burger);
        } else {
            Picasso.get().load(modelBurger.getImage_burger()).into(holder.imageViewRecyclerCategory);
        }
        holder.textPriceRecyclerCategory.setText(modelBurger.getPrice_burger());
        holder.textDescriptionRecyclerCategory.setText(modelBurger.getDescription_burger());
        // For hide and show description
        holder.textDescriptionRecyclerCategory.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                ModelBurger contentModelBurger = categoryArrayList.get(position);
                contentModelBurger.setShrink(isShrink);
                categoryArrayList.set(position, contentModelBurger);
            }
        });
        holder.textDescriptionRecyclerCategory.setText(modelBurger.getDescription_burger());
        holder.textDescriptionRecyclerCategory.resetState(modelBurger.isShrink());
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
            public void onClick(final View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                modelCart = new ModelCart(
                        holder.textNameRecyclerCategory.getText().toString(),
                        holder.textRemoveAddRecyclerCategory.getText().toString(),
                        holder.textPriceRecyclerCategory.getText().toString());
                // For add item in firebase
                databaseReference.child(userId).child("Order")
                        .child(holder.textNameRecyclerCategory.getText().toString())
                        .setValue(modelCart);
                Snackbar.make(v, "Added to cart successfully", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setList(ArrayList<ModelBurger> models) {
        this.categoryArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.linear_add_recycler_category)
        LinearLayout linearAddRecyclerCategory;
        @BindView(R.id.linear_remove_recycler_category)
        LinearLayout linearRemoveRecyclerCategory;
        @BindView(R.id.image_add_cart)
        ImageView imageAddCart;
        @BindView(R.id.text_totalprice_recycler_category)
        TextView textTotalpriceRecyclerCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
