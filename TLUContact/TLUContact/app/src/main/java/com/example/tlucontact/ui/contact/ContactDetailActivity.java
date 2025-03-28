package com.example.tlucontact.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tlucontact.R;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView tvContactPhone;
    private Button btnCall, btnBack; // Thêm btnBack

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        tvContactPhone = findViewById(R.id.tvContactPhone);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack); // Tìm btnBack từ layout

        // Xử lý nút gọi điện
        btnCall.setOnClickListener(v -> {
            String phoneNumber = tvContactPhone.getText().toString().replace("Số điện thoại: ", "");
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish()); // Đóng Activity khi nhấn nút
    }
}
