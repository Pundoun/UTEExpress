package vn.iotstar.UTEExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Shipping;
import vn.iotstar.UTEExpress.repository.IShippingRepository;
import vn.iotstar.UTEExpress.service.IShippingService;

@Service
public class ShippingServiceImpl implements IShippingService {
	@Autowired
	private IShippingRepository shippingRepository;

	@Override
	public Shipping findByOrderID(String orderID) {
		return shippingRepository.findByOrderID(orderID);
	}

	@Override
	public <S extends Shipping> S save(S entity) {
		return shippingRepository.save(entity);
	}

	@Override
	public List<Shipping> findAllByOrderID(String orderID) {
		return shippingRepository.findAllByOrderID(orderID);
	}

	@Override
	public Shipping findLatestShippingByOrderID(String orderID) {
		return shippingRepository.findLatestShippingByOrderID(orderID);
	}

	@Override
	public Integer findNewStatusOrderByOrderID(String orderID) {
		return shippingRepository.findNewStatusOrderByOrderID(orderID);
	}

	@Override
	public void deleteByShipping(Shipping shipping) {
		shippingRepository.delete(shipping);
	}
}
