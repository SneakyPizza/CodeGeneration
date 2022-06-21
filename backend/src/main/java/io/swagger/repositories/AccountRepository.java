package io.swagger.repositories;

import io.swagger.model.entities.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, UUID> {
    Object findByIBAN(String iban);
    //List<Accounts> findByUU

    List<Account> findByUsersId(UUID userid);
}
