package vn.iotstar.UTEExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Transport;
import vn.iotstar.UTEExpress.repository.ITransportRepository;
import vn.iotstar.UTEExpress.service.ITransportService;

@Service
public class TransportServiceImpl implements ITransportService{
	@Autowired
	ITransportRepository transportRepository;

	@Override
	public List<Transport> findAll() {
		return transportRepository.findAll();
	}

	@Override
	public Transport findById(Integer transportID) {
		// TODO Auto-generated method stub
		return transportRepository.findById(transportID).orElse(null);
	}
	
	

}
