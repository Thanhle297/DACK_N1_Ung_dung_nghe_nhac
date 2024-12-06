package com.example.dack1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dack1.Data.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signuppage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText signup_username, signup_password, signup_name, signup_phone;
    TextView loginText;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitleColor(Color.BLACK);
        setContentView(R.layout.activity_signuppage);

        mAuth = FirebaseAuth.getInstance();

        signup_username = findViewById(R.id.signup_username);
        signup_password = findViewById(R.id.signup_password);
        signup_name = findViewById(R.id.signup_name);
        signup_phone = findViewById(R.id.signup_phone);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.loginText);

        signupButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String email = signup_username.getText().toString().trim();
                String pass = signup_password.getText().toString().trim();
                String displayName = signup_name.getText().toString().trim();
                String phone = signup_phone.getText().toString().trim();

                checkIfAccountExists(email, pass, displayName, phone);
            }
        });

        loginText.setOnClickListener(v -> {
            Intent intent = new Intent(Signuppage.this, Loginpage.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInputs() {
        String email = signup_username.getText().toString().trim();
        String pass = signup_password.getText().toString().trim();
        String displayName = signup_name.getText().toString().trim();
        String phone = signup_phone.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(displayName) || TextUtils.isEmpty(phone)) {
            Toast.makeText(Signuppage.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() < 6) {
            Toast.makeText(Signuppage.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void checkIfAccountExists(String email, String password, String displayName, String phone) {
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (!result.getSignInMethods().isEmpty()) {
                            Toast.makeText(Signuppage.this, "Email đã được đăng ký", Toast.LENGTH_SHORT).show();
                        } else {
                            signUp(email, password, displayName, phone);
                        }
                    } else {
                        Toast.makeText(Signuppage.this, "Kiểm tra tài khoản thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp(String email, String password, String displayName, String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            String role = "user";
                            Account account = new Account(email, password, phone, displayName, userId, role);

                            saveAccountToDatabase(userId, account);
                            sendEmailVerification(user);

                            Toast.makeText(Signuppage.this, "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Signuppage.this, Loginpage.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(Signuppage.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveAccountToDatabase(String userId, Account account) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        mDatabase.child(userId).setValue(account)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Signuppage.this, "Lỗi lưu tài khoản: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Signuppage.this, "Lỗi gửi email xác nhận: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
