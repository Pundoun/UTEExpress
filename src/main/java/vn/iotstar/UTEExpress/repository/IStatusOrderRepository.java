package vn.iotstar.UTEExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.UTEExpress.entity.StatusOrder;

@Repository
public interface IStatusOrderRepository extends JpaRepository<StatusOrder, Integer>{

}
