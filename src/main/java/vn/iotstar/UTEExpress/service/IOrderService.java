package vn.iotstar.UTEExpress.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.UTEExpress.entity.Order;

public interface IOrderService {

	<S extends Order> S save(S entity);
	List <Order> findAllByCustomerID(Integer customerID);
	void deleteByOrderID(String orderID);
	Order findByID(String orderID);
	List<Order> findOrdersByShipperIDAndStatus(Integer shipperID, Integer statusOrderID);
	List<Order> findOrdersByShipperID(Integer shipperID);
	List<Order> findOrdersBySourceCityAndDestCity(String cityName);

	List<Order> findOrderByOrderStatusAndDestCity(Integer statusOrderID, String destCity);
	Optional<Order> findById(String id);
	List<Order> findOrderByOrderStatusAndSourceCity(Integer statusOrderID, String sourceCity);

}
