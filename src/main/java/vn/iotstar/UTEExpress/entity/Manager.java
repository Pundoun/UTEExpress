package vn.iotstar.UTEExpress.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="managers")
public class Manager {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer managerID;

	
	private String password;
	private String name;
	private Integer gender; //1-nam  2-nữ
	private String picture;
	private String city;
	private String address;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birth;  //java.util
	
	private String phone;
	private String cccd;
	
	@OneToOne
	@JoinColumn(name="username")
	private Account account;
	
	@OneToOne
	@JoinColumn(name="postID")
	private Post post;
}
