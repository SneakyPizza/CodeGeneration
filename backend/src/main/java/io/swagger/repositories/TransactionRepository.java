package io.swagger.repositories;

import io.swagger.model.entities.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, UUID> {

    Iterable<Transaction> findByIBAN(String iban);

    Iterable<Transaction> findByIBANAndTimestamp(String iban, LocalDateTime now);

    //find all transactions from a IBAN between two dates
    Iterable<Transaction> findByIBANAndTimestampBetween(String iban, LocalDateTime start, LocalDateTime end);
}
