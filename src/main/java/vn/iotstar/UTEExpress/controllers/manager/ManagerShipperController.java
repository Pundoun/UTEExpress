package vn.iotstar.UTEExpress.controllers.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.Manager;
import vn.iotstar.UTEExpress.entity.Post;
import vn.iotstar.UTEExpress.entity.Role;
import vn.iotstar.UTEExpress.entity.Shipper;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.IRoleService;
import vn.iotstar.UTEExpress.service.IShipperService;

@Controller
@RequestMapping("/manager/{id}/")
public class ManagerShipperController {
	@Autowired
	private IManagerService managerService;
	@Autowired
	private IShipperService shipperService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("shippers")
	public String listShippers(@PathVariable("id") Integer managerID, Model model) {
		
		Manager manager = managerService.findById(managerID).get();
		model.addAttribute("manager",manager);
		
		Integer IDPost = manager.getPost().getPostID();
		List<Shipper> shippers = shipperService.findShippersByIDPost(IDPost);
		model.addAttribute("allShippers", shippers);
		
		return "manager/shipper";
	}
	
	@GetMapping("add-shipper")
	public String addShipperView(@PathVariable("id") Integer managerID, Model model) {
		
		Manager manager = managerService.findById(managerID).get();
		model.addAttribute("manager",manager);
		model.addAttribute("shipper",new Shipper());
		
		return "manager/shipper-save";
	}
	
	@PostMapping("add-shipper")
	public String addShipper(@PathVariable("id") Integer managerID, 
	                         @ModelAttribute("shipper") Shipper shipper,
	                         @RequestParam("username") String username, 
	                         @RequestParam("city") String city,
	                         @RequestParam("roleid") Integer roleid) {
	    Optional<Manager> optionalManager = managerService.findById(managerID);

	    if (!optionalManager.isPresent()) {
	        // Redirect to an error page or handle the case when the Manager is not found
	        return "redirect:/error";  // or you can show a custom error message
	    }

	    Manager manager = optionalManager.get();
	    Post post = manager.getPost();
	    
	    shipper.setPost(post);
	    shipper.setCity(city);
	    
	    // Create account
	    Optional<Account> optionalAccount = accountService.findById(username);
	    if (optionalAccount.isPresent()) {
	        return "redirect:/manager/" + managerID + "/add-shipper?error=existUsername";
	    }
	    
	    Account account = new Account();
	    Role role = null;
	    if(roleid == 4) {
	    	role = roleService.findRoleByRoleNameIgnoreCase("SHIPPER_DIRECT");
	    }else if (roleid == 5) {
	    	role = roleService.findRoleByRoleNameIgnoreCase("SHIPPER_CUSTOMER");
	    }else if(roleid == 6) {
	    	role = roleService.findRoleByRoleNameIgnoreCase("SHIPPER_POST");
	    }else {
	    	role = roleService.findRoleByRoleNameIgnoreCase("SHIPPER_TRANSFER");
	    }
	    
	    account.setRole(role);
	    account.setUsername(username);
	    account.setPassword(shipper.getPassword());
	    
	    accountService.save(account);
	    
	    shipper.setPassword(encoder.encode(shipper.getPassword()));
	    shipper.setAccount(account);
	    shipperService.save(shipper);
	    return "redirect:/manager/" + managerID + "/shippers";
	}

	@GetMapping("/delete-shipper")
	public String deleteShipper(@PathVariable("id") Integer managerID, @RequestParam("shipperId") Integer shipperID) {
		
		// Tìm shipper theo ID
	    Optional<Shipper> shipper = shipperService.findById(shipperID);
	    
	    if (shipper.isPresent()) {
	    	Account account = shipper.get().getAccount();
	    	accountService.delete(account);
	    	
	        // Xóa shipper nếu tồn tại
	        shipperService.delete(shipper.get());
	    } else {
	        // Nếu không tìm thấy, có thể chuyển hướng với thông báo lỗi
	        return "redirect:/manager/" + managerID + "/shippers?error=shipperNotFound";
	    }
	    // Chuyển hướng về danh sách shippers
	    return "redirect:/manager/" + managerID + "/shippers";

	}
}
