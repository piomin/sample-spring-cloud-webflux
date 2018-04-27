package pl.piomin.service.customer.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pl.piomin.service.customer.model.Customer;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
	
}
