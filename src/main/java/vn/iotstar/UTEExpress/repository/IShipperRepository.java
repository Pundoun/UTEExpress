package vn.iotstar.UTEExpress.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.dto.PostShipperCountDTO;
import vn.iotstar.UTEExpress.entity.Shipper;

@Repository
public interface IShipperRepository extends JpaRepository<Shipper, Integer>{
	
	@Query("SELECT s FROM Shipper s WHERE s.post.postID = :postID")
	List<Shipper> findShippersByIDPost(@Param("postID") Integer postID);
	
	// tìm shipper theo role id
	@Query("SELECT s FROM Shipper s " +
	           "JOIN s.account a " +
	           "JOIN a.role r " +
	           "WHERE r.roleID = :roleID AND s.city = :cityName")
	    List<Shipper> findShippersByRoleId(Integer roleID, String cityName);
	
	// tìm shipper theo userna,e
	@Query("SELECT s FROM Shipper s WHERE s.account.username = :username")
	Shipper findShipperByUsername(@Param("username") String username);
	
	//Tìm số lượng Shipper theo post
	@Query("SELECT p.postName AS postName, COUNT(s.shipperID) AS shipperCount " +
		       "FROM Post p LEFT JOIN Shipper s ON p.postID = s.post.postID " +
		       "GROUP BY p.postID, p.postName")

	List<PostShipperCountDTO> getShipperCountByPost();
}
