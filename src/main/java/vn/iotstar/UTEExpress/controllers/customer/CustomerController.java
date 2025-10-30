package vn.iotstar.UTEExpress.controllers.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.iotstar.UTEExpress.dto.OrderDTO;
import vn.iotstar.UTEExpress.service.ICityService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.service.IOrderService;
import vn.iotstar.UTEExpress.service.IShippingService;
import vn.iotstar.UTEExpress.entity.Order;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	ICustomerService customerService;
	@Autowired
	ICityService cityService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IShippingService shippingService;

	/*
	 * Khi người dùng vừa đăng nhập nó sẽ chuyển sang trang quản lý đơn hàng đầu
	 * tiên
	 */
	@GetMapping("/{id}")
	public String Order(@PathVariable("id") Integer customerID, Model model) {
		List<Order> orders = orderService.findAllByCustomerID(customerID);
		List<OrderDTO> orderDTO = new ArrayList<>();

		for (Order order : orders) {
			if (order.getCustomer().getCustomerID() == customerID) {
				OrderDTO dto = new OrderDTO();
				dto.setOrderID(order.getOrderID());
				dto.setWeight(order.getWeight());
				dto.setHeight(order.getHeight());
				dto.setWidth(order.getWidth());
				dto.setSourceCity(order.getSourceCity());
				dto.setDestCity(order.getDestCity());
				dto.setSource(order.getSource());
				dto.setDest(order.getDest());
				dto.setNameReceiver(order.getNameReceiver());
				dto.setPhoneReceiver(order.getPhoneReceiver());
				dto.setCodFee(order.getCodFee());
				dto.setShipFee(order.getShipFee());
				dto.setVoucherName(order.getVoucher() != null ? order.getVoucher().getVoucherName() : null);
				dto.setGoodsType(order.getGoods() != null ? order.getGoods().getGoodsType() : null);
				dto.setTransportType(order.getTransport() != null ? order.getTransport().getTransportType() : null);
				dto.setCOD_surcharge(order.getCodSurcharge());
				dto.setTotal(order.getTotal());
				dto.setStatusOrderID(shippingService.findNewStatusOrderByOrderID(order.getOrderID()));
				
				if (dto.getStatusOrderID() <8) orderDTO.add(dto);
			}
		}

		// show thong tin don hang
		model.addAttribute("orders", orderDTO);
		model.addAttribute("id", customerID);
		return "customer/home";
	}
	
	@GetMapping("order/success/{id}")
	public String OrderSuccess(@PathVariable("id") Integer customerID, Model model) {
		List<Order> orders = orderService.findAllByCustomerID(customerID);
		List<OrderDTO> orderDTO = new ArrayList<>();

		for (Order order : orders) {
			if (order.getCustomer().getCustomerID() == customerID) {
				OrderDTO dto = new OrderDTO();
				dto.setOrderID(order.getOrderID());
				dto.setWeight(order.getWeight());
				dto.setHeight(order.getHeight());
				dto.setWidth(order.getWidth());
				dto.setSourceCity(order.getSourceCity());
				dto.setDestCity(order.getDestCity());
				dto.setSource(order.getSource());
				dto.setDest(order.getDest());
				dto.setNameReceiver(order.getNameReceiver());
				dto.setPhoneReceiver(order.getPhoneReceiver());
				dto.setCodFee(order.getCodFee());
				dto.setShipFee(order.getShipFee());
				dto.setVoucherName(order.getVoucher() != null ? order.getVoucher().getVoucherName() : null);
				dto.setGoodsType(order.getGoods() != null ? order.getGoods().getGoodsType() : null);
				dto.setTransportType(order.getTransport() != null ? order.getTransport().getTransportType() : null);
				dto.setCOD_surcharge(order.getCodSurcharge());
				dto.setTotal(order.getTotal());
				dto.setStatusOrderID(shippingService.findNewStatusOrderByOrderID(order.getOrderID()));
				
				if (dto.getStatusOrderID() ==8) orderDTO.add(dto);
			}
		}

		// show thong tin don hang
		model.addAttribute("orders", orderDTO);
		model.addAttribute("id", customerID);
		return "customer/orderSuccess";
	}
	
	@GetMapping("/order/failed/{id}")
	public String OrderFailed(@PathVariable("id") Integer customerID, Model model) {
		List<Order> orders = orderService.findAllByCustomerID(customerID);
		List<OrderDTO> orderDTO = new ArrayList<>();

		for (Order order : orders) {
			if (order.getCustomer().getCustomerID() == customerID) {
				OrderDTO dto = new OrderDTO();
				dto.setOrderID(order.getOrderID());
				dto.setWeight(order.getWeight());
				dto.setHeight(order.getHeight());
				dto.setWidth(order.getWidth());
				dto.setSourceCity(order.getSourceCity());
				dto.setDestCity(order.getDestCity());
				dto.setSource(order.getSource());
				dto.setDest(order.getDest());
				dto.setNameReceiver(order.getNameReceiver());
				dto.setPhoneReceiver(order.getPhoneReceiver());
				dto.setCodFee(order.getCodFee());
				dto.setShipFee(order.getShipFee());
				dto.setVoucherName(order.getVoucher() != null ? order.getVoucher().getVoucherName() : null);
				dto.setGoodsType(order.getGoods() != null ? order.getGoods().getGoodsType() : null);
				dto.setTransportType(order.getTransport() != null ? order.getTransport().getTransportType() : null);
				dto.setCOD_surcharge(order.getCodSurcharge());
				dto.setTotal(order.getTotal());
				dto.setStatusOrderID(shippingService.findNewStatusOrderByOrderID(order.getOrderID()));
				
				if (dto.getStatusOrderID() ==9) orderDTO.add(dto);
			}
		}

		// show thong tin don hang
		model.addAttribute("orders", orderDTO);
		model.addAttribute("id", customerID);
		return "customer/orderFailed";
	}

	// Hiện tổng số đơn đã giao, đang giao
	// Hiện tiền COD
	@GetMapping("/statistic/{id}")
	public String Statistic(@PathVariable("id") Integer customerID, Model model) {
		List<Order> orders = orderService.findAllByCustomerID(customerID);
		List<OrderDTO> orderDTO = new ArrayList<>();

		for (Order order : orders) {
			if (order.getCustomer().getCustomerID() == customerID) {
				OrderDTO dto = new OrderDTO();
				dto.setOrderID(order.getOrderID());
				dto.setWeight(order.getWeight());
				dto.setHeight(order.getHeight());
				dto.setWidth(order.getWidth());
				dto.setSourceCity(order.getSourceCity());
				dto.setDestCity(order.getDestCity());
				dto.setSource(order.getSource());
				dto.setDest(order.getDest());
				dto.setNameReceiver(order.getNameReceiver());
				dto.setPhoneReceiver(order.getPhoneReceiver());
				dto.setCodFee(order.getCodFee());
				dto.setShipFee(order.getShipFee());
				dto.setVoucherName(order.getVoucher() != null ? order.getVoucher().getVoucherName() : null);
				dto.setGoodsType(order.getGoods() != null ? order.getGoods().getGoodsType() : null);
				dto.setTransportType(order.getTransport() != null ? order.getTransport().getTransportType() : null);
				dto.setCOD_surcharge(order.getCodSurcharge());
				dto.setTotal(order.getTotal());
				dto.setStatusOrderID(shippingService.findNewStatusOrderByOrderID(order.getOrderID()));
				
				if (dto.getStatusOrderID() ==8) orderDTO.add(dto);
			}
		}
		
		// Khởi tạo dữ liệu thống kê
		double totalCODFee = 0.0;
		double totalCODSurcharge = 0.0;
		double total= 0;
		double voucher=0;
		double shipFee =0;

		// Duyệt qua danh sách đơn hàng để tính tổng
		for (OrderDTO order : orderDTO) {
			totalCODFee += order.getCodFee(); // Tổng COD Fee
			totalCODSurcharge += order.getCOD_surcharge(); // Tổng COD Surcharge
			total += order.getTotal();	//Tổng tiền phải trả (bao gồm luôn cod)
			shipFee += order.getShipFee();	//Tổng tiền ship
		}
		
		total -= totalCODFee -totalCODSurcharge; //Tổng tiền ship 
		
		
		// Chuẩn bị dữ liệu cho Thymeleaf
		model.addAttribute("totalCODFee", totalCODFee);
		model.addAttribute("totalCODSurcharge", totalCODSurcharge);
		

		return "customer/statistic";
	}

}