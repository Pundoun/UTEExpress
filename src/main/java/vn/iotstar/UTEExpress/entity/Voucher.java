package vn.iotstar.UTEExpress.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="vouchers")
public class Voucher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer voucherID;
	
	private String voucherName;
	private float discount; //0-100
	private Integer amount;
	
	private Date dateStart;
	private Date dateEnd;

	
	private String description;
	

	@ManyToOne
	@JoinColumn(name="goodsID")
	private Goods goods;
	
	@ManyToOne
	@JoinColumn(name="transportID")
	private Transport transport;
	
	@OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
	private List<Order> orders;
	
}
