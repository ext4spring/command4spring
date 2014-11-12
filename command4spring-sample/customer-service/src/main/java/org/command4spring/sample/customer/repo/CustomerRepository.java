package org.command4spring.sample.customer.repo;

import org.command4spring.sample.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
