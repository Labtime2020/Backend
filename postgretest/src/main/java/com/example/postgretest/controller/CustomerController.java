package com.example.postgretest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.postgretest.model.Customer;
import com.example.postgretest.model.CustomerUI;
import com.example.postgretest.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class CustomerController {
	@Autowired
	CustomerRepository repository;
	
	@GetMapping("/bulkcreate")
	public String bulkcreate(){
		// save a single Customer
		repository.save(new Customer("Rajesh", "Bhojwani"));
		
		// save a list of Customers
        repository.saveAll(Arrays.asList(new Customer("Salim", "Khan")
                       , new Customer("Rajesh", "Parihar")
                       , new Customer("Rahul", "Dravid")
                       , new Customer("Dharmendra", "Bhojwani")));
		
		return "Customers are created";
	}

	@GetMapping("/OlaMundo")
	public String OlaMundo(){
		return "OlaMundo";
	}

	@PostMapping("/create")
	public String create(@RequestBody CustomerUI customer){
		// save a single Customer
		System.out.println("tentando criar!");
		repository.save(new Customer(customer.getFirstName(), customer.getLastName()));

		System.out.println("criado!");

		return "Customer is created";
	}
	
	@GetMapping("/findall")
	public List<CustomerUI> findAll(){

		List<Customer> customers = repository.findAll();
		List<CustomerUI> customerUI = new ArrayList<>();
		
		for (Customer customer : customers) {
			customerUI.add(new CustomerUI(customer.getFirstName(),customer.getLastName()));
		}

		return customerUI;
	}
	
	@RequestMapping("/search/{id}")
	public Customer search(@PathVariable long id){

		return repository.findById(id).get();
	}
	
	@RequestMapping("/searchbyfirstname/{firstname}")
	public List<CustomerUI> fetchDataByLastName(@PathVariable String firstname){
	
		List<Customer> customers = repository.findByFirstName(firstname);
		List<CustomerUI> customerUI = new ArrayList<>();
		
		for (Customer customer : customers) {
			customerUI.add(new CustomerUI(customer.getFirstName(),customer.getLastName()));
		}

		return customerUI;
	}
}