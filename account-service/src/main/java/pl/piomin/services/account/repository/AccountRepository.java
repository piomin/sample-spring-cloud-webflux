package pl.piomin.services.account.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pl.piomin.services.account.model.Account;
import reactor.core.publisher.Flux;

public interface AccountRepository extends ReactiveCrudRepository<Account, String> {

	Flux<Account> findByCustomerId(String customerId);
	
}
