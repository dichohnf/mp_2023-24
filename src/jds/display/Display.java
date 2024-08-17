package jds.display;

import java.util.List;

import jds.display.interfaces.video.VideoInterface;
import jds.display.mulfunction.MulfunctionChecker;
import jds.exception.AbsentVideoInterfaceException;

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
	
	VideoInterface getSelectedInterface();
	
	String mulfunctionTest(MulfunctionChecker checker);
	
}