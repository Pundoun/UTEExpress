package vn.iotstar.UTEExpress.service;

import java.util.List;

import vn.iotstar.UTEExpress.entity.StatusOrder;

public interface IStatusOrderService {
	public List <StatusOrder> findAll();
	public StatusOrder findByStatusID(Integer id);
}
