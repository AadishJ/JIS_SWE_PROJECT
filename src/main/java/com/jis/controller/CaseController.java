package com.jis.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jis.dto.AccessCaseRequest;
import com.jis.dto.AdjournmentRequest;
import com.jis.dto.CaseStatusResponse;
import com.jis.dto.CloseCaseRequest;
import com.jis.dto.CreateCaseRequest;
import com.jis.dto.EditCaseRequest;
import com.jis.dto.HearingByDateResponse;
import com.jis.dto.ResolvedCaseResponse;
import com.jis.dto.ScheduleHearingRequest;
import com.jis.entity.Adjournment;
import com.jis.entity.Case;
import com.jis.entity.Hearing;
import com.jis.repository.CaseRepository;
import com.jis.security.AuthenticatedUser;
import com.jis.service.CaseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;
    private final CaseRepository caseRepository;

    @PostMapping
    public Case createCase(
            @RequestBody CreateCaseRequest request,
            @AuthenticationPrincipal AuthenticatedUser currentUser) {
        return caseService.createCase(request, currentUser.userId());
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

    @GetMapping("/hearings")
    public List<HearingByDateResponse> getHearingsByDate(
            @RequestParam(name = "hearingDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hearingDate,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate requestedDate = hearingDate != null ? hearingDate : date;
        return caseService.getHearingsByDate(requestedDate);
    }

    @GetMapping("/hearings/{hearingDate}")
    public List<HearingByDateResponse> getHearingsByDatePath(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hearingDate) {
        return caseService.getHearingsByDate(hearingDate);
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

    @GetMapping("/resolved")
    public List<ResolvedCaseResponse> getResolvedCases(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return caseService.getResolvedCases(from, to);
    }

    @GetMapping("/judgments")
    public List<ResolvedCaseResponse> getJudgments(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return caseService.getJudgments(from, to);
    }

    @PostMapping("/adjournments")
    public Adjournment recordAdjournment(@RequestBody AdjournmentRequest request) {
        return caseService.recordAdjournment(request);
    }

    @PostMapping("/access")
    public Case accessClosedCase(
            @RequestBody AccessCaseRequest request,
            @AuthenticationPrincipal AuthenticatedUser currentUser) {
        return caseService.accessClosedCase(request.getCin(), currentUser.userId());
    }

    @GetMapping("/{cin}")
    public Case getCase(@PathVariable String cin) {
        return caseRepository.findById(cin)
                .orElseThrow(() -> new RuntimeException("Case not found"));
    }

    @GetMapping("/{cin}/status")
    public CaseStatusResponse getCaseStatus(@PathVariable String cin) {
        return caseService.getCaseStatus(cin);
    }
}