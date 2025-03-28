package com.example.tlucontact.ui.department;

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
import com.example.tlucontact.data.db.DepartmentDAO;
import com.example.tlucontact.data.model.Contact;
import com.example.tlucontact.data.model.Department;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DepartmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private List<Department> departmentList;
    private SearchView searchView;
    private Spinner spinnerSort;
    private DepartmentDAO departmentDAO;
    public static final int REQUEST_CODE_EDIT = 1001;
    public static final int REQUEST_CODE_ADD = 1002;
    private Button btnAddDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        // Mở database và lấy danh sách phòng ban
        departmentDAO = new DepartmentDAO(this);
        departmentDAO.open();
        departmentList = departmentDAO.getAllDepartments();

        recyclerView = findViewById(R.id.recyclerDepartments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchDepartment);
        spinnerSort = findViewById(R.id.spinnerSort);
        Button btnBack = findViewById(R.id.btnBackToHome);

        adapter = new DepartmentAdapter(this, departmentList);
        recyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_department, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DepartmentActivity", "onItemSelected: position=" + position);
                sortDepartments(position);
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
            Intent intent = new Intent(DepartmentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddDepartment = findViewById(R.id.btnAddDepartment);
        btnAddDepartment.setOnClickListener(v -> {
            Intent intent = new Intent(DepartmentActivity.this, AddDepartmentActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });
    }

    private void sortDepartments(int position) {
        Log.d("DepartmentActivity", "sortDepartments: position=" + position);
        if (departmentList == null || departmentList.isEmpty()) {
            Log.d("DepartmentActivity", "sortDepartments: departmentList is empty");
            return;
        }

        Collections.sort(departmentList, new Comparator<Department>() {
            @Override
            public int compare(Department d1, Department d2) {
                String name1 = d1.getName().toLowerCase();
                String name2 = d2.getName().toLowerCase();

                switch (position) {
                    case 0: // Tên A-Z
                        return name1.compareTo(name2);
                    case 1: // Tên Z-A
                        return name2.compareTo(name1);
                    default:
                        return 0;
                }
            }
        });

        Log.d("DepartmentActivity", "sortDepartments: departmentList sorted");
        adapter.filter(searchView.getQuery().toString());
        adapter.notifyDataSetChanged();
        Log.d("DepartmentActivity", "sortDepartments: notifyDataSetChanged called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK && data != null) {
            Department newDepartment = (Department) data.getSerializableExtra("department");
            if (newDepartment != null) {
                departmentList.add(newDepartment);
                // Cập nhật filteredList bằng cách gọi filter()
                adapter.filter(searchView.getQuery().toString());
            }
        }
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            Department updatedDepartment = (Department) data.getSerializableExtra("department");
            if (updatedDepartment != null) {
                //departmentDAO.updateDepartment(updatedDepartment);
                //refreshDepartmentList();
                for (int i = 0; i < departmentList.size(); i++) {
                    if (departmentList.get(i).getId().equals(updatedDepartment.getId())) {
                        departmentList.set(i, updatedDepartment);
                        break;
                    }
                }
                adapter.filter(searchView.getQuery().toString());
                Log.d("DepartmentActivity", "onActivityResult: Department edited: " + updatedDepartment.getName());
            }
//        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK && data != null) { // Kiểm tra data != null
//            Department newDepartment = (Department) data.getSerializableExtra("new_department");
//
//            if (newDepartment != null) {
//                departmentDAO.addDepartment(newDepartment);
//                refreshDepartmentList();
//            }
//        }
        }
    }

//    private void refreshDepartmentList() {
//        departmentList.clear();
//        departmentList.addAll(departmentDAO.getAllDepartments());
//        adapter.notifyDataSetChanged();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        departmentDAO.close();
    }
}