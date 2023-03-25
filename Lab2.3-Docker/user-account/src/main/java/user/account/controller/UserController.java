package user.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import user.account.model.User;

@Controller
public class UserController {
    private final User user;

    public UserController(User user) {
        this.user = user;
    }

    @GetMapping("/")
    public String getStock(ModelMap map) {
//        map.addAttribute("toDoLists", user.getCompanyName());
//        map.addAttribute("newToDoList", new ToDoList());
        return "index";
    }


}