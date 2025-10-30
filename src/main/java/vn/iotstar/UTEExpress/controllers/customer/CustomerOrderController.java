package vn.iotstar.UTEExpress.controllers.customer;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.entity.Customer;
import vn.iotstar.UTEExpress.entity.Goods;
import vn.iotstar.UTEExpress.entity.Order;
import vn.iotstar.UTEExpress.entity.Shipper;
import vn.iotstar.UTEExpress.entity.Shipping;
import vn.iotstar.UTEExpress.entity.StatusOrder;
import vn.iotstar.UTEExpress.entity.Transport;
import vn.iotstar.UTEExpress.entity.Voucher;
import vn.iotstar.UTEExpress.service.ICityService;
import vn.iotstar.UTEExpress.service.ICustomerService;
import vn.iotstar.UTEExpress.service.IGoodsService;
import vn.iotstar.UTEExpress.service.IOrderService;
import vn.iotstar.UTEExpress.service.IShipperService;
import vn.iotstar.UTEExpress.service.IShippingService;
import vn.iotstar.UTEExpress.service.IStatusOrderService;
import vn.iotstar.UTEExpress.service.ITransportService;
import vn.iotstar.UTEExpress.service.IVoucherService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/customer/order")
public class CustomerOrderController {
	@Autowired
	ICityService cityService;
	@Autowired
	ICustomerService customerService;
	@Autowired
	ITransportService transportService;
	@Autowired
	IGoodsService goodsService;
	@Autowired
	IVoucherService voucherService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IShippingService shippingService;
	@Autowired
	IShipperService shipperService;
	@Autowired
	IStatusOrderService statusOrderService;

	@GetMapping("/{id}")
	public String getMethodName(@PathVariable("id") Integer customerID, Model model) {
		List<City> cities = cityService.findAll();
		List<Transport> transports = transportService.findAll();
		List<Goods> goods = goodsService.findAll();
		Customer customer = customerService.findCustomerByIDUser(customerID);

		// Voucher
		Date currentDate = Date.from(Instant.now());
		List<Voucher> vouchers = voucherService.findValidVoucher(currentDate);

		model.addAttribute("vouchers", vouchers);
		model.addAttribute("goods", goods);
		model.addAttribute("customer", customer);
		model.addAttribute("transports", transports);
		model.addAttribute("cities", cities);
		model.addAttribute("id", customerID);
		return "customer/customer-create-order";
	}

	@PostMapping("/create/{id}") // id nay idCustomer
	public String createOrder(@PathVariable("id") Integer id, @RequestParam("nameReceiver") String nameReceiver,
			@RequestParam("phoneReceiver") String phoneReceiver, @RequestParam("dest") String dest,
			@RequestParam("destCity") String destCity, @RequestParam("source") String source,
			@RequestParam("sourceCity") String sourceCity, @RequestParam("weight") Integer weight,
			@RequestParam("height") Integer height, @RequestParam("width") Integer width,
			@RequestParam("codFee") float codFee, @RequestParam("shipFee") float shipFee,
			@RequestParam("goodsID") Integer goodsID, @RequestParam("transportID") Integer transportID,
			@RequestParam("voucherID") Integer voucherID, @RequestParam("COD_surcharge") float codSurcharge,
			@RequestParam("total") float total, Model model) {
		
		if (customerService.findById(id).getIsActive() !=1)
		{
			model.addAttribute("errorMessage", "Need to active account");
			return "customer/customer-create-order";
		}
		Order order = new Order();

		int srccity = cityService.findByCityName(sourceCity).getCityID();
		int destcity = cityService.findByCityName(destCity).getCityID();

		// Tạo UUID cho OrderID
		String uniqueId = UUID.randomUUID().toString();
		// Them 2 ki tu dau va cuoi trong idOrder
		uniqueId = String.format("%02d", srccity) + uniqueId + String.format("%02d", destcity);

		// Gán giá trị cho các thuộc tính của order
		order.setOrderID(uniqueId); // Giả sử uniqueId đã được tính toán hoặc tạo trước đó
		order.setWeight(weight); // Giả sử weight đã có giá trị
		order.setHeight(height); // Giả sử height đã có giá trị
		order.setWidth(width); // Giả sử width đã có giá trị
		order.setDest(dest); // Gán địa chỉ điểm đến
		order.setSource(source); // Gán địa chỉ nguồn
		order.setDestCity(destCity); // Gán thành phố đích
		order.setSourceCity(sourceCity); // Gán thành phố nguồn
		order.setNameReceiver(nameReceiver); // Gán tên người nhận
		order.setPhoneReceiver(phoneReceiver); // Gán số điện thoại người nhận
		order.setCodFee(codFee); // Gán phí COD
		order.setShipFee(shipFee); // Gán phí vận chuyển
		order.setCodSurcharge(codSurcharge);
		order.setTotal(total);

		Goods goods = goodsService.findById(goodsID); // Giả sử bạn đã lấy được goods từ cơ sở dữ liệu
		order.setGoods(goods);

		Transport transport = transportService.findById(transportID); // Giả sử bạn đã lấy được transport từ cơ sở dữ
																		// liệu
		order.setTransport(transport);

		Customer customer = customerService.findById(id); // Giả sử bạn đã lấy được customer từ cơ sở dữ liệu
		order.setCustomer(customer);

		// Gán các đối tượng liên kết
		if (!voucherID.equals(0)) {
			Voucher voucher = voucherService.findById(voucherID); // Giả sử bạn đã lấy được voucher từ cơ sở dữ liệu
			order.setVoucher(voucher);

			// Kiểm tra có áp dụng Voucher không
			if (voucher != null) {
				voucher.setAmount(voucher.getAmount() - 1);
				voucherService.save(voucher);
			}
		}

		// Cập nhật shipping
		Shipping shipping = new Shipping();
		shipping.setOrderID(uniqueId);
		shipping.setStatusOrderID(0); // set trang thai pending
		shipping.setTotal(total);
		shipping.setDateUpdate(Date.from(Instant.now()));
		shipping.setShipper(null);
		shippingService.save(shipping);

		// Gán thêm shipping cho ORDER
		order.setShipping(shipping);
		
		orderService.save(order); // Save Order

		return "redirect:/customer/" + id; // Return confirmation page after form submission
	}

	// Xem Status của từng đơn hàng
	@GetMapping("/status/{id}") // ID này là ID đơn hàng
	public String StatusOrder(@PathVariable("id") String orderID, Model model) {
		List<Shipping> shipping = shippingService.findAllByOrderID(orderID);
		Order order = orderService.findByID(orderID);

		Shipper shipper = new Shipper(); // Shipper này là người phụ trách giao trực tiếp đến người nhận

		for (Shipping shipping2 : shipping) {
			if (shipping2.getStatusOrderID() == 8) { // Tìm thông tin Shipper khi shipper trạng thái Shipping
				shipper = shipping2.getShipper(); // gán nó cho shipper
				break;
			}
		}
		List<StatusOrder> statusOrder = statusOrderService.findAll();
		model.addAttribute("statusorder", statusOrder);

		model.addAttribute("shipper", shipper); // Thông tin Shipper giao hàng
		model.addAttribute("order", order); // Thông tin đơn hàng
		model.addAttribute("shipping", shipping); // thông tin trạng thái vị trí đơn hàng
		model.addAttribute("id", order.getCustomer().getCustomerID());

		return "customer/trackOrder";
	}

	// Huỷ đơn hàng nhưng chỉ huỷ được các đơn nào ở trạng thái pending
	@GetMapping("/cancel/{id}") // ID này là ID đơn hàng
	public String cancelOrder(@PathVariable("id") String orderID, Model model) {
		// CustomerID
		Order order = orderService.findByID(orderID);
		Integer customerID = order.getCustomer().getCustomerID();

		// Kiểm tra trạng thái trạng thái mới nhất của Order trong SHIPPING
		Integer statusOrder = shippingService.findNewStatusOrderByOrderID(orderID);

		// Nếu trạng thái đơn hàng đã Confirmed thì không huỷ đươc
		if (statusOrder > 0) {
			model.addAttribute("errorMessage", "Order confirmed can't cancel");
			model.addAttribute("id", customerID); // add lai id thanh CustomerID

			return "redirect:/customer/" + customerID;
		} else {

			// Nếu đơn hàng còn trạng thái Pending
			// Ta sẽ xem đơn hàng có áp dụng voucher không
			Voucher voucher = order.getVoucher();
			// Nếu voucher không null thì tăng số lượng voucher lên
			if (voucher != null) {
				voucher.setAmount(voucher.getAmount() + 1);
				voucherService.save(voucher);
			}
			// Ta sẽ xoá Shipping trong SHIPPING
			Shipping shipping = shippingService.findLatestShippingByOrderID(orderID);
			shippingService.deleteByShipping(shipping);
			// Ta sẽ xoá Order trong ORDER
			orderService.deleteByOrderID(orderID);

			model.addAttribute("id", customerID); // add lai id thanh CustomerID

			return "redirect:/customer/" + customerID;
		}
	}

}