package vn.iotstar.UTEExpress.controllers.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.UTEExpress.dto.VoucherDTO;
import vn.iotstar.UTEExpress.entity.Goods;
import vn.iotstar.UTEExpress.entity.Transport;
import vn.iotstar.UTEExpress.entity.Voucher;
import vn.iotstar.UTEExpress.service.IGoodsService;
import vn.iotstar.UTEExpress.service.ITransportService;
import vn.iotstar.UTEExpress.service.IVoucherService;

@Controller
@RequestMapping("/admin/{id}/voucher")
public class AdminVoucherController {
	@Autowired
	IVoucherService voucherService;
	@Autowired
	ITransportService transportService;
	@Autowired
	IGoodsService goodsService;

	@GetMapping("")
	public String VoucherHome(@PathVariable Integer id, Model model) {
		// Tìm ra danh sách các Voucher còn hạn sử dụng
		Date currentDate = Date.from(Instant.now());
		List<Voucher> vouchers = voucherService.findValidVoucher(currentDate);
		List<VoucherDTO> voucherDTOs = new ArrayList<>();
		for (Voucher voucher : vouchers) {
			VoucherDTO dto = new VoucherDTO();
			dto.setAmount(voucher.getAmount());
			dto.setDateEnd(voucher.getDateEnd());
			dto.setDateStart(voucher.getDateStart());
			dto.setGoods(goodsService.findById(voucher.getGoods().getGoodsID()));
			dto.setTransport(transportService.findById(voucher.getTransport().getTransportID()));
			dto.setVoucherID(voucher.getVoucherID());
			dto.setVoucherName(voucher.getVoucherName());
			voucherDTOs.add(dto);
		}

		model.addAttribute("vouchers", voucherDTOs);

		return "admin/voucher";
	}

	@GetMapping("/expired")
	public String VoucherExpired(@PathVariable Integer id, Model model) {
		// Tìm ra danh sách các Voucher hết hạn sử dụng hoặc có số lượng =0
		Date currentDate = Date.from(Instant.now());
		List<Voucher> vouchers = voucherService.findExpiredVoucher(currentDate);
		List<VoucherDTO> voucherDTOs = new ArrayList<>();
		for (Voucher voucher : vouchers) {
			VoucherDTO dto = new VoucherDTO();
			dto.setAmount(voucher.getAmount());
			dto.setDateEnd(voucher.getDateEnd());
			dto.setDateStart(voucher.getDateStart());
			dto.setGoods(goodsService.findById(voucher.getGoods().getGoodsID()));
			dto.setTransport(transportService.findById(voucher.getTransport().getTransportID()));
			dto.setVoucherID(voucher.getVoucherID());
			dto.setVoucherName(voucher.getVoucherName());
			voucherDTOs.add(dto);
		}

		model.addAttribute("vouchers", voucherDTOs);
		return "admin/voucher";
	}

	@GetMapping("/inactive")
	public String VoucherInactive(@PathVariable Integer id, Model model) {
		// Tìm ra danh sách các Voucher hết hạn sử dụng hoặc có số lượng =0
		Date currentDate = Date.from(Instant.now());
		List<Voucher> vouchers = voucherService.findInactiveVoucher(currentDate);
		List<VoucherDTO> voucherDTOs = new ArrayList<>();
		for (Voucher voucher : vouchers) {
			VoucherDTO dto = new VoucherDTO();
			dto.setAmount(voucher.getAmount());
			dto.setDateEnd(voucher.getDateEnd());
			dto.setDateStart(voucher.getDateStart());
			dto.setGoods(goodsService.findById(voucher.getGoods().getGoodsID()));
			dto.setTransport(transportService.findById(voucher.getTransport().getTransportID()));
			dto.setVoucherID(voucher.getVoucherID());
			dto.setVoucherName(voucher.getVoucherName());
			voucherDTOs.add(dto);
		}

		model.addAttribute("vouchers", voucherDTOs);
		return "admin/voucher";
	}

	@GetMapping("/delete/{voucherid}")
	public String deleteVoucher(@PathVariable Integer id, @PathVariable Integer voucherid) {
		voucherService.deleteVoucher(voucherid);
		return "redirect:/admin/" + id + "/voucher";
	}

	// EDIT VOUCHER
	@GetMapping("/edit/{voucherid}")
	public String editVoucher(@PathVariable Integer id, @PathVariable Integer voucherid, Model model) {
		Voucher voucher = voucherService.findById(voucherid);
		model.addAttribute("voucher", voucher);

		List<Transport> transports = transportService.findAll();
		List<Goods> goods = goodsService.findAll();

		model.addAttribute("transports", transports);
		model.addAttribute("goods", goods);

		return "admin/editVoucher";
	}

	@PostMapping("/save/edit/{voucherid}")
	public String SaveEditVoucher(@PathVariable Integer id, @PathVariable Integer voucherid,
			@RequestParam("voucherName") String voucherName,
	        @RequestParam("amount") Integer amount,
	        @RequestParam("discount") float discount,
	        @RequestParam("dateStart") String dateStart,
	        @RequestParam("dateEnd") String dateEnd,
	        @RequestParam("description") String description,
	        @RequestParam("transportID") Integer transportID,
	        @RequestParam("goodsID") Integer goodsID) throws ParseException {
		
		Voucher voucher = voucherService.findById(voucherid);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		voucher.setAmount(amount);
		voucher.setDateEnd(dateFormat.parse(dateEnd));
		voucher.setDateStart(dateFormat.parse(dateStart));
		voucher.setVoucherName(voucherName);
		voucher.setTransport(transportService.findById(transportID));
		voucher.setGoods(goodsService.findById(goodsID));
		voucher.setDescription(description);
		voucher.setDiscount(discount);
		
		voucherService.save(voucher);
		
		return "redirect:/admin/" + id +"/voucher";
	}

	@GetMapping("/add")
	public String addVoucher(@PathVariable Integer id, Model model) {
		List<Transport> transports = transportService.findAll();
		List<Goods> goods = goodsService.findAll();

		model.addAttribute("transports", transports);
		model.addAttribute("goods", goods);
		return "admin/addVoucher";
	}

	@PostMapping("/save")
	public String SaveVoucher(@PathVariable Integer id, @RequestParam("voucherName") String voucherName,
			@RequestParam("amount") Integer amount, @RequestParam("discount") float discount,
			@RequestParam("dateStart") String dateStart, @RequestParam("dateEnd") String dateEnd,
			@RequestParam("description") String description, @RequestParam("transportID") Integer transportID,
			@RequestParam("goodsID") Integer goodsID) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Voucher voucher = new Voucher();
		voucher.setVoucherName(voucherName);
		voucher.setAmount(amount);
		voucher.setDateStart(dateFormat.parse(dateStart));
		voucher.setDateEnd(dateFormat.parse(dateEnd));
		voucher.setDiscount(discount);
		voucher.setDescription(description);
		voucher.setTransport(transportService.findById(transportID));
		voucher.setGoods(goodsService.findById(goodsID));
		voucherService.save(voucher); // Lưu Voucher

		// Thêm voucher
		return "redirect:/admin/" + id + "/voucher";
	}

}
