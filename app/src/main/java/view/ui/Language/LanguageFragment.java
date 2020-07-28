package view.ui.Language;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import view.MainActivity;

public class LanguageFragment extends Fragment {

    @BindView(R.id.button_english)
    Button buttonEnglish;
    @BindView(R.id.button_arabic)
    Button buttonArabic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_language, container, false);
        ButterKnife.bind(this, root);
        onClick();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    private void onClick(){
        buttonArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageFragment.this.setLanguage("ar");
            }
        });
        buttonEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageFragment.this.setLanguage("en");
            }
        });

    }
    private void setLanguage(String locale) {
        Resources resource = getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        Configuration configuration = resource.getConfiguration();
        configuration.setLocale(new Locale(locale.toLowerCase()));
        resource.updateConfiguration(configuration, displayMetrics);
        saveLocale(locale);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }
    public void saveLocale(String lang) {
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", lang);
        editor.commit();
    }
}