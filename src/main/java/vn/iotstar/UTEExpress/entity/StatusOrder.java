package vn.iotstar.UTEExpress.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="statusOrder")
public class StatusOrder {
/*	
	Status
	idstatus - nameStatus

	0 - Pending 

	1 - Confirmed

	2 - Shipper take 

	3 - Success

	4 - At Post Office

	5 - In transit to Dest Post

	6 - Shipper is shipping

	7 - Delivered

	8 - Failed*/
	@Id
	private Integer IDStatusOrder;
	private String nameStatus;


}
