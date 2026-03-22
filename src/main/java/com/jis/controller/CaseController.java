package com.jis.controller;

import java.util.List;

import com.jis.dto.AccessCaseRequest;
import com.jis.dto.AdjournmentRequest;
import com.jis.dto.CloseCaseRequest;
import com.jis.dto.CreateCaseRequest;
import com.jis.dto.EditCaseRequest;
import com.jis.dto.ScheduleHearingRequest;
import com.jis.entity.Adjournment;
import com.jis.entity.Case;
import com.jis.entity.Hearing;
import com.jis.service.CaseService;
import com.jis.repository.CaseRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;
    private final CaseRepository caseRepository;

    @PostMapping
    public Case createCase(@RequestBody CreateCaseRequest request) {
        return caseService.createCase(request);
    }

    @PutMapping("/{cin}")
    public Case editCase(
            @PathVariable String cin,
            @RequestBody EditCaseRequest request) {
        return caseService.editCase(cin, request);
    }

    @PostMapping("/hearings")
    public Hearing scheduleHearing(@RequestBody ScheduleHearingRequest request) {
        return caseService.scheduleHearing(request);
    }

    @PostMapping("/{cin}/close")
    public Case closeCase(
            @PathVariable String cin,
            @RequestBody CloseCaseRequest request) {
        return caseService.closeCase(cin, request);
    }

    @GetMapping("/pending")
    public List<Case> getPendingCases() {
        return caseService.getPendingCases();
    }

    @PostMapping("/adjournments")
    public Adjournment recordAdjournment(@RequestBody AdjournmentRequest request) {
        return caseService.recordAdjournment(request);
    }

    @PostMapping("/access")
    public Case accessClosedCase(@RequestBody AccessCaseRequest request) {
        return caseService.accessClosedCase(request);
    }

    @GetMapping("/{cin}")
    public Case getCase(@PathVariable String cin) {
        return caseRepository.findById(cin)
                .orElseThrow(() -> new RuntimeException("Case not found"));
    }
}