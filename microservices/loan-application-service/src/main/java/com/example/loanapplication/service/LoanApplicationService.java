package com.example.loanapplication.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.exceptions.InvalidInputException;
import com.example.exceptions.NotFoundException;
import com.example.http.HttpErrorInfo;
import com.example.loanapplication.model.Customer;
import com.example.loanapplication.model.CustomerDto;
import com.example.loanapplication.model.LoanApplication;
import com.example.loanapplication.model.LoanApplicationDto;
import com.example.loanapplication.model.LoanApplicationsDto;
import com.example.loanapplication.repository.LoanApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LoanApplicationService {

	private static final Logger LOGGER = LogManager.getLogger(LoanApplicationService.class);

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	ObjectMapper mapper;

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${customerprovider.uri}")
	private String customerServiceURI;

	public LoanApplication save(LoanApplication loanApplication) {
		LOGGER.info("Saving new loanApplication with customerId:{}", loanApplication.getCustomerId());
		loanApplication.setStatus("APPLIED");
		return loanApplicationRepository.save(loanApplication);
	}

	public LoanApplicationsDto findCustomerById(Integer customerId) {
		LOGGER.info("Finding loans by CustomerId:{}", customerId);

		if (customerId < 1)
			throw new InvalidInputException("Invalid productId: " + customerId);
//		if (customerId == 13)
//			throw new NotFoundException("No product found for productId: " + customerId);
		Optional<Customer> customerOpt = findCustomer(customerId);
		if (customerOpt.isPresent()) {
			List<LoanApplication> loanApplications = loanApplicationRepository
					.findLoanApplicationsByCustomerId(customerId);
			LoanApplicationsDto responseLoanApplications = convertToDto(loanApplications, customerOpt.get());
			return responseLoanApplications;
		}
		throw new NotFoundException("No Customer found for customerId: " + customerId);
	}

	public LoanApplication findById(Integer id) {
		LOGGER.info("Finding loan by Id:{}", id);
		return loanApplicationRepository.findLoanApplicationById(id);
	}

	public Optional<Customer> findCustomer(Integer customerId) {
		try {
			LOGGER.info("Finding Customer by Id:{}", customerId);
			restTemplate.exchange(customerServiceURI + customerId, HttpMethod.GET, null, Customer.class);

			return Optional.ofNullable(restTemplate.getForObject(customerServiceURI + customerId, Customer.class));
		} catch (HttpClientErrorException e) {

			switch (e.getStatusCode()) {

			case NOT_FOUND:
				throw new NotFoundException(getErrorMessage(e));

			case UNPROCESSABLE_ENTITY:
				throw new InvalidInputException(getErrorMessage(e));

			default:
				LOGGER.warn("Got a unexpected HTTP error: {}, will rethrow it", e.getStatusCode());
				LOGGER.warn("Error body: {}", e.getResponseBodyAsString());
				throw e;
			}

		}
	}

	public LoanApplicationsDto convertToDto(List<LoanApplication> loans, Customer customer) {
		CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
		List<LoanApplicationDto> loanDtoList = loans.stream().map(this::convertToDto).collect(Collectors.toList());

		return new LoanApplicationsDto(customerDto, loanDtoList);

	}

	public LoanApplicationDto convertToDto(LoanApplication loan) {
		LoanApplicationDto loanApplicationDto = modelMapper.map(loan, LoanApplicationDto.class);
		return loanApplicationDto;
	}

	private String getErrorMessage(HttpClientErrorException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}

}
