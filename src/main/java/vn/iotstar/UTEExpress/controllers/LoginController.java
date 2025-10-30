package vn.iotstar.UTEExpress.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.IAdminService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.service.IEmailService;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.IShipperService;
import vn.iotstar.UTEExpress.service.impl.CityServiceImpl;

@Controller
public class LoginController {

	@Autowired
	private CityServiceImpl cityService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IManagerService managerService;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IShipperService shipperService;
	@Autowired
	private IEmailService emailService;
	@Autowired
	private IAdminService adminService;

	@GetMapping("/login")
	public String loginViews(Model model, HttpServletRequest request) {
		// Kiểm tra session
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("account") != null) {
			Account account = (Account) session.getAttribute("account");
			return redirectBasedOnRole(account);
		}

		// Nếu chưa có session
		List<City> cities = cityService.findAll();
		model.addAttribute("allCity", cities);
		return "home/login"; // Trang đăng nhập
	}

	private String redirectBasedOnRole(Account account) {
		switch (account.getRole().getRoleID()) {
		case 1:
			return "redirect:/admin/" + adminService.findAdminByUsername(account.getUsername()).getAdminID();
		case 2:
			return "redirect:/manager/" + managerService.findManagerByUsername(account.getUsername()).getManagerID();
		case 3:
			return "redirect:/customer/"
					+ customerService.findCustomerByUserName(account.getUsername()).getCustomerID();
		case 4:
			return "redirect:/shippercustocus/"
					+ shipperService.findShipperByUsername(account.getUsername()).getShipperID();
		case 5:
			return "redirect:/shippercustopost/"
					+ shipperService.findShipperByUsername(account.getUsername()).getShipperID();
		case 6:
			return "redirect:/shipperposttocus/"
					+ shipperService.findShipperByUsername(account.getUsername()).getShipperID();
		case 7:
			return "redirect:/shipperposttopost/"
					+ shipperService.findShipperByUsername(account.getUsername()).getShipperID();
		default:
			return "redirect:/home"; // Mặc định nếu không có role phù hợp
		}
	}

	@GetMapping("/forgot-password")
	public String showForgotPasswordPage() {

		return "home/forgot-password";
	}

	@PostMapping("/process-forgotpassword")
	public String processForgotPassword(@RequestParam("username") String email, Model model) {
		Account account = accountService.findByUsername(email);

		if (account != null) {
			// Tạo OTP và lưu vào token
			String otp = generateOtp();

			// Gửi OTP qua email
			emailService.sendOtp(email, otp);

			model.addAttribute("email", email); // Truyền email vào model
			model.addAttribute("otp", otp);
			return "home/forgot-verify-otp"; // Chuyển đến trang xác minh OTP
		} else {
			model.addAttribute("error", "Email không tồn tại trong hệ thống.");//
			return "home/forgot-password"; // Trả về form quên mật khẩu
		}
	}
	  @PostMapping("/forgot-verify-otp")
	    public String verifyOtp(@RequestParam("otpxacthuc") String otpInput,
	                            @RequestParam("email") String email,
	                            @RequestParam("otp") String otpFromSession,
	                            HttpServletRequest request,
	                            Model model) {
	        System.out.println(otpInput+ email+otpFromSession+"alooo");
	        // Kiểm tra OTP từ người dùng nhập có khớp với OTP đã gửi qua email
	        if (otpInput.equals(otpFromSession)) {
	        	model.addAttribute("email", email);
	        
	            // OTP đúng, chuyển hướng đến trang nhập mật khẩu mới
	            return "home/reset-password"; // Trang để người dùng nhập mật khẩu mới
	        } else {
	            // OTP sai, thông báo lỗi và quay lại trang xác minh OTP
	            model.addAttribute("error", "Mã OTP không hợp lệ.");
	            return "home/forgot-password"; // Quay lại trang xác minh OTP
	        }
	    }
	  @PostMapping("/process-reset-password")
	    public String resetPassword(@RequestParam("newPassword") String newPassword,
	                                @RequestParam("confirmPassword") String confirmPassword,
	                                @RequestParam("email") String email,
	                                Model model) {
	        // Kiểm tra mật khẩu mới và mật khẩu xác nhận có khớp không
	        if (!newPassword.equals(confirmPassword)) {
	            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
	            return "home/reset-password"; // Quay lại trang reset password nếu có lỗi
	        }
	        System.out.println(newPassword+confirmPassword);
	        System.out.println(email);
	        // Lấy tài khoản theo email và cập nhật mật khẩu
	        Account account = accountService.findByUsername(email);
	        if (account != null) {
	            // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
	            account.setPassword(newPassword); // Bạn có thể sử dụng PasswordEncoder nếu cần mã hóa mật khẩu
	            accountService.save(account); // Lưu tài khoản đã được cập nhật
	            System.out.println(email);
	            return "home/login"; // Điều hướng đến trang đăng nhập
	        } else {
	            model.addAttribute("error", "Tài khoản không tồn tại.");
	            return "home/reset-password"; // Quay lại trang reset password nếu không tìm thấy tài khoản
	        }
	    }

//Phương thức tạo mã OTP ngẫu nhiên (có thể sử dụng thư viện hoặc tự viết
	// logic)
	private String generateOtp() {
		int otp = 100000 + (int) (Math.random() * 900000); // Sinh mã OTP 6 chữ số
		return String.valueOf(otp);
	}

	@GetMapping("/login/waiting")
	public String processLogin(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName(); // Lấy username

		Account account = accountService.findById(username).get();

		if (account == null || account.getRole() == null) {
			return "redirect:/login?error=account-doesnt-exist";
		}

		// Lưu thông tin vào session
		HttpSession session = request.getSession();
		session.setAttribute("account", account);

		// Điều hướng dựa trên vai trò
		return redirectBasedOnRole(account);

	}
}
