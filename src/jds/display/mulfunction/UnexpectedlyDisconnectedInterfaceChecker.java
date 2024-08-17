package jds.display.mulfunction;

import jds.display.Display;

public final class UnexpectedlyDisconnectedInterfaceChecker extends MulfunctionChecker {

	public UnexpectedlyDisconnectedInterfaceChecker(MulfunctionChecker nextChecker) {
		super(nextChecker);
	}
	
	@Override
	public String checkMulfunction(Display display) {
		if(display.getConnectedInterfaces()
				.stream().allMatch(
						connected -> connected != display.getSelectedInterface())) {
			display.displayError("Input Error: Unexpectedly disconnected interface");
			return "Input Error: Unexpectedly disconnected interface";
		}
		return super.checkMulfunction(display);
	}

}
