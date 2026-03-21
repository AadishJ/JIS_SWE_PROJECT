package com.jis.repository;

import com.jis.entity.Adjournment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjournmentRepository extends JpaRepository<Adjournment, Integer> {
}