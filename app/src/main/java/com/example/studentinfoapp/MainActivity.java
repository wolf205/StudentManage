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
    private List<Task> currentTasks; // Biến toàn cục để lưu danh sách hiện tại

    // 1. KHÔI PHỤC: Lắng nghe kết quả trả về từ AddTaskActivity
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    // Lấy dữ liệu từ AddTaskActivity truyền về
                    String title = data.getStringExtra("new_title");
                    String dueDate = data.getStringExtra("new_due_date");
                    String priorityStr = data.getStringExtra("new_priority");
                    boolean isCompleted = data.getBooleanExtra("is_completed", false);

                    // Chuyển đổi chuỗi priority thành số
                    int priority = 3; // Low mặc định
                    if ("High".equals(priorityStr)) priority = 1;
                    else if ("Medium".equals(priorityStr)) priority = 2;

                    // Tạo ID giả (Trong thực tế Database sẽ lo việc này)
                    int newId = currentTasks.isEmpty() ? 1 : currentTasks.get(currentTasks.size() - 1).getId() + 1;

                    // Tạo Task mới và thêm vào danh sách
                    Task newTask = new Task(newId, title, dueDate, priority, isCompleted);
                    currentTasks.add(newTask);

                    // Cập nhật lại Adapter (Phải truyền 1 List mới để DiffUtil nhận diện thay đổi)
                    adapter.updateTasks(new ArrayList<>(currentTasks));

                    Toast.makeText(this, "Đã thêm: " + title, Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. Chuẩn bị dữ liệu danh sách
        currentTasks = new ArrayList<>();
        currentTasks.add(new Task(1, "Học RecyclerView", "15/04/2026", 1, false));
        currentTasks.add(new Task(2, "Làm bài tập Android", "20/04/2026", 2, true));

        // 2. KHỞI TẠO ADAPTER TRƯỚC (Quan trọng)
        adapter = new TaskAdapter(currentTasks, task -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("task_id", task.getId());
            startActivity(intent);
        });

        // 3. GÁN ADAPTER VÀO RECYCLERVIEW
        recyclerView.setAdapter(adapter);

        // 4. BÂY GIỜ MỚI GỌI CÁC PHƯƠNG THỨC CỦA ADAPTER
        adapter.updateTasks(new ArrayList<>(currentTasks));

        setupSwipeToDelete();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
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

                // Xóa khỏi danh sách gốc và adapter
                currentTasks.remove(deletedTask);
                adapter.removeTask(position);

                Toast.makeText(MainActivity.this, "Đã xóa: " + deletedTask.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}