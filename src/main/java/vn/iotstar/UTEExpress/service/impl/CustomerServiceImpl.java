package vn.iotstar.UTEExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Customer;
import vn.iotstar.UTEExpress.repository.ICustomerRepository;
import vn.iotstar.UTEExpress.service.ICustomerService;

@Service
public class CustomerServiceImpl implements ICustomerService{
	@Autowired
	private ICustomerRepository customerRepository;
	
	// coche
	@Override
	public List<Customer> findInactiveCustomersByCity(String cityName) {
		return customerRepository.findInactiveCustomersByCity(cityName);
	}

	@Override
	public Customer findCustomerByUserName(String username) {
		return customerRepository.findCustomerByUserName(username);
	}

	@Override
	public <S extends Customer> S save(S entity) {
		return customerRepository.save(entity);
	}
	// end coche

	//Van
	@Override
	public Customer findCustomerByIDUser(Integer IDUser) {
		// TODO Auto-generated method stub
		return customerRepository.findById(IDUser).get();
	}

	@Override
	public Customer findById(Integer id) {
		// TODO Auto-generated method stub
		return customerRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Customer entity) {
		customerRepository.delete(entity);
	}

	@Override
	public List<Customer> findCustomersByCity(String cityName) {
		return customerRepository.findCustomersByCity(cityName);
	}
	
	//
	
	
}
