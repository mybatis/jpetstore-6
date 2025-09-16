package com.jpetstore.api.repository;

import com.jpetstore.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    @Query("SELECT a FROM Account a JOIN a.signOn s WHERE a.username = :username AND s.password = :password")
    Optional<Account> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}