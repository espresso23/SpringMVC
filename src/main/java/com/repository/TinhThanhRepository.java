package com.repository;

import com.model.TinhThanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinhThanhRepository extends JpaRepository<TinhThanh, String> {
}
