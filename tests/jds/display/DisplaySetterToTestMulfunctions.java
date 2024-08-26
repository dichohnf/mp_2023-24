package jds.display;

import java.util.Optional;

import jds.display.interfaces.VideoInterface;

public class DisplaySetterToTestMulfunctions {
	
	private DisplaySetterToTestMulfunctions() {
	}
	
	public static void setResolution(Display display, String resolution) {
		((StandardDisplay) display).resolution = resolution; 
	}
	
	public static void setSelectedVideoInterface(Display display, VideoInterface videointerface) {
		((StandardDisplay) display).selectedInterface = Optional.of(videointerface); 
	}

}
