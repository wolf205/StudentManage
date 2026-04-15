package com.example.studentinfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskViewModel taskViewModel;

    // 1. Lắng nghe kết quả từ AddTaskActivity (Xử lý cả Thêm mới và Chỉnh sửa)
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String title = data.getStringExtra("new_title");
                    String dueDate = data.getStringExtra("new_due_date");
                    String priorityStr = data.getStringExtra("new_priority");
                    boolean isCompleted = data.getBooleanExtra("is_completed", false);
                    boolean isEditMode = data.getBooleanExtra("is_edit_mode", false);

                    int priority = 3;
                    if ("High".equals(priorityStr)) priority = 1;
                    else if ("Medium".equals(priorityStr)) priority = 2;

                    if (isEditMode) {
                        // Chế độ CẬP NHẬT
                        int taskId = data.getIntExtra("task_id_to_update", -1);
                        if (taskId != -1) {
                            Task updatedTask = new Task(taskId, title, dueDate, priority, isCompleted);
                            taskViewModel.updateTask(updatedTask);
                            // Toast đã được gọi bên AddTaskActivity nên ở đây có thể bỏ qua hoặc để lại tùy ý
                        }
                    } else {
                        // Chế độ THÊM MỚI
                        List<Task> currentTasks = taskViewModel.getTaskList().getValue();
                        int newId = (currentTasks == null || currentTasks.isEmpty()) ? 1 : currentTasks.get(currentTasks.size() - 1).getId() + 1;

                        Task newTask = new Task(newId, title, dueDate, priority, isCompleted);
                        taskViewModel.insertTask(newTask);
                    }
                }
            });

    // 2. Lắng nghe kết quả từ TaskDetailActivity (Xử lý tín hiệu Xóa)
    private final ActivityResultLauncher<Intent> taskDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    boolean actionDelete = data.getBooleanExtra("action_delete", false);

                    if (actionDelete) {
                        int taskIdToDelete = data.getIntExtra("task_id_to_delete", -1);
                        if (taskIdToDelete != -1) {
                            // Tìm Task trong danh sách hiện tại và gọi ViewModel xóa
                            List<Task> currentTasks = taskViewModel.getTaskList().getValue();
                            if (currentTasks != null) {
                                for (Task t : currentTasks) {
                                    if (t.getId() == taskIdToDelete) {
                                        taskViewModel.deleteTask(t);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Khởi tạo Adapter
        adapter = new TaskAdapter(task -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.putExtra("task_title", task.getTitle());
            intent.putExtra("task_date", task.getDueDate());
            intent.putExtra("task_priority", task.getPriority());
            intent.putExtra("task_status", task.isCompleted());

            // Dùng launcher mới thay vì startActivity()
            taskDetailLauncher.launch(intent);
        });
        recyclerView.setAdapter(adapter);

        // Quan sát LiveData
        taskViewModel.getTaskList().observe(this, tasks -> {
            adapter.updateTasks(tasks);
        });

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

                taskViewModel.deleteTask(deletedTask);
                Toast.makeText(MainActivity.this, "Đã xóa: " + deletedTask.getTitle(), Toast.LENGTH_SHORT).show();
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }
}