package vn.iotstar.UTEExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Goods;
import vn.iotstar.UTEExpress.repository.IGoodsRepository;
import vn.iotstar.UTEExpress.service.IGoodsService;

@Service
public class GoodsServiceImpl implements IGoodsService {
	
	@Autowired
	IGoodsRepository goodsRepository;
	
	@Override
	public List<Goods> findAll() {
		
		return goodsRepository.findAll();
	}

	@Override
	public Goods findById(Integer goodsID) {
		// TODO Auto-generated method stub
		return goodsRepository.findById(goodsID).orElse(null);
	}

}
