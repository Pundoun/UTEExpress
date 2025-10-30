package vn.iotstar.UTEExpress.controllers.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerLogout {
	@GetMapping("/logout")
	public String logout() {
		//Kết thúc Session ở đây
		return "redirect:/";
	}
}
