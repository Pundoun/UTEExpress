package vn.iotstar.UTEExpress.service;

import java.util.List;
import vn.iotstar.UTEExpress.entity.Customer;

public interface ICustomerService {

	<S extends Customer> S save(S entity);

	Customer findCustomerByUserName(String username);

	List<Customer> findInactiveCustomersByCity(String cityName);
	
	Customer findCustomerByIDUser(Integer IDUser);

	Customer findById(Integer id);

	List<Customer> findCustomersByCity(String cityName);

	void delete(Customer entity);


}
