package vn.iotstar.UTEExpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.City;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer>  {
	City findByCityName (String cityName);
	
	@Query("SELECT c FROM City c JOIN c.posts p")
	List<City> findCitiesHasPost();
}
