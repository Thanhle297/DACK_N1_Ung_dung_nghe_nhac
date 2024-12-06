package com.example.dack1.Data;

public class Account {
    private String username;  // Email hoặc tên đăng nhập
    private String pass;      // Mật khẩu
    private String phone;     // Số điện thoại (String để giữ số 0)
    private String name;      // Tên đầy đủ
    private String id;        // UID từ Firebase Authentication
    private String role;      // Vai trò của tài khoản (admin, user, guest)

    public Account() {} // Constructor mặc định

    public Account(String username, String pass, String phone, String name, String id, String role) {
        this.username = username;
        this.pass = pass;
        this.phone = phone;
        this.name = name;
        this.id = id;
        this.role = role;
    }

    // Getter và Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", pass='" + pass + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
