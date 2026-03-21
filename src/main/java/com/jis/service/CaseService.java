package com.jis.service;

import com.jis.dto.CreateCaseRequest;
import com.jis.entity.Case;
import com.jis.entity.User;
import com.jis.repository.CaseRepository;
import com.jis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final CinGenerator cinGenerator;

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
}