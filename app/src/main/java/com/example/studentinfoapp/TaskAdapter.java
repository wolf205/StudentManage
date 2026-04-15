package com.example.studentinfoapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentinfoapp.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList = new ArrayList<>();
    private OnTaskClickListener listener;

    // Interface để bắt sự kiện click
    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    // 1. Sử dụng DiffUtil để tối ưu hóa việc update list (Bài tập nâng cao)
    public void updateTasks(List<Task> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TaskDiffCallback(this.taskList, newTasks));
        this.taskList.clear();
        this.taskList.addAll(newTasks);
        diffResult.dispatchUpdatesTo(this);
    }

    public Task getTaskAt(int position) {
        return taskList.get(position);
    }

    public void removeTask(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    // 2. Lớp ViewHolder (Lưu trữ references của các view)
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView dateView;
        private ImageView priorityIcon;
        private CheckBox completionCheckbox;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.taskTitle);
            dateView = itemView.findViewById(R.id.taskDate);
            priorityIcon = itemView.findViewById(R.id.priorityIcon);
            completionCheckbox = itemView.findViewById(R.id.checkComplete);
        }

        public void bind(Task task, OnTaskClickListener listener) {
            titleView.setText(task.getTitle());
            dateView.setText(task.getDueDate());
            completionCheckbox.setChecked(task.isCompleted());

            // Đổi màu/icon tùy theo độ ưu tiên (1: High đỏ, 2: Medium cam, 3: Low xanh)
            if (task.getPriority() == 1) {
                priorityIcon.setColorFilter(Color.RED);
            } else if (task.getPriority() == 2) {
                priorityIcon.setColorFilter(Color.rgb(255, 165, 0)); // Màu cam
            } else {
                priorityIcon.setColorFilter(Color.GREEN);
            }

            // Bắt sự kiện click vào toàn bộ item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskClick(task);
                }
            });
        }
    }

    // 3. Lớp hỗ trợ DiffUtil so sánh dữ liệu cũ và mới
    private static class TaskDiffCallback extends DiffUtil.Callback {
        private final List<Task> oldList;
        private final List<Task> newList;

        public TaskDiffCallback(List<Task> oldList, List<Task> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }

        @Override
        public int getNewListSize() { return newList.size(); }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Task oldTask = oldList.get(oldItemPosition);
            Task newTask = newList.get(newItemPosition);
            return oldTask.getTitle().equals(newTask.getTitle()) &&
                    oldTask.isCompleted() == newTask.isCompleted();
        }
    }
}