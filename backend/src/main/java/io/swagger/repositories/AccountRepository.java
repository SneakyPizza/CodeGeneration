package io.swagger.repositories;

import io.swagger.model.AccountDTO;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, UUID> {
    Object findByIBAN(String iban);
    //List<Accounts> findByUU

    List<Account> findByUserId(UUID userid);
}
