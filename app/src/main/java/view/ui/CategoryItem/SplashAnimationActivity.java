package view.ui.CategoryItem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.testeverything.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashAnimationActivity extends AppCompatActivity {
    Animation topAnim;
    private static int SPLASH_SCREEN = 1000;
    @BindView(R.id.imageSplash)
    ImageView imageSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_animation);
        ButterKnife.bind(this);
        startAnimation();
        timeSplash();
    }
    private void startAnimation(){
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation_fast);
        imageSplash.setAnimation(topAnim);
    }

    private void timeSplash(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_SCREEN);
                    Intent intent = new Intent(SplashAnimationActivity.this, BurgerItemActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}