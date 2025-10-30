package vn.iotstar.UTEExpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import vn.iotstar.UTEExpress.entity.Manager;

public interface IManagerService {

	long count();

	Optional<Manager> findById(Integer id);

	List<Manager> findAll();

	<S extends Manager> S save(S entity);
	
	 Manager findManagerByIDPost( Integer postID);
	 
	 void deleteManager(Integer managerID);

	Manager findManagerByUsername(String username);

}
