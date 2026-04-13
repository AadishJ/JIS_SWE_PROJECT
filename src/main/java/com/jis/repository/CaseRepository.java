package com.jis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jis.entity.Case;

public interface CaseRepository extends JpaRepository<Case, String> {

    List<Case> findByStatusOrderByCinAsc(Case.Status status);
}