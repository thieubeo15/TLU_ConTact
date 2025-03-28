package com.example.tlucontact.data.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private String id;
    private String name;
    private String position;
    private String phone;
    private String email;
    private String departmentId; // Thay vì lưu department string, lưu ID để liên kết với Department

    public Contact(String id, String name, String position, String phone, String email, String departmentId) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.departmentId = departmentId;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
}
