package vn.iotstar.UTEExpress.service;

import java.util.List;

import vn.iotstar.UTEExpress.entity.Transport;

public interface ITransportService {
	
	List <Transport> findAll();

	Transport findById(Integer transportID);

}
