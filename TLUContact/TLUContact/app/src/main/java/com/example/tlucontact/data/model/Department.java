package com.example.tlucontact.data.model;

import java.io.Serializable;
import java.util.UUID;

public class Department implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String address;

    // Constructor không có ID -> Tạo UUID tự động
    public Department(String name, String phone, String address) {
        this.id = UUID.randomUUID().toString(); // Tạo ID tự động
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // Constructor có ID (dùng khi lấy từ database)
    public Department(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
