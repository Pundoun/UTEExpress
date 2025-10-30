package vn.iotstar.UTEExpress.controllers.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.UTEExpress.entity.Manager;
import vn.iotstar.UTEExpress.entity.Order;
import vn.iotstar.UTEExpress.entity.Shipper;
import vn.iotstar.UTEExpress.entity.Shipping;
import vn.iotstar.UTEExpress.entity.StatusOrder;
import vn.iotstar.UTEExpress.service.IManagerService;
import vn.iotstar.UTEExpress.service.IOrderService;
import vn.iotstar.UTEExpress.service.IShipperService;
import vn.iotstar.UTEExpress.service.impl.ShippingServiceImpl;
import vn.iotstar.UTEExpress.service.impl.StatusOrderServiceImpl;

@Controller
@RequestMapping("/manager/{id}/")
public class ManagerOrderController {
	@Autowired
	private IManagerService managerService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IShipperService shipperService;
	@Autowired
	private StatusOrderServiceImpl statusOderService;
	@Autowired
	private ShippingServiceImpl shippingService;
	
	@GetMapping("orders")
	public String orderDashBoard(@PathVariable("id") Integer managerID, Model model) {
		Manager manager = managerService.findById(managerID).get();
		
		model.addAttribute("manager",manager);
		
		return "manager/order";
	}
	
	@GetMapping("all-orders")
	public String allOrderOfPost(@PathVariable("id") Integer managerID, Model model) {
		
		// order có srccity == city va destcity = city
		Manager manager = managerService.findById(managerID).get();
		model.addAttribute("manager", manager);
		
		String cityName = manager.getCity();
		List<Order> allOrders = orderService.findOrdersBySourceCityAndDestCity(cityName);
		
		model.addAttribute("allOrders", allOrders);
		
		return "manager/allOrder";
	}
	
	@GetMapping("orders-pending")
	public String pending(@PathVariable("id") Integer managerID, Model model) {
		Manager manager = managerService.findById(managerID).get();
		
		model.addAttribute("manager",manager);
		String cityName = manager.getCity();
		
		//allPending
		List<Order> allPending = orderService.findOrderByOrderStatusAndSourceCity(0, cityName);
		model.addAttribute("allPending", allPending);
		
		return "manager/order-pending";
	}
	
	/* Order Pending */
	@GetMapping("assign-shipper")
	public String assignShipperToOrder(@PathVariable("id") Integer managerID, @RequestParam("orderid") String orderID,
										Model model) {
		// Get manager using Optional
		Optional<Manager> managerOptional = managerService.findById(managerID);
		if (managerOptional.isPresent()) {
		    Manager manager = managerOptional.get();
		    model.addAttribute("manager", manager);
		} else {
		    // Handle the case where the manager is not found
		    System.out.println(":((((((");  // Redirect to an error page or return a specific view
		}

		// Get order using Optional
		Optional<Order> orderOptional = orderService.findById(orderID);
		Order order = null;
		if (orderOptional.isPresent()) {
		    order = orderOptional.get();
		    model.addAttribute("order", order);
		} else {
		    // Handle the case where the order is not found
		    System.out.println("Order not found!");  // Redirect to an error page or return a specific view
		}
		
		// trang thai don
		Shipping shipping = shippingService.findLatestShippingByOrderID(orderID);
		if(shipping == null) System.out.println("null");
		model.addAttribute("shipping", shipping);
		
		//tìm shipper tương ứng
		List<Shipper> shippers = null;
		// cùng city
		if (order.getSourceCity().equals(order.getDestCity())) {
			// hỏa tốc pending -> confirm -> ....
			if(order.getTransport().getTransportID() == 3) {
				//shipper vận thằng - 4
				shippers = shipperService.findShippersByRoleId(4, order.getSourceCity());
				model.addAttribute("shippers", shippers);
			}else {
				shippers = shipperService.findShippersByRoleId(5,order.getSourceCity());
				model.addAttribute("shippers", shippers);
			}
			// các loại khác pending - confirm ...
		}else {
			shippers = shipperService.findShippersByRoleId(5,order.getSourceCity());
			model.addAttribute("shippers", shippers);
		}
		//add shipper vô ciew chọn
		
		// các status order
		List<StatusOrder> statusOrders = statusOderService.findAll();
		model.addAttribute("statusOder", statusOrders);
		
		return "manager/assign-order";
	}
	
	@PostMapping("update-order")
	public String updateOrderStatusAndShipper(@PathVariable("id") Integer managerID, @RequestParam("orderid") String orderID,
								
														HttpServletRequest request) {
		// update order: gán shipper
		Integer shipperID = 0;
		try {
			shipperID = Integer.parseInt(request.getParameter("shipperID"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(shipperID); //ok
		Optional<Shipper> optionalShipper = shipperService.findById(shipperID);
		Shipper shipper = null;
		if (optionalShipper.isPresent()) {
		    shipper = optionalShipper.get();
		    // Thực hiện các logic cần thiết với shipper
		} else {
		    // Xử lý khi không tìm thấy shipper với shipperID
		    System.out.println("Shipper not found for ID: " + shipperID);
		    return "redirect:/manager/" + managerID + "/orders-pending?error=shipper-not-found";
		}
		
		// xử lý date
		String updateDateString = request.getParameter("dateUpdate");
		Date updateDay = null;
		if(updateDateString != null ) {
			// Manually parse the birth string to Date
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    
		    try {
		    	updateDay = dateFormat.parse(updateDateString);
	//	        System.out.println(updateDateString);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return "redirect:/manager/" + managerID + "/orders-pending?error=error"; // Handle invalid date
		    }
		}

	    // update statusorder
		// update shipping
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
	    shipping.setStatusOrderID(1);
	    shipping.setDateUpdate(updateDay);
	    shipping.setShipper(shipper);
	    //save
	    shippingService.save(shipping);
	    
	    
	    
		/*
		 * if(shipping != null) { System.out.println("shipping nor null"); //ok }
		 */
	    
	
		return "redirect:/manager/" + managerID + "/orders-pending";
	}
	
	// order at post
	@GetMapping("orders-in-post")
	public String listOrderInPost(@PathVariable("id") Integer managerID, Model model) {
		Manager manager = managerService.findById(managerID).get();
		
		model.addAttribute("manager",manager);
		String cityName = manager.getCity();
		
		List<Order> allInPost = new ArrayList<>();
		
		List<Order> allInPost1 = orderService.findOrderByOrderStatusAndSourceCity(4, cityName);
		List<Order> allInPost2 = orderService.findOrderByOrderStatusAndDestCity(6, cityName);
		
		// Gộp allInPost1 và allInPost2 vào allInPost
		allInPost.addAll(allInPost1);
		allInPost.addAll(allInPost2);
		
		model.addAttribute("allInPost", allInPost);
		
		return "manager/order-in-post";
	}
	
	// phân order trong post cho shipper
	@GetMapping("update-order-inpost")
	public String assignShipperToOrderInPostView(@PathVariable("id") Integer managerID, @RequestParam("orderid") String orderID,
										Model model) {
		// lấy manager
		Manager manager = managerService.findById(managerID).get();
		model.addAttribute("manager", manager);
		
		
		//lấy order
		Order order = orderService.findById(orderID).get();
		model.addAttribute("order", order);
		
		// các status order
		List<StatusOrder> statusOrders = statusOderService.findAll();
		model.addAttribute("statusOder", statusOrders);
		
		// lấy shipper
		// nếu cùng city statusorder = 4 -> giao shipper tu post - client
		List<Shipper> shippers = null;
		if(order.getSourceCity().equals(order.getDestCity()) && order.getShipping().getStatusOrderID() == 4  ) {
			shippers = shipperService.findShippersByRoleId(6,order.getSourceCity());
		}else if((!order.getSourceCity().equals(order.getDestCity())) && order.getShipping().getStatusOrderID() == 4) {
		// nếu khác city statusorder = 4 -> giao shipper transit post - post
			shippers = shipperService.findShippersByRoleId(7,order.getSourceCity());
		}else if((!order.getSourceCity().equals(order.getDestCity())) && order.getShipping().getStatusOrderID() == 6 ) {
			// nếu khác city statusorder = 6 -> giao shipper tu post - client
			shippers = shipperService.findShippersByRoleId(6,order.getDestCity());
		}
		
		model.addAttribute("shippers", shippers);
		
		return "manager/updateOrderInPost";
	}
	
	// xử lí phân shipper
	@PostMapping("update-order-inpost")
	public String assignShipperToOrderInPost(@PathVariable("id") Integer managerID, @RequestParam("orderid") String orderID,
										HttpServletRequest request) {
		// lấy manager
		Manager manager = managerService.findById(managerID).get();
		
		// update order: gán shipper
			Integer shipperID = 0;
			try {
				shipperID = Integer.parseInt(request.getParameter("shipperID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.println(shipperID); //ok
			Optional<Shipper> optionalShipper = shipperService.findById(shipperID);
			Shipper shipper = null;
			if (optionalShipper.isPresent()) {
			    shipper = optionalShipper.get();
			    // Thực hiện các logic cần thiết với shipper
			} else {
			    // Xử lý khi không tìm thấy shipper với shipperID
			    System.out.println("Shipper not found for ID: " + shipperID);
			    return "redirect:/manager/" + managerID + "/update-order-inpost?error=shipper-not-found";
			}
			
			// xử lý date
			String updateDateString = request.getParameter("dateUpdate");
			Date updateDay = null;
			if(updateDateString != null ) {
				// Manually parse the birth string to Date
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			    
			    try {
			    	updateDay = dateFormat.parse(updateDateString);
		//	        System.out.println(updateDateString);
			    } catch (Exception e) {
			        e.printStackTrace();
			        return "redirect:/manager/" + managerID + "/update-order-inpost?error=error"; // Handle invalid date
			    }
			}
	
			// update statusorder
			// update shipping
		    Shipping shipping = shippingService.findLatestShippingByOrderID(orderID);
		    
		    
//		    ///////////Creat old shipping////////
//			Shipping shippingOld = new Shipping();
//			shippingOld.setShipper(shipping.getShipper());
//			shippingOld.setDateUpdate(shipping.getDateUpdate());
//			shippingOld.setStatusOrderID(shipping.getStatusOrderID()); 
//			shippingOld.setTotal(shipping.getTotal());
//			shippingOld.setOrderID(orderID);  //Vừa sửa
//			shippingService.save(shippingOld);
//			///////////////////////////////////
			
		    shipping.setDateUpdate(updateDay);
		    shipping.setShipper(shipper);
		    
		    //save
		    shippingService.save(shipping);
		    
		  
		    
			/*
			 * //order Order order = orderService.findById(orderID).get();
			 * order.setShipping(shipping); orderService.save(order);
			 */
	
	
		return "redirect:/manager/" + managerID + "/orders-in-post";
	}
}
