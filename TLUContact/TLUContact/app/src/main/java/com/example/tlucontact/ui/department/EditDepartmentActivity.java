package com.example.tlucontact.ui.department;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.DepartmentDAO;
import com.example.tlucontact.data.model.Department;

public class EditDepartmentActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress;
    private Button btnSave, btnCancel;
    private DepartmentDAO departmentDAO;
    private Department department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_department);

        // Ánh xạ View
        etName = findViewById(R.id.etEditDepartmentName);
        etPhone = findViewById(R.id.etEditDepartmentPhone);
        etAddress = findViewById(R.id.etEditDepartmentAddress);
        btnSave = findViewById(R.id.btnUpdateDepartment);
        btnCancel = findViewById(R.id.btnCancel);

        // Khởi tạo DAO
        departmentDAO = new DepartmentDAO(this);

        // Nhận dữ liệu từ Intent
        department = (Department) getIntent().getSerializableExtra("department");
        if (department != null) {
            etName.setText(department.getName());
            etPhone.setText(department.getPhone());
            etAddress.setText(department.getAddress());
        }

        // Sự kiện lưu
        btnSave.setOnClickListener(v -> updateDepartment());

        // Hủy bỏ
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateDepartment() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin contact
        department.setName(name);
        department.setPhone(phone);
        department.setAddress(address);


        departmentDAO.open();
        // Cập nhật vào database
        if (departmentDAO.updateDepartment(department) > 0) {
            Log.d("EditDepartmentActivity", "Department updated successfully: " + department.getName());
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("department", department);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Log.e("EditDepartmentActivity", "Error updating contact");
            Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}