package io.swagger.repositories;

import io.swagger.model.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {


    User findByUsername(String username);
    List<User> findByFirstName(String firstname);
    List<User> findByLastName(String lastName);
    //User findById(UUID id);
}
