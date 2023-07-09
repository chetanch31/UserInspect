package visual.camp.sample.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import visual.camp.sample.app.R;
import visual.camp.sample.app.activity.ui.home.ScreenActivity;
//firebase imports
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private TextView registerTextView;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if (currentUser.isEmailVerified()) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                // Login successful
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        registerTextView = findViewById(R.id.registerTextView);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }

                loginProgressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loginProgressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser.isEmailVerified()) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        // Login successful
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please check your email for verification.", Toast.LENGTH_SHORT).show();
                                        // Email not verified
                                    }
                                } else {
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Toast.makeText(LoginActivity.this, "Login failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(LoginActivity.this, "Can't go back.", Toast.LENGTH_SHORT).show();
    }
}
