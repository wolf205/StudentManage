package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTaskTitle, editDueDate, editTaskDesc;
    private RadioGroup radioPriority;
    private Spinner spinnerCategory;
    private CheckBox checkCompleted;
    private Button btnSave;

    // Biến để phân biệt đang là chế độ sửa hay thêm mới
    private boolean isEditMode = false;
    private int editTaskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // 1. Ánh xạ View
        editTaskTitle = findViewById(R.id.editTaskTitle);
        editDueDate = findViewById(R.id.editDueDate);
        editTaskDesc = findViewById(R.id.editTaskDesc);
        radioPriority = findViewById(R.id.radioPriority);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        checkCompleted = findViewById(R.id.checkCompleted);
        btnSave = findViewById(R.id.btnSave);

        // Khởi tạo ArrayAdapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // 2. Kiểm tra xem có phải đang mở ở chế độ Chỉnh sửa không
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("is_edit_mode", false)) {
            isEditMode = true;
            editTaskId = intent.getIntExtra("edit_task_id", -1);

            // Đổ dữ liệu cũ vào View
            String oldTitle = intent.getStringExtra("edit_task_title");
            String oldDate = intent.getStringExtra("edit_task_date");
            int oldPriority = intent.getIntExtra("edit_task_priority", 3);

            editTaskTitle.setText(oldTitle);
            editDueDate.setText(oldDate);

            // Tick đúng priority cũ
            if (oldPriority == 1) {
                radioPriority.check(R.id.radioPriorityHigh);
            } else if (oldPriority == 2) {
                radioPriority.check(R.id.radioPriorityMedium);
            } else {
                // Giả sử bạn có ID này trong XML, nếu không hãy điều chỉnh cho khớp ID thật
                radioPriority.check(R.id.radioPriorityLow);
            }

            // Đổi chữ trên nút
            btnSave.setText("Cập nhật");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chỉnh sửa công việc");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Thêm công việc mới");
            }
        }

        // 3. Xử lý sự kiện khi bấm nút Save / Cập nhật
        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                saveTask();
            }
        });
    }

    // Hàm kiểm tra tính hợp lệ của Form
    private boolean validateForm() {
        String taskName = editTaskTitle.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();

        if (taskName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên công việc", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dueDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập hạn chót", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (taskName.length() < 3) {
            Toast.makeText(this, "Tên công việc phải từ 3 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Hàm trích xuất dữ liệu và gửi về MainActivity
    private void saveTask() {
        String title = editTaskTitle.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();
        String desc = editTaskDesc.getText().toString().trim();
        boolean isCompleted = checkCompleted.isChecked();
        String category = spinnerCategory.getSelectedItem().toString();

        // Lấy mức độ ưu tiên
        int selectedPriorityId = radioPriority.getCheckedRadioButtonId();
        String priority = "Low";
        if (selectedPriorityId == R.id.radioPriorityMedium) {
            priority = "Medium";
        } else if (selectedPriorityId == R.id.radioPriorityHigh) {
            priority = "High";
        }

        // Đóng gói dữ liệu trả về
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_title", title);
        resultIntent.putExtra("new_due_date", dueDate);
        resultIntent.putExtra("new_priority", priority);
        resultIntent.putExtra("new_category", category);
        resultIntent.putExtra("new_desc", desc);
        resultIntent.putExtra("is_completed", isCompleted);

        // Gửi cờ báo hiệu đây là dữ liệu Cập nhật hay Thêm mới
        resultIntent.putExtra("is_edit_mode", isEditMode);
        if (isEditMode) {
            resultIntent.putExtra("task_id_to_update", editTaskId);
        }

        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, isEditMode ? "Đã cập nhật công việc!" : "Đã lưu công việc mới!", Toast.LENGTH_SHORT).show();
        finish();
    }
}