package de.hongo.exception;

public class LogicalMongoDBWordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogicalMongoDBWordException() {
		super();
	}

	public LogicalMongoDBWordException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public LogicalMongoDBWordException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LogicalMongoDBWordException(String arg0) {
		super(arg0);
	}

	public LogicalMongoDBWordException(Throwable arg0) {
		super(arg0);
	}

}
