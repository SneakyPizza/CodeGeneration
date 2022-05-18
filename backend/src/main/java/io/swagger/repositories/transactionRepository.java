package io.swagger.repositories;

import io.swagger.model.entities.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface transactionRepository extends PagingAndSortingRepository<Transaction, UUID> {

}
