package vn.iotstar.UTEExpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, String>{
	
	@Query("SELECT o FROM Order o WHERE o.customer.customerID = :customerID")
    List<Order> findOrderByCustomerID(@Param("customerID") Integer customerID);
	
	//tìm order dửa theo phương thức vận chuyển
	@Query("SELECT o FROM Order o WHERE o.transport.transportType = :transportType")
    List<Order> findOrderByTransportType(@Param("transportType") String transportType);
	
	//tìm order pending dưa tren orderstatus và src city
	@Query("SELECT o FROM Order o " +
		       "JOIN Shipping s ON o.shipping.shippingID = s.shippingID " +
		       "JOIN StatusOrder so ON s.statusOrderID = so.IDStatusOrder " +
		       "WHERE so.IDStatusOrder = :statusOrderID " +
		       "AND o.sourceCity = :sourceCity " +
		       "AND s.shipper IS NULL")
	List<Order> findOrderByOrderStatusAndSourceCity(Integer statusOrderID, String sourceCity);

    
  //tìm order pending dưa tren orderstatus và dest city
	@Query("SELECT o FROM Order o " +
		       "JOIN Shipping s ON o.shipping.shippingID = s.shippingID " +
		       "JOIN StatusOrder so ON s.statusOrderID = so.IDStatusOrder " +
		       "WHERE so.IDStatusOrder = :statusOrderID " +
		       "AND o.destCity = :destCity " +
		       "AND s.shipper IS NULL")
	List<Order> findOrderByOrderStatusAndDestCity(Integer statusOrderID, String destCity);

	@Query("SELECT o FROM Order o " +
		       "WHERE o.sourceCity = :cityName " +
		       "OR o.destCity = :cityName")
	List<Order> findOrdersBySourceCityAndDestCity(String cityName);
	
	//--SHIPPER
	// tìm order dựa tren shipper id
	@Query("SELECT o FROM Order o " +
		       "JOIN o.shipping s " +
		       "WHERE s.shipper.shipperID = :shipperID")
	List<Order> findOrdersByShipperID(@Param("shipperID") Integer shipperID);
	
	// tìm order dựa tren shipper id va status order
	@Query("SELECT o FROM Order o " +
		       "JOIN o.shipping s " +
		       "WHERE s.shipper.shipperID = :shipperID " +
		       "AND s.statusOrderID = :statusOrderID")
	List<Order> findOrdersByShipperIDAndStatus(
	        @Param("shipperID") Integer shipperID, 
	        @Param("statusOrderID") Integer statusOrderID);
		//phân trang
		/*
		 * @Query("SELECT o FROM Order o " + "JOIN o.shipping s " +
		 * "WHERE s.shipper.shipperID = :shipperID " +
		 * "AND s.statusOrderID = :statusOrderID") Page<Order>
		 * findOrdersByShipperIDAndStatus(
		 * 
		 * @Param("shipperID") Integer shipperID,
		 * 
		 * @Param("statusOrderID") Integer statusOrderID, Pageable pageable);
		 */
	
	@Query("select o from Order o where o.customer.customerID = :customerID")
	List <Order> findAllByCustomerID(@Param("customerID") Integer customerID);
	
	@Query("SELECT COUNT(o) " +
	           "FROM Order o " +
	           "JOIN o.customer c " +
	           "JOIN o.shipping s " +
	           "WHERE c.city = :city AND s.statusOrderID = :statusOrderID")
    Integer countOrdersByCityAndStatus(@Param("city") String city, 
                                    @Param("statusOrderID") Integer statusOrderID);
}
