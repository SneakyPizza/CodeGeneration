package io.swagger.repositories;

import io.swagger.model.entities.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, UUID> {

    Iterable<Transaction> findByfromUserId(UUID userId);

    Iterable<Transaction> findByIBAN(String iban);

}
