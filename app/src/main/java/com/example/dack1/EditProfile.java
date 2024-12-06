package com.example.dack1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    private EditText editUsername, editPassword, editName, editPhone;
    private Button editButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");

        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editButton = findViewById(R.id.editButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            editButton.setOnClickListener(v -> {
                String newEmail = editUsername.getText().toString().trim();
                String newPassword = editPassword.getText().toString().trim();
                String newName = editName.getText().toString().trim();
                String newPhone = editPhone.getText().toString().trim();

                if (TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(newName) || TextUtils.isEmpty(newPhone)) {
                    Toast.makeText(EditProfile.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentUser.updateEmail(newEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                // Cập nhật thông tin vào Firebase
                                mDatabase.child(userId).child("username").setValue(newEmail);
                                mDatabase.child(userId).child("name").setValue(newName);
                                mDatabase.child(userId).child("phone").setValue(newPhone);
                                mDatabase.child(userId).child("pass").setValue(newPassword);

                                Toast.makeText(EditProfile.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();

                                // Quay lại trang profile sau khi cập nhật thành công
                                finish();  // Đóng EditProfile và quay lại Activity trước đó (Profile)
                            } else {
                                Toast.makeText(EditProfile.this, "Lỗi cập nhật mật khẩu: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(EditProfile.this, "Lỗi cập nhật email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditProfile.this, Loginpage.class);
            startActivity(intent);
            finish();
        }
    }

    // Ghi đè phương thức onBackPressed() để quay lại Profile khi người dùng nhấn nút back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Khi người dùng quay lại, sẽ chuyển về profile
        Intent intent = new Intent(EditProfile.this, profile.class);  // Đảm bảo Profile là tên Activity đúng
        startActivity(intent);
        finish();
    }
}
