package vn.iotstar.UTEExpress.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.entity.Manager;
import vn.iotstar.UTEExpress.entity.Post;
import vn.iotstar.UTEExpress.entity.Shipper;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostShipperCountDTO {
	 	private Integer postId;
	    private String postName;
	    private Long shipperCount;
}
