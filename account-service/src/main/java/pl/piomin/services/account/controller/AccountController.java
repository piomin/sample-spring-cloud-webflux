package pl.piomin.services.account.controller;

import java.util.logging.Logger;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountController {

	private static final Logger LOGGER = Logger.getLogger(AccountController.class.getName());
	
	@Autowired
	private AccountRepository repository;

	@GetMapping(value = "/account/customer/{customer}")
	public Flux<Account> findByCustomer(@PathVariable("customer") String customerId) {
		LOGGER.info("findByCustomer: " + customerId);
//		return repository.findByCustomerId(customerId)
//				.map(a -> new Account(a.getId(), a.getCustomerId(), a.getNumber(), a.getAmount()));
//		return Flux.just(new Account("123", "1", 500));
		return repository.findByCustomerId(customerId);
	}

	@GetMapping(value = "/account")
	public Flux<Account> findAll() {
		LOGGER.info("findAll");
//		return repository.findAll().map(a -> new Account(a.getId(), a.getCustomerId(), a.getNumber(), a.getAmount()));
//		return Flux.just(new Account("123", "1", 500));
		return repository.findAll();
	}

	@GetMapping(value = "/account/{id}")
	public Mono<Account> findById(@PathVariable("id") String id) {
		LOGGER.info("findById: " + id);
//		return repository.findById(id)
//				.map(a -> new Account(a.getId(), a.getCustomerId(), a.getNumber(), a.getAmount()));
//		return Mono.just(new Account("123", "1", 500));
		return repository.findById(id);
	}

	@PostMapping(value = "/account")
	public Flux<Account> create(@RequestBody Publisher<Account> accountStream) {
//		return repository
//				.save(Mono.from(accountStream)
//						.map(a -> new pl.piomin.services.account.model.Account(a.getNumber(), a.getCustomerId(),
//								a.getAmount())))
//				.map(a -> new Account(a.getId(), a.getCustomerId(), a.getNumber(), a.getAmount()));
//		return Mono.just(new Account("123", "1", 500));
		return repository.saveAll(accountStream);
	}
	
}
