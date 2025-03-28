package com.example.tlucontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.data.db.ContactDAO;
import com.example.tlucontact.data.db.DepartmentDAO;
import com.example.tlucontact.data.model.Contact;
import com.example.tlucontact.data.model.Department;
import com.example.tlucontact.ui.contact.ContactActivity;
import com.example.tlucontact.ui.department.DepartmentActivity;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button btnDepartment, btnContact;
    private DepartmentDAO departmentDAO;
    private ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ Button từ layout
        btnDepartment = findViewById(R.id.btn_department);
        btnContact = findViewById(R.id.btn_contact);

        // Khởi tạo DAO và mở kết nối
        departmentDAO = new DepartmentDAO(this);
        contactDAO = new ContactDAO(this);
        departmentDAO.open();
        contactDAO.open();

        // Thêm dữ liệu mẫu nếu chưa có
        insertSampleData();

        // Xử lý sự kiện nhấn nút
        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DepartmentActivity.class));
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
            }
        });

        // Đóng kết nối database khi không cần thiết
        departmentDAO.close();
        contactDAO.close();
    }

    private void insertSampleData() {
        List<Department> departments = departmentDAO.getAllDepartments();
        if (departments.isEmpty()) {
            // Thêm dữ liệu mẫu cho phòng ban
            Department d1 = new Department(UUID.randomUUID().toString(), "Phòng CNTT", "024-123456", "Tầng 5, Tòa nhà A");
            Department d2 = new Department(UUID.randomUUID().toString(), "Phòng Hành Chính", "024-654321", "Tầng 3, Tòa nhà B");
            departmentDAO.addDepartment(d1);
            departmentDAO.addDepartment(d2);
        }

        List<Contact> contacts = contactDAO.getAllContacts();
        if (contacts.isEmpty()) {
            // Thêm dữ liệu mẫu cho liên hệ
            Contact c1 = new Contact(UUID.randomUUID().toString(), "Nguyễn Văn A", "Giảng viên", "0987654321", "a@example.com", departments.get(0).getId());
            Contact c2 = new Contact(UUID.randomUUID().toString(), "Trần Thị B", "Nhân viên", "0912345678", "b@example.com", departments.get(1).getId());
            contactDAO.addContact(c1);
            contactDAO.addContact(c2);
        }
    }
}
