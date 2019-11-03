# teamlab-beginner-s-task

## データベース設計

|Column|Type|Options|
|------|----|-------|
|id|integer|null: false, primary_key|
|title|string|null: false|
|done|boolean|null: false|
|deadline|string|null: false|
|create_day|string|null: false|

## 仕様技術
統合開発環境
- IntelliJ IDEA

ビルドツール
- Gradle

SQL
- MySQL

言語
- Java

ライブラリ
- Spring Framework
- Thymeleaf

## ページ一覧
### トップ画面
- 機能：ToDoの表示、追加、状態変更
### ToDo編集画面
- 機能：ToDoの編集
### 検索画面
- 機能：ToDoの検索

## 特筆事項(備忘録)
- DBとの接続にSpringFrameworkのJpaを使用
- ControllerとViewの値の引き渡しにTodoItemFormクラスを使用？
