package de.example.webapp.controller;

import de.example.webapp.model.UserDTO;
import de.example.webapp.model.UserSearchRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class UserSearchController {
    @PostMapping("/api/users/search")
    public UserDTO findUserForAdvisorAssignment(@RequestBody @Valid UserSearchRequestDTO userSearchRequest) {
        log.info("findUserForAdvisorAssignment: Entering userSearchRequest: {}", userSearchRequest);
        return null;
    }
}
