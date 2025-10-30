package vn.iotstar.UTEExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.StatusOrder;
import vn.iotstar.UTEExpress.repository.IStatusOrderRepository;
import vn.iotstar.UTEExpress.service.IStatusOrderService;

@Service
public class StatusOrderServiceImpl implements IStatusOrderService {
	@Autowired IStatusOrderRepository statusOrderRepository;

	@Override
	public List<StatusOrder> findAll() {
		// TODO Auto-generated method stub
		return statusOrderRepository.findAll();
	}

	@Override
	public StatusOrder findByStatusID(Integer id) {
		// TODO Auto-generated method stub
		return statusOrderRepository.findById(id).orElse(null);
	}
}
