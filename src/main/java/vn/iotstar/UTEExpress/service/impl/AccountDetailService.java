package vn.iotstar.UTEExpress.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.repository.IAccountRepository;

@Service
public class AccountDetailService implements UserDetailsService{
	
	@Autowired
	private IAccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<Account> account = accountRepository.findById(username);
	    if (account.isPresent()) {
	        System.out.println("User found: " + account.get().getUsername());
	        return account.get();
	    }
	    System.out.println("User not found: " + username);
	    throw new UsernameNotFoundException(username);
	}


}
