package com.example.postgretest.repository;

import java.util.List;

import com.example.postgretest.model.Customer;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
	List<Customer> findByFirstName(String FirstName);
	List<Customer> findAll();
}