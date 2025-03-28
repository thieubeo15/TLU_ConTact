package com.example.tlucontact.ui.department;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.DepartmentDAO;
import com.example.tlucontact.data.model.Contact;
import com.example.tlucontact.data.model.Department;

import java.util.UUID;

public class AddDepartmentActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress;
    private Button btnAdd, btnCancel;
    private DepartmentDAO departmentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        // Ánh xạ View
        etName = findViewById(R.id.etDepartmentName);
        etPhone = findViewById(R.id.etDepartmentPhone);
        etAddress = findViewById(R.id.etDepartmentAddress);
        btnAdd = findViewById(R.id.btnSaveDepartment);
        btnCancel = findViewById(R.id.btnCancel);

        // Khởi tạo DAO
        departmentDAO = new DepartmentDAO(this);
        //departmentDAO.open(); // Mở kết nối CSDL

        // Sự kiện thêm phòng ban
        btnAdd.setOnClickListener(v -> addDepartment());

        // Sự kiện hủy
        btnCancel.setOnClickListener(v -> finish());
    }

    private void addDepartment() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Department
        Department department = new Department(UUID.randomUUID().toString(),name, phone, address); // Sử dụng constructor tạo UUID
        //Contact contact = new Contact(UUID.randomUUID().toString(), name, position, phone, email, department)
        // Thêm vào cơ sở dữ liệu
        departmentDAO.open();
        if (departmentDAO.addDepartment(department) > 0) {
            Toast.makeText(this, "Thêm phòng ban thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("department", department); // Truyền department
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi thêm phòng ban!", Toast.LENGTH_SHORT).show();
        }
        departmentDAO.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        departmentDAO.close(); // Đóng kết nối CSDL
    }
}