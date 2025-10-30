package vn.iotstar.UTEExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.UTEExpress.entity.Transport;

@Repository
public interface ITransportRepository extends JpaRepository<Transport, Integer> {

}
