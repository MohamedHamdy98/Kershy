package view;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testeverything.R;

public class SharedPreferencsConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencsConfig(Context applicationContext) {

    }

    public void SharedPreferencsConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources()
                .getString(R.string.d),Context.MODE_PRIVATE);
    }
    public void login(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.app_name),status);
        editor.commit();
    }
    public boolean readLogin(){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.project_id),false);
        return status;
    }
}
