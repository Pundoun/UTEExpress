package vn.iotstar.UTEExpress.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Admin;
import vn.iotstar.UTEExpress.repository.IAdminRepository;
import vn.iotstar.UTEExpress.service.IAdminService;
@Service
public class AdminServiceImpl implements IAdminService {
	
	@Autowired 
	IAdminRepository adminRepository;
	@Override
	public Admin findByID(Integer adminID) {
		// TODO Auto-generated method stub
		return adminRepository.findById(adminID).get();
	}
	@Override
	public Admin findAdminByUsername(String username) {
		return adminRepository.findAdminByUsername(username);
	}

	
}
