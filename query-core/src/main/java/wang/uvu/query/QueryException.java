package wang.uvu.query;

public class QueryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public QueryException(String message) {
		super(message);
	}
	
	public QueryException(Exception e) {
		super(e);
	}

	public QueryException(String message, Exception e) {
		super(message, e);
	}
}
