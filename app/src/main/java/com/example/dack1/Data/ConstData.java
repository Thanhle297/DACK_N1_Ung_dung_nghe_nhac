package com.example.dack1.Data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;

public class ConstData {
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public final static Account current = new Account("","","","","","");

    public static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Randomly choose between lowercase and uppercase characters
            String characterSet = (random.nextBoolean()) ? LOWERCASE_CHARS : UPPERCASE_CHARS;

            // Append a randomly chosen character to the string
            int randomIndex = random.nextInt(characterSet.length());
            char randomChar = characterSet.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    // Hàm để xóa tất cả dữ liệu trong một đường dẫn cụ thể
    public static void deleteData(String path) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        // Sử dụng phương thức removeValue để xóa tất cả dữ liệu
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> {

                })
                .addOnFailureListener(e -> {

                });
    }
}