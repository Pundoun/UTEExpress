package vn.iotstar.UTEExpress.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Manager;
import vn.iotstar.UTEExpress.repository.IManagerRepository;
import vn.iotstar.UTEExpress.service.IManagerService;

@Service
public class ManagerServiceImpl implements IManagerService{
	@Autowired
	private IManagerRepository managerRepository;

	@Override
	public <S extends Manager> S save(S entity) {
		return managerRepository.save(entity);
	}

	@Override
	public List<Manager> findAll() {
		return managerRepository.findAll();
	}

	@Override
	public Optional<Manager> findById(Integer id) {
		return managerRepository.findById(id);
	}

	@Override
	public long count() {
		return managerRepository.count();
	}

	@Override
	public Manager findManagerByUsername(String username) {
		return managerRepository.findManagerByUsername(username);
	}
	
	@Override
	public Manager findManagerByIDPost(Integer postID) {
		// TODO Auto-generated method stub
		return managerRepository.findManagerByIDPost(postID);
	}

	@Override
	public void deleteManager(Integer managerID) {
		managerRepository.deleteById(managerID);;
		
	}
	
	
}
