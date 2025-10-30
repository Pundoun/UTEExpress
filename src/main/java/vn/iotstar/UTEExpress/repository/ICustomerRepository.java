package vn.iotstar.UTEExpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Customer;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Integer>{
	@Query("SELECT c FROM Customer c WHERE c.isActive = 0 AND c.city IN " +
	           "(SELECT p.city.cityName FROM Post p WHERE p.city.cityName = :cityName)")
	List<Customer> findInactiveCustomersByCity(@Param("cityName") String cityName);
	
	@Query("SELECT c FROM Customer c WHERE c.account.username = :username")
    Customer findCustomerByUserName(@Param("username") String username);
	

	@Query("SELECT c FROM Customer c WHERE c.city IN " +
	           "(SELECT p.city.cityName FROM Post p WHERE p.city.cityName = :cityName)")
	List<Customer> findCustomersByCity(@Param("cityName") String cityName);

}
