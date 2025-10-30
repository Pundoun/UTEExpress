package vn.iotstar.UTEExpress.service;

import java.util.List;
import vn.iotstar.UTEExpress.entity.Post;

public interface IPostService {
	public List <Post> findAll();
	public Post findByNamePost (String namePost);
	public void save(Post post);
	public Post findByID (Integer postID);
	public void delete(Integer postID);
}
