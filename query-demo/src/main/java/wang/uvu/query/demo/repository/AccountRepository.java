package wang.uvu.query.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import wang.uvu.query.demo.domain.Account;
import wang.uvu.query.repository.QueryRepository;


public interface AccountRepository extends JpaRepository<Account, String>, QueryRepository<Account> {
	
}
