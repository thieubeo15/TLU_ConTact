package com.example.tlucontact.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlucontact.R;
import com.example.tlucontact.data.db.ContactDAO;
import com.example.tlucontact.data.model.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> contactList;
    private List<Contact> filteredList;
    private Context context;
    private ContactDAO contactDAO;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.filteredList = new ArrayList<>(contactList);
        this.contactDAO = new ContactDAO(context);
        this.contactDAO.open(); // Mở database
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = filteredList.get(position);
        holder.staffName.setText(contact.getName());
        holder.staffPosition.setText(contact.getPosition());
        holder.staffEmail.setText(contact.getEmail());

        // Mở chi tiết liên hệ
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactDetailActivity.class);
            intent.putExtra("contact", contact);
            context.startActivity(intent);
        });

        // Chỉnh sửa liên hệ
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditContactActivity.class);
            intent.putExtra("contact", contact);
            intent.putExtra("position", holder.getAdapterPosition());
            ((ContactActivity) context).startActivityForResult(intent, ContactActivity.REQUEST_CODE_EDIT);
        });


        // Xóa liên hệ
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Tìm kiếm liên hệ
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(contactList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Contact contact : contactList) {
                if (contact.getName().toLowerCase().contains(lowerCaseQuery) ||
                        contact.getEmail().toLowerCase().contains(lowerCaseQuery) ||
                        contact.getPosition().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Hộp thoại xác nhận xóa
    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa liên hệ này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteContact(position))
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xóa liên hệ khỏi danh sách và database
    private void deleteContact(int position) {
        if (position >= 0 && position < filteredList.size()) {
            Contact contactToRemove = filteredList.get(position);

            // Xóa khỏi database
            contactDAO.open();
            contactDAO.deleteContact(contactToRemove.getId());
            contactDAO.close();

            // Xóa khỏi danh sách chính và danh sách hiển thị
            contactList.remove(contactToRemove);
            filteredList.remove(position);

            notifyItemRemoved(position);
        }
    }

    // ViewHolder chứa ánh xạ UI
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView staffName, staffPosition, staffEmail;
        ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            staffName = itemView.findViewById(R.id.tvName);
            staffPosition = itemView.findViewById(R.id.tvPosition);
            staffEmail = itemView.findViewById(R.id.tvEmail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
