package vn.iotstar.UTEExpress.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="shipping")
public class Shipping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer shippingID;
	// do de id nen t ko tao onetomany

	private String orderID;  
	
	private Integer statusOrderID;
	
//	Total
//	province = abs( mã tỉnh gửi - mã tỉnh nhận)
//	if (province==0) province=1
//    let shipFee = transportFee * goodFee * province * weight 
//	codFee = codFee * 1.1
//
//	total = shipFee + codFee - discount (voucher)

	private float total;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateUpdate;
	
	@ManyToOne
	@JoinColumn(name="shipperID")
	private Shipper shipper;
	
	@OneToOne(mappedBy = "shipping", cascade = CascadeType.ALL)
	private Order order;
}
