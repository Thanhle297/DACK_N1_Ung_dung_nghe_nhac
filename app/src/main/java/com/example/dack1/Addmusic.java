package com.example.dack1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Addmusic extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;

    EditText name;
    EditText author;
    EditText image;
    EditText music;
    Button add;
    Uri imageUri;
    Uri audioUri;
    DatabaseReference mDatabase;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitleColor(Color.BLACK);
        setContentView(R.layout.activity_addmusic);

        // Khởi tạo Firebase Database và Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Music");

        name = findViewById(R.id.name);
        author = findViewById(R.id.author);
        image = findViewById(R.id.image);
        music = findViewById(R.id.music);
        add = findViewById(R.id.button5);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(PICK_IMAGE_REQUEST);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(PICK_AUDIO_REQUEST);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    uploadMusic();
                } else {
                    Toast.makeText(Addmusic.this, "Vui lòng nhập đầy đủ thông tin và chọn file", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        if (requestCode == PICK_IMAGE_REQUEST) {
            intent.setType("image/*");
        } else if (requestCode == PICK_AUDIO_REQUEST) {
            intent.setType("audio/*");
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                image.setText(imageUri.toString());
                Toast.makeText(this, "Đã chọn ảnh thành công", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_AUDIO_REQUEST) {
                audioUri = data.getData();
                music.setText(audioUri.toString());
                Toast.makeText(this, "Đã chọn âm thanh thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        return imageUri != null && audioUri != null && !name.getText().toString().trim().isEmpty() && !author.getText().toString().trim().isEmpty();
    }

    private void uploadMusic() {
        String musicId = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("Images/" + musicId + ".jpg");
        StorageReference audioRef = storageReference.child("Music/" + musicId + ".mp3");

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        uploadAudio(musicId, imageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Addmusic.this, "Upload ảnh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAudio(String musicId, String imageUrl) {
        StorageReference audioRef = storageReference.child("Music/" + musicId + ".mp3");
        audioRef.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String audioUrl = uri.toString();
                        saveMusicToDatabase(musicId, imageUrl, audioUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Addmusic.this, "Upload âm thanh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMusicToDatabase(String musicId, String imageUrl, String audioUrl) {
        Music music = new Music(musicId, imageUrl, audioUrl, name.getText().toString().trim(), author.getText().toString().trim(), 0);
        mDatabase.child(musicId).setValue(music).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Addmusic.this, "Đã thêm bài hát thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Addmusic.this, Admin.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Addmusic.this, "Thêm bài hát thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Addmusic.this, Admin.class);
        startActivity(intent);
        finish();
    }
}