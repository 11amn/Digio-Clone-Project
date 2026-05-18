package com.digio.digio_clone.repository;

import com.digio.digio_clone.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
