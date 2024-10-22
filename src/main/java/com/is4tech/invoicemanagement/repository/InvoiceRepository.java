package com.is4tech.invoicemanagement.repository;

import com.is4tech.invoicemanagement.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(value = "SELECT i.* FROM invoice i JOIN user u ON i.user_id = u.id WHERE i.date BETWEEN :startDate AND :endDate " +
            "AND (:fullName IS NULL OR :fullName = '' OR u.full_name = :fullName)", nativeQuery = true)
    Page<Invoice> findByDateRangeAndFullNameOptional(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate,
                                                     @Param("fullName") String fullName,
                                                     Pageable pageable);

}
