package vn.iotstar.UTEExpress.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Order;
import vn.iotstar.UTEExpress.repository.IOrderRepository;
import vn.iotstar.UTEExpress.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService{
	@Autowired
	private IOrderRepository orderRepository;


	@Override
	public List<Order> findAllByCustomerID(Integer customerID) {
		// TODO Auto-generated method stub
		return orderRepository.findAllByCustomerID(customerID);
	}

	@Override
	public Order findByID(String orderID) {
		// TODO Auto-generated method stub
		return orderRepository.findById(orderID).get();
	}

	@Override
	public void deleteByOrderID(String orderID) {
		orderRepository.deleteById(orderID);
		
	}

	@Override
	public List<Order> findOrderByOrderStatusAndSourceCity(Integer statusOrderID, String sourceCity) {
		return orderRepository.findOrderByOrderStatusAndSourceCity(statusOrderID, sourceCity);
	}

	@Override
	public Optional<Order> findById(String id) {
		return orderRepository.findById(id);
	}

	@Override
	public List<Order> findOrderByOrderStatusAndDestCity(Integer statusOrderID, String destCity) {
		return orderRepository.findOrderByOrderStatusAndDestCity(statusOrderID, destCity);
	}

	@Override
	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}

	@Override
	public List<Order> findOrdersBySourceCityAndDestCity(String cityName) {
		return orderRepository.findOrdersBySourceCityAndDestCity(cityName);
	}

	@Override
	public List<Order> findOrdersByShipperID(Integer shipperID) {
		return orderRepository.findOrdersByShipperID(shipperID);
	}

	@Override
	public List<Order> findOrdersByShipperIDAndStatus(Integer shipperID, Integer statusOrderID) {
		return orderRepository.findOrdersByShipperIDAndStatus(shipperID, statusOrderID);
	}

	public Integer countOrdersByCityAndStatus(String city, Integer statusOrderID) {
		return orderRepository.countOrdersByCityAndStatus(city, statusOrderID);
	}

	/*
	 * public Page<Order> findOrdersByShipperIDAndStatus(Integer shipperID, Integer
	 * statusOrderID, int page, int size) { Pageable pageable = PageRequest.of(page,
	 * size); return orderRepository.findOrdersByShipperIDAndStatus(shipperID,
	 * statusOrderID, pageable); }
	 */
	
}
