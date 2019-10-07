package teamlab.beginner_task;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @Autowired
    TodoItemRepository repository;

    @RequestMapping
    public String index(@ModelAttribute TodoItemForm todoItemForm) {
        todoItemForm.setTodoItems(this.repository.findAll());
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
    public String search() {
        return "search";
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @Transactional(readOnly=false)
    public ModelAndView search(@RequestParam("title") String title,ModelAndView mav) {
        mav.setViewName("search");
        int DoneSearch=1;
        mav.addObject("DoneSearch",DoneSearch);

        if(title == "") {
            mav = new ModelAndView("redirect:search");
        }else if(title !=""){
            List<TodoItem> listname = repository.findByTitleLikeAndDoneFalseOrderByTitleAsc("%"+title+"%");
            mav.addObject("datalist",listname);
            mav.addObject("numberOfdata", listname.size());
        }
        return mav;
    }



    /**
     * editメソッド
     * idからt編集するodoItemを探しreturnする
     * @param id
     * @return mav todoItemクラス
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute TodoItemForm todoItemForm, @RequestParam("id") Long id ,ModelAndView mav) {
        mav.setViewName("edit");
        TodoItem item = this.repository.findById(id).get();
        mav.addObject("editItem",item);
        return mav;
    }

    /**
     * editDoneメソッド
     * 編集操作の実行メソッド
     * @param id
     * @return redirect:/
     */
    @RequestMapping(value = "/editDone", method = RequestMethod.POST)
    public String editDone(@RequestParam("id") Long id ,@RequestParam("title") String title,@RequestParam("deadline") String deadline){
        TodoItem item = this.repository.findById(id).get();
        if(title == "" && deadline == "") {

        }else if(title != "" && deadline == ""){
            item.setTitle(title);
        }else if(title == "" && deadline != ""){
            item.setDeadline(deadline);
        }else if(title != "" && deadline != ""){
            item.setTitle(title);
            item.setDeadline(deadline);
        }
        this.repository.save(item);
        return "redirect:/";
    }
}