package wang.uvu.query.demo;

import wang.uvu.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class AccountQuery extends Query{
	private String accountId;
	private String level;
	private String amount;
	private String balance;
	private String freezeAmount;
	private String createAt;
	private String version;
	private String disable;
}
