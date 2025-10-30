package vn.iotstar.UTEExpress.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="transports")
public class Transport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transportID;
	private String transportType;
	private float transportFee;
	
	@OneToMany(mappedBy = "transport", cascade = CascadeType.ALL)
	private List<Voucher> vouchers;
	
	@OneToMany(mappedBy = "transport",cascade = CascadeType.ALL)
	private List<Order> orders;

}
