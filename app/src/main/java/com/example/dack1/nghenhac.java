package com.example.dack1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class nghenhac extends AppCompatActivity {
    private Music music;
    private ImageView imageView, imageView2, nextButton, previousButton;
    private TextView authorNameTextView, songNameTextView, textViewAuthor, textViewName;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler = new Handler();  // Handler để cập nhật SeekBar

    private int currentTrackIndex = 0;  // Chỉ số bài hát hiện tại trong danh sách
    private String[] songIds = {"song1", "song2", "song3", "song4", "song5", "song6","song7","song8"};  // Danh sách các ID bài hát trong Firebase
    private int[] songList = {R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4, R.raw.music5, R.raw.music6, R.raw.music7, R.raw.music8};  // Danh sách các bài nhạc trong thư mục raw

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleColor(Color.BLACK);
        setContentView(R.layout.activity_nghenhac);

        // Khởi tạo các view
        authorNameTextView = findViewById(R.id.authorNameTextView);
        songNameTextView = findViewById(R.id.songNameTextView);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewName = findViewById(R.id.textViewName);

        imageView = findViewById(R.id.imageView7);
        imageView2 = findViewById(R.id.imageView2);
        seekBar = findViewById(R.id.seekBar);
        nextButton = findViewById(R.id.imageView8);  // Nút Next
        previousButton = findViewById(R.id.imageView9);  // Nút Previous

        // Khởi tạo Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("Music");

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        currentTrackIndex = intent.getIntExtra("trackIndex", 0);  // Lấy chỉ số bài hát được chọn từ Intent

        // Lấy thông tin bài hát từ Firebase
        loadSongFromFirebase(songIds[currentTrackIndex]);

        // Lắng nghe sự kiện Play/Pause
        imageView.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();  // Tạm dừng nhạc
                imageView.setImageResource(R.drawable.start);
            } else {
                mediaPlayer.start();  // Phát nhạc
                imageView.setImageResource(R.drawable.stop);
            }
            isPlaying = !isPlaying;
        });

        // Sự kiện thay đổi SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);  // Di chuyển đến vị trí mới trong bài hát
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Nút Next (chuyển bài hát tiếp theo)
        nextButton.setOnClickListener(v -> {
            currentTrackIndex++;  // Chuyển đến bài hát tiếp theo
            if (currentTrackIndex < songList.length) {
                loadSongFromFirebase(songIds[currentTrackIndex]);
            } else {
                currentTrackIndex = 0;  // Quay lại bài nhạc đầu tiên
                loadSongFromFirebase(songIds[currentTrackIndex]);
            }
        });

        // Nút Previous (chuyển bài hát trước đó)
        previousButton.setOnClickListener(v -> {
            currentTrackIndex--;  // Chuyển đến bài hát trước đó
            if (currentTrackIndex >= 0) {
                loadSongFromFirebase(songIds[currentTrackIndex]);
            } else {
                currentTrackIndex = songList.length - 1;  // Quay lại bài hát cuối cùng
                loadSongFromFirebase(songIds[currentTrackIndex]);
            }
        });
    }

    // Hàm tải thông tin bài hát từ Firebase
    private void loadSongFromFirebase(String songId) {
        mDatabase.child(songId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy dữ liệu bài hát từ Firebase
                String musicName = dataSnapshot.child("name").getValue(String.class);
                String musicAuthor = dataSnapshot.child("author").getValue(String.class);
                String musicImage = dataSnapshot.child("downloadImg").getValue(String.class);

                // Cập nhật UI với dữ liệu mới
                if (musicImage != null) {
                    Picasso.get().load(musicImage).into(imageView2);
                }

                songNameTextView.setText(musicName);
                authorNameTextView.setText(musicAuthor);
                textViewName.setText(musicName);
                textViewAuthor.setText(musicAuthor);

                // Khởi tạo MediaPlayer với bài hát mới
                playTrack(currentTrackIndex);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(nghenhac.this, "Error loading song data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm phát bài nhạc trong danh sách
    private void playTrack(int trackIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, songList[trackIndex]);  // Tạo MediaPlayer với bài nhạc mới
        setupMediaPlayer();  // Thiết lập lại MediaPlayer
        mediaPlayer.start();  // Bắt đầu phát nhạc
        isPlaying = true;  // Đặt trạng thái phát nhạc
        imageView.setImageResource(R.drawable.stop);  // Cập nhật icon nút Play/Pause
    }

    // Thiết lập các tham số cho MediaPlayer
    private void setupMediaPlayer() {
        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mediaPlayer.getDuration());  // Cập nhật SeekBar
            updateSeekBar();  // Bắt đầu cập nhật SeekBar
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            // Chuyển bài hát khi hoàn thành
            currentTrackIndex++;
            if (currentTrackIndex < songList.length) {
                loadSongFromFirebase(songIds[currentTrackIndex]);  // Phát bài nhạc tiếp theo
            } else {
                currentTrackIndex = 0;  // Quay lại bài nhạc đầu tiên
                loadSongFromFirebase(songIds[currentTrackIndex]);
            }
        });
    }

    // Cập nhật SeekBar mỗi giây
    private void updateSeekBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());  // Cập nhật SeekBar với thời gian hiện tại
            handler.postDelayed(this::updateSeekBar, 1000);  // Tiếp tục cập nhật sau mỗi giây
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();  // Giải phóng tài nguyên khi thoát Activity
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();  // Tạm dừng nhạc khi Activity bị pause
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();  // Giải phóng tài nguyên khi Activity bị hủy
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // Tiếp tục phát nhạc khi Activity quay lại trạng thái resume
        }
    }
}


