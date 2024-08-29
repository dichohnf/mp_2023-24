package jds.display.malfunction;

import jds.display.Display;

public final class UnexpectedlyDisconnectedInterfaceChecker extends MalfunctionChecker {

	public UnexpectedlyDisconnectedInterfaceChecker(MalfunctionChecker nextChecker) {
		super(nextChecker);
	}
	
	@Override
	public String checkMulfunction(Display display) {
		if(display.getConnectedInterfaces().isEmpty())
			return displayErrorAndReturnMessage(display, "No connected interface detected");
		else if(display.getConnectedInterfaces()
				.stream().allMatch(
						connected -> !connected.equals(display.getSelectedInterface())))
			return displayErrorAndReturnMessage(display, "Unexpectedly disconnected interface");
		return super.checkMulfunction(display);
	}

	private String displayErrorAndReturnMessage(Display display, String message) {
		display.displayError(message);
		return message;
	}

}
