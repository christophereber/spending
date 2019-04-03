package com.eberhart.spending.dao;

import com.eberhart.spending.data.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionDAO extends CrudRepository <Transaction, Integer> {

}
