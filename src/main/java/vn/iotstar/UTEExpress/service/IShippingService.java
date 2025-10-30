package vn.iotstar.UTEExpress.service;

import java.util.List;

import vn.iotstar.UTEExpress.entity.Shipping;

public interface IShippingService  {

	<S extends Shipping> S save(S entity);

	Shipping findByOrderID(String orderID);

	List<Shipping> findAllByOrderID(String orderID);
	
	Shipping findLatestShippingByOrderID(String orderID);
	
	Integer findNewStatusOrderByOrderID (String orderID);
	
	void deleteByShipping(Shipping shipping);
}
