package com.example.tlucontact.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.ContactDAO;
import com.example.tlucontact.data.model.Contact;

import java.util.UUID;

public class AddContactActivity extends AppCompatActivity {

    private EditText etName, etPosition, etPhone, etEmail, etDepartment;
    private Button btnAdd, btnBack;
    private ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Ánh xạ View
        etName = findViewById(R.id.etName);
        etPosition = findViewById(R.id.etPosition);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etDepartment = findViewById(R.id.etDepartment);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnCancel);

        // Khởi tạo DAO
        contactDAO = new ContactDAO(this);

        // Sự kiện thêm liên hệ
        btnAdd.setOnClickListener(v -> addContact());

        // Quay lại màn hình trước
        btnBack.setOnClickListener(v -> finish());
    }

    private void addContact() {
        String name = etName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty() || department.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Contact contact = new Contact(UUID.randomUUID().toString(), name, position, phone, email, department);

        // Thêm vào cơ sở dữ liệu
        contactDAO.open();
        if (contactDAO.addContact(contact) > 0) {
            Toast.makeText(this, "Thêm liên hệ thành công!", Toast.LENGTH_SHORT).show();
            // Gửi dữ liệu liên hệ mới về ContactActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("contact", contact);
            setResult(RESULT_OK, resultIntent); // Gửi kết quả thành công
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi thêm liên hệ!", Toast.LENGTH_SHORT).show();
        }
        contactDAO.close();
    }
}