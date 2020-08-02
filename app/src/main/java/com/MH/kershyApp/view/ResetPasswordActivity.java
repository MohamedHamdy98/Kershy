package com.MH.kershyApp.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.MH.kershyApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResetPasswordActivity extends AppCompatActivity {


    @BindView(R.id.editText_reset_password)
    EditText editTextResetPassword;
    @BindView(R.id.prgressBar_reset)
    ProgressBar prgressBarReset;
    @BindView(R.id.button_goto_logIn_activity)
    Button buttonGotoLogInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeEdit);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        resetPassword();
    }
    private void resetPassword(){
        buttonGotoLogInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextResetPassword.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplication(), R.string.enterEmail, Toast.LENGTH_SHORT).show();
                    return;
                }
                prgressBarReset.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ResetPasswordActivity.this, R.string.EmailSent, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ResetPasswordActivity.this,LogInActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, R.string.Failed_send_email, Toast.LENGTH_LONG).show();
                                }
                                prgressBarReset.setVisibility(View.GONE);
                            }
                        });
            }
        });

    }
}