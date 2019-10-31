package teamlab.beginner_task;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.servlet.ModelAndView;
import teamlab.beginner_task.TodoItem;
import teamlab.beginner_task.TodoItemForm;
import teamlab.beginner_task.TodoItemRepository;

/**
 * @author kenshin
 * @version 1.0
 */

@Controller
public class HomeController {

    private final TodoItemRepository repository;
    private String errorMessage;
    public HomeController(TodoItemRepository repository){
        this.repository  = repository;
    }

    @RequestMapping
    public String index(@ModelAttribute TodoItemForm todoItemForm) {
        todoItemForm.setTodoItems(this.repository.findAll());
        todoItemForm.setExistTodo(true);
        if(!StringUtils.isEmpty(errorMessage)){
            todoItemForm.setErrorMessage(errorMessage);
            errorMessage = null;
        }


        List todoItems = todoItemForm.getTodoItems();
        if(todoItems.isEmpty()){todoItemForm.setExistTodo(false);}
        return "index";
    }

    @RequestMapping(value = "/done", method = RequestMethod.POST)
    public String done(@RequestParam("id") long id) {
        //TodoItem item = this.repository.findById(id).get();
        try{
            TodoItem item = this.repository.findById(id).orElseThrow(() -> new RuntimeException());
            item.setDone(!item.getDone());
            this.repository.save(item);
        }catch (RuntimeException e){
            errorMessage = "todoがnullです。";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/new")

    public String newItem(@ModelAttribute TodoItemForm todoItemForm, TodoItem item) {
        List<TodoItem> checkList = repository.findByTitle(item.getTitle());
        if(StringUtils.isEmpty(item.getTitle()) || StringUtils.isEmpty(item.getDeadline())) {
            errorMessage = ("タイトルまたは期限がありません。");
            return "redirect:/";
        }
        if(!CollectionUtils.isEmpty(checkList)){
            errorMessage = ("同じタイトルがあります。");
            return "redirect:/";
        }
        if(item.getTitle().length()>30){
            errorMessage = "タイトルが長すぎです。";
            return "redirect:/";
        }
        DateFormat checkDay = new SimpleDateFormat("yyyy年MM月dd日");
        checkDay.setLenient(false);
        try {
            checkDay.parse(item.getDeadline());
        } catch (ParseException e) {
            // 日付妥当性NG時の処理を記述
            errorMessage = ("日付が正しくありません。");
            return "redirect:/";
        }

        //Date today = new Date();
        //SimpleDateFormat d2 = new SimpleDateFormat("yyyy年MM月dd日");
        //item.setCreate_day(d2.format(today));
        item.setCreate_day(new Date());
        item.setDone(false);
        this.repository.save(item);
        return "redirect:/";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute TodoItemForm todoItemForm) {
        if(!StringUtils.isEmpty(errorMessage)){
            todoItemForm.setErrorMessage(errorMessage);
            errorMessage = null;
        }
        return "search";
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView search(@RequestParam("title") String title,ModelAndView mav) {
        mav.setViewName("search");
        int DoneSearch=1;
        mav.addObject("DoneSearch",DoneSearch);

        if(StringUtils.isEmpty(title)) {
            errorMessage ="タイトルがありません。";
            mav.addObject("errorMessage",errorMessage);
            errorMessage = null;
            return mav;
        }

        if(title.length()>30){
            errorMessage = "タイトルが長すぎです。";
            mav.addObject("errorMessage",errorMessage);
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
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(deadline)) {
            errorMessage = "タイトルまたは期限を記入してください。";
            return "forward:edit";
        }else if(!StringUtils.isEmpty(title) && StringUtils.isEmpty(deadline)){
            List<TodoItem> checkList = repository.findByTitle(title);
            if(!CollectionUtils.isEmpty(checkList)){
                errorMessage = ("同じタイトルがあります。");
                return "forward:edit";
            }
            if(title.length()>30){
                errorMessage = "タイトルが長すぎです。";
                return "forward:edit";
            }

            item.setTitle(title);
        }else if(StringUtils.isEmpty(title) && !StringUtils.isEmpty(deadline)){
            DateFormat checkDay = new SimpleDateFormat("yyyy年MM月dd日");
            checkDay.setLenient(false);
            try {
                checkDay.parse(deadline);
            } catch (ParseException e) {
                // 日付妥当性NG時の処理を記述
                errorMessage = ("日付が正しくありません。");
                return "forward:edit";
            }

            item.setDeadline(deadline);
        }else if(!StringUtils.isEmpty(title) && !StringUtils.isEmpty(deadline)){
            List<TodoItem> checkList = repository.findByTitle(title);
            if(!CollectionUtils.isEmpty(checkList)){
                errorMessage = ("同じタイトルがあります。");
                return "forward:edit";
            }
            if(title.length()>30){
                errorMessage = "タイトルが長すぎです。";
                return "forward:edit";
            }

            DateFormat checkDay = new SimpleDateFormat("yyyy年MM月dd日");
            checkDay.setLenient(false);
            try {
                checkDay.parse(deadline);
            } catch (ParseException e) {
                // 日付妥当性NG時の処理を記述
                errorMessage = ("日付が正しくありません。");
                return "forward:edit";
            }

            item.setTitle(title);
            item.setDeadline(deadline);
        }
        this.repository.save(item);
        return "redirect:/";
    }
}