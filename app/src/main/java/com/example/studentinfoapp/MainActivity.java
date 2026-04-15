package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> currentTasks;

    // Lắng nghe kết quả từ AddTaskActivity
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String title = data.getStringExtra("new_title");
                    String dueDate = data.getStringExtra("new_due_date");
                    String priorityStr = data.getStringExtra("new_priority");

                    int priority = 3;
                    if ("High".equals(priorityStr)) priority = 1;
                    else if ("Medium".equals(priorityStr)) priority = 2;

                    int newId = currentTasks.isEmpty() ? 1 : currentTasks.get(currentTasks.size() - 1).getId() + 1;
                    Task newTask = new Task(newId, title, dueDate, priority, false);

                    currentTasks.add(newTask);
                    adapter.updateTasks(new ArrayList<>(currentTasks));
                    Toast.makeText(this, "Đã thêm: " + title, Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ View
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fabAdd);

        // 2. Thiết lập LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Chuẩn bị dữ liệu ban đầu
        currentTasks = new ArrayList<>();
        currentTasks.add(new Task(1, "Học RecyclerView", "15/04/2026", 1, false));
        currentTasks.add(new Task(2, "Làm bài thực hành Lab 07", "20/04/2026", 2, true));

        // 4. KHỞI TẠO ADAPTER TRƯỚC (Sửa lỗi NullPointerException)
        adapter = new TaskAdapter(task -> {
            // Thống nhất Key truyền dữ liệu sang TaskDetailActivity
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.putExtra("task_title", task.getTitle());
            intent.putExtra("task_date", task.getDueDate());
            intent.putExtra("task_priority", task.getPriority());
            intent.putExtra("task_status", task.isCompleted());
            startActivity(intent);
        });

        // 5. Gán Adapter và cập nhật dữ liệu
        recyclerView.setAdapter(adapter);
        adapter.updateTasks(new ArrayList<>(currentTasks));

        // 6. Các tính năng bổ sung
        setupSwipeToDelete();
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task deletedTask = adapter.getTaskAt(position);
                currentTasks.remove(deletedTask);
                adapter.removeTask(position);
                Toast.makeText(MainActivity.this, "Đã xóa: " + deletedTask.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }
}