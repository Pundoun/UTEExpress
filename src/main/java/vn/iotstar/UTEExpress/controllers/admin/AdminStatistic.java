package vn.iotstar.UTEExpress.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.UTEExpress.dto.PostShipperCountDTO;
import vn.iotstar.UTEExpress.service.IShipperService;


@Controller
@RequestMapping("/admin/{id}/statistic")
public class AdminStatistic {
	@Autowired IShipperService shipperService;
	@GetMapping("/shipFee")
	public String shipFee(@RequestParam String param) {
		return new String();
	}
	
	@GetMapping("/codsurcharge")
	public String codSurcharge(@RequestParam String param) {
		return new String();
	}
	
//	@GetMapping("")
//	public String amountShipperPost(@PathVariable Integer id, Model model) {
//		//List<PostShipperCountDTO> data = shipperService.getShipperCountByPost();
//		 // Chuyển dữ liệu sang Model
//        List<String> postNames = data.stream()
//                                     .map(PostShipperCountDTO::getPostName)
//                                     .toList();
//        List<Long> shipperCounts = data.stream()
//                                       .map(PostShipperCountDTO::getShipperCount)
//                                       .toList();
//        
//        model.addAttribute("postNames", postNames);
//        model.addAttribute("shipperCounts", shipperCounts);
//        
//        return "admin/statistic";
//	}
	
	
	
}
