package vn.iotstar.UTEExpress.controllers.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.UTEExpress.dto.PostDTO;
import vn.iotstar.UTEExpress.entity.Admin;
import vn.iotstar.UTEExpress.entity.Post;
import vn.iotstar.UTEExpress.service.IAdminService;
import vn.iotstar.UTEExpress.service.ICityService;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.IPostService;

@Controller
@RequestMapping("/admin/")
public class AdminController {
	@Autowired ICityService cityService;
	@Autowired IPostService postService;
	@Autowired IManagerService	managerService;
	@Autowired IAdminService adminService;
	
	@GetMapping("{id}")
	public String AdminHome(@PathVariable("id") Integer adminID, Model model) {
		//List <City> listCity = cityService.findAll();
		
		List <Post> postIn = postService.findAll();
		List <PostDTO> postDTO = new ArrayList<>();
		
		for (Post post : postIn) {
			PostDTO dto = new PostDTO();
			dto.setPostID(post.getPostID());
			dto.setCity(post.getCity());
			dto.setManager(managerService.findManagerByIDPost(post.getPostID()));
			dto.setPostName(post.getPostName());
			postDTO.add(dto);
		}
		
		model.addAttribute("posts", postDTO);
		return "admin/home";
	}
	
	@GetMapping("{id}/profile")
	public String AdminProfile(@PathVariable("id") Integer adminID, Model model) {
		//List <City> listCity = cityService.findAll();
		
		Admin admin = adminService.findByID(adminID);
		
		model.addAttribute("admin", admin);
		return "admin/adminProfile";
	}
	
}
