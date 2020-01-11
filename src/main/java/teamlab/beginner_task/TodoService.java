package teamlab.beginner_task;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class TodoService{

    private final TodoItemRepository repository;
    public String errorMessage;
    public TodoService(TodoItemRepository repository){
        this.repository  = repository;
    }

    /**
     * findAllメソッド
     * repositoryのtodoItemを全てreturn
     * @return repositoryのtodoItemを全て
     */
    public List<TodoItem> findAll(){
        return this.repository.findAll();
    }

    /**
     * repositorySaveメソッド
     * repositoryにitemを保存
     * @return void
     */
    public void repositorySave(TodoItem item){
        this.repository.save(item);
    }

    /**
     * repositorySaveメソッド
     * repositoryにitemを保存
     * @return void
     */
    public List<TodoItem> findForSearching(String title){
        return repository.findByTitleLikeAndDoneFalseOrderByTitleAsc("%"+title+"%");
    }

    /**
     * errorMessageCheckメソッド
     * errormessageの中身を確認し、TodoItemFormにおくる
     * @param errorMessage エラーメッセージの内容
     * @param todoItemForm フォームクラス
     * @return String null エラーメッセージを確認したので空にする
     */
    public String errorMessageCheck(String errorMessage, TodoItemForm todoItemForm){
        if(!StringUtils.isEmpty(errorMessage)){
            todoItemForm.setErrorMessage(errorMessage);
        }
        return null;
    }

    /**
     * switchDoneメソッド
     * 引数idのtodoのDoneを反転するメソッド
     * @param id
     * @param errorMessage エラーメッセージの内容
     * @return String errorMessage todoが空の時エラーメッセージを返す
     */
    public String switchDone(Long id, String errorMessage){
        try{
            TodoItem item = this.repository.findById(id).orElseThrow(myException::new);
            item.setDone(!item.getDone());
            this.repository.save(item);
        }catch (myException e){
            errorMessage = "todoがnullです。";
        }
        return errorMessage;
    }

    /**
     * newTodoCheckメソッド
     * 新しいTodoのエラーチェック
     * @param item 入力された新しいTodoItem
     * @param errorMessage エラーメッセージの内容
     * @return String errorMessage エラーがあった時エラーメッセージを返す
     */
    public String newTodoCheck(TodoItem item, String errorMessage){
        List<TodoItem> checkList = repository.findByTitle(item.getTitle());
        if(!CollectionUtils.isEmpty(checkList)){
            errorMessage = ("同じタイトルがあります。");
            return errorMessage;
        }
        DateFormat checkDay = new SimpleDateFormat("yyyy年MM月dd日");
        checkDay.setLenient(false);
        try {
            checkDay.parse(item.getDeadline());
        } catch (ParseException e) {
            // 日付妥当性NG時の処理を記述
            errorMessage = ("日付が正しくありません。");
            return errorMessage;
        }
        return null;
    }

    /**
     * searchTitleCheckメソッド
     * title検索のエラーチェック
     * @param title 入力されたタイトル
     * @return String errorMessage エラーがあった時エラーメッセージを返す
     */
    public String  searchTitleCheck(String title){
        if(StringUtils.isEmpty(title)) {
            String  errorMessage ="タイトルがありません。";
            return errorMessage;
        }

        if(title.length()>30){
            String  errorMessage = "タイトルが長すぎです。";
            return errorMessage;
        }
        return null;
    }

    /**
     * serchTodoByIdMavメソッド
     * 引数idのtodoを探すメソッド
     * @param id 探したいtodoのid
     * @param mav ModelAndView
     * @return String errorMessage todoが空の時エラーメッセージを返す
     */
    public String searchTodoByIdMav(Long id, ModelAndView mav){
        String errorMessage = null;
        try{
            TodoItem item = this.repository.findById(id).orElseThrow(myException::new);
            mav.addObject("editItem",item);
        }catch (myException e){
            errorMessage = "todoがnullです。";
        }
        return errorMessage;
    }

    /**
     * serchTodoByIdメソッド
     * 引数idのtodoを探すメソッド
     * @param id 探したいtodoのid
     * @return TodoItem item 検索したtodoを返す
     */
    public TodoItem searchTodoById(Long id){

        try{
            TodoItem item = this.repository.findById(id).orElseThrow(myException::new);
            return item;
        }catch (myException e){
            this.errorMessage = "todoがnullです。";
        }
        return null;
    }


    /**
     * checkEditメソッド
     * todo編集の入力エラーチェック
     * @param title 入力されたタイトル
     * @param deadline 入力された期日
     * @param item 保存先TodoItem
     * @return String errorMessage エラーがあった時エラーメッセージを返す
     */
    public String checkEdit(String title, String deadline, TodoItem item) {
        String errorMessage;
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(deadline)) {
            errorMessage = "タイトルまたは期限を記入してください。";
            return errorMessage;
        } else if (!StringUtils.isEmpty(title) && StringUtils.isEmpty(deadline)) {
            List<TodoItem> checkList = repository.findByTitle(title);
            if (!CollectionUtils.isEmpty(checkList)) {
                errorMessage = ("同じタイトルがあります。");
                return errorMessage;
            }
            if (title.length() > 30) {
                errorMessage = "タイトルが長すぎです。";
                return errorMessage;
            }

            item.setTitle(title);
        } else if (StringUtils.isEmpty(title) && !StringUtils.isEmpty(deadline)) {
            DateFormat checkDay = new SimpleDateFormat("yyyy年MM月dd日");
            checkDay.setLenient(false);
            try {
                checkDay.parse(deadline);
            } catch (ParseException e) {
                // 日付妥当性NG時の処理を記述
                errorMessage = ("日付が正しくありません。");
                return errorMessage;
            }

            item.setDeadline(deadline);
        } else if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(deadline)) {
            List<TodoItem> checkList = repository.findByTitle(title);
            if (!CollectionUtils.isEmpty(checkList)) {
                errorMessage = ("同じタイトルがあります。");
                return errorMessage;
            }
            if (title.length() > 30) {
                errorMessage = "タイトルが長すぎです。";
                return errorMessage;
            }

            DateFormat checkDay = new SimpleDateFormat("yyyy年MM月dd日");
            checkDay.setLenient(false);
            try {
                checkDay.parse(deadline);
            } catch (ParseException e) {
                // 日付妥当性NG時の処理を記述
                errorMessage = ("日付が正しくありません。");
                return errorMessage;
            }

            item.setTitle(title);
            item.setDeadline(deadline);
        }
        return null;
    }

}
