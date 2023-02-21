package cn.sparrowmini.pem.service.exception;

public class DenyPermissionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DenyPermissionException(String message) {
		super(message);
	}
}
