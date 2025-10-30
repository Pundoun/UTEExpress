package vn.iotstar.UTEExpress.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.UTEExpress.entity.Shipper;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
	private String orderID;
	private String nameReceiver;
    private String phoneReceiver;
    private String dest;
    private String destCity;
    private String source;
    private String sourceCity;
    private double weight;
    private double height;
    private double width;
    private double codFee;
    private double shipFee;
    private String goodsType;
    private String transportType;
    private String voucherName;
    private double COD_surcharge;
    private Date dateUpdate;
    private Shipper shipper;
    private double total;
    private Integer statusOrderID;
}
