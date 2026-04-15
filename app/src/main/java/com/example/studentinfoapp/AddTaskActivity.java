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

import com.example.studentinfoapp.R;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTaskTitle, editDueDate, editTaskDesc;
    private RadioGroup radioPriority;
    private Spinner spinnerCategory;
    private CheckBox checkCompleted;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Ánh xạ View
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

        // Xử lý sự kiện khi bấm nút Save
        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                saveTask();
            }
        });
    }

    // Hàm kiểm tra tính hợp lệ của Form
    private boolean validateForm() {
        // Luôn sử dụng trim() để loại bỏ khoảng trắng dư thừa
        String taskName = editTaskTitle.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();

        // 1. Kiểm tra không rỗng
        if (taskName.isEmpty()) {
            Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dueDate.isEmpty()) {
            Toast.makeText(this, "Please enter due date", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 2. Kiểm tra độ dài
        if (taskName.length() < 3) {
            Toast.makeText(this, "Task name must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Dữ liệu hợp lệ
    }

    // Hàm trích xuất dữ liệu và đóng gói gửi về màn hình chính
    private void saveTask() {
        String title = editTaskTitle.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();
        String desc = editTaskDesc.getText().toString().trim();

        // Lấy trạng thái CheckBox
        boolean isCompleted = checkCompleted.isChecked();

        // Lấy lựa chọn từ RadioGroup
        int selectedPriorityId = radioPriority.getCheckedRadioButtonId();
        String priority = "Low"; // Mặc định
        if (selectedPriorityId == R.id.radioPriorityMedium) {
            priority = "Medium";
        } else if (selectedPriorityId == R.id.radioPriorityHigh) {
            priority = "High";
        }

        // Lấy lựa chọn từ Spinner
        String category = spinnerCategory.getSelectedItem().toString();

        // Đóng gói dữ liệu vào Intent (để kết hợp với Lab 02 - ActivityResultLauncher)
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_title", title);
        resultIntent.putExtra("new_due_date", dueDate);
        resultIntent.putExtra("new_priority", priority);
        resultIntent.putExtra("new_category", category);
        resultIntent.putExtra("new_desc", desc);
        resultIntent.putExtra("is_completed", isCompleted);

        // Thiết lập kết quả thành công và đóng Activity
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Task Saved Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}