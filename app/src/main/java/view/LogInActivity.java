package view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.text_input_layout_email)
    TextInputLayout textInputLayoutEmail;
    @BindView(R.id.text_input_layout_password)
    TextInputLayout textInputLayoutPassword;
    private FirebaseAuth mAuth;
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
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.textView_resetPassword)
    TextView textViewResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeEdit);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        signUp();
        resetPass();
        logIn();
        keepLogIn();
    }

    // To go to reset password activity..
    private void resetPass() {
        textViewResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInActivity.this.startActivity(new Intent(LogInActivity.this, ResetPasswordActivity.class));
                LogInActivity.this.finish();
                LogInActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    // To go to sigIn activity..
    private void signUp() {
        textViewGotoSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                LogInActivity.this.startActivity(intent);
                LogInActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    // To keep logIn...
    private void keepLogIn() {
        SharedPreferences preferences = getSharedPreferences("keep", MODE_PRIVATE);
        String check = preferences.getString("remember", "");
        if (check.equals("true")) {
            startActivity(new Intent(LogInActivity.this, CategoryActivity.class));
        } else if (check.equals("false")) {
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    SharedPreferences sharedPreferences = LogInActivity.this.getSharedPreferences("keep", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                } else if (!buttonView.isChecked()) {
                    SharedPreferences sharedPreferences = LogInActivity.this.getSharedPreferences("keep", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });

    }

    // To go to home(Offers Fragment) activity..
    // To login...
    private void logIn() {
        buttonGotoMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = editTextLoginGmail.getText().toString().trim();
                final String password = editTextLoginPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    textInputLayoutEmail.setError(getString(R.string.enterEmail));
                    return;
                } else {
                    textInputLayoutEmail.setError(null);
                }
                if (TextUtils.isEmpty(password)) {
                    textInputLayoutPassword.setError(getString(R.string.enterPassword));
                    return;
                } else {
                    textInputLayoutPassword.setError(null);
                }
                if (password.length() < 6) {
                    textInputLayoutPassword.setError(getString(R.string.passMore6));
                }
                ProgressDialog progressDialog = new ProgressDialog(LogInActivity.this);
                progressDialog.setMessage(getString(R.string.waitlogin));
                progressDialog.show();
                checkEmailIsExist(email, password, v, progressDialog);
            }
        });
    }

    private void checkEmailIsExist(String email, String password, View v, ProgressDialog progressDialog) {
        //check email already exist or not.
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (email == null) {
                            textInputLayoutEmail.setError(getString(R.string.enterEmail));
                        } else {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                            if (isNewUser) {
                                Toast.makeText(LogInActivity.this,
                                        R.string.haveAccount
                                        , Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Snackbar.make(v, R.string.logSuccess, Snackbar.LENGTH_SHORT).show();
                                                    LogInActivity.this.startActivity(new Intent(LogInActivity.this, CategoryActivity.class));
                                                    LogInActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    // For set progress bar in delivery fragment..
                                                    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference databaseReference = database.getReference("Cart")
                                                            .child(userId);
                                                    databaseReference.child("id").setValue(userId);
                                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                                            .getReference("Users").child(userId);
                                                    reference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String name = dataSnapshot.child("userName").getValue(String.class);
                                                            DatabaseReference databaseReference =
                                                                    database.getReference("Cart")
                                                                            .child(userId);
                                                            databaseReference.child("UserName").setValue(name);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                } else {
                                                    String issue = task.getException().getMessage();
                                                    Snackbar.make(v, issue, Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }


                    }
                });
    }
}