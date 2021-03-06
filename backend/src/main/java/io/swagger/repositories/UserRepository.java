package io.swagger.repositories;

import io.swagger.model.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

    //optional findByUsername
    Optional<User> findByUsername(String username);
    Optional<List<User>> findByFirstName(String firstname);
    Optional<List<User>> findByLastName(String lastname);
}
