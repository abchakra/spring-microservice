package de.example.webapp.controller;

import de.example.webapp.model.RegistrationRequestDTO;
import de.example.webapp.model.RegistrationResponseVO;
import de.example.webapp.service.RegistrationOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationOrchestratorService registrationOrchestratorService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseVO> register(
            @RequestBody @Valid RegistrationRequestDTO registrationRequest) {
        return ResponseEntity.ok().body(registrationOrchestratorService.register(registrationRequest));
    }
}
