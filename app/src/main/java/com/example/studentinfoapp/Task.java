package com.example.studentinfoapp;

public class Task {
    private int id;
    private String title;
    private String dueDate;
    private int priority; // 1: High, 2: Medium, 3: Low
    private boolean isCompleted;

    public Task(int id, String title, String dueDate, int priority, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    // Các hàm Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return isCompleted; }

    // Các hàm Setters (nếu cần)
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
