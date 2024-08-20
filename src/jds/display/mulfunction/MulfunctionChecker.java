package jds.display.mulfunction;

import jds.display.Display;

public abstract class MulfunctionChecker {

	private final MulfunctionChecker nextChecker;

	protected MulfunctionChecker(MulfunctionChecker nextChecker) {
		this.nextChecker = nextChecker;
	}
	
	public String checkMulfunction(Display display) {
		return (nextChecker != null)
				?nextChecker.checkMulfunction(display)
				:"No mulfunction detected";
	}

}
