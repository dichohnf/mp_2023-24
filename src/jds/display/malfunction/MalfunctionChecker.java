package jds.display.malfunction;

import jds.display.Display;

public abstract class MalfunctionChecker {

	private final MalfunctionChecker nextChecker;

	protected MalfunctionChecker(MalfunctionChecker nextChecker) {
		this.nextChecker = nextChecker;
	}
	
	public String checkMulfunction(Display display) {
		return (nextChecker != null)
				? nextChecker.checkMulfunction(display)
				: "No mulfunction detected";
	}

}
