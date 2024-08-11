package vision.display.mulfunction;

import vision.display.Display;

public final class UnexpectedlyDisconnectedInterfaceChecker extends MulfunctionChecker {

	public UnexpectedlyDisconnectedInterfaceChecker(MulfunctionChecker nextChecker) {
		super(nextChecker);
	}
	
	@Override
	public String checkMulfunction(Display display) {
		if(display.getConnectedInterfaces()
				.stream().allMatch(
						connected -> connected != display.getSelectedInputInterface())) {
			display.displayError("Input Error: Unexpectedly disconnected interface");
			return "Input Error: Unexpectedly disconnected interface";
		}
		return super.checkMulfunction(display);
	}

}
