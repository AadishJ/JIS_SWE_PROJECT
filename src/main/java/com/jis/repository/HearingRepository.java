package com.jis.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jis.entity.Hearing;

public interface HearingRepository extends JpaRepository<Hearing, Integer> {

    List<Hearing> findByHearingDateOrderByCourtSlotAsc(LocalDate hearingDate);

    Optional<Hearing> findTopByCaseEntityCinOrderByHearingDateDesc(String cin);
}