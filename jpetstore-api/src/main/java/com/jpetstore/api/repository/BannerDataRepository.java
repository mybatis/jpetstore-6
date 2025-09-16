package com.jpetstore.api.repository;

import com.jpetstore.api.entity.BannerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerDataRepository extends JpaRepository<BannerData, String> {
}
