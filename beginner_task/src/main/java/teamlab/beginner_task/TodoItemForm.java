package teamlab.beginner_task;

import java.util.List;

import teamlab.beginner_task.TodoItem;

public class TodoItemForm {
    private boolean isDone;

    private List<TodoItem> todoItems;

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

}