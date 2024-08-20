package jds.display;

public class DisplaySetterToTestMulfunctions {
	
	private DisplaySetterToTestMulfunctions() {
	}
	
	public static void setResolution(Display display, String resolution) {
		((StandardDisplay) display).resolution = resolution; 
	}

}
