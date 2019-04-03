package com.eberhart.spending.controllers;

import com.eberhart.spending.dao.TransactionDAO;
import com.eberhart.spending.data.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/reset")
public class ResetController {

    @Autowired
    TransactionDAO transactionDAO;

    @PostMapping
    public String post() {
        transactionDAO.deleteAll();
        return "redirect:/";
    }


}
