package com.ruzz.butilordering;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ruzz.butilordering.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth appAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        appAuthentication = FirebaseAuth.getInstance();

        Button loginButton = binding.loginButton;
        TextView resetPassword = binding.btnForgotPassword;
        TextView linkRegister = binding.linkToRegister;

        loginButton.setOnClickListener(v -> {
            loginUser();
        });

        resetPassword.setOnClickListener(v -> {
            goToResetPassword();
        });

        linkRegister.setOnClickListener(v -> {
            goToRegisterActivity();
        });

        setContentView(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = appAuthentication.getCurrentUser();

        if (currentUser != null) {
            goToHomeActivity();
        }
    }


    private void loginUser() {
        String email = binding.txtEmail.getText().toString();
        String password = binding.txtPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Email and password is required.",
                    Toast.LENGTH_SHORT).show();
        }

        appAuthentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        goToHomeActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Email or password is incorrect.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToResetPassword() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}