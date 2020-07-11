package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverything.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.ModelItemOffer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterItemOffer extends RecyclerView.Adapter<MyAdapterItemOffer.ViewHolder> {

    private ArrayList<ModelItemOffer> modelItemOfferArrayList = new ArrayList<>();
    Context context;

    public MyAdapterItemOffer(ArrayList<ModelItemOffer> modelItemOfferArrayList, Context context) {
        this.modelItemOfferArrayList = modelItemOfferArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_offer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelItemOffer modelItemOffer = modelItemOfferArrayList.get(position);
        if (modelItemOffer.getImage_offer().equals("default")) {
            holder.imageBurgerItemOffer.setImageResource(R.drawable.ic_burger);
        } else {
            Picasso.get().load(modelItemOffer.getImage_offer())
                    .into(holder.imageBurgerItemOffer);
            //Glide.with(context).load(modelBurger.getImage_burger()).into( holder.imageViewRecyclerCategory);
        }
        holder.nameBurgerItemOffer.setText(modelItemOffer.getName_offer());
        holder.oldPriceBurgerItemOffer.setText(modelItemOffer.getOld_price_offer());
        holder.newPriceBurgerItemOffer.setText(modelItemOffer.getNew_price_offer());
    }

    @Override
    public int getItemCount() {
        return modelItemOfferArrayList.size();
    }

    public void setList(ArrayList<ModelItemOffer> models) {
        this.modelItemOfferArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_burger_item_offer)
        ImageView imageBurgerItemOffer;
        @BindView(R.id.name_burger_item_offer)
        TextView nameBurgerItemOffer;
        @BindView(R.id.oldPrice_burger_item_offer)
        TextView oldPriceBurgerItemOffer;
        @BindView(R.id.newPrice_burger_item_offer)
        TextView newPriceBurgerItemOffer;
        @BindView(R.id.cardView_image_burger_category)
        CardView cardViewImageBurgerCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
