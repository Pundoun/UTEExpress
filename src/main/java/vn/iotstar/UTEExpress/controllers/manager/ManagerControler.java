package vn.iotstar.UTEExpress.controllers.manager;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jakarta.servlet.http.Part;
import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.Customer;
import vn.iotstar.UTEExpress.entity.Manager;
import vn.iotstar.UTEExpress.entity.Order;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.impl.OrderServiceImpl;
import vn.iotstar.UTEExpress.utils.Constants;

@Controller
@RequestMapping("/manager")
public class ManagerControler {
	@Autowired
	private IManagerService managerService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private OrderServiceImpl orderService;
	
	// login xong sẽ về trang này nếu role là manager
	@GetMapping("/{id}")
	public String managerHome(@PathVariable("id") Integer managerID, Model model) {
		// find manager by id
		Manager manager = managerService.findById(managerID).get();
		// truyền model lên views
		model.addAttribute("manager", manager);
		return "manager/home";
	}
	
	@GetMapping("/{id}/manager-info")
	public String managerDetailsInfo(@PathVariable("id") Integer managerID, Model model) {
		// find manager by id
		Manager manager = managerService.findById(managerID).get();
//		if(manager != null) System.out.println("khac null");
		// truyền model lên views
		model.addAttribute("manager", manager);
		
		return "manager/account-info";
	}
	
	@PostMapping("/{id}/manager-info")
	public String updateManagerInfo(@PathVariable("id") Integer managerID, HttpServletRequest request, @ModelAttribute("manager") Manager manager) {
		String name = request.getParameter("name");
	    Integer gender = Integer.parseInt(request.getParameter("gender"));
	    String birthString = request.getParameter("birth");
	 
	    String address = request.getParameter("address");
	    String phone = request.getParameter("phone");
	    String cccd = request.getParameter("cccd");
	    String oldPassword = request.getParameter("oldPassword");
	    String newPassword = request.getParameter("newPassword");
	    String confirmPassword = request.getParameter("confirmPassword");
	    
	    Manager oldManager = managerService.findById(managerID).get();
	    if(!encoder.matches(oldPassword,oldManager.getAccount().getPassword())) {  // sửa
	    	return "redirect:/manager/" + managerID + "/manager-info?status=wrong-pass";
	    } else if(!newPassword.equals(confirmPassword)) {   
	    	return "redirect:/manager/" + managerID + "/manager-info?status=missmatch";
	    }
	    oldManager.setName(name);
	    oldManager.setGender(gender);
	    oldManager.setAddress(address);
	    oldManager.setPassword(encoder.encode(confirmPassword));
	    oldManager.setPhone(phone);
	    oldManager.setCccd(cccd);
	    
	    // Xử lý images
	    String fname = "";
	    String uploadPath = Constants.UPLOAD_PATH;
	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdir();
	    }

	    try {
	        Part part = request.getPart("images1");
	        if (part.getSize() > 0) {
	            String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
	            String ext = filename.substring(filename.lastIndexOf(".") + 1);
	            fname = System.currentTimeMillis() + "." + ext;
	            part.write(uploadPath + "/" + fname);
	            oldManager.setPicture(fname);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    // htem
	    Account account = oldManager.getAccount();
	    account.setPassword(confirmPassword);
	    accountService.save(account);
	    
	    // Manually parse the birth string to Date
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date birthDate = null;
	    try {
	        birthDate = dateFormat.parse(birthString);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/manager/" + managerID + "/manager-info?status=invalid-birth-date"; // Handle invalid date
	    }
	    oldManager.setBirth(birthDate);
	    //save
	    try {
			managerService.save(oldManager);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	    return "redirect:/manager/" + managerID + "/manager-info?status=success";
	}
	
	@GetMapping("/{id}/statistic")
	public String getStatisticView(@PathVariable("id") Integer managerID, Model model) {
		Manager manager = managerService.findById(managerID).get();
		model.addAttribute("manager", manager);
		
		String cityName = manager.getCity();
		List<Customer> customers = customerService.findCustomersByCity(cityName);
		
		model.addAttribute("customers", customers);
		
		return "manager/manager-statistic";
	}
	
	@PostMapping("/{id}/statistic")
	public String submitStatistic(@PathVariable("id") Integer managerID, @RequestParam("customerId") Integer customerID,
												Model model) {
		Customer customer = customerService.findById(customerID);
		List<Order> orders = orderService.findAllByCustomerID(customerID);
		
		double totalAmount = 0;
		if(orders != null) {
			
			Manager manager = managerService.findById(managerID).get();
			model.addAttribute("manager", manager);
			model.addAttribute("customerName",customer.getName());
			
			String cityName = manager.getCity();
			List<Customer> customers = customerService.findCustomersByCity(cityName);
			model.addAttribute("customers", customers);
			
			model.addAttribute("orders", orders);
			totalAmount = orders.stream()
                    .mapToDouble(Order::getTotal)
                    .sum();
			model.addAttribute("totalAmount", totalAmount);
			return "manager/manager-statistic";
		}
		
		return "redirect:/manager/" + managerID + "/statistic";
	}
}
