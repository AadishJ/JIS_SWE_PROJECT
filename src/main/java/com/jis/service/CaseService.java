package com.jis.service;

import java.util.List;

import com.jis.dto.AccessCaseRequest;
import com.jis.dto.AdjournmentRequest;
import com.jis.dto.CloseCaseRequest;
import com.jis.dto.CreateCaseRequest;
import com.jis.dto.EditCaseRequest;
import com.jis.dto.ScheduleHearingRequest;
import com.jis.entity.Adjournment;
import com.jis.entity.Case;
import com.jis.entity.CaseAccess;
import com.jis.entity.Hearing;
import com.jis.entity.Payment;
import com.jis.entity.User;
import com.jis.repository.CaseRepository;
import com.jis.repository.UserRepository;
import com.jis.repository.HearingRepository;
import com.jis.repository.AdjournmentRepository;
import com.jis.repository.PaymentRepository;
import com.jis.repository.CaseAccessRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final CinGenerator cinGenerator;
    private final HearingRepository hearingRepository;
    private final AdjournmentRepository adjournmentRepository;
    private final PaymentRepository paymentRepository;
    private final CaseAccessRepository caseAccessRepository;

    public Case createCase(CreateCaseRequest request) {

        // Get registrar
        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate role
        if (user.getRole() != User.Role.REGISTRAR) {
            throw new RuntimeException("Only registrar can create cases");
        }

        // Generate CIN
        String cin = cinGenerator.generateCin();

        // Create case
        Case caseEntity = new Case();
        caseEntity.setCin(cin);
        caseEntity.setDefendantDetails(request.getDefendantDetails());
        caseEntity.setCrimeType(request.getCrimeType());
        caseEntity.setArrestInfo(request.getArrestInfo());
        caseEntity.setProsecutorDetails(request.getProsecutorDetails());
        caseEntity.setCreatedBy(user);

        // status + createdAt auto handled

        return caseRepository.save(caseEntity);
    }

    public Case editCase(String cin, EditCaseRequest request) {

        Case caseEntity = caseRepository.findById(cin)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        // Cannot edit closed case
        if (caseEntity.getStatus() == Case.Status.CLOSED) {
            throw new RuntimeException("Cannot edit closed case");
        }

        // Update fields
        caseEntity.setDefendantDetails(request.getDefendantDetails());
        caseEntity.setCrimeType(request.getCrimeType());
        caseEntity.setArrestInfo(request.getArrestInfo());
        caseEntity.setProsecutorDetails(request.getProsecutorDetails());

        return caseRepository.save(caseEntity);
    }

    public Hearing scheduleHearing(ScheduleHearingRequest request) {

        Case caseEntity = caseRepository.findById(request.getCin())
                .orElseThrow(() -> new RuntimeException("Case not found"));

        // Only pending cases
        if (caseEntity.getStatus() != Case.Status.PENDING) {
            throw new RuntimeException("Cannot schedule hearing for closed case");
        }

        Hearing hearing = new Hearing();
        hearing.setCaseEntity(caseEntity);
        hearing.setHearingDate(request.getHearingDate());
        hearing.setCourtSlot(request.getCourtSlot());

        return hearingRepository.save(hearing);
    }

    public Case closeCase(String cin, CloseCaseRequest request) {

        Case caseEntity = caseRepository.findById(cin)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        if (caseEntity.getStatus() == Case.Status.CLOSED) {
            throw new RuntimeException("Case already closed");
        }

        if (request.getJudgmentSummary() == null || request.getJudgmentSummary().isEmpty()) {
            throw new RuntimeException("Judgment summary required");
        }

        caseEntity.setJudgmentSummary(request.getJudgmentSummary());
        caseEntity.setStatus(Case.Status.CLOSED);

        return caseRepository.save(caseEntity);
    }

    public List<Case> getPendingCases() {
        return caseRepository.findAll()
                .stream()
                .filter(c -> c.getStatus() == Case.Status.PENDING)
                .toList();
    }

    public Adjournment recordAdjournment(AdjournmentRequest request) {

        Hearing hearing = hearingRepository.findById(request.getHearingId())
                .orElseThrow(() -> new RuntimeException("Hearing not found"));

        // Create adjournment
        Adjournment adj = new Adjournment();
        adj.setHearing(hearing);
        adj.setReason(request.getReason());
        adj.setNewHearingDate(request.getNewHearingDate());

        // Update hearing date
        hearing.setHearingDate(request.getNewHearingDate());
        hearingRepository.save(hearing);

        return adjournmentRepository.save(adj);
    }

    public Case accessClosedCase(AccessCaseRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Case caseEntity = caseRepository.findById(request.getCin())
                .orElseThrow(() -> new RuntimeException("Case not found"));

        // Only closed cases
        if (caseEntity.getStatus() != Case.Status.CLOSED) {
            throw new RuntimeException("Case is not closed");
        }

        //  Judge → free access
        if (user.getRole() == User.Role.JUDGE) {
            return caseEntity;
        }

        //  Lawyer → must pay
        if (user.getRole() == User.Role.LAWYER) {

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(new java.math.BigDecimal("100.00"));
            payment.setStatus(Payment.Status.SUCCESS); // mock success

            payment = paymentRepository.save(payment);

            // Save access record
            CaseAccess access = new CaseAccess();
            access.setUser(user);
            access.setCaseEntity(caseEntity);
            access.setPayment(payment);

            caseAccessRepository.save(access);

            return caseEntity;
        }

        throw new RuntimeException("Unauthorized access");
    }
}