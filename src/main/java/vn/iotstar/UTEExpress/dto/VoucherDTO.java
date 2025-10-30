package vn.iotstar.UTEExpress.dto;

import java.util.Date;
import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.UTEExpress.entity.Goods;
import vn.iotstar.UTEExpress.entity.Order;
import vn.iotstar.UTEExpress.entity.Transport;
import vn.iotstar.UTEExpress.entity.Voucher;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherDTO {
private Integer voucherID;
	
	private String voucherName;
	private float discount; //0-100
	private Integer amount;
	
	private Date dateStart;
	private Date dateEnd;

	private String description;
	
	private Goods goods;
	
	private Transport transport;
}
