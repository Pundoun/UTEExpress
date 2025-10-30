package vn.iotstar.UTEExpress.service;

import java.util.List;
import java.util.Optional;

import vn.iotstar.UTEExpress.dto.PostShipperCountDTO;
import vn.iotstar.UTEExpress.entity.Shipper;

public interface IShipperService {

	Optional<Shipper> findByShipperID(Integer shipperID);

	Shipper findShipperByUsername(String username);

	List<Shipper> findShippersByRoleId(Integer roleID);

	void delete(Shipper entity);

	Optional<Shipper> findById(Integer id);

	<S extends Shipper> S save(S entity);

	List<Shipper> findShippersByIDPost(Integer postID);

	List<Shipper> findShippersByRoleId(Integer roleID, String cityname);
	
}
