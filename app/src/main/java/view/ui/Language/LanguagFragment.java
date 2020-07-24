package view.ui.Language;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.testeverything.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import view.LogInActivity;
import view.MainActivity;

public class LanguagFragment extends Fragment {

    @BindView(R.id.button_english)
    Button buttonEnglish;
    @BindView(R.id.button_arabic)
    Button buttonArabic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_languag, container, false);
        ButterKnife.bind(this, root);
        onClick();
        return root;
    }
    private void onClick(){
        buttonArabic.setOnClickListener(v -> setLanguage("ar"));
        buttonEnglish.setOnClickListener(v -> setLanguage("en"));

    }

    private void setLanguage(String locale) {
        Resources resource = getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        Configuration configuration = resource.getConfiguration();
        configuration.setLocale(new Locale(locale.toLowerCase()));
        resource.updateConfiguration(configuration, displayMetrics);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }
}