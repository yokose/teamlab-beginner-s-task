package teamlab.beginner_task;


import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

import teamlab.beginner_task.TodoItem;
import teamlab.beginner_task.TodoItemForm;
import teamlab.beginner_task.TodoItemRepository;

@Controller
public class HomeController {

    @Autowired
    TodoItemRepository repository;

    @RequestMapping
    public String index(@ModelAttribute TodoItemForm todoItemForm, @RequestParam("isDone") Optional<Boolean> isDone) {
        todoItemForm.setDone(isDone.isPresent() ? isDone.get() : false);
        todoItemForm.setTodoItems(this.repository.findByDoneOrderByTitleAsc(todoItemForm.isDone()));
        return "index";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    public String done(@RequestParam("id") long id) {
        TodoItem item = this.repository.findById(id).get();
        item.setDone(true);
        this.repository.save(item);
        return "redirect:/?isDone=false";
    }

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public String restore(@RequestParam("id") long id) {
        TodoItem item = this.repository.findById(id).get();
        item.setDone(false);
        this.repository.save(item);
        return "redirect:/?isDone=true";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newItem(TodoItem item) {
        item.setDone(false);

        Date d = new Date();
        SimpleDateFormat d2 = new SimpleDateFormat("yyyy年MM月dd日");
        item.setCreate_day(d2.format(d));

        this.repository.save(item);
        return "redirect:/";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute TodoItemForm todoItemForm, @RequestParam("isDone") Optional<Boolean> isDone) {
        todoItemForm.setDone(isDone.isPresent() ? isDone.get() : false);
        todoItemForm.setTodoItems(this.repository.findByDoneOrderByTitleAsc(todoItemForm.isDone()));
        return "search";
    }
}