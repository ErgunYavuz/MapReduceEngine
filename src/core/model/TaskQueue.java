package core.model;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class TaskQueue {
    private Queue<String> tasks = new ConcurrentLinkedQueue<>();
    private int totalTasks;

    public TaskQueue(List<String> data) {
        this.tasks.addAll(data);
        this.totalTasks = data.size();
    }

    public String getNextTask() {
        return tasks.poll();
    }

    public boolean hasMoreTasks() {
        return !tasks.isEmpty();
    }

    public int getRemainingTasks() {
        return tasks.size();
    }

    public int getTotalTasks() {
        return totalTasks;
    }
}