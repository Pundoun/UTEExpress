package vn.iotstar.UTEExpress.service;

import java.util.List;

import vn.iotstar.UTEExpress.entity.Goods;

public interface IGoodsService {
	List <Goods> findAll();

	Goods findById(Integer goodsID);
}
