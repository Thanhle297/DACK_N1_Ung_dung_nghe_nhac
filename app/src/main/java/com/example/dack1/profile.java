package com.example.dack1;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
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

public class profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView topname;  // TextView hiển thị tên người dùng ở phía trên
    private TextView topusername;  // TextView hiển thị tên đăng nhập ở phía trên
    private TextView name;  // TextView hiển thị tên người dùng
    private TextView username;  // TextView hiển thị tên đăng nhập
    private TextView phone;  // TextView hiển thị số điện thoại
    private TextView pass;  // TextView hiển thị mật khẩu (ẩn)
    private Button logoutButton;  // Nút Đăng xuất
    private  Button editButton; // Nút cập nhật thông tin tài khoản

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitleColor(Color.BLACK);
        setContentView(R.layout.activity_profile);

        // Khởi tạo FirebaseAuth và Realtime Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");

        // Liên kết các thành phần giao diện với mã nguồn thông qua ID
        topname = findViewById(R.id.titleName);
        topusername = findViewById(R.id.titleUsername);
        name = findViewById(R.id.profileName);
        phone = findViewById(R.id.phonenum);
        username = findViewById(R.id.profileUsername);
        pass = findViewById(R.id.profilePassword);
        logoutButton = findViewById(R.id.logoutButton);  // Kết nối với nút Đăng xuất
        editButton = findViewById(R.id.editButton);

        // Lấy thông tin người dùng hiện tại từ Firebase Authentication
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();  // Lấy UID của người dùng từ FirebaseAuth

            // Truy vấn dữ liệu người dùng từ Realtime Database theo UID
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Lấy thông tin người dùng từ Realtime Database
                    Account account = dataSnapshot.getValue(Account.class);
                    if (account != null) {
                        // Hiển thị thông tin người dùng trên giao diện
                        topname.setText(account.getName());
                        name.setText(account.getName());
                        topusername.setText(account.getUsername());
                        username.setText(account.getUsername());
                        phone.setText(account.getPhone());
                        pass.setText(account.getPass());  // Không hiển thị mật khẩu thực sự
                    } else {
                        Toast.makeText(profile.this, "Thông tin người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(profile.this, "Lỗi khi lấy dữ liệu từ Realtime Database", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(profile.this, Loginpage.class);
            startActivity(intent);
            finish();
        }

        // Thiết lập sự kiện cho nút Đăng xuất
        logoutButton.setOnClickListener(v -> {
            // Đăng xuất người dùng
            mAuth.signOut();
            Toast.makeText(profile.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            // Chuyển về màn hình đăng nhập sau khi đăng xuất
            Intent intent = new Intent(profile.this, Loginpage.class);
            startActivity(intent);
            finish();
        });

        editButton.setOnClickListener(view -> {
            Intent intent = new Intent(profile.this,EditProfile.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Chuyển về màn hình MainActivity khi người dùng nhấn nút quay lại
        Intent intent = new Intent(profile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
