package jds.exception;

import java.util.NoSuchElementException;

public final class AbsentFrameException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;
	
	public AbsentFrameException(String message) {
		super(message);
	}

}
