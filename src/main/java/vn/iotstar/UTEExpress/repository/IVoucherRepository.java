package vn.iotstar.UTEExpress.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Voucher;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Integer> {
	 // Find voucher by transport ID (correct method name)
    Optional<Voucher> findByTransport_TransportID(Integer transportID);
    
    // Query to get valid vouchers by current date
    @Query("SELECT v FROM Voucher v WHERE v.dateStart <= :currentDate AND v.dateEnd >= :currentDate")
    Page<Voucher> getValidVouchersByDate(String id, Date currentDate, Pageable pageable);
    
    // Native query to get suitable vouchers based on order date, transport, and goods
    @NativeQuery("SELECT v.* FROM vouchers v "
            + "JOIN orders o ON o.transportid = v.transportid AND o.goodsid = v.goodsid "
            + "WHERE o.orderdate BETWEEN v.date_start AND v.date_end")
    Page<Voucher> getSuitableVoucher(String IdVoucher, String IdOrder, Pageable pageable);
    
    @NativeQuery("SELECT v.* FROM vouchers v "
            + "WHERE :currentDate BETWEEN v.date_start AND v.date_end AND v.amount>0")
    List<Voucher> findValidVoucherByDate(@Param("currentDate") Date currentDate);
    
    
    @NativeQuery("SELECT v.* FROM vouchers v "
            + "WHERE :currentDate > v.date_end OR v.amount=0")
    List<Voucher> findExpiredVoucher(Date currentDate);
	
    @NativeQuery("SELECT v.* FROM vouchers v "
            + "WHERE :currentDate < v.date_start")
	List<Voucher> findInactiveVoucher(Date currentDate);
    
    
    
}
