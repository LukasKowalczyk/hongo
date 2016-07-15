package de.hongo.exception;

public class MongelpCollectionConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MongelpCollectionConnectionException() {
		super();
	}

	public MongelpCollectionConnectionException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MongelpCollectionConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MongelpCollectionConnectionException(String arg0) {
		super(arg0);
	}

	public MongelpCollectionConnectionException(Throwable arg0) {
		super(arg0);
	}

}
