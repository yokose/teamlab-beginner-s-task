package teamlab.beginner_task;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import com.sun.tools.javac.comp.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.web.servlet.ModelAndView;

/**
 * @author kenshin
 * @version 1.0
 */

@Controller
public class HomeController {

    private final TodoItemRepository repository;
    private String errorMessage;
    private final TodoService todoService;
    public HomeController(TodoItemRepository repository, TodoService todoService){
        this.repository  = repository;
        this.todoService = todoService;
    }

    @ModelAttribute
    TodoItem setUpTodoItem() {
        return new TodoItem();
    }

    @ModelAttribute
    TodoItemForm setUpTodoItemForm() {
        TodoItemForm todoItemForm = new TodoItemForm();
        todoItemForm.setTodoItems(this.repository.findAll());
        todoItemForm.setExistTodo(true);
        List todoItems = todoItemForm.getTodoItems();
        if(todoItems.isEmpty()){todoItemForm.setExistTodo(false);}

        return todoItemForm;
    }



    @RequestMapping
    public String index(@ModelAttribute TodoItemForm todoItemForm) {
        todoItemForm.setTodoItems(this.repository.findAll());
        todoItemForm.setExistTodo(true);

        errorMessage = todoService.errorMessageCheck(errorMessage, todoItemForm);

        List todoItems = todoItemForm.getTodoItems();
        if(todoItems.isEmpty()){todoItemForm.setExistTodo(false);}
        return "index";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    public String done(@RequestParam("id") long id) {
        errorMessage = todoService.switchDone(id, errorMessage);

        return "redirect:/";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newItem(@ModelAttribute TodoItemForm todoItemForm, @Validated TodoItem item, BindingResult result) {
        if(result.hasErrors()){
            return "index";
        }
       errorMessage = todoService.newTodoCheck(item, errorMessage);
       if(!StringUtils.isEmpty(errorMessage)){
           return "redirect:/";
       }

        item.setCreate_day(new Date());
        item.setDone(false);
        this.repository.save(item);
        return "redirect:/";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute TodoItemForm todoItemForm) {
        errorMessage = todoService.errorMessageCheck(errorMessage, todoItemForm);
        return "search";
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView search(@RequestParam("title") String title,ModelAndView mav) {
        mav.setViewName("search");
        boolean DoneSearch=true;
        mav.addObject("DoneSearch",DoneSearch);

        errorMessage = todoService.searchTitleCheck(title);
        if(!StringUtils.isEmpty(errorMessage)){
            mav.addObject("errorMessage", errorMessage);
            errorMessage = null;
            return mav;
        }

        List<TodoItem> listname = repository.findByTitleLikeAndDoneFalseOrderByTitleAsc("%"+title+"%");
        mav.addObject("datalist",listname);
        mav.addObject("numberOfdata", listname.size());

        return mav;
    }



    /**
     * editメソッド
     * idから編集するtodoItemを探しreturnする
     * @param id
     * @return mav todoItemクラス
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute TodoItemForm todoItemForm, @RequestParam("id") Long id ,ModelAndView mav) {
        mav.setViewName("edit");
        TodoItem item = this.repository.findById(id).get();
        mav.addObject("editItem",item);
        if(!StringUtils.isEmpty(errorMessage)){
            mav.addObject("errorMessage",errorMessage);
            errorMessage = null;
        }
        return mav;
    }

    /**
     * editDoneメソッド
     * 編集操作の実行メソッド
     * @param id
     * @return redirect:/
     */
    @RequestMapping(value = "/editDone", method = RequestMethod.POST)
    public String editDone(@ModelAttribute TodoItemForm todoItemForm,@RequestParam("id") Long id ,@RequestParam("title") String title,@RequestParam("deadline") String deadline){
        TodoItem item = this.repository.findById(id).get();
        errorMessage =  todoService.checkEdit(title, deadline, item);
        if(!StringUtils.isEmpty(errorMessage)){
            return "forward:edit";
        }

        this.repository.save(item);
        return "redirect:/";
    }
}