package springbook.user.sqlservice;

public class SqlNotFoundException extends Exception {
	 
	public SqlNotFoundException(String message) {
		super(message);
	}
	
	public SqlNotFoundException(String message, Throwable cause) {
		super(message,cause);
	}
}
