package io.swagger.repositories;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Transaction;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, UUID> {

    Iterable<Transaction> findByIBAN(String iban);

    Iterable<Transaction> findByIBANAndTimestamp(String iban, LocalDateTime now);

    //find all transactions from a IBAN between two dates
    Iterable<Transaction> findByIBANAndTimestampBetween(String iban, LocalDateTime start, LocalDateTime end);

    //find all transactions where id = TARGET_ID custom query
    @Query("select t from Transaction t where t.Target.id = ?1")
    Iterable<Transaction> findByTargetId(UUID id);

    @Query("select t from Transaction t where t.Origin.id = ?1")
    Iterable<Transaction> findByOriginId(UUID id);

    @Query("select t from Transaction t where t.Origin.id = ?1 and t.timestamp between ?2 and ?3")
    Iterable<Transaction> findByOriginIdAndTimestampBetween(UUID id, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select t from Transaction t where t.Target.id = ?1 and t.timestamp between ?2 and ?3")
    Iterable<Transaction> findByTargetIdAndTimestampBetween(UUID id, LocalDateTime startDate, LocalDateTime endDate);
}
