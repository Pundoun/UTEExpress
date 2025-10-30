package vn.iotstar.UTEExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Account;

@Repository
public interface IAccountRepository extends JpaRepository<Account, String>{

	Account findByUsername(String email);

}
