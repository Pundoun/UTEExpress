package vn.iotstar.UTEExpress.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="roles")
public class Role {
	/*
	 * Role idrole - nameRole
	 * 
	 * 1 - manager
	 * 
	 * 2 - customer
	 * 
	 * 3 - shipper giao hàng trực tiếp (áp dụng trong cùng 1 thành phố, giao hoả
	 * tốc)
	 * 
	 * 4 - shipper lấy hàng từ khách gửi đến bưu cục
	 * 
	 * 5 - shipper lấy hàng từ bưu cục đến cho khách nhận
	 * 
	 * 6 - shipper chở hàng từ bưu cục này đến bưu cục khác
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleID;
	
	private String roleName;
	
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
	private List<Account> accounts;

}
