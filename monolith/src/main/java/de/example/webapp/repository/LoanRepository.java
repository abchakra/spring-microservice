package de.example.webapp.repository;

import de.example.webapp.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {
}
