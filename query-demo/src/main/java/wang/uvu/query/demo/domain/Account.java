package wang.uvu.query.demo.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Account {
	@Id
	private String accountId;
	private Integer level;
	private BigDecimal amount;
	private double balance;
	private float freezeAmount;
	private Date createAt;
	private int version;
	private boolean disable;
}
