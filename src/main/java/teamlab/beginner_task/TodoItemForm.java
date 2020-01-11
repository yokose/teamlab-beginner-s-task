package teamlab.beginner_task;

import java.io.Serializable;
import java.util.List;

import teamlab.beginner_task.TodoItem;

public class TodoItemForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean existTodo;

    private List<TodoItem> todoItems;

    private String errorMessage;

    public List<TodoItem> getTodoItems() { return todoItems; }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public boolean getExistTodo() { return existTodo; }

    public void setExistTodo(boolean existTodo) { this.existTodo = existTodo; }

    public String  getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}