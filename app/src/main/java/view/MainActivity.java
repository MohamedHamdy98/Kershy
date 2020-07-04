package view;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testeverything.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    private static int SPLASH_SCREEN = 3000;
    @BindView(R.id.imageSplash)
    ImageView imageSplash;
    @BindView(R.id.textSplash)
    TextView textSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
      timeSplash();
    }

    private void startAnimation(){
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageSplash.setAnimation(topAnim);
        textSplash.setAnimation(bottomAnim);
    }

    private void timeSplash(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_SCREEN);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){
                        Intent i = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    } else{
                        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                   // Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    //startActivity(intent);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, SPLASH_SCREEN);
    }
}
