package com.example.loanapplication.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.loanapplication.model.Customer;
import com.example.loanapplication.model.CustomerDto;
import com.example.loanapplication.model.LoanApplication;
import com.example.loanapplication.model.LoanApplicationDto;
import com.example.loanapplication.model.LoanApplicationsDto;
import com.example.loanapplication.repository.LoanApplicationRepository;

@Service
public class LoanApplicationService {

	private static final Logger LOGGER = LogManager.getLogger(LoanApplicationService.class);
	RestTemplate restTemplate = new RestTemplate();

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

	public LoanApplicationsDto findCustomerById(Integer id) {
		LOGGER.info("Finding loans by CustomerId:{}", id);
//		Optional<Customer> customerOpt = Optional
//				.ofNullable(new Customer(id, "John", "Smith", "johnsmith@gmail.com", "+4917589"));
		Optional<Customer> customerOpt = findCustomer(id);
		if (customerOpt.isPresent()) {
			List<LoanApplication> loanApplications = loanApplicationRepository.findLoanApplicationsByCustomerId(id);
			LoanApplicationsDto responseLoanApplications = convertToDto(loanApplications, customerOpt.get());
			return responseLoanApplications;
		}

		return null;
	}

	public LoanApplication findById(Integer id) {
		LOGGER.info("Finding loan by Id:{}", id);
		return loanApplicationRepository.findLoanApplicationById(id);
	}

	public Optional<Customer> findCustomer(Integer customerId) {
		try {
			return Optional.ofNullable(restTemplate.getForObject(customerServiceURI + customerId, Customer.class));
		} catch (HttpClientErrorException e) {
			return Optional.empty();
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
}