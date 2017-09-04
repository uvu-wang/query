package wang.uvu.query;

import java.util.List;

import lombok.Data;

@Data
public abstract class Query<Q extends Query<?>> {
	
	private int page_ = 1;// 从1开始
	private int size_ = 1;
	private String orders_;
	private String fields_;
	private boolean or_ = false; 
	
	private List<Q> querys_;
}
