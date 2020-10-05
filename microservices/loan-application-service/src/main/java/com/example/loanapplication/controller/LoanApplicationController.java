package com.example.loanapplication.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanapplication.model.LoanApplication;
import com.example.loanapplication.model.LoanApplicationsDto;
import com.example.loanapplication.service.LoanApplicationService;

@RestController
public class LoanApplicationController {
	private static final Logger LOGGER = LogManager.getLogger(LoanApplicationController.class);

	@Autowired
	private LoanApplicationService loanApplicationService;

	/**
	 * Get the LoanApplications with specified CustomerID
	 * 
	 * @param id CustomerID of the LoanApplications to get
	 * @return ResponseEntity with the found LoanApplications or NOT_FOUND if no
	 *         LoanApplication found
	 */
	@GetMapping("/loanapplications")
	public ResponseEntity<?> getLoanapplications(@RequestParam Integer customerId) {

		LoanApplicationsDto loanApplications = loanApplicationService.findCustomerById(customerId);
		if (loanApplications != null) {
			try {
				return ResponseEntity.ok().eTag(Integer.toString(customerId))
						.location(new URI("/loanapplications?customerId=" + customerId)).body(loanApplications);
			} catch (URISyntaxException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/loanapplications/{id}")
	public ResponseEntity<?> getLoanapplicationsById(@PathVariable Integer id) {

		LoanApplicationsDto loanApplications = loanApplicationService.findCustomerById(id);
		if (loanApplications != null) {
			try {
				return ResponseEntity.ok().eTag(Integer.toString(id))
						.location(new URI("/loanapplications?customerId=" + id)).body(loanApplications);
			} catch (URISyntaxException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Saves a new LoanApplication
	 * 
	 * @param loanApplication LoanApplication to save
	 * @return ResponseEntity with the saved LoanApplication
	 */
	@PostMapping("/loanapplications")
	public ResponseEntity<?> saveLoanApplication(@RequestBody LoanApplication loanApplication) {
		LOGGER.info("Adding new loanApplication with customerId:{}", loanApplication.getCustomerId());
		LoanApplication newLoanApplication = loanApplicationService.save(loanApplication);
		try {
			return ResponseEntity.created(new URI("/loanapplications/" + newLoanApplication.getId()))
					.eTag(Integer.toString(newLoanApplication.getCustomerId())).body(newLoanApplication);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
