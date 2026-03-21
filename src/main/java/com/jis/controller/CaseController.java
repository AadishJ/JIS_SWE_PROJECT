package com.jis.controller;

import com.jis.dto.CreateCaseRequest;
import com.jis.entity.Case;
import com.jis.service.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @PostMapping
    public Case createCase(@RequestBody CreateCaseRequest request) {
        return caseService.createCase(request);
    }
}