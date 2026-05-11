package com.digio.digio_clone.Repository;

import com.digio.digio_clone.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
