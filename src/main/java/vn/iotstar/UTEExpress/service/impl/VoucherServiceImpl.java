package vn.iotstar.UTEExpress.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Voucher;
import vn.iotstar.UTEExpress.repository.IVoucherRepository;
import vn.iotstar.UTEExpress.service.IVoucherService;

@Service
public class VoucherServiceImpl implements IVoucherService {
	@Autowired
	IVoucherRepository voucherRepository;
	
	@Override
	public List<Voucher> findValidVoucher( Date currentDate) {
		// TODO Auto-generated method stub
		return voucherRepository.findValidVoucherByDate(currentDate);
	}

	@Override
	public Voucher findById(Integer voucherID) {
		// TODO Auto-generated method stub
		return voucherRepository.findById(voucherID).get();
	}
	

	@Override
	public <S extends Voucher> S save(S entity) {
		// TODO Auto-generated method stub
		return voucherRepository.save(entity);
	}

	@Override
	public List<Voucher> findExpiredVoucher(Date currentDate) {
		// TODO Auto-generated method stub
		return voucherRepository.findExpiredVoucher(currentDate);
	}

	@Override
	public List<Voucher> findInactiveVoucher(Date currentDate) {
		// TODO Auto-generated method stub
		return voucherRepository.findInactiveVoucher(currentDate);
	}

	@Override
	public void deleteVoucher(Integer voucherid) {
		voucherRepository.deleteById(voucherid);
		
	}
}
