package vn.iotstar.UTEExpress.service;

import vn.iotstar.UTEExpress.entity.Admin;

public interface IAdminService {
	public Admin findByID(Integer adminID);

	Admin findAdminByUsername(String username);
	
}
