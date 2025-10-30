package vn.iotstar.UTEExpress.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false); // Lấy session hiện tại, không tạo mới
	    if (session != null) {
	        session.invalidate(); // Xóa toàn bộ dữ liệu trong session
	    }
	    return "redirect:/login"; // Chuyển hướng người dùng về trang đăng nhập
	}
}
