package teamlab.beginner_task;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


class myExcepton extends Exception{
    myExcepton(){

    }
}

@Service
public class TodoService {

    private final TodoItemRepository repository;
    public TodoService(TodoItemRepository repository){
        this.repository  = repository;
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
            TodoItem item = this.repository.findById(id).orElseThrow(myExcepton::new);
            item.setDone(!item.getDone());
            this.repository.save(item);
        }catch (myExcepton e){
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
        if(StringUtils.isEmpty(item.getTitle()) || StringUtils.isEmpty(item.getDeadline())) {
            errorMessage = ("タイトルまたは期限がありません。");
            return errorMessage;
        }
        if(!CollectionUtils.isEmpty(checkList)){
            errorMessage = ("同じタイトルがあります。");
            return errorMessage;
        }
        if(item.getTitle().length()>30){
            errorMessage = "タイトルが長すぎです。";
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

}
