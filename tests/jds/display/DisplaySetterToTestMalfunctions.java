package jds.display;

import jds.display.interfaces.VideoInterface;

public class DisplaySetterToTestMalfunctions {
	
	private DisplaySetterToTestMalfunctions() {
	}
	
	public static void setResolution(Display display, String resolution) {
		((StandardDisplay) display).forceResolution(resolution); 
	}
	
	public static void setSelectedVideoInterface(Display display, VideoInterface videointerface) {
		((StandardDisplay) display).forceInputInterface(videointerface);
	}

}
