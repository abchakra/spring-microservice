package com.example.loanapplication.model;

import java.util.List;

public class LoanApplicationsDto {

	private CustomerDto customer;
	private List<LoanApplicationDto> loans;

	public LoanApplicationsDto(CustomerDto customerDto, List<LoanApplicationDto> loanDtoList) {
		this.customer = customerDto;
		this.loans = loanDtoList;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}

	public List<LoanApplicationDto> getLoans() {
		return loans;
	}

	public void setLoans(List<LoanApplicationDto> loans) {
		this.loans = loans;
	}

}
