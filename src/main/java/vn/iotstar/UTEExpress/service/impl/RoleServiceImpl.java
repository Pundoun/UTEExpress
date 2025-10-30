package vn.iotstar.UTEExpress.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Role;
import vn.iotstar.UTEExpress.repository.IRoleRepository;
import vn.iotstar.UTEExpress.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService{
	@Autowired
	private IRoleRepository roleRepository;

	@Override
	public Role findRoleByRoleNameIgnoreCase(String roleName) {
		return roleRepository.findRoleByRoleNameIgnoreCase(roleName);
	}

	@Override
	public Role findByRoleName(String roleName) {
		// TODO Auto-generated method stub
		return roleRepository.findByRoleName(roleName);
	}	
}
