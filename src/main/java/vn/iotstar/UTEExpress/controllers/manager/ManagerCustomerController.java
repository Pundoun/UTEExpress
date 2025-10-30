package vn.iotstar.UTEExpress.controllers.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.Customer;
import vn.iotstar.UTEExpress.entity.Manager;

import vn.iotstar.UTEExpress.entity.Role;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.IRoleService;


@Controller
@RequestMapping("/manager/{id}/")
public class ManagerCustomerController {
	@Autowired
	private IManagerService managerService;
	@Autowired
	private ICustomerService customerService;
	@Autowired 
	private IAccountService accountService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	PasswordEncoder encoder;
	
	// trang co list cac customer thuoc buu cuc (dua tren city)
	@GetMapping("customer-request")
	public String customerRequestViews(Model model, @PathVariable("id") Integer managerID) {
		// find manager by id
		Manager manager = managerService.findById(managerID).get();
		model.addAttribute("manager", manager);
		List<Customer> customers = customerService.findInactiveCustomersByCity(manager.getPost().getCity().getCityName());
		model.addAttribute("requestCustomers", customers);

		return "manager/customer-request";	
	}
	
	/*
	 * chuyển về trang co list cac customer thuoc buu cuc (dua tren city) cho phep
	 * dang ki chuyen isActive thanh 1 tro ve home
	 */
	@GetMapping("active-customer")
	public String permitCustomerRequest(@PathVariable("id") Integer managerID, @RequestParam("username") String username) {
		Customer customer = customerService.findCustomerByUserName(username);
		customer.setIsActive(1);
		
		customerService.save(customer);
		
		return "redirect:/manager/" + managerID + "/customer-request";
	}
	
	@GetMapping("delete-request") 
	public String rejectCustomerRequest(@PathVariable("id") Integer managerID, @RequestParam("username") String username){
	 	// xóa customer
		Customer customer = customerService.findCustomerByUserName(username);
		customerService.delete(customer);
		
		// xóa account ứng vs customer
		Account account = accountService.findById(username).get();
		accountService.delete(account);
		 
		return "redirect:/manager/" + managerID + "/customer-request";
	}
	
	@GetMapping("customers")
	public String allCustomerOfPost(@PathVariable("id") Integer managerID, Model model) {
		Manager manager = managerService.findById(managerID).get();
		
		if(manager == null) {
			return "redirect:/";
		}
		
		String cityName = manager.getPost().getCity().getCityName();
		List<Customer> allCustomers = customerService.findCustomersByCity(cityName);
		model.addAttribute("allCustomers", allCustomers);
		model.addAttribute("manager", manager);
		
		return "manager/customer";
	}
	
	@GetMapping("add-customer")
	public String addNewCustomerView(@PathVariable("id") Integer managerID, Model model) {
		Manager manager = managerService.findById(managerID).get();
		
		if(manager == null) {
			return "redirect:/";
		}
		model.addAttribute("manager", manager);
		model.addAttribute("customer", new Customer());
		
		return "manager/customer-save";
	}
	
	@GetMapping("delete-customer")
	public String deleteCustomer(@PathVariable("id") Integer managerID, @RequestParam("username") String username){
	 	// xóa customer
		Customer customer = customerService.findCustomerByUserName(username);
		customerService.delete(customer);
		
		// xóa account ứng vs customer
		Account account = accountService.findById(username).get();
		accountService.delete(account);
		 
		return "redirect:/manager/" + managerID + "/customers";
	}
	
	@PostMapping("add-customer")
	public String addNewCustomer(@PathVariable("id") Integer managerID, @ModelAttribute("customer") Customer customer,
														HttpServletRequest request) {
		Manager manager = managerService.findById(managerID).get();
		
		if(manager == null) {
			return "redirect:/";
		}
		
		// lấy các tham số từ form
		String birthString = request.getParameter("birth");
		// Manually parse the birth string to Date
		
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date birthDate = null;
	    try {
	        birthDate = dateFormat.parse(birthString);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/manager/" + managerID + "/manager-info?status=invalid-birth-date"; // Handle invalid date
	    }
	    customer.setBirth(birthDate);
	   
	    
	    // Create account
	    Optional<Account> optionalAccount = accountService.findById(request.getParameter("username"));
	    if (optionalAccount.isPresent()) {
	        return "redirect:/manager/" + managerID + "/add-shipper?error=existUsername";
	    }
	    
	    Account account = new Account();
	    Role role = roleService.findRoleByRoleNameIgnoreCase("CUSTOMER");
	    account.setRole(role);
	    account.setUsername(request.getParameter("username"));
	    account.setPassword(customer.getPassword());
	    accountService.save(account);
	    
	    customer.setPassword(encoder.encode(customer.getPassword()));
	    customer.setAccount(account);
	    
	    customerService.save(customer);
		
		return "redirect:/manager/" + managerID + "/customers";
	}
}
