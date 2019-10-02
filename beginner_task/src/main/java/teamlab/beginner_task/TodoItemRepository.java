package teamlab.beginner_task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import teamlab.beginner_task.TodoItem;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    public List<TodoItem> findByDoneOrderByTitleAsc(boolean done);
}
