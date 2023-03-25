package stock.exchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import stock.exchange.model.Stock;

import java.util.Optional;

@Controller
public class StockController {
    private final Stock stock;

    public StockController(Stock stock) {
        this.stock = stock;
    }

    @GetMapping("/")
    public String getStock(ModelMap map) {
        map.addAttribute("toDoLists", stock.getCompanyName());
//        map.addAttribute("newToDoList", new ToDoList());
        return "index";
    }


}