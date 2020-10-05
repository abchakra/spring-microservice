package com.example.loanapplication.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.loanapplication.model.LoanApplication;


public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Integer> {
	List<LoanApplication> findLoanApplicationsByCustomerId(Integer id);
	LoanApplication findLoanApplicationById(Integer id);
}
