package com.example.dack1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {
    private Button addButton;
    private EditText searchBar;
    private RecyclerView recyclerView;

    private AdminMusicAdapter baihatAdapter;
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private ArrayList<Music> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ẩn ActionBar nếu có
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_admin);

        // Ánh xạ các thành phần giao diện
        addButton = findViewById(R.id.button4);
        searchBar = findViewById(R.id.searchbar);
        recyclerView = findViewById(R.id.recycle);


        // Thiết lập RecyclerView
        baihatAdapter = new AdminMusicAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(baihatAdapter);



        // Lắng nghe sự thay đổi trong thanh tìm kiếm
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý khi nhấn nút "Thêm"
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, Addmusic.class);
                startActivity(intent);
            }
        });

        // Đọc dữ liệu từ Firebase
        fetchMusicData();
    }

    private void fetchMusicData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Music");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musicArrayList.clear();
                filteredList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = String.valueOf(snapshot.child("id").getValue());
                    String downloadImg = String.valueOf(snapshot.child("downloadImg").getValue());
                    String downloadUrl = String.valueOf(snapshot.child("downloadUrl").getValue());
                    String name = String.valueOf(snapshot.child("name").getValue());
                    String author = String.valueOf(snapshot.child("author").getValue());
                    int type = snapshot.child("type").getValue(Integer.class);

                    // Tạo đối tượng Music
                    Music music = new Music(id, downloadImg, downloadUrl, name, author, type);
                    musicArrayList.add(music);
                }

                // Hiển thị toàn bộ danh sách ban đầu
                filteredList.addAll(musicArrayList);
                baihatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterData(String searchString) {
        filteredList.clear();

        for (Music music : musicArrayList) {
            if (music.getName().toLowerCase().contains(searchString.toLowerCase()) ||
                    music.getAuthor().toLowerCase().contains(searchString.toLowerCase())) {
                filteredList.add(music);
            }
        }

        baihatAdapter.notifyDataSetChanged();
    }
}
