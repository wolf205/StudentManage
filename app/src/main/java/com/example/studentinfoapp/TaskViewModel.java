package com.example.studentinfoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TaskViewModel extends ViewModel {
    private final MutableLiveData<List<Task>> taskListLiveData;
    private final MutableLiveData<Boolean> isLoadingLiveData; // Dùng để track trạng thái loading
    private final TaskRepository repository;

    public TaskViewModel() {
        taskListLiveData = new MutableLiveData<>();
        isLoadingLiveData = new MutableLiveData<>();
        repository = new TaskRepository();
        loadTasks();
    }

    // Trả về LiveData để Activity quan sát
    public LiveData<List<Task>> getTaskList() {
        return taskListLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingLiveData;
    }

    public void loadTasks() {
        isLoadingLiveData.setValue(true);
        // Lấy dữ liệu từ Repository
        List<Task> tasks = repository.getAllTasks();
        taskListLiveData.setValue(tasks); // Kích hoạt sự thay đổi cho UI
        isLoadingLiveData.setValue(false);
    }

    public void insertTask(Task task) {
        isLoadingLiveData.setValue(true);
        repository.insertTask(task);
        loadTasks(); // Tải lại danh sách
    }

    public void updateTask(Task task) {
        isLoadingLiveData.setValue(true);
        repository.updateTask(task);
        loadTasks();
    }

    public void deleteTask(Task task) {
        isLoadingLiveData.setValue(true);
        repository.deleteTask(task);
        loadTasks();
    }
}