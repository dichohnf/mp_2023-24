package vision.display.mulfunction;

import java.util.Optional;

import vision.display.Display;

public abstract class MulfunctionChecker {

	private final Optional<MulfunctionChecker> nextChecker;

	protected MulfunctionChecker(MulfunctionChecker nextChecker) {
		this.nextChecker = Optional.ofNullable(nextChecker);
	}
	
	public String checkMulfunction(Display display) {
		nextChecker.ifPresent(next -> next.checkMulfunction(display));
		return "No mulfunction detected";
	}

}
