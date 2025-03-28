package com.example.tlucontact.ui.department;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.DepartmentDAO;
import com.example.tlucontact.data.model.Department;
import com.example.tlucontact.ui.contact.ContactActivity;
import com.example.tlucontact.ui.contact.ContactDetailActivity;
import com.example.tlucontact.ui.contact.EditContactActivity;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

    private List<Department> departmentList;
    private List<Department> filteredList;
    private Context context;
    private OnDepartmentClickListener listener;
    private DepartmentDAO departmentDAO;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(Department department);
        void onEditClick(Department department);
    }

    public DepartmentAdapter(Context context, List<Department> departmentList) {
        this.context = context;
        //this.listener = listener;
        this.departmentList = departmentList;
        this.filteredList = new ArrayList<>(departmentList);
        this.departmentDAO = new DepartmentDAO(context);
        this.departmentDAO.open(); // Mở kết nối database
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Department department = filteredList.get(position);
        holder.departmentName.setText(department.getName());
        holder.departmentPhone.setText(department.getPhone());
        holder.departmentAddress.setText(department.getAddress());

        // Mở chi tiết liên hệ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DepartmentDetailActivity.class);
            intent.putExtra("department", department);
            context.startActivity(intent);
        });

        // Chỉnh sửa liên hệ
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditDepartmentActivity.class);
            intent.putExtra("department", department);
            intent.putExtra("position", holder.getAdapterPosition());
            ((DepartmentActivity) context).startActivityForResult(intent, DepartmentActivity.REQUEST_CODE_EDIT);
        });


        // Xóa liên hệ
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(department,position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

//    public void updateData(List<Department> newDepartments) {
//        departmentList.clear();
//        departmentList.addAll(newDepartments);
//        filteredList = new ArrayList<>(newDepartments);
//        notifyDataSetChanged();
//    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(departmentList);
        } else {
            query = query.toLowerCase();
            for (Department department : departmentList) {
                if (department.getName().toLowerCase().contains(query) ||
                        department.getPhone().contains(query) ||
                        department.getAddress().toLowerCase().contains(query)) {
                    filteredList.add(department);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(Department department, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng ban này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteDepartment(department, position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteDepartment(Department department, int position) {
        departmentDAO.deleteDepartment(department.getId()); // Xóa từ database
        departmentList.remove(department);
        filteredList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView departmentName, departmentPhone, departmentAddress;
        ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            departmentName = itemView.findViewById(R.id.tvDepartmentName);
            departmentPhone = itemView.findViewById(R.id.tvDepartmentPhone);
            departmentAddress = itemView.findViewById(R.id.tvDepartmentAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
