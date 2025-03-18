package com.example.instant.payment.repository;

import com.example.instant.payment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, Long>{
	@Transactional(readOnly = true)
	Optional<Account> findByAccountId(Long id);

	@Transactional
	@Query("SELECT a FROM Account a WHERE a.accountId = ?1")
	Optional<Account> getAccountForUpdate(Long id);
	
}
