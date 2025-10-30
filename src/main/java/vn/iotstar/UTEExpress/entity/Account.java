package vn.iotstar.UTEExpress.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="accounts")
public class Account implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	
	private String password;
	
	@ManyToOne
	@JoinColumn(name="roleID")
	private Role role;
	
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Manager manager;
	
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Customer customer;
	
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Admin admin;
	
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Shipper shipper;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(role.getRoleName()));
	}

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
	
}
