package view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testeverything.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.editText_signUp_gmail)
    EditText editTextSignUpGmail;
    @BindView(R.id.editText_signUp_password)
    EditText editTextSignUpPassword;
    @BindView(R.id.button_goto_main_activity)
    Button buttonGotoMainActivity;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.prgressBar_signUp)
    ProgressBar prgressBarSignUp;
    @BindView(R.id.editText_signUp_name)
    EditText editTextSignUpName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Users");
    @BindView(R.id.editText_signUp_phone)
    EditText editTextSignUpPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonGotoMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = editTextSignUpGmail.getText().toString();
                final String password = editTextSignUpPassword.getText().toString();
                final String name = editTextSignUpName.getText().toString();
                final String phone = editTextSignUpPhone.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    Snackbar.make(v, "Please Enter Your Phone", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Snackbar.make(v, "Please Enter Your Name", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Please Enter Email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Please Enter Password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Snackbar.make(v, "Password should be more thn 6 letters", Snackbar.LENGTH_SHORT).show();
                }
                prgressBarSignUp.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                prgressBarSignUp.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("userName", name);
                                    hashMap.put("email", email);
                                    hashMap.put("pass", password);
                                    hashMap.put("phone", phone);
                                    hashMap.put("imageURL", "default");
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    databaseReference.child(userId).setValue(hashMap);
                                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                                    Snackbar.make(v, "Authentication success.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(v, "Authentication failed.", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}