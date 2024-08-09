package vision.display;

import java.util.Optional;

import vision.exception.MulfunctionCheckException;

public abstract class MulfunctionChecker {

	private final Optional<MulfunctionChecker> nextChecker;

	protected MulfunctionChecker(MulfunctionChecker nextChecker) {
		this.nextChecker = Optional.ofNullable(nextChecker);
	}
	
	public void checkMulfunction(Display display) throws MulfunctionCheckException {
		nextChecker.orElseThrow(
				() -> new MulfunctionCheckException("No mulfunction detected"))
		.checkMulfunction(display);
	}
	
	
	

	
}
