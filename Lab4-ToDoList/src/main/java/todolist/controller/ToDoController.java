package todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import todolist.dao.ToDoDao;
import todolist.model.ToDoList;
import todolist.model.ToDoTask;

import java.util.Optional;

@Controller
public class ToDoController {
    private final ToDoDao toDoDao;

    public ToDoController(ToDoDao toDoDao) {
        this.toDoDao = toDoDao;
    }

    @GetMapping("/")
    public String getToDoLists(ModelMap map) {
        map.addAttribute("toDoLists", toDoDao.getToDoLists());
        map.addAttribute("newToDoList", new ToDoList());
        return "index";
    }

    @RequestMapping(value = "/add-todo-list", method = RequestMethod.POST)
    public String addToDoList(@ModelAttribute("toDoList") ToDoList toDoList) {
        toDoDao.addToDoList(toDoList);
        return "redirect:/";
    }

    @GetMapping("/todo-list")
    public String getToDoList(@RequestParam int listId, ModelMap map) {
        Optional<ToDoList> optionalToDoList = toDoDao.getToDoList(listId);

        if (optionalToDoList.isEmpty()) {
            return "redirect:/";
        }

        ToDoList toDoList = optionalToDoList.get();

        map.addAttribute("toDoList", toDoList);
        map.addAttribute("newToDoTask", new ToDoTask());
        map.addAttribute("toDoTasks", toDoDao.getToDoTaskByListId(listId));

        return "todo-list";
    }

    @RequestMapping(value = "/delete-todo-list", method = RequestMethod.POST)
    public String deleteToDoList(@ModelAttribute("listId") int listId) {
        toDoDao.deleteToDoTasksByListId(listId);
        toDoDao.deleteToDoList(listId);
        return "redirect:/";
    }

    @RequestMapping(value = "/add-todo-task", method = RequestMethod.POST)
    public String addToDoTask(@ModelAttribute("newToDoTask") ToDoTask toDoTask) {
        toDoDao.addToDoTask(toDoTask);
        return "redirect:/todo-list?listId=" + toDoTask.getListId();
    }

    @RequestMapping(value = "/mark-todo-task", method = RequestMethod.POST)
    public String markToDoTask(@ModelAttribute("listId") int listId,
                               @ModelAttribute("taskId") int taskId,
                               @ModelAttribute("done") boolean done) {
        toDoDao.markToDoTask(taskId, done);
        return "redirect:/todo-list?listId=" + listId;
    }

    @RequestMapping(value = "/delete-todo-task", method = RequestMethod.POST)
    public String deleteToDoTask(@ModelAttribute("listId") int listId,
                                 @ModelAttribute("taskId") int taskId) {
        toDoDao.deleteToDoTask(taskId);
        return "redirect:/todo-list?listId=" + listId;
    }
}