package vn.iotstar.UTEExpress.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.entity.Customer;
import vn.iotstar.UTEExpress.entity.Role;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.service.IRoleService;
import vn.iotstar.UTEExpress.service.impl.CityServiceImpl;

import java.util.List;
import java.util.Random;

@Controller

public class RegisterController {

	@Autowired
	private ICustomerService customerService;
	@Autowired
	CityServiceImpl cityService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private JavaMailSender emailSender; // Thêm dependency JavaMailSender
	// Hiển thị trang đăng kí

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		// Nếu chưa có session
		List<City> cities = cityService.findAll();
		model.addAttribute("allCity", cities);
		return "home/login"; // Trả về tên file `register.html` trong thư mục templates
	}

	// Phương thức để gửi OTP qua email
	public void sendOtpEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("Register UTEEXPRESS");
		message.setText("Your OTP code is: " + otp);
		emailSender.send(message);
	}

	// Phương thức tạo mã OTP ngẫu nhiên
	public String generateOtp() {
		Random random = new Random();
		int otp = random.nextInt(999999); // Sinh OTP 6 chữ số
		return String.format("%06d", otp); // Đảm bảo OTP có 6 chữ số
	}

	@PostMapping("/process-register")
	public String processRegister(HttpServletRequest request, HttpSession session) {
		String fullname = request.getParameter("fullname");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String city = request.getParameter("city");
	
		// Check if the username already exists
		if (accountService.findById(username).isPresent()) {
			return "redirect:/register?error=username-already-exists";
		}

//		// Create a new Customer
//		Customer newCustomer = new Customer();
//		newCustomer.setIsActive(0); // Default to inactive
//		newCustomer.setCity(city);
//		newCustomer.setName(fullname);
//		newCustomer.setPassword(encoder.encode(password));
//
//		// Assign the "CUSTOMER" role
//		Role role = roleService.findRoleByRoleNameIgnoreCase("CUSTOMER");
//
//		// Create a new Account with an encoded password
//		Account newAccount = new Account();
//		newAccount.setRole(role);
//		newAccount.setUsername(username);
//		newAccount.setPassword(password);
//
//		// Save the Account
//		accountService.save(newAccount);
//
//		// Link the Account to the Customer
//		newCustomer.setAccount(newAccount);
//
//		// Save the Customer
//		customerService.save(newCustomer);

		// Generate OTP and send to email
		String otp = generateOtp();
		sendOtpEmail(username, otp);

		// Lưu OTP vào session để so sánh khi xác minh
		session.setAttribute("otp", otp);
		session.setAttribute("username", username);
		session.setAttribute("city", city);
		session.setAttribute("password", password);
		session.setAttribute("fullname", fullname);

		// Redirect to OTP verification page
		return "home/verify-otp";
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam String otp, @RequestParam String email, HttpSession session) {
		// Lấy OTP từ session
		String otpFromSession = (String) session.getAttribute("otp");
		String fullname = (String) session.getAttribute("fullname");
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		String city = (String) session.getAttribute("city");

		// Kiểm tra OTP, nếu hợp lệ thì hoàn tất đăng ký
		if (otpFromSession != null && otpFromSession.equals(otp)) {
			// Cập nhật trạng thái tài khoản là active và hoàn tất đăng ký
			Account account = accountService.findById(email).orElse(null);
			if (account == null) {
				// Create a new Customer
				Customer newCustomer = new Customer();
				newCustomer.setIsActive(0); // Default to inactive
				newCustomer.setCity(city);
				newCustomer.setName(fullname);
				newCustomer.setPassword(encoder.encode(password));

				// Assign the "CUSTOMER" role
				Role role = roleService.findRoleByRoleNameIgnoreCase("CUSTOMER");

				// Create a new Account with an encoded password
				Account newAccount = new Account();
				newAccount.setRole(role);
				newAccount.setUsername(username);
				newAccount.setPassword(password);

				// Save the Account
				accountService.save(newAccount);

				// Link the Account to the Customer
				newCustomer.setAccount(newAccount);

				// Save the Customer
				customerService.save(newCustomer);
			}
			return "redirect:/login"; // Hoặc chuyển hướng đến trang thành công
		} else {
			// Thông báo OTP không hợp lệ
			return "redirect:/register?error=invalid-otp";
		}
	}
}
