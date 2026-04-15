package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Ánh xạ các TextView trong activity_task_detail.xml
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvPriority = findViewById(R.id.tvDetailPriority);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("task_title");
            String date = intent.getStringExtra("task_date");
            int priority = intent.getIntExtra("task_priority", 3);
            boolean isCompleted = intent.getBooleanExtra("task_status", false);

            // Hiển thị dữ liệu
            tvTitle.setText(title != null ? title : "Không có tiêu đề");
            tvDate.setText("Hạn chót: " + (date != null ? date : "Chưa đặt"));

            String priorityText = "Mức độ: ";
            if (priority == 1) priorityText += "Cao";
            else if (priority == 2) priorityText += "Trung bình";
            else priorityText += "Thấp";
            tvPriority.setText(priorityText);

            tvStatus.setText("Trạng thái: " + (isCompleted ? "Đã hoàn thành" : "Chưa xong"));

            // Đặt tiêu đề cho Action Bar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chi tiết công việc");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Nút quay lại
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Kết thúc Activity khi nhấn nút back trên toolbar
        return true;
    }
}