package com.jpetstore.api.repository;

import com.jpetstore.api.entity.SignOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignOnRepository extends JpaRepository<SignOn, String> {
}
