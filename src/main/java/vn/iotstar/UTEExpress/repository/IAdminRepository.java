package vn.iotstar.UTEExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Admin;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Integer> {
	//Admin findAdminByUsername(String username);
	@Query("SELECT a FROM Admin a WHERE a.account.username = :username")
    Admin findAdminByUsername(@Param("username") String username);
}
