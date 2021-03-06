package wang.uvu.query.builder;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import wang.uvu.query.Query;

public class PageBuilder {

	public static PageRequest build(Query query) {
		List<Order> orders = OrderBuilder.build(query);
		if (!orders.isEmpty()) {
			return new PageRequest((query.getPage_() - 1),query.getSize_(),
					new Sort(orders.toArray(new Order[orders.size()])));
		}
		return new PageRequest((query.getPage_() - 1), query.getSize_());
	}
}
