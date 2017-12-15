package wang.uvu.query.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import wang.uvu.query.QueryException;
import wang.uvu.query.Query;
import wang.uvu.query.utils.StringUtils;

public class FieldBuilder {

	public static List<Selection<?>> build(Root<?> root, Query query) {
		String fields = query.getFields_();
		List<Selection<?>> fieldList = new ArrayList<Selection<?>>();
		if(StringUtils.isBlank(fields)){
			return fieldList;
		}
		String[] vars = fields.split(",");
		for (String name : vars) {
			try{
				Selection<?> selection = root.get(name).alias(name);
				fieldList.add(selection);
			}catch(Exception e) {
				throw new QueryException(e);
			}
		}
		return fieldList;
	}
}
