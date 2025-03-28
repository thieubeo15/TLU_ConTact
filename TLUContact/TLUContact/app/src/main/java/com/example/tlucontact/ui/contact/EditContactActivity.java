package com.example.tlucontact.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.ContactDAO;
import com.example.tlucontact.data.model.Contact;

public class EditContactActivity extends AppCompatActivity {

    private EditText etName, etPosition, etPhone, etEmail, etDepartment;
    private Button btnSave, btnCancel;
    private ContactDAO contactDAO;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Ánh xạ View
        etName = findViewById(R.id.edtName);
        etPosition = findViewById(R.id.edtPosition);
        etPhone = findViewById(R.id.edtPhone);
        etEmail = findViewById(R.id.edtEmail);
        etDepartment = findViewById(R.id.edtDepartment);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Khởi tạo DAO
        contactDAO = new ContactDAO(this);

        // Nhận dữ liệu từ Intent
        if (getIntent() != null && getIntent().hasExtra("contact")) {
            contact = (Contact) getIntent().getSerializableExtra("contact");
            Log.d("EditContactActivity", "Contact received: " + contact.getName());
        } else {
            Toast.makeText(this, "Lỗi: Không có dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Đổ dữ liệu lên UI
        etName.setText(contact.getName());
        etPosition.setText(contact.getPosition());
        etPhone.setText(contact.getPhone());
        etEmail.setText(contact.getEmail());
        etDepartment.setText(contact.getDepartmentId());

        // Sự kiện lưu
        btnSave.setOnClickListener(v -> updateContact());

        // Quay lại màn hình trước
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateContact() {
        String name = etName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty() || department.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin contact
        contact.setName(name);
        contact.setPosition(position);
        contact.setPhone(phone);
        contact.setEmail(email);
        contact.setDepartmentId(department);

        contactDAO.open();
        // Cập nhật vào database
        if (contactDAO.updateContact(contact) > 0) {
            Log.d("EditContactActivity", "Contact updated successfully: " + contact.getName());
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("contact", contact);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Log.e("EditContactActivity", "Error updating contact");
            Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }
}
