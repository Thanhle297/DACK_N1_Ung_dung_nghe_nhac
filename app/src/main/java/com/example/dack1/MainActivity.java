package com.example.dack1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Khai báo recycleview
    RecyclerView recyclerView;
    // Khai báo adapter baihatAdapter
    baihatAdapter baihatAdapter;
    // Khai báo list chứa danh sách bài hát
    ArrayList<Music> musicArrayList = new ArrayList<>();
    // Ảnh đại diện
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitleColor(Color.BLACK);
        setContentView(R.layout.activity_main);

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Nếu người dùng chưa đăng nhập, chuyển đến màn hình đăng nhập
            Intent intent = new Intent(MainActivity.this, Loginpage.class);
            startActivity(intent);
            finish();
            return;
        }

        // Trỏ recycleView bằng Id
        recyclerView = findViewById(R.id.recycle);
        avatar = findViewById(R.id.imageView2);

        // Khởi tạo adapter
        baihatAdapter = new baihatAdapter(musicArrayList, this);
        recyclerView.setAdapter(baihatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // khi bấm vào avatar
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, profile.class);
                startActivity(intent);
                finish();
            }
        });

        // Lắng nghe sự kiện thay đổi dữ liệu từ Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Music");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa dữ liệu cũ trong ArrayList để tránh việc trùng lặp
                musicArrayList.clear();

                // Duyệt qua mỗi child node trong 'Music'
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.child("id").getValue(Long.class).toString();
                    String downloadImg = snapshot.child("downloadImg").getValue(String.class);
                    String downloadUrl = snapshot.child("downloadUrl").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String author = snapshot.child("author").getValue(String.class);
                    int type = snapshot.child("type").getValue(Long.class).intValue(); // Chuyển từ Long sang int

                    Music music = new Music(id, downloadImg, downloadUrl, name, author, type);
                    musicArrayList.add(music);
                }
                baihatAdapter.notifyDataSetChanged(); // Cập nhật adapter
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu
                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
