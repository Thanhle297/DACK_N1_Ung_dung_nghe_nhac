package com.example.dack1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dack1.Data.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loginpage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText login_username, login_password;
    Button loginButton;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitleColor(Color.BLACK);
        setContentView(R.layout.activity_loginpage);

        mAuth = FirebaseAuth.getInstance();

        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_username.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                signIn(email, password);
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginpage.this, Signuppage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkRoleAndRedirect(user.getUid());
                        }
                    } else {
                        Toast.makeText(Loginpage.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkRoleAndRedirect(String userId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("accounts");

        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Account account = snapshot.getValue(Account.class);
                    if (account != null) {
                        String role = account.getRole();
                        if ("admin".equals(role)) {
                            Intent intent = new Intent(Loginpage.this, Admin.class);
                            startActivity(intent);
                            finish();
                        } else if ("user".equals(role)) {
                            Intent intent = new Intent(Loginpage.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Loginpage.this, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Loginpage.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Loginpage.this, "Lỗi khi đọc dữ liệu từ máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
