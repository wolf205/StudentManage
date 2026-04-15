package com.example.studentinfoapp;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private List<Task> taskList;

    public TaskRepository() {
        taskList = new ArrayList<>();
        // Mock data ban đầu
        taskList.add(new Task(1, "Học RecyclerView", "15/04/2026", 1, false));
        taskList.add(new Task(2, "Làm bài thực hành Lab 07", "20/04/2026", 2, true));
    }

    // CRUD: Read
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskList); // Trả về bản sao
    }

    // CRUD: Create
    public void insertTask(Task task) {
        taskList.add(task);
    }

    // CRUD: Update
    public void updateTask(Task task) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == task.getId()) {
                taskList.set(i, task);
                return;
            }
        }
    }

    // CRUD: Delete
    public void deleteTask(Task task) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == task.getId()) {
                taskList.remove(i);
                return;
            }
        }
    }
}