package vision.display;

import java.util.List;

import vision.display.interfaces.video.VideoInterface;
import vision.display.mulfunction.MulfunctionChecker;
import vision.exception.AbsentVideoInterfaceException;

public interface Display {
	
	void displayStream();
	
	void displayMenu();
	
	void displayError(String message);
	
	void setBrightness(double newBrightness);
	
	double getBrightness();

	void setColorTemperature(int newTemperature);
	
	int getColorTemperature();
	
	void setResolution(String resolution);
	
	String getResolution();
	
	boolean connectInterface(VideoInterface videoInterface) throws AbsentVideoInterfaceException;

	boolean disconnectInterface(VideoInterface videoInterface);
	
	List<VideoInterface> getConnectedInterfaces();

	void selectInputInterface(VideoInterface videoIterface);
	
	VideoInterface getSelectedInputInterface();
	
	String mulfunctionTest(MulfunctionChecker checker);
	
}