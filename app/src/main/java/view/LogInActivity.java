package view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testeverything.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @BindView(R.id.button_goto_main_activity)
    Button buttonGotoMainActivity;
    @BindView(R.id.textView_goto_signUpActivity)
    TextView textViewGotoSignUpActivity;
    @BindView(R.id.editText_login_gmail)
    EditText editTextLoginGmail;
    @BindView(R.id.editText_login_password)
    EditText editTextLoginPassword;
    @BindView(R.id.prgressBar_login)
    ProgressBar prgressBarLogin;
    @BindView(R.id.editText_login_phone)
    EditText editTextLoginPhone;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    private SharedPreferencsConfig sharedPreferencsConfig;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        SharedPreferences preferences = getSharedPreferences("keep",MODE_PRIVATE);
        String check = preferences.getString("remember","");
        if (check.equals("true")){
            startActivity(new Intent(LogInActivity.this,CategoryActivity.class));
        } else if (check.equals("false")){
            Toast.makeText(LogInActivity.this, "Please LogIn", Toast.LENGTH_SHORT).show();
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences("keep", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(LogInActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                }else if (!buttonView.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences("keep", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(LogInActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewGotoSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        buttonGotoMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = editTextLoginGmail.getText().toString();
                final String password = editTextLoginPassword.getText().toString();
                final String phone = editTextLoginPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Snackbar.make(v, "Please Enter Your Phone", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, "Please Enter Your Email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, "Please Enter Your Password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Snackbar.make(v, "Password should be more than 6 letters", Snackbar.LENGTH_SHORT).show();
                }
                editTextLoginGmail.getText().clear();
                editTextLoginPassword.getText().clear();
                editTextLoginPhone.getText().clear();
                prgressBarLogin.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                prgressBarLogin.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LogInActivity.this, CategoryActivity.class));
                                    Snackbar.make(v, "LogIn success", Snackbar.LENGTH_SHORT).show();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    Snackbar.make(v, "LogIn failed", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Users").child(phone).child("userName");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue(String.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("saveinfo", MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("pass", password);
                        editor.apply();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LogInActivity.this, "error name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}