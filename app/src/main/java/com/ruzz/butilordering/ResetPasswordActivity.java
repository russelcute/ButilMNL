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
import com.ruzz.butilordering.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    private FirebaseAuth appAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        appAuth = FirebaseAuth.getInstance();

        Button submit = binding.btnSendResetLink;
        TextView redirectRegister = binding.redirectRegister;

        binding.floatingActionButton2.setOnClickListener(v -> {
            gotoLoginActivity();
        });

        submit.setOnClickListener(v -> sendResetPassword());

        redirectRegister.setOnClickListener(v -> gotoRegisterActivity());

        setContentView(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = appAuth.getCurrentUser();

        if (currentUser != null) {
            gotoHomeActivity();
        }
    }

    private void sendResetPassword() {
        String registeredEmail = binding.txtRegisteredEmail.getText().toString();

        if (TextUtils.isEmpty(registeredEmail)) {
            Toast.makeText(ResetPasswordActivity.this, "Email is required.",
                    Toast.LENGTH_SHORT).show();
        }

        appAuth.sendPasswordResetEmail(registeredEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        gotoLoginActivity();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Failed to send email.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void gotoRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}