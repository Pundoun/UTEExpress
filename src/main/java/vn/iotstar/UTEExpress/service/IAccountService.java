package vn.iotstar.UTEExpress.service;

import java.util.Optional;
import vn.iotstar.UTEExpress.entity.Account;

public interface IAccountService {
	void save(Account account);

	boolean isPasswordValid(String rawPassword, String encodedPassword);

	Optional<Account> findById(String id);

	void delete(Account entity);

	Account findByUsername(String email);

}
