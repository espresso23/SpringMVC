package com.repository;

import com.model.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan, String> {
    @Query("SELECT b from BenhNhan b where b.soCMND = :soCMND")
    List<BenhNhan> getBenhNhansBySoCMND(@Param("soCMND") String soCMND);

    @Query("SELECT b from BenhNhan b where b.soCMND = :soCMND")
    BenhNhan getBenhNhansBySoCMND1(@Param("soCMND") String soCMND);

}
