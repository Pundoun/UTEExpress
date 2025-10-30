package vn.iotstar.UTEExpress.service;

import java.util.List;

import vn.iotstar.UTEExpress.entity.City;

public interface ICityService {
	List<City> findAll();
	City findByCityName (String cityName);
	City findById(Integer cityID);
	List<City> findCitiesHasPost();
}
