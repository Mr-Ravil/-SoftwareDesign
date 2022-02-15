package todolist.dao;

import todolist.model.ToDoList;
import todolist.model.ToDoTask;

import java.util.List;
import java.util.Optional;

public interface ToDoDao {
    int addToDoTask(ToDoTask toDoTask);
    void deleteToDoTask(int id);
    void deleteToDoTasksByListId(int listId);
    void markToDoTask(int id, boolean done);

    List<ToDoTask> getToDoTaskByListId(int listId);

    int addToDoList(ToDoList toDoList);
    Optional<ToDoList> getToDoList(int id);
    void deleteToDoList(int id);

    List<ToDoList> getToDoLists();
}
