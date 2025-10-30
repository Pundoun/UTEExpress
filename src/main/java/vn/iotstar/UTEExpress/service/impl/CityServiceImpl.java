package vn.iotstar.UTEExpress.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.repository.ICustomerRepository;
import java.util.List;


import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.repository.ICityRepository;
import vn.iotstar.UTEExpress.service.ICityService;

@Service
public class CityServiceImpl implements ICityService{
	@Autowired
	ICityRepository cityRepository;
	
	@Override
	public List<City> findAll() {
		return cityRepository.findAll();
	}

	@Override
	public City findByCityName(String cityName) {
		// TODO Auto-generated method stub
		return cityRepository.findByCityName(cityName);
	}

	@Override
	public City findById(Integer cityID) {
		// TODO Auto-generated method stub
		return cityRepository.findById(cityID).get();
	}

	@Override
	public List<City> findCitiesHasPost() {
		// TODO Auto-generated method stub
		return cityRepository.findCitiesHasPost();
	}
}
