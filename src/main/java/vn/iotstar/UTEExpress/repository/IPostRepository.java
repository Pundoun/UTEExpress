package vn.iotstar.UTEExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.iotstar.UTEExpress.entity.Post;
import java.util.List;


public interface IPostRepository extends JpaRepository<Post, Integer> {
	Post findByPostName(String postName);
}
