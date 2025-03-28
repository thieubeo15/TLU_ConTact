package com.example.tlucontact.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlucontact.MainActivity;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.ContactDAO;
import com.example.tlucontact.data.model.Contact;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> contactList;
    private SearchView searchView;
    private Spinner spinnerSort;
    private ContactDAO contactDAO;
    public static final int REQUEST_CODE_EDIT = 1001;
    public static final int REQUEST_CODE_ADD = 1002;
    private Button btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Mở database và lấy danh sách liên hệ
        contactDAO = new ContactDAO(this);
        contactDAO.open();
        contactList = contactDAO.getAllContacts(); // Lấy danh sách từ database

        recyclerView = findViewById(R.id.recyclerContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchContacts);
        spinnerSort = findViewById(R.id.spinnerSort);
        Button btnBack = findViewById(R.id.btnBackToHome);

        adapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_contact, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ContactActivity", "onItemSelected: position=" + position);
                sortContacts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ContactActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddContact = findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(ContactActivity.this, AddContactActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });
    }

    private void sortContacts(int position) {
        Log.d("ContactActivity", "sortContacts: position=" + position);
        if (contactList == null || contactList.isEmpty()) {
            Log.d("ContactActivity", "sortContacts: contactList is empty");
            return;
        }

        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                String name1 = c1.getName().toLowerCase();
                String name2 = c2.getName().toLowerCase();
                String position1 = c1.getPosition().toLowerCase();
                String position2 = c2.getPosition().toLowerCase();

                switch (position) {
                    case 0: // Tên A-Z
                        return name1.compareTo(name2);
                    case 1: // Tên Z-A
                        return name2.compareTo(name1);
                    case 2: // Chức vụ A-Z
                        return position1.compareTo(position2);
                    case 3: // Chức vụ Z-A
                        return position2.compareTo(position1);
                    default:
                        return 0;
                }
            }
        });

        Log.d("ContactActivity", "sortContacts: contactList sorted");
        adapter.filter(searchView.getQuery().toString()); // Cập nhật filteredList
        adapter.notifyDataSetChanged();
        Log.d("ContactActivity", "sortContacts: notifyDataSetChanged called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK && data != null) {
            Contact newContact = (Contact) data.getSerializableExtra("contact");
            if (newContact != null) {
                contactList.add(newContact);
                // Cập nhật filteredList bằng cách gọi filter()
                adapter.filter(searchView.getQuery().toString());
            }
        }

        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            Contact updatedContact = (Contact) data.getSerializableExtra("contact");
            if (updatedContact != null) {
                // Cập nhật dữ liệu trong danh sách hiện tại
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getId().equals(updatedContact.getId())) {
                        contactList.set(i, updatedContact);
                        break;
                    }
                }
                // Cập nhật filteredList bằng cách gọi filter()
                adapter.filter(searchView.getQuery().toString());
                Log.d("ContactActivity", "onActivityResult: Contact edited: " + updatedContact.getName());
            }
        }
    }


//    private void refreshContactList() {
//        contactDAO.open();
//        contactList.clear();
//        contactList.addAll(contactDAO.getAllContacts()); // Lấy lại toàn bộ danh sách từ DB
//        contactDAO.close();
//        adapter.notifyDataSetChanged();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactDAO.close(); // Đóng database khi không sử dụng
    }
}
