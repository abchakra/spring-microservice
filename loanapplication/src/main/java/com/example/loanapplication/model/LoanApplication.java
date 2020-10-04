package com.example.loanapplication.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer customerId;
	private Integer amount;
	private Integer duration;
	private String status;

	public LoanApplication() {
	}

	public LoanApplication(Integer customerId, Integer amount, Integer duration) {
		this.amount = amount;
		this.duration = duration;
		this.customerId = customerId;
		this.status = "APPLIED";
	}

	public LoanApplication(Integer id, Integer customerId, Integer amount, Integer duration, String status) {
		this.id = id;
		this.amount = amount;
		this.duration = duration;
		this.customerId = customerId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LoanApplication{" + "id=" + id + ", customerId='" + customerId + '\'' + ", amount='" + amount + '\''
				+ ", duration=" + duration + '}';
	}
}
