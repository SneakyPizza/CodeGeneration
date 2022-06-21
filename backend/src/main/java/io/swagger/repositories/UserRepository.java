package io.swagger.repositories;

import io.swagger.model.entities.Users;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<Users, UUID> {

    //optional findByUsername
    Optional<Users> findByUsername(String username);
    Optional<List<Users>> findByFirstName(String firstname);
    Optional<List<Users>> findByLastName(String lastname);
}
