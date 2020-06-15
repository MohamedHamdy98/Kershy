package view.ui.CategoryItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testeverything.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.editText_search_category)
    EditText editTextSearchCategory;
    @BindView(R.id.buttonImage_search_category)
    ImageView buttonImageSearchCategory;
    @BindView(R.id.image_burger_category)
    ImageView imageBurgerCategory;
    @BindView(R.id.image_dounts_category)
    ImageView imageDountsCategory;
    @BindView(R.id.image_cola_category)
    ImageView imageColaCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        onClick_image();
        return root;
    }

    public void onClick_image(){
        imageBurgerCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_burger = new Intent(getActivity(),BurgerItemActivity.class);
                startActivity(intent_burger);

            }
        });
        imageDountsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_burger = new Intent(getActivity(),SweetActivity.class);
                startActivity(intent_burger);
            }
        });
        imageColaCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_burger = new Intent(getActivity(),DrinkActivity.class);
                startActivity(intent_burger);
            }
        });
    }

//    public void startRecyclerView(){
//        modelCategoryArrayList.add(new ModelCategory("Burger",R.drawable.burger));
//        modelCategoryArrayList.add(new ModelCategory("Drink",R.drawable.cola));
//        modelCategoryArrayList.add(new ModelCategory("Sweet",R.drawable.dounts));
//        recyclerViewCategory.setHasFixedSize(true);
//        myAdapterCategory = new MyAdapterCategory(modelCategoryArrayList,getActivity());
//        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerViewCategory.setAdapter(myAdapterCategory);
//    }
}