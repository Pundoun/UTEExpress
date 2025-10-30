package vn.iotstar.UTEExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer>{
	@Query("SELECT r FROM Role r WHERE LOWER(r.roleName) = LOWER(:roleName)")
    Role findRoleByRoleNameIgnoreCase(String roleName);
	
	Role findByRoleName(String roleName);
	
	
}
