package vn.iotstar.UTEExpress.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.iotstar.UTEExpress.entity.City;
import vn.iotstar.UTEExpress.entity.Manager;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDTO {
	private Integer postID;
	private String postName;
	private City city;
	private Manager manager;
}
