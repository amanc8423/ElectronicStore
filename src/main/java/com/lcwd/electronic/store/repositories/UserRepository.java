package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    //at runtime a query by findByEmail will be created
    Optional<User> findByEmail(String email);
    List<User> findByNameContaining(String keyword);


}
