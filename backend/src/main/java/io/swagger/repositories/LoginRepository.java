package io.swagger.repositories;

import io.swagger.model.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginRepository extends PagingAndSortingRepository<User, UUID> {
//    boolean login(String username, String password);
}
