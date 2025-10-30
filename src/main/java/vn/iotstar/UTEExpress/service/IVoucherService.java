package vn.iotstar.UTEExpress.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import vn.iotstar.UTEExpress.entity.Voucher;

public interface IVoucherService {

	List<Voucher> findValidVoucher(Date currentDate);
	
	List<Voucher> findExpiredVoucher(Date currentDate);
	
	List<Voucher> findInactiveVoucher(Date currentDate);

	Voucher findById(Integer voucherID);

	<S extends Voucher> S save(S entity);
	
	void deleteVoucher (Integer voucherid);
}
