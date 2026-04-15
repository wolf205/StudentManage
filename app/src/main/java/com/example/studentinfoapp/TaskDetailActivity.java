package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);

        String title = getIntent().getStringExtra("task_title");
        String desc = getIntent().getStringExtra("task_desc");

        tvTitle.setText(title);
        tvDesc.setText(desc);

        // Nút Edit: Mở AddTaskActivity với dữ liệu hiện tại
        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("edit_title", title);
            intent.putExtra("edit_desc", desc);
            startActivity(intent);
        });

        // Nút Delete: Quay lại màn hình chính
        findViewById(R.id.btnDelete).setOnClickListener(v -> finish());
    }
}