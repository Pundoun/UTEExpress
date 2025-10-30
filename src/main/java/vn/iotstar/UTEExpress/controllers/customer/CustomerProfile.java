package vn.iotstar.UTEExpress.controllers.customer;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.entity.Customer;
import vn.iotstar.UTEExpress.service.ICityService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.utils.ConstantUtils;

@Controller
@RequestMapping("/customer/profile")
public class CustomerProfile {
	@Autowired
	ICustomerService customerService;
	@Autowired
	ICityService cityService;
	@Autowired
	private PasswordEncoder encoder;
	// Show Profile
		@GetMapping("/{id}")
		public String Profile(@PathVariable("id") Integer customerID, Model model) {
			Customer customer = customerService.findCustomerByIDUser(customerID);

			model.addAttribute("id", customerID);
			model.addAttribute("customer", customer);
			return "customer/customer-profile";
		}

		// Edit Profile
		@GetMapping("/edit/{id}")
		public String editProfile(@PathVariable("id") Integer customerID, Model model) {
			Customer customer = customerService.findCustomerByIDUser(customerID);
			List<City> cities = cityService.findAll();

			model.addAttribute("id", customerID);
			model.addAttribute("customer", customer);
			model.addAttribute("cities", cities);
			return "customer/customer-edit-profile";
		}

		// Update Profile
		@PostMapping("/update/{id}")
		public String updateProfile(@PathVariable("id") Integer customerID, Model model, HttpServletRequest req,
				@RequestParam("name") String name,
				@RequestParam("phone") String phone, @RequestParam("address") String address,
				@RequestParam("city") String city,
				@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
				@RequestParam("gender") Integer gender, @RequestParam("cccd") String cccd,
				@RequestParam("birth") String birth) throws ParseException {
			Customer oldCustomer = customerService.findCustomerByIDUser(customerID);
			Boolean isPassword = false;
			
			//Gán Customer nhũng thông tin cập nhật
			Customer customer = new Customer();
			customer.setAccount(oldCustomer.getAccount());
			customer.setAddress(oldCustomer.getAddress());
			customer.setCustomerID(customerID);
			customer.setName(name);
			customer.setPhone(phone);
			customer.setAddress(address);
			customer.setCity(city);
			customer.setGender(gender);
			customer.setIsActive(oldCustomer.getIsActive());
			customer.setCccd(cccd);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			customer.setBirth(dateFormat.parse(birth));		
			
			// Hình cũ
			String fileOld = oldCustomer.getPicture();

			// Link từ request
			String images = req.getParameter("images");

			// Xử lý images
			String fname = "";
			String uploadPath = ConstantUtils.UPLOAD_PATH;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			try {
				Part part = req.getPart("images1");
				if (part.getSize() > 0) {
					String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
					String ext = filename.substring(filename.lastIndexOf(".") + 1);
					fname = System.currentTimeMillis() + "." + ext;
					part.write(uploadPath + "/" + fname);
					customer.setPicture(fname);
				} else if (images != null) {
					customer.setPicture(images);
				} else {
					customer.setPicture(fileOld);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			// Kiểm tra mật khẩu oldPassword, cần đảm bảo người dùng nhập đúng password để
			// cập nhật thông tin
			if (encoder.matches(oldPassword, oldCustomer.getPassword())) {
				isPassword = true; // Nếu mật khẩu nhập vào đúng thì isPassword = true
			}

			// Nếu oldPassword, kiểm tra ô newPassword có rỗng không
			if (newPassword != null && !newPassword.isEmpty()) {
				customer.setPassword(newPassword); // Nếu không rỗng gán password mới cho customer
			} else {
				customer.setPassword(oldCustomer.getPassword()); //gan password = oldCustomer
			}

			// Nếu oldPassword, không rỗng và newPassword: isPassword= true thì đổi mật khẩu mới
			if (isPassword == true) {
				customerService.save(customer); // Lưu customer
			} else {
				List<City> cities = cityService.findAll();
				model.addAttribute("errorMessage", "Mật khẩu cũ không chính xác. Vui lòng thử lại!");
				model.addAttribute("customer", customer);
				model.addAttribute("id", customerID);
				model.addAttribute("customer", customer);
				model.addAttribute("cities", cities);
				return "customer/customer-edit-profile" ;
			}
			return "redirect:/customer/profile/" + customerID;

			
		}
}
