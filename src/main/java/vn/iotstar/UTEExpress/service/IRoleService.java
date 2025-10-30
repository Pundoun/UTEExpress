package vn.iotstar.UTEExpress.service;

import vn.iotstar.UTEExpress.entity.Role;

public interface IRoleService {

	Role findRoleByRoleNameIgnoreCase(String roleName);
	Role findByRoleName(String roleName);

}
