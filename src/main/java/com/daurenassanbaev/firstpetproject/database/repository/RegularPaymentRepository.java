package com.daurenassanbaev.firstpetproject.database.repository;

import com.daurenassanbaev.firstpetproject.database.entity.RegularPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegularPaymentRepository extends JpaRepository<RegularPayment, Integer> {
    @Query("select r from RegularPayment r where r.account.owner.id=:userId")
    List<RegularPayment> findAllByUserId(Integer userId);
    @Query("select r from RegularPayment r where r.nextDueDate<=:now and r.type=:type")
    List<RegularPayment> findAllByNextDueDateAndType(LocalDateTime now, String type);
}
