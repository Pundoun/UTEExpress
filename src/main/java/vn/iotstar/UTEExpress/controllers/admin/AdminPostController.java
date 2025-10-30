package vn.iotstar.UTEExpress.controllers.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.iotstar.UTEExpress.dto.PostDTO;
import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.entity.Manager;
import vn.iotstar.UTEExpress.entity.Post;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.ICityService;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.IPostService;
import vn.iotstar.UTEExpress.service.IRoleService;
import vn.iotstar.UTEExpress.service.impl.AccountServiceImpl;
import vn.iotstar.UTEExpress.service.impl.RoleServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin/{id}/post")
public class AdminPostController {
	@Autowired
	IPostService postService;
	@Autowired
	IManagerService managerService;
	@Autowired
	ICityService cityService;
	@Autowired
	IRoleService roleService;
	@Autowired
	private AccountServiceImpl accountService;
	@Autowired
	private PasswordEncoder encoder;

	// Đây sẽ hiện thông tin Manager quản lí Post
	@GetMapping("/manager/{postid}") // Đây là postID
	public String ManagerPost(@PathVariable("postid") Integer postID, @PathVariable("id") Integer id, Model model) {
		Manager manager = managerService.findManagerByIDPost(postID);
		model.addAttribute("manager", manager);

		model.addAttribute("id", id); // Truyen adminID
		return "admin/managerPost";
	}

	// Xoá post
	@GetMapping("/delete/{postid}")
	public String deletePost(@PathVariable("postid") Integer postID, @PathVariable("id") Integer id) {
		// Them code xoa o day

		postService.delete(postID);

		// xoa luon moi quan he voi manager
		return "redirect:/admin/" + id;
	}

	// Hiện FORM Thêm Post
	@GetMapping("/add")
	public String addPost(@PathVariable("id") Integer id, Model model) {
		List<City> cities = cityService.findAll();
		List<City> citiesHasPost = cityService.findCitiesHasPost();

		List<City> inAOnly = cities.stream().filter(city -> !citiesHasPost.contains(city)).collect(Collectors.toList());

		// List<Manager> Manager = managerService.

		model.addAttribute("cities", inAOnly); // Danh sách các thành phố chưa có Post
		model.addAttribute("id", id);
		return "admin/addPost";
	}

	@PostMapping("/save")
	public String savePost(@PathVariable("id") Integer id, @RequestParam("postName") String postName,
			@RequestParam("cityID") Integer cityID) {

		City city = cityService.findById(cityID);
		// Tạo Post
		// Tạo đối tượng Post từ các tham số
		Post post = new Post();
		post.setPostName(postName);
		post.setCity(city);
		postService.save(post);
		return "redirect:/admin/" + id;
	}

	// Edit Manager cho Post
	@GetMapping("/editmanager/{postid}")
	public String editManager(@PathVariable("id") Integer id, @PathVariable("postid") Integer postid, Model model) {
		Manager manager = managerService.findManagerByIDPost(postid);
		model.addAttribute("manager", manager); // Truyền thông tin Manager đang giữ quản lí
		return "admin/formEditManager";
	}

	// Save Edit Manager cho Post
	// Chức năng chỉ reset password cho manager hoặc thay đổi tên managername thôi
	@PostMapping("/editmanager/{postid}/save")
	public String saveEditManagerPost(@PathVariable("id") Integer id, @PathVariable("postid") Integer postid,
			Model model, @RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("managername") String Managername) {
		Manager oldManager = managerService.findManagerByIDPost(postid); // Manager quan ly Post hien tai

		// Nếu username rỗng thì hiên thông báo lỗi
		if (username.isEmpty()) {
			model.addAttribute("errorMessage", "username is null");
			return "admin/formEditManager";
		} // Reset matkhau va cap nhat ten lai neu co
		else if (username.equals(oldManager.getAccount().getUsername())) {
			Account account = accountService.findById(username).orElse(null);

			// Nếu mật khẩu không rỗng và mật khẩu khác thì cập nhật lại
			if (!password.isEmpty()) {
					account.setPassword(password); // Cập nhật mật khẩu cho account
					password = encoder.encode(password);
					oldManager.setPassword(password); // Cập nhật mật khẩu cho Manager
			}
			
			// Nếu tên name không rỗng và khác thì cập nhật lại MANAGER
			if (!Managername.isEmpty() & !Managername.equals(oldManager.getName())) {
				oldManager.setName(Managername);
			}

			managerService.save(oldManager);
			accountService.save(account);
			return "redirect:/admin/" + id;
		}

		return "redirect:/admin/" + id;
	}

	// Add Manager cho Post
	@GetMapping("/addmanager/{postid}")
	public String addmanager(@PathVariable("id") Integer id, @PathVariable("postid") Integer postid) {
		return "admin/formRegisterManager";
	}

	// Save Manager cho Post
	@PostMapping("/addmanager/{postid}/save")
	public String saveManagerPost(@PathVariable("id") Integer id, @PathVariable("postid") Integer postid, Model model,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("managername") String Managername) {

		Account accountExist = accountService.findById(username).orElse(null);
		// Kiểm tra username đã tồn tại chưa
		if (accountExist == null) {
			// Tạo Account cho Manager
			Account account = new Account();
			account.setUsername(username);
			account.setPassword(password); // Lưu mật khẩu mã hoá
			account.setRole(roleService.findByRoleName("MANAGER"));
			accountService.save(account);

			// Tạo Manager trong MANAGER
			Manager manager = new Manager();
			manager.setAccount(account);
			manager.setName(Managername);
			String passwordEnconde = encoder.encode(password);
			manager.setPassword(passwordEnconde); // Lưu mật khẩu mã hoá
			Post post = postService.findByID(postid);
			manager.setCity(cityService.findById(post.getCity().getCityID()).getCityName());
			manager.setPost(post);
			managerService.save(manager);
		} else {
			// Nếu tồn tại thì
			model.addAttribute("errorMessage", "Username has already existed!");
			return "admin/formRegisterManager";
		}

		return "redirect:/admin/" + id;
	}

}
