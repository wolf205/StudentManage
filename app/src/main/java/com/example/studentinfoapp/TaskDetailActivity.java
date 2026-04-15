package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {

    private int taskId;
    private String title;
    private String date;
    private int priority;
    private boolean isCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // 1. Ánh xạ các View
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvPriority = findViewById(R.id.tvDetailPriority);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);

        // Giả sử bạn đã thêm 2 nút này vào file activity_task_detail.xml
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        // 2. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            taskId = intent.getIntExtra("task_id", -1); // Lấy thêm ID để xử lý
            title = intent.getStringExtra("task_title");
            date = intent.getStringExtra("task_date");
            priority = intent.getIntExtra("task_priority", 3);
            isCompleted = intent.getBooleanExtra("task_status", false);

            // Hiển thị dữ liệu lên UI
            tvTitle.setText(title != null ? title : "Không có tiêu đề");
            tvDate.setText("Hạn chót: " + (date != null ? date : "Chưa đặt"));

            String priorityText = "Mức độ: ";
            if (priority == 1) priorityText += "Cao";
            else if (priority == 2) priorityText += "Trung bình";
            else priorityText += "Thấp";
            tvPriority.setText(priorityText);

            tvStatus.setText("Trạng thái: " + (isCompleted ? "Đã hoàn thành" : "Chưa xong"));

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chi tiết công việc");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        // 3. Xử lý sự kiện nút Chỉnh sửa
        btnEdit.setOnClickListener(v -> {
            // Chuyển sang AddTaskActivity và truyền kèm dữ liệu cũ
            Intent editIntent = new Intent(TaskDetailActivity.this, AddTaskActivity.class);
            editIntent.putExtra("is_edit_mode", true); // Cờ đánh dấu đây là chế độ sửa
            editIntent.putExtra("edit_task_id", taskId);
            editIntent.putExtra("edit_task_title", title);
            editIntent.putExtra("edit_task_date", date);
            editIntent.putExtra("edit_task_priority", priority);
            startActivity(editIntent);

            finish(); // Đóng Activity hiện tại để khi lưu xong ở AddTask sẽ quay thẳng về MainActivity
        });

        // 4. Xử lý sự kiện nút Xóa
        btnDelete.setOnClickListener(v -> {
            // Tùy thuộc vào kiến trúc, bạn có thể gọi ViewModel ở đây
            // Hoặc đơn giản là trả kết quả về MainActivity để MainActivity gọi ViewModel xóa
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action_delete", true);
            resultIntent.putExtra("task_id_to_delete", taskId);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Đang xóa công việc...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}