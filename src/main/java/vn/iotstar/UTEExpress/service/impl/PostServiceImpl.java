package vn.iotstar.UTEExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.UTEExpress.entity.Post;
import vn.iotstar.UTEExpress.repository.IPostRepository;
import vn.iotstar.UTEExpress.service.IPostService;

@Service
public class PostServiceImpl implements IPostService{
	@Autowired IPostRepository postRepository;
	
	@Override
	public List<Post> findAll() {
		
		return postRepository.findAll();
	}

	@Override
	public Post findByNamePost(String namePost) {
		return postRepository.findByPostName(namePost) ;
	}

	@Override
	public void save(Post post) {
		postRepository.save(post);
		
	}

	@Override
	public Post findByID(Integer postID) {
		// TODO Auto-generated method stub
		return postRepository.findById(postID).get();
	}

	@Override
	public void delete(Integer postID) {
		postRepository.deleteById(postID);
		
	}
}
