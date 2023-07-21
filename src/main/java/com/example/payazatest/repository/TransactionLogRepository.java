package com.example.payazatest.repository;

import com.example.payazatest.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, String> {
}
