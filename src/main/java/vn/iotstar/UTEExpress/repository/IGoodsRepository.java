package vn.iotstar.UTEExpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Goods;

@Repository
public interface IGoodsRepository extends JpaRepository<Goods, Integer> {

}
