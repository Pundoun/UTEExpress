package vn.iotstar.UTEExpress.controllers.shipper;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
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
import vn.iotstar.UTEExpress.entity.Account;
import vn.iotstar.UTEExpress.entity.Order;
import vn.iotstar.UTEExpress.entity.Shipper;
import vn.iotstar.UTEExpress.entity.Shipping;
import vn.iotstar.UTEExpress.service.IAccountService;
import vn.iotstar.UTEExpress.service.IOrderService;
import vn.iotstar.UTEExpress.service.IShipperService;
import vn.iotstar.UTEExpress.service.impl.ShippingServiceImpl;
import vn.iotstar.UTEExpress.utils.Constants;

@Controller
@RequestMapping("/shippercustocus")
public class ShipperCusToCusController {
	@Autowired
	private IShipperService shipperService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private ShippingServiceImpl shippingService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/{id}")
	public String cusToCusHome(@PathVariable("id") Integer shipperID, Model model) {
		Shipper shipper = shipperService.findById(shipperID).get();
		model.addAttribute("shipper", shipper);
		
		return "shipper/custocus-home";
	}
	
	@GetMapping("/{id}/account-info")
	public String accountInfoView(@PathVariable("id") Integer shipperID, Model model) {
		Shipper shipper = shipperService.findById(shipperID).get();
		model.addAttribute("shipper", shipper);
		return "shipper/custocus-account-info";
	}
	
	@PostMapping("/{id}/account-info")
	public String updateAccountInfo(@PathVariable("id") Integer shipperID, Model model, HttpServletRequest request) {
		String name = request.getParameter("name");
	    Integer gender = Integer.parseInt(request.getParameter("gender"));
	    String birthString = request.getParameter("birth");
	 
	    String address = request.getParameter("address");
	    String phone = request.getParameter("phone");
	    String cccd = request.getParameter("cccd");
	    String oldPassword = request.getParameter("oldPassword");
	    String newPassword = request.getParameter("newPassword");
	    String confirmPassword = request.getParameter("confirmPassword");
	    
	    Shipper oldShipper = shipperService.findById(shipperID).get();
	    if(!encoder.matches(oldPassword, oldShipper.getPassword())) {
	    	return "redirect:/shippercustocus/" + shipperID + "/account-info?status=wrong-pass";
	    } else if(!newPassword.equals(confirmPassword)) {
	    	return "redirect:/shippercustocus/" + shipperID + "/account-info?status=missmatch";
	    }
	    oldShipper.setName(name);
	    oldShipper.setGender(gender);
	    oldShipper.setAddress(address);
	    oldShipper.setPassword(encoder.encode(newPassword));
	    oldShipper.setPhone(phone);
	    oldShipper.setCccd(cccd);
	    
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
	            oldShipper.setPicture(fname);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	 // htem
	    Account account = oldShipper.getAccount();
	    account.setPassword(confirmPassword);
	    accountService.save(account);
	    
	    // Manually parse the birth string to Date
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date birthDate = null;
	    try {
	        birthDate = dateFormat.parse(birthString);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/shippercustocus/" + shipperID + "/account-info?status=invalid-birth-date"; // Handle invalid date
	    }
	    oldShipper.setBirth(birthDate);
	    //save
	    try {
			shipperService.save(oldShipper);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	    return "redirect:/shippercustocus/" + shipperID + "/account-info?status=success";
	}
	
	@GetMapping("/{id}/shipments")
	public String getShipmentsView(@PathVariable("id") Integer shipperID, Model model) {
		
		Shipper shipper = shipperService.findById(shipperID).get();
		model.addAttribute("shipper", shipper);
		
		// shipment take
		List<Order> allShipmentsTake = orderService.findOrdersByShipperIDAndStatus(shipperID, 1);
		model.addAttribute("allShipmentsTake", allShipmentsTake);
		
		// inprogress
		List<Order> allShipmentsInProgress = orderService.findOrdersByShipperIDAndStatus(shipperID, 7);
		model.addAttribute("allShipmentsInProgress", allShipmentsInProgress);
		
		// delivered
		List<Order> allShipmentsDelivered = orderService.findOrdersByShipperIDAndStatus(shipperID, 8);
		model.addAttribute("allShipmentsDelivered", allShipmentsDelivered);
		
		// failed
		List<Order> allShipmentsFailed = orderService.findOrdersByShipperIDAndStatus(shipperID, 9);
		model.addAttribute("allShipmentsFailed", allShipmentsFailed);
		
		return "shipper/custocus-shipment";
	}
	
	@GetMapping("/{id}/take-shipment")
	public String shipperTakeShipment(@PathVariable("id") Integer shipperID, @RequestParam("orderid") String orderID) {
		//System.out.println("called");
		
		// đổi status thành shipper is shipping
		Shipping shipping = shippingService.findLatestShippingByOrderID(orderID);
		  ///////////Creat old shipping////////
				Shipping shippingOld = new Shipping();
				shippingOld.setShipper(shipping.getShipper());
				shippingOld.setDateUpdate(shipping.getDateUpdate());
				shippingOld.setStatusOrderID(shipping.getStatusOrderID()); 
				shippingOld.setTotal(shipping.getTotal());
				shippingOld.setOrderID(orderID);  //Vừa sửa
				shippingService.save(shippingOld);
				///////////////////////////////////
		shipping.setStatusOrderID(7);
		shippingService.save(shipping);
		
		return "redirect:/shippercustocus/" + shipperID + "/shipments";
	}
	
	@GetMapping("/{id}/success-shipment")
	public String successShipment(@PathVariable("id") Integer shipperID, @RequestParam("orderid") String orderID) {
		//System.out.println("called");
		
		// đổi status thành shipper is shipping
		Shipping shipping = shippingService.findLatestShippingByOrderID(orderID);
		  ///////////Creat old shipping////////
				Shipping shippingOld = new Shipping();
				shippingOld.setShipper(shipping.getShipper());
				shippingOld.setDateUpdate(shipping.getDateUpdate());
				shippingOld.setStatusOrderID(shipping.getStatusOrderID()); 
				shippingOld.setTotal(shipping.getTotal());
				shippingOld.setOrderID(orderID);  //Vừa sửa
				shippingService.save(shippingOld);
				///////////////////////////////////
		shipping.setStatusOrderID(8);
		shippingService.save(shipping);
		
		
		
		return "redirect:/shippercustocus/" + shipperID + "/shipments";
	}
	
	@GetMapping("/{id}/failed-shipment")
	public String failedShipment(@PathVariable("id") Integer shipperID, @RequestParam("orderid") String orderID) {
		//System.out.println("called");
		
		// đổi status thành shipper is shipping
		Shipping shipping = shippingService.findLatestShippingByOrderID(orderID);
		  ///////////Creat old shipping////////
				Shipping shippingOld = new Shipping();
				shippingOld.setShipper(shipping.getShipper());
				shippingOld.setDateUpdate(shipping.getDateUpdate());
				shippingOld.setStatusOrderID(shipping.getStatusOrderID()); 
				shippingOld.setTotal(shipping.getTotal());
				shippingOld.setOrderID(orderID);  //Vừa sửa
				shippingService.save(shippingOld);
				///////////////////////////////////
		shipping.setStatusOrderID(9);
		shippingService.save(shipping);
		
		
		
		return "redirect:/shippercustocus/" + shipperID + "/shipments";
	}
	
}

