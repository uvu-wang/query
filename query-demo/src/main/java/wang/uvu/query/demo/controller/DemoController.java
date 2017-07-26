package wang.uvu.query.demo.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import wang.uvu.query.demo.AccountQuery;
import wang.uvu.query.demo.domain.Account;
import wang.uvu.query.demo.repository.AccountRepository;

@RestController
public class DemoController {

	@Autowired
	private AccountRepository accountRepository;

	@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
	public Page<Account> query(AccountQuery query) {
		return accountRepository.findAll(query);
	}
	
	@RequestMapping(value = "/api/accounts", method = RequestMethod.POST)
	public Account create(@RequestBody Account account){
		return accountRepository.save(account);
	}
	
	@PostConstruct
	public void init(){
		int size = 0;
		while (size++ < 1000) {
			Account account = new Account();
			account.setDisable(size%2 == 0);
			account.setFreezeAmount((float)(new Random().nextInt(1000)));
			account.setBalance((float)(new Random().nextInt(1000)));
			account.setAmount(BigDecimal.valueOf(account.getBalance() + account.getFreezeAmount()));
			account.setLevel(1000 - size);
			account.setCreateAt(new Date());
			account.setVersion(size);
			account.setAccountId(UUID.randomUUID().toString());
			accountRepository.save(account);
		}
	}
	
}
