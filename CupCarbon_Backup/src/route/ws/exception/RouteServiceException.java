package route.ws.exception;

public class RouteServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public RouteServiceException(String message) {
		super(message);
		this.message = message;
	}
	
	public RouteServiceException(String message , Throwable cause) {
		super(message,cause);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
