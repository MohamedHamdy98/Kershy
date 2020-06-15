package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testeverything.R;

import java.util.ArrayList;

import Model.ModelCategory;
import ru.embersoft.expandabletextview.ExpandableTextView;

public class MyAdapterCategoryBurger extends RecyclerView.Adapter<MyAdapterCategoryBurger.ViewHolder> {
    private ArrayList<ModelCategory> categoryArrayList = new ArrayList<>();
    Context context;

    public MyAdapterCategoryBurger(ArrayList<ModelCategory> categoryArrayList, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ModelCategory modelCategory = categoryArrayList.get(position);
        holder.textView_name_category.setText(modelCategory.getName_category());
        holder.imageView_category_burger.setImageResource(modelCategory.getImage_category());
        holder.textView_price_category.setText(modelCategory.getPrice());
        holder.textView_description_category.setText(modelCategory.getDescription());
        holder.textView_description_category.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                ModelCategory contentModelCategory = categoryArrayList.get(position);
                contentModelCategory.setShrink(isShrink);
                categoryArrayList.set(position,contentModelCategory);
            }
        });
        holder.textView_description_category.setText(modelCategory.getDescription());
        holder.textView_description_category.resetState(modelCategory.isShrink());
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public void setList(ArrayList<ModelCategory> models) {
        this.categoryArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name_category,textView_price_category;
        ExpandableTextView textView_description_category;
        ImageView imageView_category_burger;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_category_burger = itemView.findViewById(R.id.imageView_recycler_category_burger);
            textView_name_category = itemView.findViewById(R.id.text_name_recycler_category_burger);
            textView_price_category = itemView.findViewById(R.id.text_price_recycler_category_burger);
            textView_description_category = itemView.findViewById(R.id.text_description_recycler_category_burger);
        }
    }
}
