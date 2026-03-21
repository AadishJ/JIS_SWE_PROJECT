package com.jis.repository;

import com.jis.entity.Hearing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HearingRepository extends JpaRepository<Hearing, Integer> {
}