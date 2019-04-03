package com.eberhart.spending.controllers;

import com.eberhart.spending.dao.TransactionDAO;
import com.eberhart.spending.data.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    TransactionDAO transactionDAO;

    public static final DecimalFormat moneyFormat = new DecimalFormat("$#,###,##0.##");
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM d yyyy");
    public static final BigDecimal totalAllowance = new BigDecimal(700);

    @GetMapping
    public String index(ModelMap model){

        List<Transaction> list = new ArrayList<>();
        transactionDAO.findAll().forEach(list::add);
        BigDecimal amountSpent = list.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal amountLeft = totalAllowance.subtract(amountSpent);

        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        LocalDate nextPayday = LocalDate.now();
        LocalDate endOfPeriod;
        if (dayOfMonth >= 7 && dayOfMonth < 22) {
            endOfPeriod = nextPayday.withDayOfMonth(22);
        } else if (dayOfMonth < 7) {
            endOfPeriod = nextPayday.withDayOfMonth(7);
        } else {
            endOfPeriod = nextPayday.plusMonths(1).withDayOfMonth(7);
        }

        long daysLeft = Duration.between(today.atTime(0, 0), endOfPeriod.atTime(0, 0)).toDays() ;

        model.put("trans", new Transaction());
        model.put("totalAllowance", moneyFormat.format(totalAllowance));
        model.put("amountSpent", moneyFormat.format(amountSpent));
        model.put("allowance", moneyFormat.format(amountLeft));
        model.put("list", list);
        model.put("dayOfMonth", dayOfMonth);
        model.put("endOfPeriod", endOfPeriod.format(dateFormat));
        model.put("daysLeft", daysLeft);
        model.put("spendingPerDay", amountLeft.divide(new BigDecimal(daysLeft), 2, RoundingMode.CEILING));
        return "index";
    }

    @PostMapping
    public String post(HttpServletRequest request, @ModelAttribute("trans") Transaction trans, Errors errors, Map<String, Object> model) {
        transactionDAO.save(trans);
        return "redirect:/";
    }


}
