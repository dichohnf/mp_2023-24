package jds.display.malfunction;

import jds.display.Display;

public final class UnexpectedlyChangedResolutionChecker extends MalfunctionChecker {

	public UnexpectedlyChangedResolutionChecker(MalfunctionChecker nextChecker) {
		super(nextChecker);
	}

	@Override
	public String checkMulfunction(Display display) {
		try{
			display.setResolution(display.getResolution());
			return super.checkMulfunction(display);
		}catch(Exception e) {
			e.printStackTrace();
			display.displayError("Unexpectedly changed resolution");
			return "Unexpectedly changed resolution";
		}
	}
}
